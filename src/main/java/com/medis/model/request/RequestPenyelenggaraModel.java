package com.medis.model.request;

import com.medis.model.user.PenyelenggaraModel;
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
@Table(name = "request_penyelenggara")
public class RequestPenyelenggaraModel {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String requestId;

    @NotNull
    @Column(nullable = false)
    private String nama;

    @Column
    private String nomorTelefon;

    @NotNull
    @Column(nullable = false)
    private String email;

    @NotNull
    @Column(nullable = false)
    private String organisasi;

    @NotNull
    @Column(nullable = false)
    private String pekerjaan;

    @NotNull
    @Column(nullable = false)
    private String status = "Waiting for Confirmation";

    @NotNull
    @Column(nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-ddTHH:mm")
    private LocalDateTime dateCreated = LocalDateTime.now();

    @OneToOne(cascade = CascadeType.ALL)
    private PenyelenggaraModel user;
}
