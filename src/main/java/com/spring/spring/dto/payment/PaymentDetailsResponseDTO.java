package com.spring.spring.dto.payment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class PaymentDetailsResponseDTO {
    private String status;
    private PaymentDetailsDTO data;
    private Object errors;
}
