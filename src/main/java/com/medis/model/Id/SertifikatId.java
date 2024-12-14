package com.medis.model.Id;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Getter
@Setter
public class SertifikatId  implements Serializable{
    @Embedded
    private EnrollmentId enrollmentId;

    @Column(length = 50)
    private String sertifikatId = UUID.randomUUID().toString();
}
