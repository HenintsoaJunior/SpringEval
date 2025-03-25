package com.spring.spring.dto.invoice;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class InvoiceListResponseDTO {
    private String status;
    private InvoiceDataDTO data; // Pour les listes paginées
    private Object errors;
}