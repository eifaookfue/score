package com.naka.jbs.score.domain.model.entity.score;

import java.time.LocalTime;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import lombok.Data;

@Entity
@Table
@Data
public class DlUser {

    @EmbeddedId
    private DlUserKey pk;

    private LocalTime updatedTime;
}
