package com.spring.spring.dto.invoiceline;

import java.util.List;

import com.spring.spring.dto.page.PaginationDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class InvoiceLineDataDTO {
    private List<InvoiceLineDetailsDTO> invoiceLines;
    private PaginationDTO pagination;
}
