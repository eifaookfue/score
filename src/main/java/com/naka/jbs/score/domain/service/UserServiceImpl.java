package com.naka.jbs.score.domain.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.naka.jbs.score.domain.model.Department;
import com.naka.jbs.score.domain.model.User;
import com.naka.jbs.score.domain.model.UserDepartment;
import com.naka.jbs.score.domain.repository.DepartmentRepository;
import com.naka.jbs.score.domain.repository.UserDepartmentRepository;
import com.naka.jbs.score.domain.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final UserDepartmentRepository userDepartmentRepository;
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public List<UserDepartment> getUsers() {
        List<User> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add);
        return users.stream().map(user -> UserDepartment.of(user, userDepartmentRepository.getDepartments(user.getUserId()))).collect(Collectors.toList());
    }

    @Override
    public void postUsers() {
        userRepository.save(new User("user1", "user1@naka.com"));
        userRepository.save(new User("user2", "user2@naka.com"));
        departmentRepository.save(new Department("101", "EquityTrading", "GM"));
        departmentRepository.save(new Department("102", "PrimaryService", "GM"));
        departmentRepository.save(new Department("201", "CF1", "IB"));
        redisTemplate.opsForList().leftPushAll("user:user1:departments", "102", "101");
        redisTemplate.opsForList().leftPushAll("user:user2:departments", "201");
    }

}
