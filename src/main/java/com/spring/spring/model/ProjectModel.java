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

import com.spring.spring.dto.project.ProjectDataDTO;
import com.spring.spring.dto.project.ProjectDetailsDTO;
import com.spring.spring.dto.project.ProjectResponseDTO;

import jakarta.servlet.http.HttpSession;

@Service
public class ProjectModel {

    @Autowired
    private RestTemplate restTemplate;

    private static final Logger logger = LoggerFactory.getLogger(ProjectModel.class);
    private static final String BASE_API_URL = "http://localhost:81/api/project";

    private HttpHeaders createAuthHeaders(HttpSession session) {
        HttpHeaders headers = new HttpHeaders();
        String token = (String) session.getAttribute("access_token");
        if (token != null) {
            headers.setBearerAuth(token);
        }
        return headers;
    }

    /**
     * Récupère tous les projets avec pagination
     * @param page Numéro de la page
     * @param session Session HTTP contenant le token d'authentification
     * @return ProjectResponseDTO contenant la liste des projets et les informations de pagination
     * @throws Exception en cas d'erreur
     */
    public ProjectResponseDTO getAll(int page, HttpSession session) throws Exception {
        String apiUrl = BASE_API_URL + "?page=" + page;

        try {
            HttpHeaders headers = createAuthHeaders(session);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ProjectResponseDTO response = restTemplate.exchange(
                apiUrl,
                HttpMethod.GET,
                entity,
                ProjectResponseDTO.class
            ).getBody();

            if (response != null && "success".equals(response.getStatus())) {
                logger.info("Successfully retrieved {} projects for page {}", 
                    response.getData().getProjects().size(), page);
                return response;
            } else {
                logger.warn("Invalid response from API for page {}", page);
                throw new Exception("Aucune donnée de projet disponible ou réponse invalide");
            }
        } catch (HttpClientErrorException.Unauthorized e) {
            logger.warn("Unauthorized access attempt: {}", e.getMessage());
            throw new Exception("Accès non autorisé");
        } catch (Exception e) {
            logger.error("Error retrieving projects for page {}: {}", page, e.getMessage());
            throw new Exception("Erreur lors de la récupération des projets: " + e.getMessage());
        }
    }

    /**
     * Récupère les détails d'un projet spécifique
     * @param externalId L'identifiant externe du projet
     * @param session Session HTTP contenant le token d'authentification
     * @return ProjectDetailsDTO contenant les détails du projet
     * @throws Exception en cas d'erreur
     */
    public ProjectDetailsDTO getDetails(String externalId, HttpSession session) throws Exception {
        String apiUrl = BASE_API_URL + "?external_id=" + externalId;

        try {
            HttpHeaders headers = createAuthHeaders(session);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ProjectResponseDTO response = restTemplate.exchange(
                apiUrl,
                HttpMethod.GET,
                entity,
                ProjectResponseDTO.class
            ).getBody();

            if (response != null && "success".equals(response.getStatus()) && response.getData() != null) {
                ProjectDataDTO data = response.getData();
                ProjectDetailsDTO project = data.getProjects().stream()
                    .filter(p -> p.getExternal_id().equals(externalId))
                    .findFirst()
                    .orElse(null);

                if (project != null) {
                    logger.info("Successfully retrieved details for project with externalId: {}", externalId);
                    return project;
                } else {
                    logger.warn("No project found with externalId: {}", externalId);
                    throw new Exception("Aucun projet trouvé avec cet identifiant externe");
                }
            } else {
                logger.warn("Invalid response from API for externalId: {}", externalId);
                throw new Exception("Réponse invalide de l'API");
            }
        } catch (HttpClientErrorException.Unauthorized e) {
            logger.warn("Unauthorized access attempt for project {}: {}", externalId, e.getMessage());
            throw new Exception("Accès non autorisé");
        } catch (Exception e) {
            logger.error("Error retrieving project details for externalId {}: {}", externalId, e.getMessage());
            throw new Exception("Erreur lors de la récupération des détails du projet: " + e.getMessage());
        }
    }

    /**
     * Récupère le nombre total de projets sans pagination
     * @param session Session HTTP contenant le token d'authentification
     * @return Le nombre total de projets
     * @throws Exception en cas d'erreur
     */
    public int totalProjects(HttpSession session) throws Exception {
        String apiUrl = BASE_API_URL;

        try {
            HttpHeaders headers = createAuthHeaders(session);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ProjectResponseDTO response = restTemplate.exchange(
                apiUrl,
                HttpMethod.GET,
                entity,
                ProjectResponseDTO.class
            ).getBody();

            if (response != null && "success".equals(response.getStatus()) && response.getData() != null) {
                int total = response.getData().getProjects().size();
                logger.info("Successfully retrieved total projects: {}", total);
                return total;
            } else {
                logger.warn("Invalid response from API when getting total projects");
                throw new Exception("Impossible de récupérer le nombre total de projets");
            }
        } catch (HttpClientErrorException.Unauthorized e) {
            logger.warn("Unauthorized access attempt: {}", e.getMessage());
            throw new Exception("Accès non autorisé");
        } catch (Exception e) {
            logger.error("Error retrieving total projects: {}", e.getMessage());
            throw new Exception("Erreur lors de la récupération du nombre total de projets: " + e.getMessage());
        }
    }
}