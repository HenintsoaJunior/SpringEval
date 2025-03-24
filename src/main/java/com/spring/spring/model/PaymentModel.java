package com.spring.spring.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.spring.dto.payment.PaymentDetailsResponseDTO;
import com.spring.spring.dto.payment.PaymentResponseDTO;

import jakarta.servlet.http.HttpSession;

import java.util.HashMap;
import java.util.Map;

@Service
public class PaymentModel {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    private static final Logger logger = LoggerFactory.getLogger(PaymentModel.class);
    private static final String BASE_API_URL = "http://localhost:81/api/payment";
    private static final String DELETE_API_URL = "http://localhost:81/api/payment/delete";
    private static final String UPDATE_API_URL = "http://localhost:81/api/payment/update-amount";

    private HttpHeaders createAuthHeaders(HttpSession session) {
        HttpHeaders headers = new HttpHeaders();
        String token = (String) session.getAttribute("access_token");
        if (token != null) {
            headers.setBearerAuth(token);
        }
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    /**
 * Récupère le montant dû d'une facture par son external_id
 * @param externalId Identifiant externe de la facture (ex: INV_6314bf90_9)
 * @param session Session HTTP contenant le token d'authentification
 * @return Le montant dû (amount_due) de la facture
 * @throws Exception en cas d'erreur
 */
    public Double getInvoiceAmountDue(String externalId, HttpSession session) throws Exception {
        String apiUrl = "http://localhost:81/api/invoice/montantdue?external_id=" + externalId;

        try {
            HttpHeaders headers = createAuthHeaders(session);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<String> responseEntity = restTemplate.exchange(
                apiUrl,
                HttpMethod.GET,
                entity,
                String.class
            );

            // Parse JSON response
            JsonNode jsonResponse = objectMapper.readTree(responseEntity.getBody());
            String status = jsonResponse.get("status").asText();

            if ("success".equals(status)) {
                Double amountDue = jsonResponse.get("data").get("amount_due").asDouble();
                logger.info("Successfully retrieved amount_due: {} for invoice externalId: {}", amountDue, externalId);
                return amountDue;
            } else {
                String errorMessage = jsonResponse.get("errors").get("message").get("message").asText();
                logger.warn("Failed to retrieve amount_due for externalId {}: {}", externalId, errorMessage);
                throw new Exception("Erreur lors de la récupération du montant dû: " + errorMessage);
            }
        } catch (HttpClientErrorException.Unauthorized e) {
            logger.warn("Unauthorized access attempt for invoice externalId {}: {}", externalId, e.getMessage());
            throw new Exception("Accès non autorisé");
        } catch (Exception e) {
            logger.error("Error retrieving amount_due for externalId {}: {}", externalId, e.getMessage());
            throw new Exception("Erreur lors de la récupération du montant dû: " + e.getMessage());
        }
    }

    /**
     * Récupère toutes les paiements avec pagination
     */
    public PaymentResponseDTO getAll(int page, HttpSession session) throws Exception {
        String apiUrl = BASE_API_URL + "?page=" + page;

        try {
            HttpHeaders headers = createAuthHeaders(session);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<PaymentResponseDTO> responseEntity = restTemplate.exchange(
                apiUrl,
                HttpMethod.GET,
                entity,
                PaymentResponseDTO.class
            );

            PaymentResponseDTO response = responseEntity.getBody();
            if (response != null && "success".equals(response.getStatus()) && response.getData() != null) {
                logger.info("Successfully retrieved {} payments for page {}", 
                    response.getData().getPayments().size(), page);
                return response;
            } else {
                logger.warn("Invalid response from API for page {}", page);
                throw new Exception("Aucune donnée de paiement disponible ou réponse invalide");
            }
        } catch (HttpClientErrorException.Unauthorized e) {
            logger.warn("Unauthorized access attempt: {}", e.getMessage());
            throw new Exception("Accès non autorisé");
        } catch (Exception e) {
            logger.error("Error retrieving payments for page {}: {}", page, e.getMessage());
            throw new Exception("Erreur lors de la récupération des paiements: " + e.getMessage());
        }
    }

    /**
     * Récupère les détails d'un paiement par son external_id
     */
    public PaymentDetailsResponseDTO getDetails(String externalId, HttpSession session) throws Exception {
        String apiUrl = BASE_API_URL + "?external_id=" + externalId;

        try {
            HttpHeaders headers = createAuthHeaders(session);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<PaymentDetailsResponseDTO> responseEntity = restTemplate.exchange(
                apiUrl,
                HttpMethod.GET,
                entity,
                PaymentDetailsResponseDTO.class
            );

            PaymentDetailsResponseDTO response = responseEntity.getBody();
            if (response != null && "success".equals(response.getStatus()) && response.getData() != null) {
                logger.info("Successfully retrieved payment details for externalId {}", externalId);
                return response;
            } else {
                logger.warn("Invalid response from API for externalId {}", externalId);
                throw new Exception("Aucun paiement trouvé ou réponse invalide");
            }
        } catch (HttpClientErrorException.Unauthorized e) {
            logger.warn("Unauthorized access attempt for externalId {}: {}", externalId, e.getMessage());
            throw new Exception("Accès non autorisé");
        } catch (Exception e) {
            logger.error("Error retrieving payment details for externalId {}: {}", externalId, e.getMessage());
            throw new Exception("Erreur lors de la récupération des détails du paiement: " + e.getMessage());
        }
    }

    /**
     * Récupère le nombre total de paiements
     */
    public int totalPayments(HttpSession session) throws Exception {
        String apiUrl = BASE_API_URL;

        try {
            HttpHeaders headers = createAuthHeaders(session);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<PaymentResponseDTO> responseEntity = restTemplate.exchange(
                apiUrl,
                HttpMethod.GET,
                entity,
                PaymentResponseDTO.class
            );

            PaymentResponseDTO response = responseEntity.getBody();
            if (response != null && "success".equals(response.getStatus()) && response.getData() != null) {
                int total = response.getData().getPayments().size();
                logger.info("Successfully retrieved total payments: {}", total);
                return total;
            } else {
                logger.warn("Invalid response from API when getting total payments");
                throw new Exception("Impossible de récupérer le nombre total de paiements");
            }
        } catch (HttpClientErrorException.Unauthorized e) {
            logger.warn("Unauthorized access attempt: {}", e.getMessage());
            throw new Exception("Accès non autorisé");
        } catch (Exception e) {
            logger.error("Error retrieving total payments: {}", e.getMessage());
            throw new Exception("Erreur lors de la récupération du nombre total de paiements: " + e.getMessage());
        }
    }

    /**
     * Supprime un paiement par son external_id
     * @param externalId Identifiant externe du paiement
     * @param session Session HTTP contenant le token d'authentification
     * @return Message de succès ou d'erreur
     * @throws Exception en cas d'erreur
     */
    public String deletePayment(String externalId, HttpSession session) throws Exception {
        try {
            logger.info("Attempting to delete payment - ExternalId: {}", externalId);

            HttpHeaders headers = createAuthHeaders(session);
            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("external_id", externalId);
            HttpEntity<Map<String, String>> request = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                DELETE_API_URL,
                HttpMethod.POST,
                request,
                String.class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                logger.info("Payment deleted successfully - ExternalId: {}", externalId);
                return "Paiement supprimé avec succès (External ID: " + externalId + ")";
            } else {
                logger.warn("Failed to delete payment - Status: {}", response.getStatusCode());
                throw new Exception("Erreur lors de la suppression : Statut " + response.getStatusCodeValue());
            }
        } catch (HttpClientErrorException.Unauthorized e) {
            logger.warn("Unauthorized access attempt for delete payment {}: {}", externalId, e.getMessage());
            throw new Exception("Accès non autorisé");
        } catch (Exception e) {
            logger.error("Error deleting payment - ExternalId: {}, Error: {}", externalId, e.getMessage());
            throw new Exception("Erreur lors de la suppression : " + e.getMessage());
        }
    }

    /**
     * Met à jour le montant d'un paiement
     * @param montant Nouveau montant
     * @param paymentExternalId Identifiant externe du paiement
     * @param session Session HTTP contenant le token d'authentification
     * @return Map contenant les résultats (successMessage, errorMessage, montant, paymentExternalId)
     * @throws Exception en cas d'erreur
     */
    public Map<String, Object> updatePaymentAmount(Double montant, String paymentExternalId, HttpSession session) throws Exception {
        Map<String, Object> result = new HashMap<>();

        try {
            logger.info("Attempting to update payment - Montant: {}, PaymentExternalId: {}", montant, paymentExternalId);

            HttpHeaders headers = createAuthHeaders(session);
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("montant", montant);
            requestBody.put("paymentExternalId", paymentExternalId);
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                UPDATE_API_URL,
                HttpMethod.POST,
                request,
                String.class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                JsonNode jsonResponse = objectMapper.readTree(response.getBody());
                String status = jsonResponse.get("status").asText();

                if ("success".equals(status)) {
                    JsonNode paymentData = jsonResponse.get("data");
                    Double updatedAmount = paymentData.get("amount").asDouble();
                    String updatedExternalId = paymentData.get("external_id").asText();

                    result.put("successMessage", "Montant mis à jour avec succès pour le paiement " + updatedExternalId);
                    result.put("montant", updatedAmount);
                    result.put("paymentExternalId", updatedExternalId);
                    logger.info("Payment updated successfully - Montant: {}, PaymentExternalId: {}", updatedAmount, updatedExternalId);
                } else {
                    String errorMessage = jsonResponse.get("errors").get("message").get("message").asText();
                    result.put("errorMessage", "Erreur de l'API : " + errorMessage);
                    logger.warn("API error during payment update - PaymentExternalId: {}, Error: {}", paymentExternalId, errorMessage);
                }
            } else {
                result.put("errorMessage", "Erreur lors de l'appel API : Statut " + response.getStatusCodeValue());
                logger.warn("Failed to update payment - Status: {}", response.getStatusCode());
            }
        } catch (HttpClientErrorException.Unauthorized e) {
            logger.warn("Unauthorized access attempt for update payment {}: {}", paymentExternalId, e.getMessage());
            throw new Exception("Accès non autorisé");
        } catch (Exception e) {
            logger.error("Error processing update - Montant: {}, PaymentExternalId: {}, Error: {}", 
                montant, paymentExternalId, e.getMessage());
            throw new Exception("Erreur lors de la mise à jour : " + e.getMessage());
        }

        return result;
    }
}