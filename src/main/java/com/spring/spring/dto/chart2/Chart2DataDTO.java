package com.spring.spring.dto.chart2;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Chart2DataDTO {
    private String payment_month;
    private Integer total_payments;
    private Integer total_invoices;
    private String total_paid_amount;
    private String total_invoiced_amount;
    private String outstanding_amount;
}