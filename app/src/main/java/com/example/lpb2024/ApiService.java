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

    @GET("/menu/display-all/")
    Call<MenuResponse> getAllMenus();


    @GET("/menu/menu-detail/{menu_id}/")
    Call<Menu> getMenuById(@Path("menu_id") int menuId);

    @GET("/menu/display-data/{menu_category}/")
    Call<List<Menu>> getMenusByCategory(@Path("menu_category") String menuCategory);
}
