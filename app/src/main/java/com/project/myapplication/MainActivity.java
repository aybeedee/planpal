package com.project.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find the Loginbutton by its ID
        Button signupButton = findViewById(R.id.Signupbutton);

        // Set a click listener for the Loginbutton
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create an intent to open the new activity
                Intent intent = new Intent(MainActivity.this, Signup.class);

                // Start the new activity
                startActivity(intent);
            }
        });

        Button resetButton = findViewById(R.id.resetpassbutton);

        // Set a click listener for the Loginbutton
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create an intent to open the new activity
                Intent intent = new Intent(MainActivity.this, ResetPassword.class);

                // Start the new activity
                startActivity(intent);
            }
        });
    }
}
