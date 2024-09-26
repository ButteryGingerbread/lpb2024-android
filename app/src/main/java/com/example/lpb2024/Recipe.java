package com.example.lpb2024;

import android.os.Parcel;
import android.os.Parcelable;

public class Recipe implements Parcelable {
    private Integer id;
    private String menu_name;
    private String menu_ingredients;
    private String menu_instructions;
    private String menu_image;

    // Constructor
    public Recipe(int id, String menu_name, String menu_ingredients, String menu_instructions, String menu_image) {
        this.id = id;
        this.menu_name = menu_name;
        this.menu_ingredients = menu_ingredients;
        this.menu_instructions = menu_instructions;
        this.menu_image = menu_image;
    }

    // Parcelable implementation
    protected Recipe(Parcel in) {
        id = in.readInt();
        menu_name = in.readString();
        menu_ingredients = in.readString();
        menu_instructions = in.readString();
        menu_image = in.readString();
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(menu_name);
        dest.writeString(menu_ingredients);
        dest.writeString(menu_instructions);
        dest.writeString(menu_image);
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getMenuName() {
        return menu_name;
    }

    public String getMenuIngredients() {
        return menu_ingredients;
    }

    public String getMenuInstructions() {
        return menu_instructions;
    }

    public String getMenuImage() {
        return menu_image;
    }
}
