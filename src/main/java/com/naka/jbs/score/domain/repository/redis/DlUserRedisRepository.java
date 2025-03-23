package com.naka.jbs.score.domain.repository.redis;

import org.springframework.stereotype.Repository;

import com.naka.jbs.score.domain.model.entity.score.DlUser;
import com.naka.jbs.score.domain.model.entity.score.DlUserKey;

@Repository
public class DlUserRedisRepository extends AbstractCrudRepository<DlUser, DlUserKey> {

    @Override
    public Class<DlUser> getEntityClass() {
        return DlUser.class;
    }
}
