package com.spring.spring.dto.offer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OfferResponseDTO {
    private String status;
    private OfferDataDTO data;
    private Object errors; // Peut être null ou un objet, donc Object est approprié
}