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
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import com.spring.spring.dto.lead.LeadResponseDTO;
import com.spring.spring.dto.lead.LeadDataDTO;
import com.spring.spring.dto.mapping.LeadDetailsDTO;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/api/spring")
public class LeadController {

    @Autowired
    private RestTemplate restTemplate;

    private static final Logger logger = LoggerFactory.getLogger(LeadController.class);
    private static final String BASE_API_URL = "http://localhost:81/api/lead";

    // Méthode utilitaire pour créer les headers avec le token Bearer
    private HttpHeaders createAuthHeaders(HttpSession session) {
        HttpHeaders headers = new HttpHeaders();
        String token = (String) session.getAttribute("access_token");
        if (token != null) {
            headers.setBearerAuth(token);
        }
        return headers;
    }

    @GetMapping("/leads")
    public String getLeads(@RequestParam(defaultValue = "1") int page, Model model, HttpSession session) {
        String apiUrl = BASE_API_URL + "?page=" + page;

        try {
            HttpHeaders headers = createAuthHeaders(session);
            HttpEntity<String> entity = new HttpEntity<>(headers);
            
            LeadResponseDTO response = restTemplate.exchange(
                apiUrl,
                HttpMethod.GET,
                entity,
                LeadResponseDTO.class
            ).getBody();

            if (response != null && "success".equals(response.getStatus()) && response.getData() != null) {
                logger.info("Successfully retrieved leads for page {}: {} leads", page, response.getData().getLeads().size());
                model.addAttribute("leads", response.getData().getLeads());
                model.addAttribute("pagination", response.getData().getPagination());
                model.addAttribute("status", response.getStatus());
            } else {
                logger.warn("Invalid response from API for page: {}", page);
                model.addAttribute("error", "Réponse invalide de l'API.");
            }
            return "pages/lead/leads";
        } catch (HttpClientErrorException.Unauthorized e) {
            logger.warn("Unauthorized access attempt for leads page {}: {}", page, e.getMessage());
            return "redirect:/"; // Redirection vers la page de login
        } catch (Exception e) {
            logger.error("Error retrieving leads for page {}: {}", page, e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "Une erreur s'est produite lors de la récupération des données.");
            return "pages/lead/leads";
        }
    }

    @GetMapping("/lead/{externalId}")
    public String getLeadDetails(@PathVariable("externalId") String externalId, Model model, HttpSession session) {
        String apiUrl = BASE_API_URL + "?external_id=" + externalId;

        try {
            HttpHeaders headers = createAuthHeaders(session);
            HttpEntity<String> entity = new HttpEntity<>(headers);
            
            LeadResponseDTO response = restTemplate.exchange(
                apiUrl,
                HttpMethod.GET,
                entity,
                LeadResponseDTO.class
            ).getBody();

            if (response != null && "success".equals(response.getStatus()) && response.getData() != null) {
                LeadDataDTO data = response.getData();
                LeadDetailsDTO lead = null;

                if (data.getLeads() != null && !data.getLeads().isEmpty()) {
                    lead = data.getLeads().get(0);
                }

                if (lead != null) {
                    logger.info("Lead details found for externalId {}: {}", externalId, lead);
                    model.addAttribute("lead", lead);
                    model.addAttribute("status", response.getStatus());
                } else {
                    logger.warn("No lead found with externalId: {}", externalId);
                    model.addAttribute("error", "Aucun lead trouvé avec cet identifiant externe.");
                }
            } else {
                logger.warn("Invalid response from API for externalId: {}", externalId);
                model.addAttribute("error", "Réponse invalide de l'API.");
            }
            return "pages/lead/show";
        } catch (HttpClientErrorException.Unauthorized e) {
            logger.warn("Unauthorized access attempt for lead {}: {}", externalId, e.getMessage());
            return "redirect:/"; // Redirection vers la page de login
        } catch (Exception e) {
            logger.error("Error retrieving lead details for externalId {}: {}", externalId, e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "Une erreur s'est produite lors de la récupération des détails du lead.");
            return "pages/lead/show";
        }
    }
}