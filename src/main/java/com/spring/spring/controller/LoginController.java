package com.spring.spring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.spring.spring.dto.user.LoginResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.http.HttpSession; // Ajouté pour la gestion de session

@Controller
public class LoginController {

    @Autowired
    private RestTemplate restTemplate;
    private static final Logger logger = LoggerFactory.getLogger(ChartController.class);
    private static final String API_URL = "http://localhost:81/api/auth/login";
    private static final String LOGOUT_API_URL = "http://localhost:81/api/auth/logout";

    @GetMapping("/")
    public String showLoginPage(Model model) {
        return "pages/loginAdminPage";
    }

    private HttpHeaders createAuthHeaders(HttpSession session) {
        HttpHeaders headers = new HttpHeaders();
        String token = (String) session.getAttribute("access_token");
        if (token != null) {
            headers.setBearerAuth(token);
        }
        return headers;
    }

    @GetMapping("/api/spring/logout")
    public String logOut(Model model, HttpSession session) {
        try {
            HttpHeaders headers = createAuthHeaders(session);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(
                LOGOUT_API_URL,
                HttpMethod.GET,
                entity,
                String.class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                String responseBody = response.getBody();
                if (responseBody != null && responseBody.contains("Successfully logged out")) {
                    session.invalidate();
                    model.addAttribute("successMessage", "Déconnexion réussie");
                } else {
                    model.addAttribute("errorMessage", "Réponse inattendue lors de la déconnexion");
                }
            } else {
                model.addAttribute("errorMessage", "Échec de la déconnexion via l'API");
            }
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Erreur lors de la déconnexion : " + e.getMessage());
        }
        return "pages/loginAdminPage";
    }

    @PostMapping("/api/spring/login")
    public String processLogin(
        @RequestParam("email") String email,
        @RequestParam("password") String password,
        RedirectAttributes redirectAttributes,  // Remplace Model par RedirectAttributes
        HttpSession session) {
        
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String requestBody = String.format("{\"email\":\"%s\",\"password\":\"%s\"}", email, password);
        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);
        
        try {
            ResponseEntity<LoginResponse> response = restTemplate.postForEntity(
                API_URL,
                request,
                LoginResponse.class
            );
            
            if (response.getStatusCode().is2xxSuccessful()) {
                LoginResponse loginResponse = response.getBody();
                if (loginResponse != null && loginResponse.getAccess_token() != null) {
                    session.setAttribute("access_token", loginResponse.getAccess_token());
                    session.setAttribute("token_type", loginResponse.getToken_type());
                    session.setAttribute("expires_in", loginResponse.getExpires_in());
                    
                    // Utilisation de addFlashAttribute au lieu de addAttribute
                    redirectAttributes.addFlashAttribute("user", loginResponse.getUser());
                    redirectAttributes.addFlashAttribute("message", "Connexion réussie");
                    
                    logger.info("Login successful for email: " + email);
                    return "redirect:/api/spring/dashboard";  // Redirection vers /api/spring/dashboard
                } else {
                    redirectAttributes.addFlashAttribute("errorMessage", "Échec de l'authentification");
                    logger.info("Login failed for email: " + email);
                    return "pages/loginAdminPage";
                }
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Erreur lors de la connexion à l'API");
                logger.error("API connection error: " + response.getStatusCode());
                return "pages/loginAdminPage";
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Échec de l'authentification");
            logger.error("Exception during login: " + e.getMessage());
            return "pages/loginAdminPage";
        }
    }
}