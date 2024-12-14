package com.medis.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class UserInfoResponse {
    private String uuid;
    private String nama;
    private String nomorTelefon;
    private String institusi;
    private String pekerjaan;
    private String spesialisasi;
    private String kotaKabupaten;
    private String jenisKelamin;
    private LocalDate tanggalLahir;
}
