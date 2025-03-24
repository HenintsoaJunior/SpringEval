package com.spring.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.spring.spring.dto.invoiceline.InvoiceLineResponseDTO;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/spring")
public class InvoiceLineController {

    @Autowired
    private RestTemplate restTemplate;

    private static final String API_URL = "http://localhost:81/api/invoiceLine";

    // Méthode utilitaire pour créer les headers avec le token Bearer
    private HttpHeaders createAuthHeaders(HttpSession session) {
        HttpHeaders headers = new HttpHeaders();
        String token = (String) session.getAttribute("access_token");
        if (token != null) {
            headers.setBearerAuth(token);
        }
        return headers;
    }

    @GetMapping("/invoice-line")
    public ResponseEntity<InvoiceLineResponseDTO> getInvoiceLines(HttpSession session) {
        try {
            HttpHeaders headers = createAuthHeaders(session);
            HttpEntity<String> entity = new HttpEntity<>(headers);
            
            InvoiceLineResponseDTO response = restTemplate.exchange(
                API_URL,
                HttpMethod.GET,
                entity,
                InvoiceLineResponseDTO.class
            ).getBody();

            if (response != null) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.noContent().build();
            }
        } catch (HttpClientErrorException.Unauthorized e) {
            return ResponseEntity.status(401).build(); // Retourne un statut 401 pour non autorisé
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }
}