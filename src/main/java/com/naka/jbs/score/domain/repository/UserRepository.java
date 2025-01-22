package com.naka.jbs.score.domain.repository;

import org.springframework.data.repository.CrudRepository;

import com.naka.jbs.score.domain.model.User;

public interface UserRepository extends CrudRepository<User, String> {}
