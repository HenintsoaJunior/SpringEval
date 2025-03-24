package com.spring.spring.dto.offer;

import java.util.List;

import com.spring.spring.dto.mapping.InvoiceDTO;
import com.spring.spring.dto.mapping.InvoiceLineDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class OfferDTO {
    private Long id;
    private String external_id;
    private String sent_at;
    private String source_type;
    private Long source_id;
    private Long client_id;
    private String status;
    private String deleted_at;
    private String created_at;
    private String updated_at;
    private List<InvoiceLineDTO> invoice_lines;
    private InvoiceDTO invoice;
}
