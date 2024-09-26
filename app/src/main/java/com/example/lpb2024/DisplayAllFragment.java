package com.example.lpb2024;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
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

public class DisplayAllFragment extends Fragment {

    private RecyclerView menuRecyclerView;
    private RecipeAdapter recipeAdapter;
    private List<Recipe> recipes = new ArrayList<>();
    private Spinner customerSpinner;
    private List<Customer> customers = new ArrayList<>();

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_display_all, container, false);

        menuRecyclerView = rootView.findViewById(R.id.menuRecyclerView);
        customerSpinner = rootView.findViewById(R.id.customerSpinner);

        menuRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2)); // 2 columns

        // Initialize adapter
        recipeAdapter = new RecipeAdapter(getActivity(), recipes, recipe -> {
            // Handle recipe click here
            onMenuClick(recipe);
        });
        menuRecyclerView.setAdapter(recipeAdapter);

        // Fetch customers to populate spinner
        fetchCustomers();

        return rootView;
    }

    private void fetchCustomers() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<List<Customer>> call = apiService.getCustomers();

        call.enqueue(new Callback<List<Customer>>() {
            @Override
            public void onResponse(Call<List<Customer>> call, Response<List<Customer>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    customers = response.body();
                    updateCustomerSpinner();
                } else {
                    Toast.makeText(getActivity(), "Failed to retrieve customers", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Customer>> call, Throwable t) {
                Toast.makeText(getActivity(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateCustomerSpinner() {
        ArrayAdapter<Customer> adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, customers);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        customerSpinner.setAdapter(adapter);

        // Handle spinner item selection
        customerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Fetch and display recipes based on selected customer condition
                Customer selectedCustomer = (Customer) parent.getItemAtPosition(position);
                fetchRecipesByCondition(selectedCustomer.getCondition());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Optional: Handle when no item is selected
            }
        });
    }

    private void fetchRecipesByCondition(String condition) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<List<Recipe>> call = apiService.getRecipesByCondition(condition);

        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    recipes = response.body();
                    recipeAdapter.updateData(recipes); // Use updateData to refresh the adapter
                } else {
                    Toast.makeText(getActivity(), "Failed to retrieve recipes", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Toast.makeText(getActivity(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onMenuClick(Recipe recipe) {
        Bundle bundle = new Bundle();
        bundle.putInt("recipe_id", recipe.getId());
        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        navController.navigate(R.id.action_displayAllFragment_to_singleMenuFragment, bundle);
    }

}
