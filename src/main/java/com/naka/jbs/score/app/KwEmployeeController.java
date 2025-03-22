package com.naka.jbs.score.app;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.naka.jbs.score.domain.model.entity.score.KwEmployee;
import com.naka.jbs.score.domain.service.KwEmployeeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class KwEmployeeController {

    private final KwEmployeeService kwEmployeeService;

    @GetMapping("/kwEmployee")
    public ResponseEntity<List<KwEmployee>> getKwEmployee() {
        List<KwEmployee> kwEmployees = kwEmployeeService.getAll();
        return ResponseEntity.ok(kwEmployees);
    }

    @PostMapping("/kwEmployee")
    public void postKwEmployee() {
        kwEmployeeService.postKwEmployee();
    }

    @GetMapping("/kwEmployeeRedis")
    public ResponseEntity<List<KwEmployee>> getRedisKwEmployee() {
        List<KwEmployee> kwEmployees = kwEmployeeService.getAllFromRedis();
        return ResponseEntity.ok(kwEmployees);
    }

}
