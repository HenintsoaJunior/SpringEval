package com.spring.spring.controller;

import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.spring.dto.chart1.Chart1ResponseDTO;
import com.spring.spring.dto.chart2.Chart2DataDTO;
import com.spring.spring.dto.chart2.Chart2ResponseDTO;
import com.spring.spring.dto.chart2.Chart2SummaryDTO;
import com.spring.spring.dto.chart3.Chart3DataDTO;
import com.spring.spring.dto.chart3.Chart3ResponseDTO;
import com.spring.spring.dto.mapping.ClientDTO;
import com.spring.spring.model.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class Controllers {
    private static final Logger logger = LoggerFactory.getLogger(Controllers.class);

    @Autowired private ClientModel clientModel;
    @Autowired private ProjectModel projectModel;
    @Autowired private TaskModel taskModel;
    @Autowired private OfferModel offerModel;
    @Autowired private InvoiceModel invoiceModel;
    @Autowired private PaymentModel paymentModel;
    @Autowired private ChartModel chartModel;
    @Autowired private ObjectMapper objectMapper;


    @GetMapping("/api/spring/dashboard")
    public String showDashboard(
            @RequestParam(required = false) String year,
            @RequestParam(required = false) String startMonth,
            @RequestParam(required = false) String endMonth,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String external_id,
            Model model,
            HttpSession session) {
        model.asMap().clear();
        
        try {
            loadGeneralStatistics(model, session);
            loadClientData(model, session, external_id);
            loadChart1Data(model, session, external_id);
            loadChart2Data(model, session, year, startMonth, endMonth);
            loadChart3Data(model, session, status);
            loadPayemntPrice(model, session);
            model.addAttribute("status", "success");
        } catch (Exception e) {
            handleException(model, e);
            if (e.getMessage().contains("non autorisé")) {
                return "redirect:/";
            }
        }
        
        return "pages/dashboard";
    }

    private void loadPayemntPrice(Model model, HttpSession session) {
        try {
            Chart2ResponseDTO chart2Response = chartModel.getChart2Data(session);
            
            List<Chart2SummaryDTO> summaryData = chart2Response.getData().stream()
                .map(data -> new Chart2SummaryDTO(
                    data.getTotal_paid_amount(),
                    data.getTotal_invoiced_amount(),
                    data.getOutstanding_amount()
                ))
                .collect(Collectors.toList());
            
            String chart2DataJson = objectMapper.writeValueAsString(summaryData);
            model.addAttribute("chart2Status", chart2Response.getStatus());
            //model.addAttribute("paymentsData", chart2DataJson);
            String data = chart2DataJson;
            String jsonPart = data.substring(data.indexOf("["));
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(jsonPart).get(0);
            
            model.addAttribute("paymentsData", jsonNode.get("total_paid_amount").asText());
            
        } catch (Exception e) {
            logger.error("Error in Chart 2 summary data: {}", e.getMessage());
            model.addAttribute("chart2Error", e.getMessage());
        }
    }

    private void loadGeneralStatistics(Model model, HttpSession session) throws Exception {
        model.addAttribute("totalClients", clientModel.totalClients(session));
        model.addAttribute("totalProjects", projectModel.totalProjects(session));
        model.addAttribute("totalTasks", taskModel.totalTasks(session));
        model.addAttribute("totalOffers", offerModel.totalOffers(session));
        model.addAttribute("totalInvoices", invoiceModel.totalInvoices(session));
        model.addAttribute("totalPayments", paymentModel.totalPayments(session));
        model.addAttribute("totalFacture", invoiceModel.getTotalInvoicesPrice(session));
    }

    private void loadClientData(Model model, HttpSession session, String external_id) throws Exception {
        List<ClientDTO> clients = clientModel.getAllClients(session);
        model.addAttribute("clients", clients);
        model.addAttribute("selectedexternal_id", external_id);
    }

    private void loadChart1Data(Model model, HttpSession session, String external_id) {
        try {
            Chart1ResponseDTO chart1Response = chartModel.getChart1Data(session, external_id);
            model.addAttribute("chart1Status", chart1Response.getStatus());
            model.addAttribute("chart1Data", chart1Response.getData());
        } catch (Exception e) {
            logger.error("Error in Chart 1 data: {}", e.getMessage());
            model.addAttribute("chart1Error", e.getMessage());
        }
    }

    private void loadChart2Data(Model model, HttpSession session, String year, 
                              String startMonth, String endMonth) {
        try {
            Chart2ResponseDTO chart2Response = chartModel.getChart2Data(session);
            List<Chart2DataDTO> filteredData = filterChart2Data(chart2Response.getData(), 
                                                              year, startMonth, endMonth);
            
            String chart2DataJson = objectMapper.writeValueAsString(filteredData);
            model.addAttribute("chart2Status", chart2Response.getStatus());
            model.addAttribute("chart2DataJson", chart2DataJson);

            List<String> years = getChart2Years(chart2Response.getData());
            model.addAttribute("years", years);
            model.addAttribute("selectedYear", year);
            model.addAttribute("selectedStartMonth", startMonth);
            model.addAttribute("selectedEndMonth", endMonth);
        } catch (Exception e) {
            logger.error("Error in Chart 2 data: {}", e.getMessage());
            model.addAttribute("chart2Error", e.getMessage());
        }
    }

    private List<Chart2DataDTO> filterChart2Data(List<Chart2DataDTO> data, String year, 
                                               String startMonth, String endMonth) {
        return data.stream()
            .filter(d -> {
                String paymentMonth = d.getPayment_month();
                String paymentYear = paymentMonth.substring(0, 4);
                String paymentMonthNum = paymentMonth.substring(5, 7);

                boolean matchesYear = (year == null || year.isEmpty()) || paymentYear.equals(year);
                boolean matchesStartMonth = (startMonth == null || startMonth.isEmpty()) || 
                    paymentMonthNum.compareTo(startMonth) >= 0;
                boolean matchesEndMonth = (endMonth == null || endMonth.isEmpty()) || 
                    paymentMonthNum.compareTo(endMonth) <= 0;

                return matchesYear && matchesStartMonth && matchesEndMonth;
            })
            .collect(Collectors.toList());
    }

    private List<String> getChart2Years(List<Chart2DataDTO> data) {
        return data.stream()
            .map(d -> d.getPayment_month().substring(0, 4))
            .distinct()
            .sorted()
            .collect(Collectors.toList());
    }

    private void loadChart3Data(Model model, HttpSession session, String status) {
        try {
            Chart3ResponseDTO chart3Response = chartModel.getChart3Data(session);
            List<Chart3DataDTO> filteredData = filterChart3Data(chart3Response.getData(), status);

            String chart3DataJson = objectMapper.writeValueAsString(filteredData);
            model.addAttribute("chart3Status", chart3Response.getStatus());
            model.addAttribute("chart3DataJson", chart3DataJson);

            List<String> statuses = getChart3Statuses(chart3Response.getData());
            model.addAttribute("statuses", statuses);
            model.addAttribute("selectedStatus", status);
        } catch (Exception e) {
            logger.error("Error in Chart 3 data: {}", e.getMessage());
            model.addAttribute("chart3Error", e.getMessage());
        }
    }

    private List<Chart3DataDTO> filterChart3Data(List<Chart3DataDTO> data, String status) {
        if (status != null && !status.isEmpty()) {
            return data.stream()
                .filter(d -> d.getInvoice_status().equals(status))
                .collect(Collectors.toList());
        }
        return data;
    }

    private List<String> getChart3Statuses(List<Chart3DataDTO> data) {
        return data.stream()
            .map(Chart3DataDTO::getInvoice_status)
            .distinct()
            .sorted()
            .collect(Collectors.toList());
    }

    private void handleException(Model model, Exception e) {
        logger.error("Error in showDashboard: {}", e.getMessage());
        model.addAttribute("error", e.getMessage());
    }
}