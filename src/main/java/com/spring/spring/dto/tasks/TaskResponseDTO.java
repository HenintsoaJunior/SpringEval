package com.spring.spring.dto.tasks;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class TaskResponseDTO {
    private String status;
    private TaskDataDTO data;
    private Object errors;
}
