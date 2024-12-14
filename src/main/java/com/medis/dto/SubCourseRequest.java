package com.medis.dto;

import jakarta.annotation.Nullable;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Data
public class SubCourseRequest {
    private String name;
    private String description;
    private List<MaterialRequest> materials;
    @Nullable
    private String uuid;
}
