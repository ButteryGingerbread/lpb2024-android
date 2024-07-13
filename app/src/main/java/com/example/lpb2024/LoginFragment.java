package com.example.lpb2024;

import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends Fragment {
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private ApiService apiService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        emailEditText = view.findViewById(R.id.email);
        passwordEditText = view.findViewById(R.id.password);
        loginButton = view.findViewById(R.id.btnLogin);

        apiService = ApiClient.getClient().create(ApiService.class);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
                    login(email, password);
                } else {
                    Toast.makeText(getContext(), "Email and password are required", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    private void login(String email, String password) {
        Call<LoginResponse> call = apiService.loginUser(new LoginRequest(email, password));
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse loginResponse = response.body();
                    if (loginResponse.isStatus()) {
                        Toast.makeText(getContext(), loginResponse.getMessage(), Toast.LENGTH_SHORT).show();

                        // Navigate to HomeFragment on successful login
                        NavHostFragment.findNavController(LoginFragment.this)
                                .navigate(R.id.action_loginFragment_to_homeFragment);

                    } else {
                        Toast.makeText(getContext(), loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        // Handle unsuccessful login
                    }
                } else {
                    Toast.makeText(getContext(), "Failed to login", Toast.LENGTH_SHORT).show();
                    Log.e("LoginError", "Response Code: " + response.code() + ", Response Message: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("LoginError", "Error: " + t.getMessage(), t);
            }
        });
    }




    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView tvRegister = view.findViewById(R.id.tvRegister);
        String text = "Don't have an account? Register here.";
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                NavHostFragment.findNavController(LoginFragment.this)
                        .navigate(R.id.action_loginFragment_to_registerFragment);
            }
        };

        // Set clickable span on textview
        SpannableString spannableString = new SpannableString(text);
        spannableString.setSpan(clickableSpan, 23, 35, 0);
        tvRegister.setText(spannableString);
        tvRegister.setMovementMethod(android.text.method.LinkMovementMethod.getInstance());
    }
}
