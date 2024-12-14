package com.medis.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class CourseRequest {
    private String name;
    private String description;
    private int price;
    private String image_url;
    private String image_token;
    private String examLink;
    private List<SubCourseRequest> subcourses;
}
