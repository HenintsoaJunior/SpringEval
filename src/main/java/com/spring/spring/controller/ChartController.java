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
            @RequestParam(required = false) String month,
            Model model,
            HttpSession session) {
        try {
            Chart2ResponseDTO response = chartModel.getChart2Data(session);
            List<Chart2DataDTO> filteredData = response.getData();

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
            model.addAttribute("selectedMonth", month);
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