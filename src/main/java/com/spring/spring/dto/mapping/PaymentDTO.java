package com.spring.spring.dto.mapping;

import java.time.ZonedDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class PaymentDTO {
    private Long id;
    private String externalId;
    private Long amount;
    private String description;
    private String paymentSource;
    private ZonedDateTime paymentDate;
    private String integrationPaymentId;
    private String integrationType;
    private Long invoiceId;
    private ZonedDateTime deletedAt;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
}
