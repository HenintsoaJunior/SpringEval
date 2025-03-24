package com.spring.spring.dto.chart3;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Chart3ResponseDTO {
    private String status;
    private List<Chart3DataDTO> data;
    private String errors;
}