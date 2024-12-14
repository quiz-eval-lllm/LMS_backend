package com.medis.model.Id;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import java.io.Serializable;
import java.util.UUID;

@Embeddable
public class UlasanId  implements Serializable{
    @Embedded
    private EnrollmentId enrollmentId;

    @Column(length = 50)
    private String ulasanId = UUID.randomUUID().toString();
}
