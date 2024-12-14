package com.medis.model.user;

import com.medis.model.request.RequestInvitedSpeaker;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Setter
@Getter
//@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "invited_speaker")
public class InvitedSpeakerModel extends UserModel{

    
    @OneToOne(mappedBy = "user")
    // @JoinColumn(name = "address_id", referencedColumnName = "id")
    private RequestInvitedSpeaker request;

    @Column(nullable = false)
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime masaBerlaku;
}
