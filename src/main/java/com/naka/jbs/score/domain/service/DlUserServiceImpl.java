package com.naka.jbs.score.domain.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.naka.jbs.score.domain.model.entity.score.DlUser;
import com.naka.jbs.score.domain.model.entity.score.DlUserKey;
import com.naka.jbs.score.domain.repository.redis.CrudRepository;
import com.naka.jbs.score.domain.repository.score.DlUserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DlUserServiceImpl implements DlUserService {

    private final DlUserRepository dlUserRepository;
    private final CrudRepository<DlUser, DlUserKey> dlUserRedisRepository;

    @Override
    public List<DlUser> getAll() {
        return dlUserRepository.findAll();
    }

    @Override
    public List<DlUser> getAllFromRedis() {
        return dlUserRedisRepository.findAll();
    }

    @Override
    public void postDlUser() {
        getAll().forEach(dlUser -> {
            dlUserRedisRepository.save(dlUser);
        });
    }

}
