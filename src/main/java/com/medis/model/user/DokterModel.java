package com.medis.model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.medis.model.course.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "dokter")
public class DokterModel extends UserModel {

    @Column
    private String nomorTelefon;

    @Column
    private String institusi;

    @Column
    private String pekerjaan;

    @Column
    private String spesialisasi;

    @Column
    private String kotaKabupaten;

    @Column
    private String jenisKelamin;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate tanggalLahir;

    @OneToMany(mappedBy = "user")
    List<CourseEnrollmentModel> listCourse;

    @OneToMany(mappedBy = "user")
    List<SubCourseEnrollmentModel> listSubCourse;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<MaterialEnrollmentModel> listMaterial;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    List<CourseTransactionModel> listTransaction;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    List<CourseActivityModel> listActivty;
}
