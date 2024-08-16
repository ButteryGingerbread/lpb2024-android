package com.example.lpb2024;

public class RegisterRequest {
    private String username;
    private String email;
    private String password;
    private String birth_date;
    private String gender;
    private String condition;

    public RegisterRequest(String username, String email, String password, String birth_date, String gender, String condition) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.birth_date = birth_date;
        this.gender = gender;
        this.condition = condition;
    }

    // Getters and setters (if needed)
}
