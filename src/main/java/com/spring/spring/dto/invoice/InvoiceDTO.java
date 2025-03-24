package com.spring.spring.dto.invoice;

import java.util.List;

import com.spring.spring.dto.mapping.ClientDTO;
import com.spring.spring.dto.mapping.InvoiceLineDTO;
import com.spring.spring.dto.mapping.LeadDTO;
import com.spring.spring.dto.mapping.PaymentDTO;
import com.spring.spring.dto.offer.OfferDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class InvoiceDTO {
    private Long id;
    private String external_id;
    private String status;
    private Integer invoice_number;
    private String sent_at; // Chaîne pour gérer le format "2025-03-22 07:30:10"
    private String due_at;
    private String integration_invoice_id;
    private String integration_type;
    private String source_type;
    private Long source_id;
    private Long client_id;
    private Long offer_id;
    private String deleted_at;
    private String created_at;
    private String updated_at;
    private ClientDTO client;
    private List<InvoiceLineDTO> invoice_lines;
    private OfferDTO offer;
    private LeadDTO source; // Nouvelle relation
    private List<PaymentDTO> payments; // Nouvelle relation
}
