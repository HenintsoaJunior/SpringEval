package com.spring.spring.dto.mapping;

import java.time.ZonedDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class InvoiceLineDTO {
    private Long id;
    private String externalId;
    private String title;
    private String comment;
    private Long price;
    private Long invoiceId;
    private Long offerId;
    private String type;
    private Integer quantity;
    private Long productId;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
    private ZonedDateTime deletedAt;
}
