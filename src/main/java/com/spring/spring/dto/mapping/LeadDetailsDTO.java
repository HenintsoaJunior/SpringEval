package com.spring.spring.dto.mapping;

import java.time.ZonedDateTime;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@JsonIgnoreProperties(ignoreUnknown = true) // Ignorer les champs inconnus
public class LeadDetailsDTO {
    private Long id;
    private String external_id;
    private String title;
    private String description;
    private Long status_id;
    private Long user_assigned_id;
    private Long user_created_id;
    private Long client_id;
    private int qualified; // Nouveau champ
    private String result;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSX")
    private ZonedDateTime deadline;

    private Long invoice_id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSX")
    private ZonedDateTime deleted_at;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSX")
    private ZonedDateTime created_at;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSX")
    private ZonedDateTime updated_at;

    private UserDTO user;
    private UserDTO creator;
    private ClientDTO client;
    private StatusDTO status;
    //private InvoiceDTO invoice;
    // private List<Object> comments;
    // private List<Object> activity;
    // private List<Object> appointments;
    // private List<Object> offers;
}