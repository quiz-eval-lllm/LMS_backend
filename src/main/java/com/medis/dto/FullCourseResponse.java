package com.medis.dto;

import com.medis.model.course.ReviewModel;
import com.medis.model.course.SubCourseModel;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
public class FullCourseResponse {
    private String uuid;
    private String name;
    private String slugName;
    private String description;
    private String image_url;
    private String image_token;
    private long price;
    private int totalParticipant;
    private boolean isReleased;
    private float rating;
    private int totalMaterial;
    private String examLink;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<SubCourseResponse> listSubCourse;
    private List<ReviewModel> listReview;
    private Map<Integer, Integer> ratingDistribution;
}
