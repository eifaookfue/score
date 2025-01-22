package com.naka.jbs.score.domain.service;

import java.util.List;

import com.naka.jbs.score.domain.model.UserDepartment;

public interface UserService {
    void postUsers();

    List<UserDepartment> getUsers();
}
