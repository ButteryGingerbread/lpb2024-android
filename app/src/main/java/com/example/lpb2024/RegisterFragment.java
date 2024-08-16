package com.example.lpb2024;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterFragment extends Fragment {

    private EditText username, email, password1, password2, birthDate;
    private Spinner gender, condition;
    private Button btnLogin;
    private ApiService apiService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        // Initialize views
        username = view.findViewById(R.id.username);
        email = view.findViewById(R.id.email);
        password1 = view.findViewById(R.id.password1);
        password2 = view.findViewById(R.id.password2);
        birthDate = view.findViewById(R.id.birth_date);
        gender = view.findViewById(R.id.gender);
        condition = view.findViewById(R.id.condition);
        btnLogin = view.findViewById(R.id.btnLogin);

        ArrayAdapter<CharSequence> genderAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.gender_array, android.R.layout.simple_spinner_item);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gender.setAdapter(genderAdapter);

        ArrayAdapter<CharSequence> conditionAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.condition_array, android.R.layout.simple_spinner_item);
        conditionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        condition.setAdapter(conditionAdapter);

        // Initialize API service
        apiService = ApiClient.getClient().create(ApiService.class);

        // Set up register button click listener
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        return view;
    }

    private void registerUser() {
        String usernameText = username.getText().toString().trim();
        String emailText = email.getText().toString().trim();
        String password1Text = password1.getText().toString().trim();
        String password2Text = password2.getText().toString().trim();
        String birthDateText = birthDate.getText().toString().trim();
        String genderText = gender.getSelectedItem().toString();
        String conditionText = condition.getSelectedItem().toString();

        if (usernameText.isEmpty() || emailText.isEmpty() || password1Text.isEmpty() ||
                password2Text.isEmpty() || birthDateText.isEmpty() || genderText.isEmpty() ||
                conditionText.isEmpty()) {
            Toast.makeText(getActivity(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password1Text.equals(password2Text)) {
            Toast.makeText(getActivity(), "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        RegisterRequest registerRequest = new RegisterRequest(usernameText, emailText, password1Text, birthDateText, genderText, conditionText);

        apiService.registerUser(registerRequest).enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(getActivity(), "Registration successful", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Registration failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                Toast.makeText(getActivity(), "Registration failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}
