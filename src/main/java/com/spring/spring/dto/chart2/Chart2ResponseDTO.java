package com.spring.spring.dto.chart2;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Chart2ResponseDTO {
    private String status;
    private List<Chart2DataDTO> data;
    private String errors;  // Using String since the JSON shows null or could contain error messages
}