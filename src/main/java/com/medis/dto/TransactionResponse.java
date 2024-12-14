package com.medis.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class TransactionResponse {
    private String orderId;
    private String transactionStatus;
    private String paymentType;
    private LocalDateTime transactionTime;
    private LocalDateTime settlementTime;
    private String courseName;
    private long coursePrice;

}
