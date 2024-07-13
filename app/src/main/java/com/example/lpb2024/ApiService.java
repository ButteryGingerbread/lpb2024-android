package com.example.lpb2024;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("login")  // This matches the path defined in your Django urls.py
    Call<LoginResponse> loginUser(@Body LoginRequest loginRequest);
}
