package com.example.lpb2024;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("/user/login")
    Call<LoginResponse> loginUser(@Body LoginRequest loginRequest);
}
