package com.medis.model.webinar;

import com.medis.model.Id.UlasanId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;
import java.io.Serializable;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ulasan")
public class UlasanModel implements Serializable {
//     @Id
//     private String ulasanId;

    @EmbeddedId
    private UlasanId id = new UlasanId();

//    @MapsId("enrollmentId")
//    @OneToOne(cascade = CascadeType.ALL)
//    private EnrollmentModel enrollment;
}
