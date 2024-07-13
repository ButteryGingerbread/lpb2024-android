package com.example.lpb2024;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class DisplayAllFragment extends Fragment {

    private LinearLayout menuSection;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_display_all, container, false);

        menuSection = rootView.findViewById(R.id.menu);

        // Populate section with sample data
        addMenuItemToSection("Menu 1", R.drawable.sample_image);
        addMenuItemToSection("Menu 2", R.drawable.sample_image);
        addMenuItemToSection("Menu 3", R.drawable.sample_image);
        addMenuItemToSection("Menu 4", R.drawable.sample_image);
        addMenuItemToSection("Menu 5", R.drawable.sample_image);

        return rootView;
    }

    private void addMenuItemToSection(String title, int imageResId) {
        LayoutInflater inflater = LayoutInflater.from(getActivity()); // Use getActivity() for fragment context
        View menuItemView = inflater.inflate(R.layout.card_menu_item, menuSection, false);

        ImageView menuImage = menuItemView.findViewById(R.id.menuImage);
        TextView menuTitle = menuItemView.findViewById(R.id.menuTitle);

        menuImage.setImageResource(imageResId);
        menuTitle.setText(title);

        menuSection.addView(menuItemView);

        menuItemView.setOnClickListener(view -> Toast.makeText(getActivity(), title + " clicked", Toast.LENGTH_SHORT).show());
    }
}
