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

public class HomeFragment extends Fragment {

    private LinearLayout recommendedSection;
    private LinearLayout topPicksSection;
    private LinearLayout scannedIngredientsSection;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        recommendedSection = rootView.findViewById(R.id.recommendedSection);
        topPicksSection = rootView.findViewById(R.id.topPicksSection);
        scannedIngredientsSection = rootView.findViewById(R.id.scannedIngredientsSection);

        // Populate sections with sample data
        addMenuItemToSection(recommendedSection, "Recommended Menu 1", R.drawable.sample_image);
        addMenuItemToSection(recommendedSection, "Recommended Menu 2", R.drawable.sample_image);
        addMenuItemToSection(recommendedSection, "Recommended Menu 3", R.drawable.sample_image);
        addMenuItemToSection(topPicksSection, "Top Pick Menu 1", R.drawable.sample_image);
        addMenuItemToSection(topPicksSection, "Top Pick Menu 2", R.drawable.sample_image);
        addMenuItemToSection(topPicksSection, "Top Pick Menu 3", R.drawable.sample_image);
        addMenuItemToSection(scannedIngredientsSection, "Scanned Ingredient Menu 1", R.drawable.sample_image);
        addMenuItemToSection(scannedIngredientsSection, "Scanned Ingredient Menu 2", R.drawable.sample_image);
        addMenuItemToSection(scannedIngredientsSection, "Scanned Ingredient Menu 3", R.drawable.sample_image);

        return rootView;
    }

    private void addMenuItemToSection(LinearLayout section, String title, int imageResId) {
        LayoutInflater inflater = LayoutInflater.from(getActivity()); // Use getActivity() for fragment context
        View menuItemView = inflater.inflate(R.layout.card_menu_item, section, false);

        ImageView menuImage = menuItemView.findViewById(R.id.menuImage);
        TextView menuTitle = menuItemView.findViewById(R.id.menuTitle);

        menuImage.setImageResource(imageResId);
        menuTitle.setText(title);

        section.addView(menuItemView);

        menuItemView.setOnClickListener(view -> Toast.makeText(getActivity(), title + " clicked", Toast.LENGTH_SHORT).show());
    }
}

