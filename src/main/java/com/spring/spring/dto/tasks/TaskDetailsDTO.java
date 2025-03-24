package com.spring.spring.dto.tasks;

import java.time.ZonedDateTime;
import java.util.List;

import com.spring.spring.dto.invoice.InvoiceDTO;
import com.spring.spring.dto.mapping.ClientDTO;
import com.spring.spring.dto.mapping.CommentDTO;
import com.spring.spring.dto.mapping.ProjectDTO;
import com.spring.spring.dto.mapping.StatusDTO;
import com.spring.spring.dto.mapping.UserDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class TaskDetailsDTO {
    private Long id;
    private String external_id;
    private String title;
    private String description;
    private Long status_id;
    private Long user_assigned_id;
    private Long user_created_id;
    private Long client_id;
    private Long project_id;
    private ZonedDateTime deadline;
    private ZonedDateTime deleted_at;
    private ZonedDateTime created_at;
    private ZonedDateTime updated_at;
    private UserDTO user; // Utilisateur assigné
    private InvoiceDTO invoice; // Peut être null
    private ClientDTO client;
    private UserDTO creator; // Créateur de la tâche
    private List<CommentDTO> comments;
    private StatusDTO status;
    private List<Object> appointments;
    private ProjectDTO project; // Peut être null
    private List<Object> activity;
    private List<Object> documents;
}
