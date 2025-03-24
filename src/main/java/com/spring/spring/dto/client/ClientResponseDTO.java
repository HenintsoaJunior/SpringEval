package com.spring.spring.dto.client;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ClientResponseDTO {
    private String status;
    private ClientDataDTO data;
    private Object errors;
}