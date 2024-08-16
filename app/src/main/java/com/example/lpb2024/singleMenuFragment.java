package com.example.lpb2024;

import android.os.Bundle;
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

    private static final String ARG_MENU_ID = "menu_id";
    private TextView menuTitle;
    private TextView menuIngredients;
    private TextView menuInstructions;
    private ImageView menuImage;

    public static singleMenuFragment newInstance(int menuId) {
        singleMenuFragment fragment = new singleMenuFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_MENU_ID, menuId);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_single_menu, container, false);
        menuTitle = rootView.findViewById(R.id.menuTitle);
        menuIngredients = rootView.findViewById(R.id.menuIngredients);
        menuInstructions = rootView.findViewById(R.id.menuInstructions);
        menuImage = rootView.findViewById(R.id.menuImage);

        if (getArguments() != null) {
            int menuId = getArguments().getInt(ARG_MENU_ID);
            fetchMenuDetails(menuId);
        }

        return rootView;
    }

    private void fetchMenuDetails(int menuId) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<Menu> call = apiService.getMenuById(menuId);

        call.enqueue(new Callback<Menu>() {
            @Override
            public void onResponse(Call<Menu> call, Response<Menu> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Menu menu = response.body();
                    menuTitle.setText(menu.getMenuName());
                    menuIngredients.setText(menu.getMenuIngredients());
                    menuInstructions.setText(menu.getMenuInstructions());

                    // Load the image using Glide
                    Glide.with(getActivity())
                            .load(menu.getMenuImage())
                            .into(menuImage);
                } else {
                    Toast.makeText(getActivity(), "Failed to retrieve menu details", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Menu> call, Throwable t) {
                Toast.makeText(getActivity(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
