package com.medis.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MaterialResponse {
    private String uuid;
    private String name;
    private int readingMinute;
}
