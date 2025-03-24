package com.spring.spring.dto.payment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class PaymentResponseDTO {
    private String status;
    private PaymentDataDTO data;
    private Object errors;
}
