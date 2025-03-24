package com.spring.spring.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.spring.spring.dto.offer.OfferResponseDTO;
import com.spring.spring.model.OfferModel;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/api/spring")
public class OfferController {

    @Autowired
    private OfferModel offerModel;

    private static final Logger logger = LoggerFactory.getLogger(OfferController.class);

    @GetMapping("/offers")
    public String getOffers(@RequestParam(defaultValue = "1") int page, Model model, HttpSession session) {
        try {
            OfferResponseDTO response = offerModel.getAll(page, session);
            model.addAttribute("offers", response.getData().getOffers());
            model.addAttribute("pagination", response.getData().getPagination());
            model.addAttribute("status", response.getStatus());
            return "pages/offer/offers";
        } catch (Exception e) {
            logger.error("Error in getOffers: {}", e.getMessage());
            model.addAttribute("error", e.getMessage());
            if (e.getMessage().contains("non autorisé")) {
                return "redirect:/";
            }
            return "pages/offer/offers";
        }
    }
}