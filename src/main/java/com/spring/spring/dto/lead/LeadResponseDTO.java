package com.spring.spring.dto.lead;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class LeadResponseDTO {
    private String status;
    private LeadDataDTO data;
    private Object errors; // Peut être null ou un objet/array en cas d'erreur
}