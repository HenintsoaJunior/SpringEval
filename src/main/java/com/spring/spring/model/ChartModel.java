package com.spring.spring.model;

import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.spring.spring.dto.chart1.Chart1ResponseDTO;
import com.spring.spring.dto.chart2.Chart2ResponseDTO;
import com.spring.spring.dto.chart3.Chart3ResponseDTO;

import jakarta.servlet.http.HttpSession;

@Service
public class ChartModel {

    @Autowired
    private RestTemplate restTemplate;

    private static final Logger logger = LoggerFactory.getLogger(ChartModel.class);

    // Injection de la propriété base-url depuis le fichier de configuration
    @Value("${api.base-url}")
    private String baseUrl;

    private static final String CHART1_API_PATH = "/api/chart1";
    private static final String CHART2_API_PATH = "/api/chart2";
    private static final String CHART3_API_PATH = "/api/chart3";

    private HttpHeaders createAuthHeaders(HttpSession session) {
        HttpHeaders headers = new HttpHeaders();
        String token = (String) session.getAttribute("access_token");
        if (token != null) {
            headers.setBearerAuth(token);
        }
        return headers;
    }

    public Chart1ResponseDTO getChart1Data(HttpSession session) throws Exception {
        try {
            HttpHeaders headers = createAuthHeaders(session);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<Chart1ResponseDTO> responseEntity = restTemplate.exchange(
                baseUrl + CHART1_API_PATH, // Utilisation de baseUrl
                HttpMethod.GET,
                entity,
                Chart1ResponseDTO.class
            );

            Chart1ResponseDTO response = responseEntity.getBody();
            if (response != null && "success".equals(response.getStatus()) && response.getData() != null) {
                logger.info("Successfully retrieved chart1 data with {} entries", response.getData().size());
                return response;
            } else {
                logger.warn("Invalid response from chart1 API");
                throw new Exception("Réponse invalide de l'API chart1");
            }
        } catch (HttpClientErrorException.Unauthorized e) {
            logger.warn("Unauthorized access attempt: {}", e.getMessage());
            throw new Exception("Accès non autorisé");
        } catch (Exception e) {
            logger.error("Error retrieving chart1 data: {}", e.getMessage());
            throw new Exception("Erreur lors de la récupération des données du graphique 1: " + e.getMessage());
        }
    }

    public Chart1ResponseDTO getChart1Data(HttpSession session, String external_id) throws Exception {
        try {
            HttpHeaders headers = createAuthHeaders(session);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<Chart1ResponseDTO> responseEntity = restTemplate.exchange(
                baseUrl + CHART1_API_PATH, // Utilisation de baseUrl
                HttpMethod.GET,
                entity,
                Chart1ResponseDTO.class
            );

            Chart1ResponseDTO response = responseEntity.getBody();
            if (response != null && "success".equals(response.getStatus()) && response.getData() != null) {
                if (external_id != null && !external_id.isEmpty()) {
                    response.setData(
                        response.getData().stream()
                            .filter(data -> external_id.equals(data.getExternal_id()))
                            .collect(Collectors.toList())
                    );
                }
                
                logger.info("Successfully retrieved chart1 data with {} entries", response.getData().size());
                return response;
            } else {
                logger.warn("Invalid response from chart1 API");
                throw new Exception("Réponse invalide de l'API chart1");
            } 
        } catch (HttpClientErrorException.Unauthorized e) {
            logger.warn("Unauthorized access attempt: {}", e.getMessage());
            throw new Exception("Accès non autorisé");
        } catch (Exception e) {
            logger.error("Error retrieving chart1 data: {}", e.getMessage());
            throw new Exception("Erreur lors de la récupération des données du graphique 1: " + e.getMessage());
        }
    }

    public Chart2ResponseDTO getChart2Data(HttpSession session) throws Exception {
        try {
            HttpHeaders headers = createAuthHeaders(session);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<Chart2ResponseDTO> responseEntity = restTemplate.exchange(
                baseUrl + CHART2_API_PATH, // Utilisation de baseUrl
                HttpMethod.GET,
                entity,
                Chart2ResponseDTO.class
            );

            Chart2ResponseDTO response = responseEntity.getBody();
            if (response != null && "success".equals(response.getStatus()) && response.getData() != null) {
                logger.info("Successfully retrieved chart2 data with {} entries", response.getData().size());
                return response;
            } else {
                logger.warn("Invalid response from chart2 API");
                throw new Exception("Réponse invalide de l'API chart2");
            }
        } catch (HttpClientErrorException.Unauthorized e) {
            logger.warn("Unauthorized access attempt: {}", e.getMessage());
            throw new Exception("Accès non autorisé");
        } catch (Exception e) {
            logger.error("Error retrieving chart2 data: {}", e.getMessage());
            throw new Exception("Erreur lors de la récupération des données du graphique 2: " + e.getMessage());
        }
    }

    public Chart3ResponseDTO getChart3Data(HttpSession session) throws Exception {
        try {
            HttpHeaders headers = createAuthHeaders(session);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<Chart3ResponseDTO> responseEntity = restTemplate.exchange(
                baseUrl + CHART3_API_PATH, // Utilisation de baseUrl
                HttpMethod.GET,
                entity,
                Chart3ResponseDTO.class
            );

            Chart3ResponseDTO response = responseEntity.getBody();
            if (response != null && "success".equals(response.getStatus()) && response.getData() != null) {
                logger.info("Successfully retrieved chart3 data with {} entries", response.getData().size());
                return response;
            } else {
                logger.warn("Invalid response from chart3 API");
                throw new Exception("Réponse invalide de l'API chart3");
            }
        } catch (HttpClientErrorException.Unauthorized e) {
            logger.warn("Unauthorized access attempt: {}", e.getMessage());
            throw new Exception("Accès non autorisé");
        } catch (Exception e) {
            logger.error("Error retrieving chart3 data: {}", e.getMessage());
            throw new Exception("Erreur lors de la récupération des données du graphique 3: " + e.getMessage());
        }
    }
}