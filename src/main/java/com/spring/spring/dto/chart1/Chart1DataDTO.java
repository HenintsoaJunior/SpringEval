package com.spring.spring.dto.chart1;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Chart1DataDTO {
    private Integer client_id;
    private String external_id;
    private String company_name;
    private Integer total_invoices;
    private String total_invoiced_amount;
    private String total_paid_amount;
    private String outstanding_amount;
}
