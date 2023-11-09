package com.project.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Signup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        // Find the Loginbutton by its ID
        Button loginButton = findViewById(R.id.loginbutton);

        // Set a click listener for the Loginbutton
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create an intent to open the new activity
                Intent intent = new Intent(Signup.this, MainActivity.class);

                // Start the new activity
                startActivity(intent);
            }
        });

        Button signupButton = findViewById(R.id.Signupbutton);

        // Set a click listener for the Loginbutton
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create an intent to open the new activity
                Intent intent = new Intent(Signup.this, Verification.class);

                // Start the new activity
                startActivity(intent);
            }
        });
    }
}