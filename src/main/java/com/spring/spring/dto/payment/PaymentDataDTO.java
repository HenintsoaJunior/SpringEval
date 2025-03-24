package com.spring.spring.dto.payment;

import java.util.List;

import com.spring.spring.dto.page.PaginationDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class PaymentDataDTO {
    private List<PaymentDetailsDTO> payments;
    private PaginationDTO pagination;
}
