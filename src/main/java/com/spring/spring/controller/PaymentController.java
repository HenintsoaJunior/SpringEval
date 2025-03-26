package com.spring.spring.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.spring.spring.dto.payment.PaymentDetailsResponseDTO;
import com.spring.spring.dto.payment.PaymentResponseDTO;
import com.spring.spring.model.PaymentModel;

import jakarta.servlet.http.HttpSession;

import java.util.Map;

@Controller
@RequestMapping("/api/spring")
public class PaymentController {

    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);

    @Autowired
    private PaymentModel paymentModel;

    @GetMapping("/delete")
    public String deletePayment(
            @RequestParam("external_id") String externalId,
            RedirectAttributes redirectAttributes,
            HttpSession session) { 
        try {
            String successMessage = paymentModel.deletePayment(externalId, session);
            redirectAttributes.addFlashAttribute("successMessage", successMessage);
            return "redirect:/api/spring/dashboard";
        } catch (Exception e) {
            logger.error("Error in deletePayment: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            if (e.getMessage().contains("non autorisé")) {
                return "redirect:/";
            }
            return "redirect:/api/spring/dashboard";
        }
    }

    @GetMapping("/update-montant")
    public String showFormUpdate(
            @RequestParam("externalId") String externalId,
            @RequestParam("paymentExternalId") String paymentExternalId,
            @RequestParam("amount") String amount,
            Model model,
            HttpSession session) {
        String token = (String) session.getAttribute("access_token");
        if (token == null) {
            return "redirect:/";
        }

        try {
            Double amountDue = paymentModel.getInvoiceAmountDue(externalId, session);
            logger.info("AMOUUND DUE ZANDRY A "+amountDue);
            
            if (amountDue <= 0.0) {
                logger.info("AMOUUND DUE EST BIEN LA");
                model.addAttribute("amountDue", 0.0);
            }
            model.addAttribute("amountDue", amountDue); 
            
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
        }

        model.addAttribute("externalId", externalId);
        model.addAttribute("paymentExternalId", paymentExternalId);
        model.addAttribute("amount", amount);
        return "pages/payment/update_montant";
    }

    @PostMapping("/valid-update")
    public String validUpdate(
            @RequestParam("montant") Double montant,
            @RequestParam("paymentExternalId") String paymentExternalId,
            Model model,
            HttpSession session) {
        try {
            Map<String, Object> result = paymentModel.updatePaymentAmount(montant, paymentExternalId, session);
            if (result.containsKey("successMessage")) {
                model.addAttribute("successMessage", result.get("successMessage"));
                model.addAttribute("montant", result.get("montant"));
                model.addAttribute("paymentExternalId", result.get("paymentExternalId"));
            }
            if (result.containsKey("errorMessage")) {
                model.addAttribute("errorMessage", result.get("errorMessage"));
            }
            return "pages/payment/update_montant";
        } catch (Exception e) {
            logger.error("Error in validUpdate: {}", e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
            if (e.getMessage().contains("non autorisé")) {
                return "redirect:/";
            }
            return "pages/payment/update_montant";
        }
    }

    @GetMapping("/payments")
    public String getPayments(@RequestParam(defaultValue = "1") int page, Model model, HttpSession session) {
        try {
            PaymentResponseDTO response = paymentModel.getAll(page, session);
            model.addAttribute("payments", response.getData().getPayments());
            model.addAttribute("pagination", response.getData().getPagination());
            model.addAttribute("status", response.getStatus());
            return "pages/payment/payments";
        } catch (Exception e) {
            logger.error("Error in getPayments: {}", e.getMessage());
            model.addAttribute("error", e.getMessage());
            if (e.getMessage().contains("non autorisé")) {
                return "redirect:/";
            }
            return "pages/payment/payments";
        }
    }

    @GetMapping("/payment/{externalId}")
    public String getPaymentDetails(@PathVariable("externalId") String externalId, Model model, HttpSession session) {
        try {
            PaymentDetailsResponseDTO response = paymentModel.getDetails(externalId, session);
            if (response.getData() != null && response.getData().getExternal_id().equals(externalId)) {
                model.addAttribute("payment", response.getData());
                model.addAttribute("status", response.getStatus());
                if (response.getData().getInvoice() != null) {
                    model.addAttribute("invoice", response.getData().getInvoice());
                }
            } else {
                model.addAttribute("error", "Aucun paiement trouvé avec cet identifiant externe.");
            }
            return "pages/payment/show";
        } catch (Exception e) {
            logger.error("Error in getPaymentDetails: {}", e.getMessage());
            model.addAttribute("error", e.getMessage());
            if (e.getMessage().contains("non autorisé")) {
                return "redirect:/";
            }
            return "pages/payment/show";
        }
    }
}