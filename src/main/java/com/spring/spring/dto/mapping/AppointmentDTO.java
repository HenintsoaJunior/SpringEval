package com.spring.spring.dto.mapping;

import java.time.ZonedDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentDTO {
    private Long id;
    private Long userId;
    private Long sourceId;
    private String sourceType;
    private ZonedDateTime startAt;
    private ZonedDateTime endAt;
    private String externalId;
    private String title;
    private String description;
    private String color;
    private Long clientId;
}
