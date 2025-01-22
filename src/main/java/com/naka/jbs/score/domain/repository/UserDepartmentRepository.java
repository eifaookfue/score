package com.naka.jbs.score.domain.repository;

import java.util.List;

import com.naka.jbs.score.domain.model.Department;

public interface UserDepartmentRepository {
    List<Department> getDepartments(String userId);
}
