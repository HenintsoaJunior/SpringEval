package com.spring.spring.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.spring.spring.dto.client.ClientResponseDTO;
import com.spring.spring.dto.mapping.ClientDTO;
import com.spring.spring.model.ClientModel;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/api/spring")
public class ClientController {

    @Autowired
    private ClientModel clientModel;


    @GetMapping("/clients")
    public String getClients(@RequestParam(defaultValue = "1") int page, Model model, HttpSession session) {
        try {
            ClientResponseDTO response = clientModel.getAll(page, session);
            model.addAttribute("clients", response.getData().getClients());
            model.addAttribute("pagination", response.getData().getPagination());
            model.addAttribute("status", response.getStatus());
            return "pages/client/clients";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            if (e.getMessage().contains("non autorisé")) {
                return "redirect:/";
            }
            return "pages/client/clients";
        }
    }

    @GetMapping("/client/{externalId}")
    public String getClientDetails(@PathVariable("externalId") String externalId, Model model, HttpSession session) {
        try {
            ClientDTO client = clientModel.getDetails(externalId, session);
            model.addAttribute("client", client);
            model.addAttribute("status", "success");
            model.addAttribute("tasks", client.getTasks());
            model.addAttribute("leads", client.getLeads());
            model.addAttribute("projects", client.getProjects());
            model.addAttribute("invoices", client.getInvoices());
            return "pages/client/client_details";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            if (e.getMessage().contains("non autorisé")) {
                return "redirect:/";
            }
            return "pages/client/client_details";
        }
    }
}