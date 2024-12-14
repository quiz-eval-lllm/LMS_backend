package com.medis.model.request;

import com.medis.model.user.InvitedSpeakerModel;
import com.medis.model.webinar.SeminarModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "request_invited_speaker")
public class RequestInvitedSpeaker {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String requestId;

    @ManyToOne
    SeminarModel seminar;

    @Column
    private String email;

    @NotNull
    @Column(nullable = false)
    private String status = "Waiting for Confirmation";

    @Column
    private String nama;

    @Column
    private String peran;

    @NotNull
    @Column(nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-ddTHH:mm")
    private LocalDateTime dateCreated = LocalDateTime.now();

    @OneToOne(cascade = CascadeType.ALL)
    private InvitedSpeakerModel user;
}

