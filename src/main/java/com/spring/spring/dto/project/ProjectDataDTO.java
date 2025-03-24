package com.spring.spring.dto.project;

import java.util.List;

import com.spring.spring.dto.page.PaginationDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ProjectDataDTO {
    private List<ProjectDetailsDTO> projects;
    private PaginationDTO pagination;
}
