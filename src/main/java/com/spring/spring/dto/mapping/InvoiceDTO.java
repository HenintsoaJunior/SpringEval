package com.spring.spring.dto.mapping;

import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class InvoiceDTO {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("external_id")
    private String externalId;

    @JsonProperty("status")
    private String status;

    @JsonProperty("invoice_number")
    private Integer invoiceNumber;

    @JsonProperty("sent_at")
    private String sentAt; // Note : le format est une chaîne ici ("2025-03-22 07:30:10")

    @JsonProperty("due_at")
    private ZonedDateTime dueAt;

    @JsonProperty("integration_invoice_id")
    private String integrationInvoiceId;

    @JsonProperty("integration_type")
    private String integrationType;

    @JsonProperty("source_type")
    private String sourceType;

    @JsonProperty("source_id")
    private Long sourceId;

    @JsonProperty("client_id")
    private Long clientId;

    @JsonProperty("offer_id")
    private Long offerId;

    @JsonProperty("deleted_at")
    private String deletedAt;

    @JsonProperty("created_at")
    private String createdAt;

    @JsonProperty("updated_at")
    private String updatedAt;
}