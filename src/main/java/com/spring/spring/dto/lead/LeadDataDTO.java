package com.spring.spring.dto.lead;

import java.util.List;
import com.spring.spring.dto.mapping.LeadDetailsDTO;
import com.spring.spring.dto.page.PaginationDTO;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class LeadDataDTO {
    private List<LeadDetailsDTO> leads;
    private PaginationDTO pagination;
}