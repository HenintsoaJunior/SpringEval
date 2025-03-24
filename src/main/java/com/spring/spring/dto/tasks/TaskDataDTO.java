package com.spring.spring.dto.tasks;

import java.util.List;

import com.spring.spring.dto.page.PaginationDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class TaskDataDTO {
    private List<TaskDetailsDTO> tasks;
    private PaginationDTO pagination;
}
