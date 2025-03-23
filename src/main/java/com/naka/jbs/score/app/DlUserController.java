package com.naka.jbs.score.app;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.naka.jbs.score.domain.model.entity.score.DlUser;
import com.naka.jbs.score.domain.service.DlUserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class DlUserController {

    private final DlUserService dlUserService;

    @GetMapping("/dlUser")
    public ResponseEntity<List<DlUser>> getDlUser() {
        List<DlUser> dlUsers = dlUserService.getAll();
        return ResponseEntity.ok(dlUsers);
    }

    @PostMapping("/dlUser")
    public void postDlUser() {
        dlUserService.postDlUser();
    }

    @GetMapping("/dlUserRedis")
    public ResponseEntity<List<DlUser>> getRedisDlUser() {
        List<DlUser> dlUsers = dlUserService.getAllFromRedis();
        return ResponseEntity.ok(dlUsers);
    }

}
