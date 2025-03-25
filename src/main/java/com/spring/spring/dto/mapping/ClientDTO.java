package com.spring.spring.dto.mapping;

import java.time.ZonedDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClientDTO {
    private Long id;
    private String external_id;
    private String address;
    private String zipcode;
    private String city;
    private String company_name;
    private String vat;
    private String company_type;
    private String client_number;
    private Long user_id;
    private Long industry_id;
    private String deleted_at;

    private String created_at;

    private String updated_at;
    private UserDTO user;
    private List<TaskDTO> tasks;
    private List<LeadDTO> leads;
    private List<DocumentsDTO> documents;
    private List<ProjectDTO> projects;
    private List<InvoiceDTO> invoices;
    private List<ContactDTO> contacts;
    private List<AppointmentDTO> appointments;
    private ContactDTO primaryContact;
}