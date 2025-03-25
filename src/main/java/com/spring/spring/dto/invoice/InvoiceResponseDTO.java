package com.spring.spring.dto.invoice;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class InvoiceResponseDTO {
    private String status;
    private InvoiceDTO data; // Type InvoiceDTO
    private Object errors;
}