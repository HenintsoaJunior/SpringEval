package com.spring.spring.dto.project;

import java.time.ZonedDateTime;
import java.util.List;

import com.spring.spring.dto.mapping.ActivityDTO;
import com.spring.spring.dto.mapping.ClientDTO;
import com.spring.spring.dto.mapping.StatusDTO;
import com.spring.spring.dto.mapping.TaskDTO;
import com.spring.spring.dto.mapping.UserDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class ProjectDetailsDTO {
    private Long id;
    private String external_id;
    private String title;
    private String description;
    private Long status_id;
    private Long user_assigned_id;
    private Long user_created_id;
    private Long client_id;
    private Long invoice_id;
    private ZonedDateTime deadline;
    private ZonedDateTime deleted_at;
    private ZonedDateTime created_at;
    private ZonedDateTime updated_at;
    private StatusDTO status;
    private UserDTO creator;
    private UserDTO assignee;
    private List<Object> documents;
    private ClientDTO client;
    private List<TaskDTO> tasks;
    private List<ActivityDTO> activity;
    private List<Object> comments;
}
