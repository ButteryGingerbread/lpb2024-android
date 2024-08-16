package com.example.lpb2024;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

public class HomeFragment extends Fragment {

    private static final int REQUEST_CAMERA_PERMISSION = 1001;

    private ActivityResultLauncher<Intent> cameraActivityResultLauncher;
    private NavController navController;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize ActivityResultLauncher
        cameraActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == getActivity().RESULT_OK) {
                    // Navigate to the blank fragment after taking the photo
                    if (navController == null) {
                        navController = NavHostFragment.findNavController(HomeFragment.this);
                    }
                    // Ensure this ID matches the one in your navigation graph
                    navController.navigate(R.id.action_homeFragment_to_blankFragment);
                }
            }
        });


        LinearLayout recommendedSection = rootView.findViewById(R.id.recommendedSection);
        LinearLayout topPicksSection = rootView.findViewById(R.id.topPicksSection);
        LinearLayout scannedIngredientsSection = rootView.findViewById(R.id.scannedIngredientsSection);

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

        Button displayAllButton = rootView.findViewById(R.id.displayAllButton);
        displayAllButton.setOnClickListener(v -> {
            NavHostFragment.findNavController(HomeFragment.this)
                    .navigate(R.id.action_homeFragment_to_displayAllFragment);
        });

        Button cameraButton = rootView.findViewById(R.id.cameraButton);
        cameraButton.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
            } else {
                openCamera();
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        requireActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                getActivity().finishAffinity();
            }
        });
    }

    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            cameraActivityResultLauncher.launch(cameraIntent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(getActivity(), "Camera permission is required to take photos.", Toast.LENGTH_SHORT).show();
            }
        }
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
