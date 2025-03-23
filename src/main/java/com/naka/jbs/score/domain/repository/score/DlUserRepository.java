package com.naka.jbs.score.domain.repository.score;

import org.springframework.data.jpa.repository.JpaRepository;

import com.naka.jbs.score.domain.model.entity.score.DlUser;
import com.naka.jbs.score.domain.model.entity.score.DlUserKey;

public interface DlUserRepository extends JpaRepository<DlUser, DlUserKey> {

}
