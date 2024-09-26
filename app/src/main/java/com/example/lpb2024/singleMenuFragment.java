package com.example.lpb2024;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class singleMenuFragment extends Fragment {

    private TextView recipeNameTextView;
    private TextView menuIngredientsTextView;
    private TextView menuInstructionsTextView;
    private ImageView recipeImageView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_single_menu, container, false);

        recipeNameTextView = rootView.findViewById(R.id.menuName);
        menuIngredientsTextView = rootView.findViewById(R.id.menuIngredients);
        menuInstructionsTextView = rootView.findViewById(R.id.menuInstructions);
        recipeImageView = rootView.findViewById(R.id.menuImage);

        Bundle bundle = getArguments();
        if (bundle != null) {
            int recipeId = bundle.getInt("recipe_id");
            fetchRecipeDetails(recipeId);
        }

        return rootView;
    }

    private void fetchRecipeDetails(int recipeId) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<Recipe> call = apiService.getRecipeById(recipeId);
        call.enqueue(new Callback<Recipe>() {
            @Override
            public void onResponse(Call<Recipe> call, Response<Recipe> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Recipe recipe = response.body();
                    recipeNameTextView.setText(recipe.getMenuName());

                    String[] ingredientsArray = recipe.getMenuIngredients().split(",");
                    StringBuilder formattedIngredients = new StringBuilder("<b>Ingredients:</b><br>");
                    for (String ingredient : ingredientsArray) {
                        formattedIngredients.append("- ").append(ingredient.trim()).append("<br>");
                    }

                    String[] instructionsArray = recipe.getMenuInstructions().split("\n");
                    StringBuilder formattedInstructions = new StringBuilder("<b>Instructions:</b><br>");
                    int step = 1;
                    for (String instruction : instructionsArray) {
                        formattedInstructions.append(step).append(". ").append(instruction.trim()).append("<br>");
                        step++;
                    }

                    menuIngredientsTextView.setText(Html.fromHtml(formattedIngredients.toString()));
                    menuInstructionsTextView.setText(Html.fromHtml(formattedInstructions.toString()));

                    Glide.with(getContext())
                            .load(recipe.getMenuImage())
                            .into(recipeImageView);
                } else {
                    Toast.makeText(getContext(), "Recipe not found", Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(Call<Recipe> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
