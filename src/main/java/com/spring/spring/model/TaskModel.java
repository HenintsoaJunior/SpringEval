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

import com.spring.spring.dto.tasks.TaskDataDTO;
import com.spring.spring.dto.tasks.TaskDetailsDTO;
import com.spring.spring.dto.tasks.TaskResponseDTO;

import jakarta.servlet.http.HttpSession;

@Service
public class TaskModel {

    @Autowired
    private RestTemplate restTemplate;

    private static final Logger logger = LoggerFactory.getLogger(TaskModel.class);
    private static final String BASE_API_URL = "http://localhost:81/api/task";

    private HttpHeaders createAuthHeaders(HttpSession session) {
        HttpHeaders headers = new HttpHeaders();
        String token = (String) session.getAttribute("access_token");
        if (token != null) {
            headers.setBearerAuth(token);
        }
        return headers;
    }

    /**
     * Récupère toutes les tâches avec pagination
     * @param page Numéro de la page
     * @param session Session HTTP contenant le token d'authentification
     * @return TaskResponseDTO contenant la liste des tâches et les informations de pagination
     * @throws Exception en cas d'erreur
     */
    public TaskResponseDTO getAll(int page, HttpSession session) throws Exception {
        String apiUrl = BASE_API_URL + "?page=" + page;

        try {
            HttpHeaders headers = createAuthHeaders(session);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            TaskResponseDTO response = restTemplate.exchange(
                apiUrl,
                HttpMethod.GET,
                entity,
                TaskResponseDTO.class
            ).getBody();

            if (response != null && "success".equals(response.getStatus())) {
                logger.info("Successfully retrieved {} tasks for page {}", 
                    response.getData().getTasks().size(), page);
                return response;
            } else {
                logger.warn("Invalid response from API for page {}", page);
                throw new Exception("Aucune donnée de tâche disponible ou réponse invalide");
            }
        } catch (HttpClientErrorException.Unauthorized e) {
            logger.warn("Unauthorized access attempt: {}", e.getMessage());
            throw new Exception("Accès non autorisé");
        } catch (Exception e) {
            logger.error("Error retrieving tasks for page {}: {}", page, e.getMessage());
            throw new Exception("Erreur lors de la récupération des tâches: " + e.getMessage());
        }
    }

    /**
     * Récupère les détails d'une tâche spécifique
     * @param externalId L'identifiant externe de la tâche
     * @param session Session HTTP contenant le token d'authentification
     * @return TaskDetailsDTO contenant les détails de la tâche
     * @throws Exception en cas d'erreur
     */
    public TaskDetailsDTO getDetails(String externalId, HttpSession session) throws Exception {
        String apiUrl = BASE_API_URL + "?external_id=" + externalId;

        try {
            HttpHeaders headers = createAuthHeaders(session);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            TaskResponseDTO response = restTemplate.exchange(
                apiUrl,
                HttpMethod.GET,
                entity,
                TaskResponseDTO.class
            ).getBody();

            if (response != null && "success".equals(response.getStatus()) && response.getData() != null) {
                TaskDataDTO data = response.getData();
                TaskDetailsDTO task = data.getTasks().stream()
                    .filter(t -> t.getExternal_id().equals(externalId))
                    .findFirst()
                    .orElse(null);

                if (task != null) {
                    logger.info("Successfully retrieved details for task with externalId: {}", externalId);
                    return task;
                } else {
                    logger.warn("No task found with externalId: {}", externalId);
                    throw new Exception("Aucune tâche trouvée avec cet identifiant externe");
                }
            } else {
                logger.warn("Invalid response from API for externalId: {}", externalId);
                throw new Exception("Réponse invalide de l'API");
            }
        } catch (HttpClientErrorException.Unauthorized e) {
            logger.warn("Unauthorized access attempt for task {}: {}", externalId, e.getMessage());
            throw new Exception("Accès non autorisé");
        } catch (Exception e) {
            logger.error("Error retrieving task details for externalId {}: {}", externalId, e.getMessage());
            throw new Exception("Erreur lors de la récupération des détails de la tâche: " + e.getMessage());
        }
    }

    /**
     * Récupère le nombre total de tâches sans pagination
     * @param session Session HTTP contenant le token d'authentification
     * @return Le nombre total de tâches
     * @throws Exception en cas d'erreur
     */
    public int totalTasks(HttpSession session) throws Exception {
        String apiUrl = BASE_API_URL;

        try {
            HttpHeaders headers = createAuthHeaders(session);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            TaskResponseDTO response = restTemplate.exchange(
                apiUrl,
                HttpMethod.GET,
                entity,
                TaskResponseDTO.class
            ).getBody();

            if (response != null && "success".equals(response.getStatus()) && response.getData() != null) {
                int total = response.getData().getTasks().size();
                logger.info("Successfully retrieved total tasks: {}", total);
                return total;
            } else {
                logger.warn("Invalid response from API when getting total tasks");
                throw new Exception("Impossible de récupérer le nombre total de tâches");
            }
        } catch (HttpClientErrorException.Unauthorized e) {
            logger.warn("Unauthorized access attempt: {}", e.getMessage());
            throw new Exception("Accès non autorisé");
        } catch (Exception e) {
            logger.error("Error retrieving total tasks: {}", e.getMessage());
            throw new Exception("Erreur lors de la récupération du nombre total de tâches: " + e.getMessage());
        }
    }
}