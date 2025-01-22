package com.naka.jbs.score.app;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.naka.jbs.score.domain.model.UserDepartment;
import com.naka.jbs.score.domain.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/user")
    public void postUsers() {
        userService.postUsers();
    }

    @GetMapping("/user")
    public ResponseEntity<List<UserDepartment>> getUsers() {
        List<UserDepartment> users = userService.getUsers();
        return ResponseEntity.ok(users);
    }
}
