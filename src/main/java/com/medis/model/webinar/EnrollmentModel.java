package com.medis.model.webinar;

import com.medis.model.Id.EnrollmentId;
import com.medis.model.user.UserModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import jakarta.persistence.*;
//import jakarta.validation.constraints.NotNull;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "enrollment")
public class EnrollmentModel {
    // @Id
    // private String enrollmentId;

    @EmbeddedId
    private EnrollmentId id = new EnrollmentId();


    @OneToOne(mappedBy = "enrollment", fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn
    private SertifikatModel sertifikat;

//    @OneToOne(mappedBy = "enrollment", cascade = CascadeType.ALL)
//    @PrimaryKeyJoinColumn
//    private UlasanModel ulasan;

    @OneToOne(mappedBy = "enrollment", cascade = CascadeType.ALL)
    private TransaksiModel transaksi;

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "user_id")
    private UserModel user;


    @MapsId("seminarId")
    @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "user_id")
    private SeminarModel seminar;

    @Column()
    private String tipeEnrollment;

    // ga kepake kayaknya
    @Column(name = "signature")
    private String signature;

    @Column(name = "rate")
    private Integer rate;

    @Column(name = "signature_expire_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime signatureExpireDate;


}
