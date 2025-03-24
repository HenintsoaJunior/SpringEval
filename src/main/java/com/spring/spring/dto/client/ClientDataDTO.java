package com.spring.spring.dto.client;

import java.util.List;

import com.spring.spring.dto.mapping.ClientDTO;
import com.spring.spring.dto.page.PaginationDTO;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


@NoArgsConstructor @AllArgsConstructor

public class ClientDataDTO {
    private List<ClientDTO> clients;
    private PaginationDTO pagination;

    // Getters, setters, et constructeurs
    public List<ClientDTO> getClients() { return clients; }
    public void setClients(List<ClientDTO> clients) { this.clients = clients; }
    public PaginationDTO getPagination() { return pagination; }
    public void setPagination(PaginationDTO pagination) { this.pagination = pagination; }
}