package com.example.lpb2024;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerFragment extends Fragment {
    private EditText nameEditText;
    private Spinner conditionSpinner;
    private Button submitButton;
    private TableLayout customerTable;
    private ApiService apiService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_customer, container, false);

        nameEditText = view.findViewById(R.id.name);
        conditionSpinner = view.findViewById(R.id.condition);
        submitButton = view.findViewById(R.id.btnLogin);
        customerTable = view.findViewById(R.id.customer_table);

        // Set up spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.conditions_array, // This is the array from res/values/strings.xml
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        conditionSpinner.setAdapter(adapter);

        apiService = ApiClient.getClient().create(ApiService.class);

        // Fetch and display customers
        fetchAndDisplayCustomers();

        // Button listener
        submitButton.setOnClickListener(v -> addCustomer());

        return view;
    }


    private void addCustomer() {
        String name = nameEditText.getText().toString().trim();
        String condition = conditionSpinner.getSelectedItem().toString();

        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(condition)) {
            CustomerRequest customerRequest = new CustomerRequest(name, condition);
            Call<CustomerResponse> call = apiService.createCustomer(customerRequest);
            call.enqueue(new Callback<CustomerResponse>() {
                @Override
                public void onResponse(Call<CustomerResponse> call, Response<CustomerResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        CustomerResponse customerResponse = response.body();
                        // Assuming CustomerResponse has name and condition fields
                        String successMessage = "Customer created successfully!\nName: "
                                + customerResponse.getName() + "\nCondition: "
                                + customerResponse.getCondition();
                        Toast.makeText(requireContext(), successMessage, Toast.LENGTH_SHORT).show();

                        // Clear input fields
                        nameEditText.setText("");
                        conditionSpinner.setSelection(0);

                        // Refresh table with updated customer data (optional)
                        fetchAndDisplayCustomers();
                    } else {
                        Toast.makeText(requireContext(), "Failed to add customer", Toast.LENGTH_SHORT).show();
                        Log.e("CustomerError", "Response Code: " + response.code() + ", Response Message: " + response.message());
                    }
                }

                @Override
                public void onFailure(Call<CustomerResponse> call, Throwable t) {
                    Toast.makeText(requireContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("CustomerError", "Error: " + t.getMessage(), t);
                }
            });
        } else {
            Toast.makeText(requireContext(), "Name and condition are required", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchAndDisplayCustomers() {
        Call<List<Customer>> call = apiService.getCustomers();
        call.enqueue(new Callback<List<Customer>>() {
            @Override
            public void onResponse(Call<List<Customer>> call, Response<List<Customer>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Customer> customers = response.body();
                    updateCustomerTable(customers);
                } else {
                    Toast.makeText(requireContext(), "Failed to load customers", Toast.LENGTH_SHORT).show();
                    Log.e("CustomerError", "Response Code: " + response.code() + ", Response Message: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Customer>> call, Throwable t) {
                Toast.makeText(requireContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("CustomerError", "Error: " + t.getMessage(), t);
            }
        });
    }

    private void updateCustomerTable(List<Customer> customers) {
        customerTable.removeAllViews();

        // Add header row
        TableRow headerRow = new TableRow(getContext());
        TextView headerName = new TextView(getContext());
        headerName.setText("Name");
        headerName.setPadding(8, 8, 8, 8);
        TextView headerCondition = new TextView(getContext());
        headerCondition.setText("Condition");
        headerCondition.setPadding(8, 8, 8, 8);
        headerRow.addView(headerName);
        headerRow.addView(headerCondition);
        customerTable.addView(headerRow);

        // Populate table with customer data
        for (Customer customer : customers) {
            TableRow row = new TableRow(getContext());
            TextView name = new TextView(getContext());
            name.setText(customer.getName());
            name.setPadding(8, 8, 8, 8);
            TextView condition = new TextView(getContext());
            condition.setText(customer.getCondition());
            condition.setPadding(8, 8, 8, 8);
            row.addView(name);
            row.addView(condition);
            customerTable.addView(row);
        }
    }
}
