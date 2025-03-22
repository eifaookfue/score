package com.naka.jbs.score.domain.repository.redis;

import java.util.List;

import org.springframework.data.repository.Repository;

public interface CrudRepository<T, ID> extends Repository<T, ID> {

    <S extends T> void save(S entity);

    List<T> findAll();

    Class<T> getEntityClass();
}
