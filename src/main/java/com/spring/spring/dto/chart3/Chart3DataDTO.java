package com.spring.spring.dto.chart3;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Chart3DataDTO {
    private String invoice_status;
    private Integer total_invoices;
    private Integer total_payments;
    private String total_paid_amount;  // String because it can be null in JSON
    private String total_invoiced_amount;
    private String outstanding_amount;
}