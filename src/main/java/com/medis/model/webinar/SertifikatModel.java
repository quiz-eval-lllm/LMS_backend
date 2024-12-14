package com.medis.model.webinar;

import com.medis.model.Id.SertifikatId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "sertifikat")
public class SertifikatModel {
    // @Id
    // private String sertifikatId;

    @EmbeddedId
    private SertifikatId id = new SertifikatId();

    @Column(nullable = false)
    @NotNull
    private String sertifikatUrl;

    @Column
    @NotNull
    private LocalDate createdAt = LocalDate.now();


    @Column
    @NotNull
    private LocalDate expiredAt = LocalDate.now().plusYears(5);

    @MapsId("enrollmentId")
    @OneToOne(cascade = CascadeType.ALL)
    private EnrollmentModel enrollment;
}
