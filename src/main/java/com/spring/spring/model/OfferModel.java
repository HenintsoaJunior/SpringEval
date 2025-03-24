package com.spring.spring.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.spring.spring.dto.offer.OfferResponseDTO;

import jakarta.servlet.http.HttpSession;

@Service
public class OfferModel {

    @Autowired
    private RestTemplate restTemplate;

    private static final Logger logger = LoggerFactory.getLogger(OfferModel.class);
    private static final String BASE_API_URL = "http://localhost:81/api/offer";

    private HttpHeaders createAuthHeaders(HttpSession session) {
        HttpHeaders headers = new HttpHeaders();
        String token = (String) session.getAttribute("access_token");
        if (token != null) {
            headers.setBearerAuth(token);
        }
        return headers;
    }

    public OfferResponseDTO getAll(int page, HttpSession session) throws Exception {
        String apiUrl = BASE_API_URL + "?page=" + page;

        try {
            HttpHeaders headers = createAuthHeaders(session);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            // Logger la réponse brute
            ResponseEntity<String> rawResponse = restTemplate.exchange(
                apiUrl,
                HttpMethod.GET,
                entity,
                String.class
            );
            logger.info("Raw response from {}: Status={}, Body={}", apiUrl, rawResponse.getStatusCode(), rawResponse.getBody());

            ResponseEntity<OfferResponseDTO> responseEntity = restTemplate.exchange(
                apiUrl,
                HttpMethod.GET,
                entity,
                OfferResponseDTO.class
            );

            OfferResponseDTO response = responseEntity.getBody();
            if (response != null && "success".equals(response.getStatus())) {
                logger.info("Successfully retrieved {} offers for page {}", 
                    response.getData().getOffers().size(), page);
                return response;
            } else {
                logger.warn("Invalid response from API for page {}", page);
                throw new Exception("Aucune donnée d'offre disponible ou réponse invalide");
            }
        } catch (HttpClientErrorException.Unauthorized e) {
            logger.warn("Unauthorized access attempt: {}", e.getMessage());
            throw new Exception("Accès non autorisé");
        } catch (Exception e) {
            logger.error("Error retrieving offers for page {}: {}", page, e.getMessage());
            throw new Exception("Erreur lors de la récupération des offres: " + e.getMessage());
        }
    }

    public int totalOffers(HttpSession session) throws Exception {
        String apiUrl = BASE_API_URL;

        try {
            HttpHeaders headers = createAuthHeaders(session);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            // Logger la réponse brute
            ResponseEntity<String> rawResponse = restTemplate.exchange(
                apiUrl,
                HttpMethod.GET,
                entity,
                String.class
            );
            logger.info("Raw response from {}: Status={}, Body={}", apiUrl, rawResponse.getStatusCode(), rawResponse.getBody());

            ResponseEntity<OfferResponseDTO> responseEntity = restTemplate.exchange(
                apiUrl,
                HttpMethod.GET,
                entity,
                OfferResponseDTO.class
            );

            OfferResponseDTO response = responseEntity.getBody();
            if (response != null && "success".equals(response.getStatus()) && response.getData() != null) {
                int total = response.getData().getOffers().size();
                logger.info("Successfully retrieved total offers: {}", total);
                return total;
            } else {
                logger.warn("Invalid response from API when getting total offers");
                throw new Exception("Impossible de récupérer le nombre total d'offres");
            }
        } catch (HttpClientErrorException.Unauthorized e) {
            logger.warn("Unauthorized access attempt: {}", e.getMessage());
            throw new Exception("Accès non autorisé");
        } catch (Exception e) {
            logger.error("Error retrieving total offers: {}", e.getMessage());
            throw new Exception("Erreur lors de la récupération du nombre total d'offres: " + e.getMessage());
        }
    }
}