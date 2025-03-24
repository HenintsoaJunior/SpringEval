package com.spring.spring.dto.payment;

import java.time.ZonedDateTime;

import com.spring.spring.dto.invoice.InvoiceDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class PaymentDetailsDTO {
    private Long id;
    private String external_id;
    private Long amount;
    private String description;
    private String payment_source;
    private ZonedDateTime payment_date;
    private String integration_payment_id;
    private String integration_type;
    private Long invoice_id;
    private ZonedDateTime deleted_at;
    private ZonedDateTime created_at;
    private ZonedDateTime updated_at;
    private InvoiceDTO invoice;
}
