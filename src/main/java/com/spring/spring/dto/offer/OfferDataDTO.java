package com.spring.spring.dto.offer;

import java.util.List;

import com.spring.spring.dto.page.PaginationDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OfferDataDTO {
    private List<OfferDTO> offers;
    private PaginationDTO pagination;
}