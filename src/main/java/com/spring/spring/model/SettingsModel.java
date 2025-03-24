package com.spring.spring.model;

import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.spring.spring.dto.setting.SettingsResponseDTO;

@Service
public class SettingsModel {

    @Autowired
    private RestTemplate restTemplate;

    private static final Logger logger = LoggerFactory.getLogger(SettingsModel.class);
    private static final String BASE_API_URL = "http://localhost:81/api/taux";

    private HttpHeaders createAuthHeaders(HttpSession session) {
        HttpHeaders headers = new HttpHeaders();
        String token = (String) session.getAttribute("access_token");
        if (token != null) {
            headers.setBearerAuth(token);
        }
        return headers;
    }

    /**
     * Récupère le taux de remise global depuis l'API.
     *
     * @param session Session HTTP contenant le token d'authentification
     * @return SettingsResponseDTO contenant le taux de remise
     * @throws Exception en cas d'erreur
     */
    public SettingsResponseDTO getTaux(HttpSession session) throws Exception {
        String apiUrl = BASE_API_URL;

        try {
            HttpHeaders headers = createAuthHeaders(session);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            SettingsResponseDTO response = restTemplate.exchange(
                apiUrl,
                HttpMethod.GET,
                entity,
                SettingsResponseDTO.class
            ).getBody();

            if (response != null && "success".equals(response.getStatus())) {
                logger.info("Successfully retrieved global discount rate: {}", 
                    response.getData().getGlobal_discount_rate());
                return response;
            } else {
                logger.warn("Invalid response from API");
                throw new Exception("Aucune donnée de taux disponible ou réponse invalide");
            }
        } catch (HttpClientErrorException.Unauthorized e) {
            logger.warn("Unauthorized access attempt: {}", e.getMessage());
            throw new Exception("Accès non autorisé");
        } catch (Exception e) {
            logger.error("Error retrieving global discount rate: {}", e.getMessage());
            throw new Exception("Erreur lors de la récupération du taux: " + e.getMessage());
        }
    }

    /**
     * Met à jour le taux de remise global via l'API.
     *
     * @param globalDiscountRate Le nouveau taux à définir
     * @param session Session HTTP contenant le token d'authentification
     * @return SettingsResponseDTO contenant la réponse de l'API
     * @throws Exception en cas d'erreur
     */
    public SettingsResponseDTO updateTaux(Double globalDiscountRate, HttpSession session) throws Exception {
        String apiUrl = BASE_API_URL;

        try {
            HttpHeaders headers = createAuthHeaders(session);
            headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);

            // Corps de la requête
            String requestBody = "{\"global_discount_rate\": " + globalDiscountRate + "}";
            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

            SettingsResponseDTO response = restTemplate.exchange(
                apiUrl,
                HttpMethod.PUT,
                entity,
                SettingsResponseDTO.class
            ).getBody();

            if (response != null && "success".equals(response.getStatus())) {
                logger.info("Successfully updated global discount rate to: {}", 
                    response.getData().getGlobal_discount_rate());
                return response;
            } else {
                logger.warn("Invalid response from API");
                throw new Exception("Échec de la mise à jour du taux");
            }
        } catch (HttpClientErrorException.Unauthorized e) {
            logger.warn("Unauthorized access attempt: {}", e.getMessage());
            throw new Exception("Accès non autorisé");
        } catch (Exception e) {
            logger.error("Error updating global discount rate: {}", e.getMessage());
            throw new Exception("Erreur lors de la mise à jour du taux: " + e.getMessage());
        }
    }
}