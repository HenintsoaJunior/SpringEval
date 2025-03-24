package com.spring.spring.controller;

import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.spring.dto.chart1.Chart1ResponseDTO;
import com.spring.spring.dto.chart2.Chart2DataDTO;
import com.spring.spring.dto.chart2.Chart2ResponseDTO;
import com.spring.spring.dto.chart3.Chart3DataDTO;
import com.spring.spring.dto.chart3.Chart3ResponseDTO;
import com.spring.spring.model.ChartModel;
import com.spring.spring.model.ClientModel;
import com.spring.spring.model.InvoiceModel;
import com.spring.spring.model.OfferModel;
import com.spring.spring.model.PaymentModel;
import com.spring.spring.model.ProjectModel;
import com.spring.spring.model.TaskModel;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class Controllers {
    private static final Logger logger = LoggerFactory.getLogger(Controllers.class);

    @Autowired
    private ClientModel clientModel;
    
    @Autowired
    private ProjectModel projectModel;
    
    @Autowired
    private TaskModel taskModel;
    
    @Autowired
    private OfferModel offerModel;
    
    @Autowired
    private InvoiceModel invoiceModel;
    
    @Autowired
    private PaymentModel paymentModel;
    
    @Autowired
    private ChartModel chartModel;
    
    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping("/api/spring/dashboard")
    public String showDashboard(
            @RequestParam(required = false) String year,
            @RequestParam(required = false) String month,
            @RequestParam(required = false) String status,
            Model model, 
            HttpSession session) {
        try {
            // Partie statistiques générales
            int totalClients = clientModel.totalClients(session);
            model.addAttribute("totalClients", totalClients);
            
            int totalProjects = projectModel.totalProjects(session);
            model.addAttribute("totalProjects", totalProjects);
            
            int totalTasks = taskModel.totalTasks(session);
            model.addAttribute("totalTasks", totalTasks);
            
            int totalOffers = offerModel.totalOffers(session);
            model.addAttribute("totalOffers", totalOffers);
            
            int totalInvoices = invoiceModel.totalInvoices(session);
            model.addAttribute("totalInvoices", totalInvoices);
            
            int totalPayments = paymentModel.totalPayments(session);
            model.addAttribute("totalPayments", totalPayments);

            // Partie Chart 1
            try {
                Chart1ResponseDTO chart1Response = chartModel.getChart1Data(session);
                model.addAttribute("chart1Status", chart1Response.getStatus());
                model.addAttribute("chart1Data", chart1Response.getData());
            } catch (Exception e) {
                logger.error("Error in Chart 1 data: {}", e.getMessage());
                model.addAttribute("chart1Error", e.getMessage());
            }

            // Partie Chart 2
            try {
                Chart2ResponseDTO chart2Response = chartModel.getChart2Data(session);
                List<Chart2DataDTO> filteredData = chart2Response.getData();

                if (year != null && !year.isEmpty()) {
                    filteredData = filteredData.stream()
                        .filter(d -> d.getPayment_month().startsWith(year))
                        .collect(Collectors.toList());
                }
                if (month != null && !month.isEmpty()) {
                    filteredData = filteredData.stream()
                        .filter(d -> d.getPayment_month().substring(5, 7).equals(month))
                        .collect(Collectors.toList());
                }

                String chart2DataJson = objectMapper.writeValueAsString(filteredData);
                model.addAttribute("chart2Status", chart2Response.getStatus());
                model.addAttribute("chart2DataJson", chart2DataJson);

                List<String> years = chart2Response.getData().stream()
                    .map(d -> d.getPayment_month().substring(0, 4))
                    .distinct()
                    .sorted()
                    .collect(Collectors.toList());
                model.addAttribute("years", years);
                model.addAttribute("selectedYear", year);
                model.addAttribute("selectedMonth", month);
            } catch (Exception e) {
                logger.error("Error in Chart 2 data: {}", e.getMessage());
                model.addAttribute("chart2Error", e.getMessage());
            }

            // Partie Chart 3
            try {
                Chart3ResponseDTO chart3Response = chartModel.getChart3Data(session);
                List<Chart3DataDTO> filteredData = chart3Response.getData();

                if (status != null && !status.isEmpty()) {
                    filteredData = filteredData.stream()
                        .filter(d -> d.getInvoice_status().equals(status))
                        .collect(Collectors.toList());
                }

                String chart3DataJson = objectMapper.writeValueAsString(filteredData);
                model.addAttribute("chart3Status", chart3Response.getStatus());
                model.addAttribute("chart3DataJson", chart3DataJson);

                List<String> statuses = chart3Response.getData().stream()
                    .map(Chart3DataDTO::getInvoice_status)
                    .distinct()
                    .sorted()
                    .collect(Collectors.toList());
                model.addAttribute("statuses", statuses);
                model.addAttribute("selectedStatus", status);
            } catch (Exception e) {
                logger.error("Error in Chart 3 data: {}", e.getMessage());
                model.addAttribute("chart3Error", e.getMessage());
            }

            model.addAttribute("status", "success");
        } catch (Exception e) {
            logger.error("Error in showDashboard: {}", e.getMessage());
            model.addAttribute("error", e.getMessage());
            if (e.getMessage().contains("non autorisé")) {
                return "redirect:/";
            }
        }
        
        return "pages/dashboard"; // Afficher la vue dashboard
    }
}