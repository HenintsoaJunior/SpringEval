package com.spring.spring.dto.mapping;

import java.time.ZonedDateTime;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ActivityDTO {
    private Long id;
    private String externalId;
    private String logName;
    private Long causerId;
    private String causerType;
    private String text;
    private String sourceType;
    private Long sourceId;
    private String ipAddress;
    private Map<String, Object> properties;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
    private ZonedDateTime deletedAt;
}