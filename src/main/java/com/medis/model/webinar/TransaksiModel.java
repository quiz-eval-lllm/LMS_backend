package com.medis.model.webinar;

import com.medis.model.user.UserModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import jakarta.persistence.*;
//import jakarta.validation.constraints.NotNull;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "transaksi")
public class TransaksiModel {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String transaksiId;

    @Column(name = "order_id")
    private String orderId;

    @Column(name = "snap_token")
    private String snapToken;

    @Column(name = "status_transaksi")
    private String statusTransaksi;

    @Column(name = "status_cause")
    private String statusCause;

    @Column(name = "payment_type")
    private String paymentType;

    @Column(name = "transaction_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd' 'HH:mm")
    private LocalDateTime transactionTime;

    @Column(name = "settlement_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd' 'HH:mm")
    private LocalDateTime settlementTime;

    @Column(name = "token_expire_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime tokenExpireDate;

    @OneToOne(cascade = CascadeType.ALL)
    private EnrollmentModel enrollment;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "uuid", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private UserModel user;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "seminar_id", referencedColumnName = "seminarId", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private SeminarModel seminar;
}
