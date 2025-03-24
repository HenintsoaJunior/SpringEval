package com.spring.spring.dto.mapping;

import java.time.ZonedDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class LeadDTO {
    private Long id;
    private String external_id;
    private String title;
    private String description;
    private Long status_id;
    private Long user_assigned_id;
    private Long client_id;
    private Long user_created_id;
    private Integer qualified;
    private Object result;
    private ZonedDateTime deadline;
    private ZonedDateTime deleted_at;
    private ZonedDateTime created_at;
    private ZonedDateTime updated_at;

}
