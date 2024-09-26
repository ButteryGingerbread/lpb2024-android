package com.example.lpb2024;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
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
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private static final int REQUEST_CAMERA_PERMISSION = 1001;
    private static final int MODEL_INPUT_SIZE = 224; // Example size, change according to your model

    private ActivityResultLauncher<Intent> cameraActivityResultLauncher;
    private NavController navController;
    private ImageClassifier imageClassifier;
    private List<Customer> customers = new ArrayList<>();
    private List<String> detectedClasses = new ArrayList<>(); // Class-level variable
    private List<Recipe> filteredMenus = new ArrayList<>(); // Class-level variable
    private String[] classNames = {
            "Apel", "Ayam", "Bawang Merah", "Bawang Putih", "Blueberry",
            "Brokoli", "Cabe", "Daging Sapi Cincang", "Ikan", "Jamur",
            "Kembang Kol", "Kentang", "Mentega", "Nanas", "Nasi",
            "Stroberi", "Tauge", "Telur", "Tomat", "Wortel"
    };

    private Customer selectedCustomer; // Variable to store the selected customer

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        imageClassifier = new ImageClassifier(requireContext(), "model.tflite", MODEL_INPUT_SIZE);
        Log.d("HomeFragment", "Model loaded successfully.");

        cameraActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == getActivity().RESULT_OK && result.getData() != null) {
                    Bundle extras = result.getData().getExtras();
                    Bitmap photo = (Bitmap) extras.get("data");

                    if (photo != null) {
                        processCapturedImage(photo);
                    }
                }
            }
        });

        LinearLayout recommendedSection = rootView.findViewById(R.id.recommendedSection);
        LinearLayout topPicksSection = rootView.findViewById(R.id.topPicksSection);
        LinearLayout scannedIngredientsSection = rootView.findViewById(R.id.scannedIngredientsSection);

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
        displayAllButton.setOnClickListener(v -> NavHostFragment.findNavController(HomeFragment.this)
                .navigate(R.id.action_homeFragment_to_displayAllFragment));

        Button cameraButton = rootView.findViewById(R.id.cameraButton);
        cameraButton.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
            } else {
                fetchCustomersAndShowDialog();
            }
        });

        Button customerButton = rootView.findViewById(R.id.customerButton);
        customerButton.setOnClickListener(v -> {
            if (navController == null) {
                navController = NavHostFragment.findNavController(HomeFragment.this);
            }
            navController.navigate(R.id.action_homeFragment_to_customerFragment);
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        requireActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                requireActivity().finishAffinity();
            }
        });
    }

    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
            cameraActivityResultLauncher.launch(cameraIntent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fetchCustomersAndShowDialog();
            } else {
                Toast.makeText(getActivity(), "Camera permission is required to take photos.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void processCapturedImage(Bitmap bitmap) {
        Log.d("HomeFragment", "Processing captured image");
        Log.d("HomeFragment", "Selected Customer: " + (selectedCustomer != null ? selectedCustomer.getName() : "No customer selected"));
        if (imageClassifier == null) {
            Log.e("HomeFragment", "Image classifier is not available");
            Toast.makeText(getActivity(), "Image classifier is not available", Toast.LENGTH_SHORT).show();
            return;
        }

        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, MODEL_INPUT_SIZE, MODEL_INPUT_SIZE, false);

        float[] confidenceScores = imageClassifier.classifyImage(resizedBitmap);
        Log.d("HomeFragment", "Image classified, processing results");

        if (confidenceScores.length != classNames.length) {
            Log.e("HomeFragment", "Mismatch between model output size and classNames length.");
            return;
        }

        float threshold = 0.2f;
        detectedClasses.clear(); // Clear previous detections
        for (int i = 0; i < confidenceScores.length; i++) {
            if (confidenceScores[i] >= threshold) {
                // Only add the class name without percentage
                String className = classNames[i];
                detectedClasses.add(className);
                Log.d("HomeFragment", "Detected Class: " + className);
            }
        }

        if (detectedClasses.isEmpty()) {
            detectedClasses.add("None detected");
            Log.d("HomeFragment", "No classes detected above the threshold.");
        }

        sendScannedDataToBackend(selectedCustomer, detectedClasses); // New method to send data

    }

    private void addMenuItemToSection(LinearLayout section, String title, int imageResId) {
        LayoutInflater inflater = LayoutInflater.from(requireActivity());
        View menuItemView = inflater.inflate(R.layout.card_menu_item, section, false);

        ImageView menuImage = menuItemView.findViewById(R.id.menuImage);
        TextView menuTitle = menuItemView.findViewById(R.id.menuTitle);

        menuImage.setImageResource(imageResId);
        menuTitle.setText(title);

        section.addView(menuItemView);

        menuItemView.setOnClickListener(view -> Toast.makeText(getActivity(), title + " clicked", Toast.LENGTH_SHORT).show());
    }

    private void fetchCustomersAndShowDialog() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<List<Customer>> call = apiService.getCustomers();

        call.enqueue(new Callback<List<Customer>>() {
            @Override
            public void onResponse(Call<List<Customer>> call, Response<List<Customer>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    customers = response.body();
                    showCustomerSelectionDialog();
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

    private void showCustomerSelectionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle("Select a Customer");

        String[] customerNames = new String[customers.size()];
        for (int i = 0; i < customers.size(); i++) {
            customerNames[i] = customers.get(i).getName();
        }

        builder.setItems(customerNames, (dialog, which) -> {
            selectedCustomer = customers.get(which);
            Log.d("HomeFragment", "Selected Customer: " + selectedCustomer.getName());
            openCamera();
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private boolean isRequestInProgress = false;

    private void sendScannedDataToBackend(Customer customer, List<String> ingredients) {
        if (isRequestInProgress) {
            Log.d("HomeFragment", "Request is already in progress.");
            return;
        }

        if (customer == null) {
            Log.e("HomeFragment", "No customer selected. Cannot send request.");
            return;
        }

        isRequestInProgress = true;
        Log.d("HomeFragment", "Sending customerName: " + customer.getName());
        Log.d("HomeFragment", "Sending scannedIngredients: " + ingredients.toString());

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        ScannedDataRequest request = new ScannedDataRequest(customer.getName(), ingredients);

        Call<List<Recipe>> call = apiService.getFilteredRecipes(request);

        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                isRequestInProgress = false;
                if (response.isSuccessful() && response.body() != null) {
                    filteredMenus = response.body();
                    if (filteredMenus.isEmpty()) {
                        Toast.makeText(getActivity(), "No recipes found", Toast.LENGTH_SHORT).show();
                    }
                    // Navigate to BlankFragment and pass the data
                    navigateToBlankFragment(customer, detectedClasses, filteredMenus);
                } else {
                    Toast.makeText(getActivity(), "Failed to filter recipes", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                isRequestInProgress = false;
                Toast.makeText(getActivity(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void navigateToBlankFragment(Customer customer, List<String> detectedClasses, List<Recipe> filteredRecipes) {
        if (navController == null) {
            navController = NavHostFragment.findNavController(HomeFragment.this);
        }

        Bundle bundle = new Bundle();
        bundle.putParcelable("selected_customer", customer);  // This line should now work
        bundle.putStringArrayList("detected_classes", new ArrayList<>(detectedClasses));
        bundle.putParcelableArrayList("filtered_recipes", new ArrayList<>(filteredRecipes));

        navController.navigate(R.id.action_homeFragment_to_blankFragment, bundle);
    }
}
