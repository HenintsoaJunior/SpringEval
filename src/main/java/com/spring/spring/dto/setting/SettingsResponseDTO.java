package com.spring.spring.dto.setting;

public class SettingsResponseDTO {
    private String status;
    private Data data;
    private Object[] errors;
    private String message;

    // Getters et Setters
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public Object[] getErrors() {
        return errors;
    }

    public void setErrors(Object[] errors) {
        this.errors = errors;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    // Classe interne pour les données
    public static class Data {
        private Double global_discount_rate;

        public Double getGlobal_discount_rate() {
            return global_discount_rate;
        }

        public void setGlobal_discount_rate(Double global_discount_rate) {
            this.global_discount_rate = global_discount_rate;
        }

        
    }
}