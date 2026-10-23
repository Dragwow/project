package com.yumimi.authservice.service.impl;

import com.yumimi.authservice.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;

@Service
public class JwtServiceImpl implements JwtService {

    private final PublicKey publicKey;
    private final PrivateKey privateKey;
    private final long expirationMs;

    public JwtServiceImpl(
            @Value("${jwt.private-key-path:secrets/private.pem}")
            String privateKeyPath,

            @Value("${jwt.public-key-path:secrets/public.pem}")
            String publicKeyPath,

            @Value("${jwt.expiration-ms:3600000}")
            long expirationMs

    ){
        if (expirationMs <= 0){
            throw new IllegalArgumentException(
                    "Jwt expiration must be greater then zero"
            );
        }

        this.privateKey = loadPrivateKey(privateKeyPath);
        this.publicKey = loadPublicKey(publicKeyPath);
        this.expirationMs = expirationMs;
    }

    @Override
    public String generateToken(String username) {
        if (username == null || username.isBlank()){
            throw new IllegalArgumentException(
                    "Username must be blank"
            );
        }

        Date now = new Date();
        Date expiration = new Date(
                now.getTime() + expirationMs
        );

        return Jwts.builder()
                .subject(username)
                .issuedAt(now)
                .expiration(expiration)
                .signWith(privateKey, Jwts.SIG.RS256)
                .compact();

    }

    @Override
    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    @Override
    public boolean isTokenValid(String token) {
        if (token == null || token.isBlank()){
            return false;
        }
        try {
            Claims claims = extractClaims(token);

            return claims.getSubject() != null
                    && !claims.getSubject().isBlank();
        }catch (JwtException | IllegalArgumentException exception){
            return false;
        }
    }

    private Claims extractClaims(String token){
        if (token == null || token.isBlank()){
            throw new IllegalArgumentException(
                    "JWT must be blank"
            );
        }

        return Jwts.parser()
                .verifyWith(publicKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private PrivateKey loadPrivateKey(String filePath){
        try {
            String pem = Files.readString(
                    Path.of(filePath),
                    StandardCharsets.US_ASCII
            );

            byte[] keyBytes = decodePem(
                    pem,
                    "PRIVATE KEY"
            );

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");

            return  keyFactory.generatePrivate(new PKCS8EncodedKeySpec(keyBytes));

        }catch (IOException | GeneralSecurityException exception){
            throw new IllegalStateException(
                    "Cannot load JWT private key from configured path",
                    exception
            );
        }
    }

    private PublicKey loadPublicKey(String filePath){
        try {
            String pem = Files.readString(
                    Path.of(filePath),
                    StandardCharsets.US_ASCII
            );

            byte[] keyBytes = decodePem(
                    pem,
                    "PUBLIC KEY"
            );

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");

            return  keyFactory.generatePublic(new X509EncodedKeySpec(keyBytes));

        }catch (IOException | GeneralSecurityException exception){
            throw new IllegalStateException(
                    "Cannot load JWT public key from configured path",
                    exception
            );
        }
    }

    private byte[] decodePem(String pem, String keyType) {
        String header = "-----BEGIN " + keyType + "-----";
        String footer = "-----END " + keyType + "-----";

        if (!pem.contains(header) || !pem.contains(footer)) {
            throw new IllegalArgumentException(
                    "Invalid PEM format for " + keyType
            );
        }

        String encoded = pem
                .replace(header, "")
                .replace(footer, "")
                .replaceAll("\\s", "");

        return Base64.getDecoder().decode(encoded);
    }
}
