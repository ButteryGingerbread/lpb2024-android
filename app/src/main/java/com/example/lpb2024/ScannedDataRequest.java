package com.example.lpb2024;

import java.util.List;

public class ScannedDataRequest {
    private String customerName;
    private List<String> scannedIngredients;

    // Constructor
    public ScannedDataRequest(String customerName, List<String> scannedIngredients) {
        this.customerName = customerName;
        this.scannedIngredients = scannedIngredients;
    }

    // Getters and setters
    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public List<String> getScannedIngredients() {
        return scannedIngredients;
    }

    public void setScannedIngredients(List<String> scannedIngredients) {
        this.scannedIngredients = scannedIngredients;
    }
}
