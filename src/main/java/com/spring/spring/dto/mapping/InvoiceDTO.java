package com.spring.spring.dto.mapping;

import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class InvoiceDTO {
    private Long id;
    private String externalId;
    private String status;
    private Integer invoiceNumber;
    private String sentAt; // Note : le format est une chaîne ici ("2025-03-22 07:30:10")
    private ZonedDateTime dueAt;
    private String integrationInvoiceId;
    private String integrationType;
    private String sourceType;
    private Long sourceId;
    private Long clientId;
    private Long offerId;
    private ZonedDateTime deletedAt;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
}
