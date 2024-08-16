package com.example.lpb2024;

import com.google.gson.annotations.SerializedName;

public class Menu {
    private int id;
    @SerializedName("menu_name")
    private String menuName;
    @SerializedName("menu_ingredients")
    private String menuIngredients;
    @SerializedName("menu_instructions")
    private String menuInstructions;
    @SerializedName("menu_category")
    private String menuCategory;
    @SerializedName("menu_image")
    private String menuImage;

    // Getters and Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getMenuIngredients() {
        return menuIngredients;
    }

    public void setMenuIngredients(String menuIngredients) {
        this.menuIngredients = menuIngredients;
    }

    public String getMenuInstructions() {
        return menuInstructions;
    }

    public void setMenuInstructions(String menuInstructions) {
        this.menuInstructions = menuInstructions;
    }

    public String getMenuCategory() {
        return menuCategory;
    }

    public void setMenuCategory(String menuCategory) {
        this.menuCategory = menuCategory;
    }

    public String getMenuImage() {
        return menuImage;
    }

    public void setMenuImage(String menuImage) {
        this.menuImage = menuImage;
    }
}
