package com.naka.jbs.score.domain.model.entity.score;

import jakarta.persistence.Embeddable;

import lombok.Data;

@Embeddable
@Data
public class DlUserKey {
    private String dlName;
    private String uid;
}
