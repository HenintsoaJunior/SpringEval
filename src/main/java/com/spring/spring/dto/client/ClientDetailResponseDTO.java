package com.spring.spring.dto.client;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ClientDetailResponseDTO {
    private ClientDataDTO data;  // Changement ici pour encapsuler la liste et la pagination
    private String status;
    private Object errors;     // Changé en Object car "errors" est null dans l'exemple
}