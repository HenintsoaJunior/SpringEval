package com.spring.spring.controller;

import com.spring.spring.dto.setting.SettingsResponseDTO;
import com.spring.spring.model.SettingsModel;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/spring")
public class TauxController {

    @Autowired
    private SettingsModel settingsModel;

    private static final Logger logger = LoggerFactory.getLogger(TauxController.class);

    /**
     * Affiche le formulaire pour le taux de remise global.
     *
     * @return La vue du formulaire
     */
    @GetMapping("/form-taux")
    public String showFormTaux(Model model, HttpSession session) {
        try {
            SettingsResponseDTO response = settingsModel.getTaux(session);
            model.addAttribute("taux", response.getData().getGlobal_discount_rate());
            model.addAttribute("status", response.getStatus());
            model.addAttribute("successMessage", response.getMessage());
        } catch (Exception e) {
            logger.error("Error loading form-taux: {}", e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
            if (e.getMessage().contains("non autorisé")) {
                return "redirect:/";
            }
        }
        return "pages/conf/taux";
    }

    /**
     * Récupère et affiche le taux de remise global.
     *
     * @param model   Le modèle pour passer les données à la vue
     * @param session La session HTTP pour l'authentification
     * @return La vue avec les données du taux
     */
    @GetMapping("/taux")
    public String getTaux(Model model, HttpSession session) {
        try {
            SettingsResponseDTO response = settingsModel.getTaux(session);
            model.addAttribute("taux", response.getData().getGlobal_discount_rate());
            model.addAttribute("status", response.getStatus());
            model.addAttribute("successMessage", response.getMessage());
            return "pages/conf/taux";
        } catch (Exception e) {
            logger.error("Error in getTaux: {}", e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
            if (e.getMessage().contains("non autorisé")) {
                return "redirect:/";
            }
            return "pages/conf/taux";
        }
    }

    /**
     * Met à jour le taux de remise global via un formulaire.
     *
     * @param globalDiscountRate Le nouveau taux soumis via le formulaire
     * @param model              Le modèle pour passer les données à la vue
     * @param session            La session HTTP pour l'authentification
     * @return La vue avec le résultat de la mise à jour
     */
    @PostMapping("/taux/update")
    public String updateTaux(@RequestParam("globalDiscountRate") Double globalDiscountRate, 
                             Model model, HttpSession session) {
        try {
            SettingsResponseDTO response = settingsModel.updateTaux(globalDiscountRate, session);
            model.addAttribute("taux", response.getData().getGlobal_discount_rate());
            model.addAttribute("status", response.getStatus());
            model.addAttribute("successMessage", response.getMessage());
            return "pages/conf/taux";
        } catch (Exception e) {
            logger.error("Error in updateTaux: {}", e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
            if (e.getMessage().contains("non autorisé")) {
                return "redirect:/";
            }
            return "pages/conf/taux";
        }
    }
}