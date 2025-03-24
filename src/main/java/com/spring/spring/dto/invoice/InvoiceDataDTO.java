package com.spring.spring.dto.invoice;

import java.util.List;

import com.spring.spring.dto.page.PaginationDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class InvoiceDataDTO {
    private List<InvoiceDTO> invoices;
    private PaginationDTO pagination;
}
