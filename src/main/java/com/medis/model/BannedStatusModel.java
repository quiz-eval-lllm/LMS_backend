package com.medis.model;

import com.medis.model.Id.BannedStatusId;
import com.medis.model.user.UserModel;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "banned_status")
public class BannedStatusModel {
    @EmbeddedId
    private BannedStatusId bannedStatusId = new BannedStatusId();

    @OneToOne(fetch = FetchType.LAZY, optional = true)
    // @JoinColumn(name = "user_id")
    private UserModel user;

    @Column(nullable = false)
    @NotNull
    private String jenisPelanggaran;

    @Column(nullable = false)
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime masaBerlaku;

    public void setUser(UserModel user) {
        if(user != null){
            this.user = user;
            bannedStatusId.setUserId(user.getUuid());
        }else {
            this.user = null;
        }
    }
}

