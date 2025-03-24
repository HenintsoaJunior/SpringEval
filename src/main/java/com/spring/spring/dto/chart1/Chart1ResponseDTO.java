package com.spring.spring.dto.chart1;

import java.util.List;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Chart1ResponseDTO {
    private String status;
    private List<Chart1DataDTO> data;
    private String errors;
}
