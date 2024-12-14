package com.medis.model.user;

import com.medis.model.BannedStatusModel;
import com.medis.model.webinar.EnrollmentModel;
import com.medis.model.webinar.TransaksiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@SuperBuilder
@Inheritance(strategy=InheritanceType.JOINED)
@Table(name = "user")
public class UserModel implements Serializable {

    @Id
    @Column(length = 50)
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String uuid;

    @NotNull
    @Column(name="nama", nullable = false)
    private String nama;

    @NotNull
    @Column(name="email", nullable = false, unique = true)
    private String email;

    @NotNull
    @Column(name="role", nullable = false)
    private String role;

    @NotNull
    @Column(name="password", nullable = false)
    private String password;

    @Column(name="photo_url")
    private String photoUrl = "/img/profile-picture2.jpg";

    @OneToMany(mappedBy = "user")
    private List<TransaksiModel> listTransaksi;

    @OneToMany(mappedBy = "user")
    @PrimaryKeyJoinColumn
    private List<EnrollmentModel> listEnrollment;

    @Column(unique = true)
    private String forgotUrl;

    public void setForgotUrl(String forgotUrl) {
        if (forgotUrl.equals("generate")) {
            this.forgotUrl = UUID.randomUUID().toString();
        }else{
            this.forgotUrl = null;
        }
    }


    @NotNull
    @Column(nullable = false)
    private String bannedStatus = "Clear";

    @OneToOne(mappedBy = "user")
    private BannedStatusModel bannedStatusModel;


    public void setBannedStatusModel(BannedStatusModel bannedStatusModel) {
        if(bannedStatusModel != null){
            this.bannedStatusModel = bannedStatusModel;
            this.bannedStatus = "Banned";
        }else{
            this.bannedStatusModel = null;
            this.bannedStatus = "Clear";
        }
    }
}