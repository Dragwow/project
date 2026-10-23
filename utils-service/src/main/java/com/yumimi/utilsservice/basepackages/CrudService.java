package com.yumimi.utilsservice.basepackages;

public interface CrudService<E extends BaseEntity>{

    void create(E entity);
    void update(E entity);
    void delete(Long id);
    E findById(Long id);

}
