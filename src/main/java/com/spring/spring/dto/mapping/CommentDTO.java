package com.spring.spring.dto.mapping;

import java.time.ZonedDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class CommentDTO {
    private Long id;
    private String externalId;
    private String description;
    private String sourceType;
    private Long sourceId;
    private Long userId;
    private ZonedDateTime deletedAt;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
}
