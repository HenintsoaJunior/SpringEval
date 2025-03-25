package com.spring.spring.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.spring.dto.chart1.Chart1ResponseDTO;
import com.spring.spring.dto.chart2.Chart2DataDTO;
import com.spring.spring.dto.chart2.Chart2ResponseDTO;
import com.spring.spring.dto.chart3.Chart3DataDTO;
import com.spring.spring.dto.chart3.Chart3ResponseDTO;
import com.spring.spring.model.ChartModel;

import jakarta.servlet.http.HttpSession;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/api/spring")
public class ChartController {

    private static final Logger logger = LoggerFactory.getLogger(ChartController.class);

    @Autowired
    private ChartModel chartModel;

    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping("/chart1")
    public String getChartData(Model model, HttpSession session) {
        try {
            Chart1ResponseDTO response = chartModel.getChart1Data(session);
            model.addAttribute("status", response.getStatus());
            model.addAttribute("chartData", response.getData());
            return "pages/graph/chart";
        } catch (Exception e) {
            logger.error("Error in getChartData: {}", e.getMessage());
            model.addAttribute("error", e.getMessage());
            if (e.getMessage().contains("non autorisé")) {
                return "redirect:/";
            }
            return "pages/graph/chart";
        }
    }

    @GetMapping("/chart2")
    public String getChart2Data(
            @RequestParam(required = false) String year,
            @RequestParam(required = false) String startMonth,
            @RequestParam(required = false) String endMonth,
            Model model,
            HttpSession session) {
        try {
            Chart2ResponseDTO response = chartModel.getChart2Data(session);
            List<Chart2DataDTO> filteredData = response.getData();

            // Debugging : Afficher les paramètres reçus
            logger.info("Paramètres reçus - year: {}, startMonth: {}, endMonth: {}", year, startMonth, endMonth);

            // Appliquer les filtres de manière combinée
            filteredData = filteredData.stream()
                .filter(d -> {
                    String paymentMonth = d.getPayment_month(); // Format attendu : "YYYY-MM"
                    String paymentYear = paymentMonth.substring(0, 4);
                    String paymentMonthNum = paymentMonth.substring(5, 7);

                    // Filtre par année (si spécifié)
                    boolean matchesYear = (year == null || year.isEmpty()) || paymentYear.equals(year);

                    // Filtre par mois de début (si spécifié)
                    boolean matchesStartMonth = (startMonth == null || startMonth.isEmpty()) || paymentMonthNum.compareTo(startMonth) >= 0;

                    // Filtre par mois de fin (si spécifié)
                    boolean matchesEndMonth = (endMonth == null || endMonth.isEmpty()) || paymentMonthNum.compareTo(endMonth) <= 0;

                    // Debugging : Afficher les valeurs pour chaque entrée
                    logger.debug("paymentMonth: {}, matchesYear: {}, matchesStartMonth: {}, matchesEndMonth: {}", 
                                paymentMonth, matchesYear, matchesStartMonth, matchesEndMonth);

                    // Retourner true si tous les critères applicables sont satisfaits
                    return matchesYear && matchesStartMonth && matchesEndMonth;
                })
                .collect(Collectors.toList());

            // Debugging : Afficher les données filtrées
            logger.info("Données filtrées : {}", filteredData);

            String chartDataJson = objectMapper.writeValueAsString(filteredData);
            model.addAttribute("status", response.getStatus());
            model.addAttribute("chartDataJson", chartDataJson);

            List<String> years = response.getData().stream()
                .map(d -> d.getPayment_month().substring(0, 4))
                .distinct()
                .sorted()
                .collect(Collectors.toList());
            model.addAttribute("years", years);

            model.addAttribute("selectedYear", year);
            model.addAttribute("selectedStartMonth", startMonth);
            model.addAttribute("selectedEndMonth", endMonth);
            return "pages/graph/chart2";
        } catch (Exception e) {
            logger.error("Error in getChart2Data: {}", e.getMessage());
            model.addAttribute("error", e.getMessage());
            if (e.getMessage().contains("non autorisé")) {
                return "redirect:/";
            }
            return "pages/graph/chart2";
        }
    }

    @GetMapping("/chart3")
    public String getChart3Data(
            @RequestParam(required = false) String status,
            Model model,
            HttpSession session) {
        try {
            Chart3ResponseDTO response = chartModel.getChart3Data(session);
            List<Chart3DataDTO> filteredData = response.getData();

            if (status != null && !status.isEmpty()) {
                filteredData = filteredData.stream()
                    .filter(d -> d.getInvoice_status().equals(status))
                    .collect(Collectors.toList());
            }

            String chartDataJson = objectMapper.writeValueAsString(filteredData);
            model.addAttribute("status", response.getStatus());
            model.addAttribute("chartDataJson", chartDataJson);

            List<String> statuses = response.getData().stream()
                .map(Chart3DataDTO::getInvoice_status)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
            model.addAttribute("statuses", statuses);
            model.addAttribute("selectedStatus", status);
            return "pages/graph/chart3";
        } catch (Exception e) {
            logger.error("Error in getChart3Data: {}", e.getMessage());
            model.addAttribute("error", e.getMessage());
            if (e.getMessage().contains("non autorisé")) {
                return "redirect:/";
            }
            return "pages/graph/chart3";
        }
    }
}