package com.medis.dto;

import com.medis.model.user.DokterModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class CourseParticipantResponse {
    private String uuid;
    private LocalDateTime createdAt;
    private int finishedMaterial;
    private UserInfoResponse user;
}
