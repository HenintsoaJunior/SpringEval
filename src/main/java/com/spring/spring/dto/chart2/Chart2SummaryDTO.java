package com.spring.spring.dto.chart2;

import java.math.BigDecimal;

public class Chart2SummaryDTO {
    private String total_paid_amount;
    private String total_invoiced_amount;
    private String outstanding_amount;

    // Constructor accepting Strings
    public Chart2SummaryDTO(String total_paid_amount, 
                          String total_invoiced_amount, 
                          String outstanding_amount) {
        this.total_paid_amount = total_paid_amount;
        this.total_invoiced_amount = total_invoiced_amount;
        this.outstanding_amount = outstanding_amount;
    }

    // Getters and setters
    public String getTotal_paid_amount() {
        return total_paid_amount;
    }

    public void setTotal_paid_amount(String total_paid_amount) {
        this.total_paid_amount = total_paid_amount;
    }

    public String getTotal_invoiced_amount() {
        return total_invoiced_amount;
    }

    public void setTotal_invoiced_amount(String total_invoiced_amount) {
        this.total_invoiced_amount = total_invoiced_amount;
    }

    public String getOutstanding_amount() {
        return outstanding_amount;
    }

    public void setOutstanding_amount(String outstanding_amount) {
        this.outstanding_amount = outstanding_amount;
    }

    // Optional: Add methods to get values as BigDecimal if needed
    public BigDecimal getTotalPaidAmountAsBigDecimal() {
        return total_paid_amount != null ? new BigDecimal(total_paid_amount) : null;
    }

    public BigDecimal getTotalInvoicedAmountAsBigDecimal() {
        return total_invoiced_amount != null ? new BigDecimal(total_invoiced_amount) : null;
    }

    public BigDecimal getOutstandingAmountAsBigDecimal() {
        return outstanding_amount != null ? new BigDecimal(outstanding_amount) : null;
    }
}