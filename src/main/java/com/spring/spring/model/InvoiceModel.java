package com.spring.spring.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.spring.spring.dto.invoice.InvoiceResponseDTO;

import jakarta.servlet.http.HttpSession;

@Service
public class InvoiceModel {

    @Autowired
    private RestTemplate restTemplate;

    private static final Logger logger = LoggerFactory.getLogger(InvoiceModel.class);
    private static final String BASE_API_URL = "http://localhost:81/api/invoice";

    private HttpHeaders createAuthHeaders(HttpSession session) {
        HttpHeaders headers = new HttpHeaders();
        String token = (String) session.getAttribute("access_token");
        if (token != null) {
            headers.setBearerAuth(token);
        }
        return headers;
    }

    /**
     * Récupère toutes les factures avec pagination
     * @param page Numéro de la page
     * @param session Session HTTP contenant le token d'authentification
     * @return InvoiceResponseDTO contenant la liste des factures et les informations de pagination
     * @throws Exception en cas d'erreur
     */
    public InvoiceResponseDTO getAll(int page, HttpSession session) throws Exception {
        String apiUrl = BASE_API_URL + "?page=" + page;

        try {
            HttpHeaders headers = createAuthHeaders(session);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            InvoiceResponseDTO response = restTemplate.exchange(
                apiUrl,
                HttpMethod.GET,
                entity,
                InvoiceResponseDTO.class
            ).getBody();

            if (response != null && "success".equals(response.getStatus())) {
                logger.info("Successfully retrieved {} invoices for page {}", 
                    response.getData().getInvoices().size(), page);
                return response;
            } else {
                logger.warn("Invalid response from API for page {}", page);
                throw new Exception("Aucune donnée de facture disponible ou réponse invalide");
            }
        } catch (HttpClientErrorException.Unauthorized e) {
            logger.warn("Unauthorized access attempt: {}", e.getMessage());
            throw new Exception("Accès non autorisé");
        } catch (Exception e) {
            logger.error("Error retrieving invoices for page {}: {}", page, e.getMessage());
            throw new Exception("Erreur lors de la récupération des factures: " + e.getMessage());
        }
    }

    /**
     * Récupère le nombre total de factures sans pagination
     * @param session Session HTTP contenant le token d'authentification
     * @return Le nombre total de factures
     * @throws Exception en cas d'erreur
     */
    public int totalInvoices(HttpSession session) throws Exception {
        String apiUrl = BASE_API_URL;

        try {
            HttpHeaders headers = createAuthHeaders(session);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            InvoiceResponseDTO response = restTemplate.exchange(
                apiUrl,
                HttpMethod.GET,
                entity,
                InvoiceResponseDTO.class
            ).getBody();

            if (response != null && "success".equals(response.getStatus()) && response.getData() != null) {
                int total = response.getData().getInvoices().size();
                logger.info("Successfully retrieved total invoices: {}", total);
                return total;
            } else {
                logger.warn("Invalid response from API when getting total invoices");
                throw new Exception("Impossible de récupérer le nombre total de factures");
            }
        } catch (HttpClientErrorException.Unauthorized e) {
            logger.warn("Unauthorized access attempt: {}", e.getMessage());
            throw new Exception("Accès non autorisé");
        } catch (Exception e) {
            logger.error("Error retrieving total invoices: {}", e.getMessage());
            throw new Exception("Erreur lors de la récupération du nombre total de factures: " + e.getMessage());
        }
    }
}