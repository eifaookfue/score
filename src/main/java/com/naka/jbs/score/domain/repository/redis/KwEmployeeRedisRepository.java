package com.naka.jbs.score.domain.repository.redis;

import org.springframework.stereotype.Repository;

import com.naka.jbs.score.domain.model.entity.score.KwEmployee;

@Repository
public class KwEmployeeRedisRepository extends AbstractCrudRepository<KwEmployee, String> {
    @Override
    public Class<KwEmployee> getEntityClass() {
        return KwEmployee.class;
    }
}
