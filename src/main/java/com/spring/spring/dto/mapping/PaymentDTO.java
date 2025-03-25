package com.spring.spring.dto.mapping;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class PaymentDTO {
    private Long id;
    private String external_id;
    private Long amount;
    private String description;
    private String payment_source;
    private String payment_date;
    private String integration_payment_id;
    private String integration_type;
    private Long invoice_id;
    private String deleted_at;
    private String created_at;
    private String updated_at;
}
