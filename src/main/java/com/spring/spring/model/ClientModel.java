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

import com.spring.spring.dto.client.ClientResponseDTO;
import com.spring.spring.dto.client.ClientDataDTO;
import com.spring.spring.dto.mapping.ClientDTO;

import jakarta.servlet.http.HttpSession;

import java.util.List;

@Service
public class ClientModel {
    
    @Autowired
    private RestTemplate restTemplate;
    
    private static final Logger logger = LoggerFactory.getLogger(ClientModel.class);
    private static final String BASE_API_URL = "http://localhost:81/api/client";
    
    // Méthode utilitaire pour créer les headers avec le token Bearer
    private HttpHeaders createAuthHeaders(HttpSession session) {
        HttpHeaders headers = new HttpHeaders();
        String token = (String) session.getAttribute("access_token");
        if (token != null) {
            headers.setBearerAuth(token);
        }
        return headers;
    }
    
    /**
     * Récupère tous les clients avec pagination
     * @param page Numéro de la page (par défaut 1)
     * @param session Session HTTP contenant le token d'authentification
     * @return ClientResponseDTO contenant la liste des clients et les informations de pagination
     * @throws Exception en cas d'erreur lors de la récupération des données
     */
    public ClientResponseDTO getAll(int page, HttpSession session) throws Exception {
        String apiUrl = BASE_API_URL + "?page=" + page;
        
        try {
            HttpHeaders headers = createAuthHeaders(session);
            HttpEntity<String> entity = new HttpEntity<>(headers);
            
            ClientResponseDTO response = restTemplate.exchange(
                apiUrl,
                HttpMethod.GET,
                entity,
                ClientResponseDTO.class
            ).getBody();
            
            if (response != null && "success".equals(response.getStatus())) {
                logger.info("Successfully retrieved {} clients for page {}", 
                    response.getData().getClients().size(), page);
                return response;
            } else {
                logger.warn("Invalid response from API for page {}", page);
                throw new Exception("Aucune donnée de client disponible ou réponse invalide");
            }
            
        } catch (HttpClientErrorException.Unauthorized e) {
            logger.warn("Unauthorized access attempt: {}", e.getMessage());
            throw new Exception("Accès non autorisé");
        } catch (Exception e) {
            logger.error("Error retrieving clients for page {}: {}", page, e.getMessage());
            throw new Exception("Erreur lors de la récupération des clients: " + e.getMessage());
        }
    }
    
    /**
     * Récupère les détails d'un client spécifique par son externalId
     * @param externalId L'identifiant externe du client
     * @param session Session HTTP contenant le token d'authentification
     * @return ClientDTO contenant les détails du client
     * @throws Exception en cas d'erreur lors de la récupération des données
     */
    public ClientDTO getDetails(String externalId, HttpSession session) throws Exception {
        String apiUrl = BASE_API_URL + "?external_id=" + externalId;
        
        try {
            HttpHeaders headers = createAuthHeaders(session);
            HttpEntity<String> entity = new HttpEntity<>(headers);
            
            ClientResponseDTO response = restTemplate.exchange(
                apiUrl,
                HttpMethod.GET,
                entity,
                ClientResponseDTO.class
            ).getBody();
            
            if (response != null && "success".equals(response.getStatus()) && response.getData() != null) {
                ClientDataDTO data = response.getData();
                ClientDTO client = data.getClients().stream()
                    .filter(c -> c.getExternal_id().equals(externalId))
                    .findFirst()
                    .orElse(null);
                    
                if (client != null) {
                    logger.info("Successfully retrieved details for client with externalId: {}", externalId);
                    return client;
                } else {
                    logger.warn("No client found with externalId: {}", externalId);
                    throw new Exception("Aucun client trouvé avec cet identifiant externe");
                }
            } else {
                logger.warn("Invalid response from API for externalId: {}", externalId);
                throw new Exception("Réponse invalide de l'API");
            }
            
        } catch (HttpClientErrorException.Unauthorized e) {
            logger.warn("Unauthorized access attempt for client {}: {}", externalId, e.getMessage());
            throw new Exception("Accès non autorisé");
        } catch (Exception e) {
            logger.error("Error retrieving client details for externalId {}: {}", externalId, e.getMessage());
            throw new Exception("Erreur lors de la récupération des détails du client: " + e.getMessage());
        }
    }

    /**
     * Compte le nombre total de clients
     * @param session Session HTTP contenant le token d'authentification
     * @return Nombre total de clients
     * @throws Exception en cas d'erreur lors de la récupération des données
     */
    public int totalClients(HttpSession session) throws Exception {
        String apiUrl = BASE_API_URL;
        
        try {
            HttpHeaders headers = createAuthHeaders(session);
            HttpEntity<String> entity = new HttpEntity<>(headers);
            
            ClientResponseDTO response = restTemplate.exchange(
                apiUrl,
                HttpMethod.GET,
                entity,
                ClientResponseDTO.class
            ).getBody();
            
            if (response != null && "success".equals(response.getStatus()) && response.getData() != null) {
                int total = response.getData().getClients().size();
                logger.info("Successfully retrieved total clients: {}", total);
                return total;
            } else {
                logger.warn("Invalid response from API when getting total clients");
                throw new Exception("Impossible de récupérer le nombre total de clients");
            }
        } catch (HttpClientErrorException.Unauthorized e) {
            logger.warn("Unauthorized access attempt: {}", e.getMessage());
            throw new Exception("Accès non autorisé");
        } catch (Exception e) {
            logger.error("Error retrieving total clients: {}", e.getMessage());
            throw new Exception("Erreur lors de la récupération du nombre total de clients: " + e.getMessage());
        }
    }

    /**
     * Récupère la liste complète des clients sans pagination
     * @param session Session HTTP contenant le token d'authentification
     * @return Liste de tous les clients
     * @throws Exception en cas d'erreur lors de la récupération des données
     */
    public List<ClientDTO> getAllClients(HttpSession session) throws Exception {
        String apiUrl = BASE_API_URL; // Pas de paramètre de pagination
        
        try {
            HttpHeaders headers = createAuthHeaders(session);
            HttpEntity<String> entity = new HttpEntity<>(headers);
            
            ClientResponseDTO response = restTemplate.exchange(
                apiUrl,
                HttpMethod.GET,
                entity,
                ClientResponseDTO.class
            ).getBody();
            
            if (response != null && "success".equals(response.getStatus()) && response.getData() != null) {
                List<ClientDTO> allClients = response.getData().getClients();
                logger.info("Successfully retrieved {} clients", allClients.size());
                return allClients;
            } else {
                logger.warn("Invalid response from API when retrieving all clients");
                throw new Exception("Impossible de récupérer la liste des clients");
            }
            
        } catch (HttpClientErrorException.Unauthorized e) {
            logger.warn("Unauthorized access attempt: {}", e.getMessage());
            throw new Exception("Accès non autorisé");
        } catch (Exception e) {
            logger.error("Error retrieving all clients: {}", e.getMessage());
            throw new Exception("Erreur lors de la récupération de tous les clients: " + e.getMessage());
        }
    }
}