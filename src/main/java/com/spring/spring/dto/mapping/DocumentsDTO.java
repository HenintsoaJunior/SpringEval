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
public class DocumentsDTO {
    private Long id;
    private String name;
    private Long size;
    private String path;
    private String original_filename;
    private String mime;
    private String external_id;
    private Long client_id;
    private Long integration_id;
    private String integration_type;
    private String source_type;
    private Long source_id;
    private ZonedDateTime deleted_at;
    private ZonedDateTime created_at;
    private ZonedDateTime updated_at;
}
