package com.naka.jbs.score.domain.repository.score;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.naka.jbs.score.domain.model.entity.score.KwEmployee;

@Repository
public interface KwEmployeeRepository extends JpaRepository<KwEmployee, String> {}
