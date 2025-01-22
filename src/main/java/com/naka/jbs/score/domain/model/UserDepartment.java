package com.naka.jbs.score.domain.model;

import java.util.List;

import org.springframework.beans.BeanUtils;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDepartment {
    private String userId;
    private String email;
    private List<Department> departments;

    public static UserDepartment of(User user, List<Department> departments) {
        UserDepartment userDepartment = new UserDepartment();
        BeanUtils.copyProperties(user, userDepartment);
        userDepartment.setDepartments(departments);
        return userDepartment;
    }

    public static String createDepartmentsKey(String userId) {
        return String.join(":", "user", userId, "departments");
    }
}
