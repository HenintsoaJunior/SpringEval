package com.spring.spring.dto.project;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ProjectResponseDTO {
    private String status;
    private ProjectDataDTO data;
    private Object errors;
}
