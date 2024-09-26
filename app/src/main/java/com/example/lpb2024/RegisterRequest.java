package com.example.lpb2024;

import com.google.gson.annotations.SerializedName;

public class RegisterRequest {
    @SerializedName("username")
    private String username;

    @SerializedName("email")
    private String email;

    @SerializedName("password1")
    private String password1;

    @SerializedName("password2")
    private String password2;

    public RegisterRequest(String username, String email, String password1, String password2) {
        this.username = username;
        this.email = email;
        this.password1 = password1;
        this.password2 = password2;
    }

    // Getters and setters can be added here if needed
}
