package com.medis.model.webinar;

import com.medis.model.request.RequestInvitedSpeaker;
import com.medis.model.user.PenyelenggaraModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;
import jakarta.persistence.*;
//import jakarta.validation.constraints.NotNull;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "seminar")
public class SeminarModel {
    @Id
    @Column(length = 50)
    private String seminarId;

    @Column(name = "meeting_id")
    private String meetingId;

    @Column(name = "join_url")
    private String joinUrl;

    @Column(name = "title", length = 200)
    private String title;

    @Column(name = "nama_perusahaan")
    private String namaPerusahaan;

    @Column(name = "date_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime dateTime;

    @Column(name = "duration")
    private int duration;

    @Column(name = "lokasi")
    private String lokasi; // kayaknya gaperlu

    @Column(name = "jumlah_tiket")
    private int jumlahTiket;

    @Column(name = "harga")
    private String harga;

    @Column(name = "password")
    private String password;

    @Column(name = "is_finished")
    private Boolean isFinished;

    @Column(name = "is_recorded")
    private Boolean isRecorded;

    @Column(name = "auto_record")
    private Boolean autoRecord;

    @Column(name = "deskripsi", length = 2000)
    private String deskripsi;

    @OneToMany(mappedBy = "seminar")
    private List<TransaksiModel> listTransaksi;

    @OneToMany
    List<RequestInvitedSpeaker> requestSpeaker;

    @OneToMany(mappedBy = "seminar")
    @PrimaryKeyJoinColumn
    private List<EnrollmentModel> listEnrollment;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "penyelenggara_uuid", referencedColumnName = "uuid", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private PenyelenggaraModel penyelenggara;

//    @Column(name="nama_seminar")
//    private String namaSeminar;

    @Column(name="tiket_terjual")
    private int tiketTerjual;

    @Column(name="file_url")
    private String fileUrl;

    @Column(name="photo_url")
    private String photoUrl;

    @Column(name="video_url")
    private String videoUrl;

    @Column(name="certificate_url")
    private String certificateUrl;

    @Transient
    public String getKetersediaan() {
        if(jumlahTiket > tiketTerjual){
            return "Available";
        }
        return "Unavailable";
    }

    public String getStatus() {
        if(LocalDateTime.now().isAfter(dateTime)){
            return "Done";
        }
        return "Upcoming";
    }
}
