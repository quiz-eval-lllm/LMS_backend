package com.medis.model.user;

import com.medis.model.request.RequestPenyelenggaraModel;
import com.medis.model.webinar.SeminarModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@Setter
@Getter
//@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "penyelenggara")
public class PenyelenggaraModel extends UserModel {

    @OneToMany(mappedBy = "penyelenggara")
    private List<SeminarModel> listSeminar;

    @NotNull
    @Column(nullable = false)
    private Integer kuotaPembuatan;

    @Column
    private String nomorTelefon;


    @NotNull
    @Column(nullable = false)
    private String organisasi;

    @NotNull
    @Column(nullable = false)
    private String pekerjaan;



    @OneToOne(mappedBy = "user")
    // @JoinColumn(name = "address_id", referencedColumnName = "id")
    private RequestPenyelenggaraModel request;

}
