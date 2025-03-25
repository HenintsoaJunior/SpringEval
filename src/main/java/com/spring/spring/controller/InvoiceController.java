package com.spring.spring.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.spring.spring.dto.invoice.InvoiceDTO;
import com.spring.spring.dto.invoice.InvoiceListResponseDTO;
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
            InvoiceListResponseDTO response = invoiceModel.getAll(page, session); // Changement du type de retour
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

    @GetMapping("/invoice/{externalId}")
    public String getInvoiceDetails(@PathVariable("externalId") String externalId, Model model, HttpSession session) {
        try {
            InvoiceDTO invoice = invoiceModel.getDetails(externalId, session);
            model.addAttribute("invoice", invoice);
            model.addAttribute("status", "success");
            model.addAttribute("client", invoice.getClient());
            model.addAttribute("invoiceLines", invoice.getInvoice_lines());
            model.addAttribute("offer", invoice.getOffer());
            model.addAttribute("source", invoice.getSource());
            model.addAttribute("payments", invoice.getPayments());
            double total = invoice.getInvoice_lines().stream()
            .mapToDouble(line -> line.getQuantity() * line.getPrice())
            .sum();
            model.addAttribute("total", total);

            return "pages/invoice/show";
        } catch (Exception e) {
            logger.error("Error in getInvoiceDetails: {}", e.getMessage());
            model.addAttribute("error", e.getMessage());
            if (e.getMessage().contains("non autorisé")) {
                return "redirect:/";
            }
            return "pages/invoice/show"; // Correction du chemin (était "show")
        }
    }
}