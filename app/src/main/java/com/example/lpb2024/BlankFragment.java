package com.example.lpb2024;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BlankFragment extends Fragment {

    private TextView detectedIngredientsTextView;
    private RecyclerView recipesRecyclerView;
    private RecipeAdapter recipeAdapter;
    private List<Recipe> filteredRecipes = new ArrayList<>();
    private List<String> detectedIngredients = new ArrayList<>();
    private String customerCondition;

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_blank, container, false);

        recipesRecyclerView = rootView.findViewById(R.id.recipesRecyclerView);
        detectedIngredientsTextView = rootView.findViewById(R.id.detectedIngredientsTextView);

        Bundle bundle = getArguments();
        if (bundle != null) {
            filteredRecipes = bundle.getParcelableArrayList("filtered_recipes");
            detectedIngredients = bundle.getStringArrayList("detected_classes");
            customerCondition = bundle.getString("customer_condition"); // Assume you pass customerCondition in the Bundle

            if (filteredRecipes != null && !filteredRecipes.isEmpty()) {
                RecipeAdapter.OnRecipeClickListener listener = recipe -> {
                    onMenuClick(recipe);
                };

                recipeAdapter = new RecipeAdapter(getContext(), filteredRecipes, listener);
                recipesRecyclerView.setAdapter(recipeAdapter);
                recipesRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
            } else {
                Toast.makeText(getContext(), "No recipes found", Toast.LENGTH_SHORT).show();
            }

            if (detectedIngredients != null && !detectedIngredients.isEmpty()) {
                detectedIngredientsTextView.setText("Detected Ingredients: " + detectedIngredients.toString());
            } else {
                detectedIngredientsTextView.setText("No ingredients detected.");
            }

            // Fetch filtered recipes with current customer condition and detected ingredients
            if (customerCondition != null) {
                fetchFilteredMenus(customerCondition, detectedIngredients);
            }
        }

        return rootView;
    }

    private void fetchFilteredMenus(String customerCondition, List<String> ingredients) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        ScannedDataRequest request = new ScannedDataRequest(customerCondition, ingredients);
        Call<List<Recipe>> call = apiService.getFilteredRecipes(request); // Adjust method name if needed
        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    filteredRecipes = response.body();
                    if (recipeAdapter != null) {
                        recipeAdapter.updateData(filteredRecipes);
                    } else {
                        // Initialize the adapter if not already done
                        RecipeAdapter.OnRecipeClickListener listener = recipe -> {
                            // Handle recipe click
                        };
                        recipeAdapter = new RecipeAdapter(getContext(), filteredRecipes, listener);
                        recipesRecyclerView.setAdapter(recipeAdapter);
                        recipesRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
                    }
                } else {
                    detectedIngredientsTextView.setText("No recipes found.");
                }
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                detectedIngredientsTextView.setText("Error: " + t.getMessage());
            }
        });
    }

    private void onMenuClick(Recipe recipe) {
        Bundle bundle = new Bundle();
        bundle.putInt("recipe_id", recipe.getId());
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        navController.navigate(R.id.action_blankFragment_to_singleMenuFragment, bundle);
    }
}