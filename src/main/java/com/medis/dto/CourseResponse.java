package com.medis.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class CourseResponse {
    private String uuid;
    private String name;
    private String slugName;
    private String description;
    private String image_url;
    private String image_token;
    private long price;
    private int totalParticipant;
    private int totalMaterial;
    private boolean isReleased;
    private float rating;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}