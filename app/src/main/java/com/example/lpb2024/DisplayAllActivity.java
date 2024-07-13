package com.example.lpb2024;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class DisplayAllActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_all);

        // Example of adding HomeFragment to the activity
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new DisplayAllFragment())
                    .commit();
        }
    }
}