package com.medis.dto;

import jakarta.annotation.Nullable;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class MaterialRequest {
    private String name;
    private String description;
    private String type;
    private String material_url;
    private String token;
    private int readingMinute;
    @Nullable
    private String uuid;
}
