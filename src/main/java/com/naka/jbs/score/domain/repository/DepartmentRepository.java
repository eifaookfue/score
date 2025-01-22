package com.naka.jbs.score.domain.repository;

import org.springframework.data.repository.CrudRepository;

import com.naka.jbs.score.domain.model.Department;

public interface DepartmentRepository extends CrudRepository<Department, String> {}
