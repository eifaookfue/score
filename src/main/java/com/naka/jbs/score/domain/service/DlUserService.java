package com.naka.jbs.score.domain.service;

import java.util.List;

import com.naka.jbs.score.domain.model.entity.score.DlUser;

public interface DlUserService {

    List<DlUser> getAll();

    List<DlUser> getAllFromRedis();

    void postDlUser();

}
