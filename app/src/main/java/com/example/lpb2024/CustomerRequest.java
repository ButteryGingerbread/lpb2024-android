package com.example.lpb2024;

public class CustomerRequest {
    public String name;
    public String condition;

    public CustomerRequest(String name, String condition) {
        this.name = name;
        this.condition = condition;
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }
}


