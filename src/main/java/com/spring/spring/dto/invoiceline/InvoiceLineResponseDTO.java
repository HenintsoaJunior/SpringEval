package com.spring.spring.dto.invoiceline;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class InvoiceLineResponseDTO {
    private String status;
    private InvoiceLineDataDTO data;
    private Object errors;
}
