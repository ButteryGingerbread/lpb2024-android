// DisplayAllFragment.java
package com.example.lpb2024;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    private MenuAdapter menuAdapter;
    private List<Menu> menus = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_display_all, container, false);
        menuRecyclerView = rootView.findViewById(R.id.menuRecyclerView);
        menuRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2)); // 2 columns

        // Initialize adapter with click listener
        menuAdapter = new MenuAdapter(getActivity(), menus, this::onMenuClick);
        menuRecyclerView.setAdapter(menuAdapter);

        fetchMenus();

        return rootView;
    }

    private void fetchMenus() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<MenuResponse> call = apiService.getAllMenus();

        call.enqueue(new Callback<MenuResponse>() {
            @Override
            public void onResponse(Call<MenuResponse> call, Response<MenuResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    menus = response.body().getMenus(); // Get the list of menus from the response
                    menuAdapter = new MenuAdapter(getActivity(), menus, DisplayAllFragment.this::onMenuClick);
                    menuRecyclerView.setAdapter(menuAdapter);
                } else {
                    Toast.makeText(getActivity(), "Failed to retrieve menus", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MenuResponse> call, Throwable t) {
                Toast.makeText(getActivity(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onMenuClick(Menu menu) {
        Bundle bundle = new Bundle();
        bundle.putInt("menu_id", menu.getId());
        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        navController.navigate(R.id.action_displayAllFragment_to_singleMenuFragment, bundle);
    }


}




