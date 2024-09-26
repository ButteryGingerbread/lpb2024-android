package com.example.lpb2024;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {
    @POST("/user/login/")
    Call<LoginResponse> loginUser(@Body LoginRequest loginRequest);

    @POST("/user/register/")
    Call<RegisterResponse> registerUser(@Body RegisterRequest registerRequest);

    @GET("/menu/menu-detail/{menu_id}/")
    Call<Recipe> getRecipeById(@Path("menu_id") int id);

    @GET("/menu/display-data/{menu_category}")
    Call<List<Recipe>> getRecipesByCondition(@Path("menu_category") String condition);

    @POST("/customer/create/")
    Call<CustomerResponse> createCustomer(@Body CustomerRequest customerRequest);

    @GET("/customer/list/")
    Call<List<Customer>> getCustomers();

    @POST("/ingredients/filter-recipes/")
    Call<List<Recipe>> getFilteredRecipes(@Body ScannedDataRequest request);
}
