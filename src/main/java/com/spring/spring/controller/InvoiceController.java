package com.spring.spring.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.spring.spring.dto.invoice.InvoiceResponseDTO;
import com.spring.spring.model.InvoiceModel;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/api/spring")
public class InvoiceController {

    @Autowired
    private InvoiceModel invoiceModel;

    private static final Logger logger = LoggerFactory.getLogger(InvoiceController.class);

    @GetMapping("/invoices")
    public String getInvoices(@RequestParam(defaultValue = "1") int page, Model model, HttpSession session) {
        try {
            InvoiceResponseDTO response = invoiceModel.getAll(page, session);
            model.addAttribute("invoices", response.getData().getInvoices());
            model.addAttribute("pagination", response.getData().getPagination());
            model.addAttribute("status", response.getStatus());
            return "pages/invoice/invoices";
        } catch (Exception e) {
            logger.error("Error in getInvoices: {}", e.getMessage());
            model.addAttribute("error", e.getMessage());
            if (e.getMessage().contains("non autorisé")) {
                return "redirect:/";
            }
            return "pages/invoice/invoices";
        }
    }
}