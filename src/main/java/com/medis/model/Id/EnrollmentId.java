package com.medis.model.Id;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class EnrollmentId implements Serializable{
    @Column(length = 50)
    private String userId;

    @Column(length = 50)
    private String seminarId;

//    @GeneratedValue(generator = "system-uuid")
//    @GenericGenerator(name = "system-uuid", strategy = "uuid")
//    private String orderId;

//    @Column(length = 50)
//    private String enrollmentId = UUID.randomUUID().toString();
}
