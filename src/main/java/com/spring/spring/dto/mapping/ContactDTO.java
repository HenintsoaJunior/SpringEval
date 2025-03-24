package com.spring.spring.dto.mapping;

import java.time.ZonedDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class ContactDTO {
    private Long id;
    private String external_id;
    private String name;
    private String email;
    private String primary_number;
    private String secondary_number;
    private Long client_id;
    private Integer is_primary;
    private ZonedDateTime deleted_at;
    private ZonedDateTime created_at;
    private ZonedDateTime updated_at;

}