package com.spring.spring.dto.page;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaginationDTO {
    private Integer total;
    private Integer per_page;
    private Integer current_page;
    private Integer last_page;
    private Integer from;
    private Integer to;
}