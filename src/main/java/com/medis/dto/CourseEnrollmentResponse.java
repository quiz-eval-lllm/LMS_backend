package com.medis.dto;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class CourseEnrollmentResponse {
    private String uuid;
    private LocalDateTime createdAt;
    private int finishedMaterial;
    private boolean isReviewed;
    private CourseResponse course;
}