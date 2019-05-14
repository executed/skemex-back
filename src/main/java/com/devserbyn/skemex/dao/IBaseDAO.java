package com.devserbyn.skemex.dao;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface IBaseDAO<T, PK extends Serializable> {

    T save(T entity);

    Optional<T> findById(PK pk);

    void delete(T entity);

    void deleteById(PK pk);

    List<T> findAll();

    List<T> findWithPagination(int first,int count);

    boolean existsById(PK pk);
}
