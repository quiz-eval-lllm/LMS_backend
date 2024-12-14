package com.medis.model.Id;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.UUID;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BannedStatusId implements Serializable {
    @Column(length = 50)
    private String userId;

    @Column(length = 50)
    private String bannedId = UUID.randomUUID().toString();


}
