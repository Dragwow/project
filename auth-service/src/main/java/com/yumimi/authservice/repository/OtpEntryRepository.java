package com.yumimi.authservice.repository;

import com.yumimi.authservice.entity.OtpEntry;
import com.yumimi.utilsservice.basepackages.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OtpEntryRepository extends BaseRepository<OtpEntry> {
    Optional<OtpEntry> findTopByEmailAndUsedFalseOrderByCreatedAtDesc(String email);

}
