package com.naka.jbs.score.domain.model.entity.score;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Data;

@Entity
@Table
@Data
public class KwEmployee {

    @Id
    private String uid;

    private String userName;
}
