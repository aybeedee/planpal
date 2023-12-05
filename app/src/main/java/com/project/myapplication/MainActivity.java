package com.project.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button signupButton = findViewById(R.id.Signupbutton);
        Button loginButton = findViewById(R.id.loginbutton);
        TextInputLayout email = findViewById(R.id.email);
        TextInputEditText emailEditText = email.findViewById(R.id.email_edit_text);
        TextInputEditText passwordEditText = findViewById(R.id.password_edit_text);
        ImageButton toggleButton = findViewById(R.id.toggle_button);
        FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailInput = emailEditText.getText().toString();
                String passwordInput = passwordEditText.getText().toString();

                if ((emailInput.trim().length() != 0) && (passwordInput.trim().length() != 0)) {
                    mAuth.signInWithEmailAndPassword(
                            emailEditText.getText().toString(), passwordEditText.getText().toString()
                    ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                //Toast.makeText(MainActivity.this, "Log In Successful", Toast.LENGTH_LONG).show();
                                //Toast.makeText(MainActivity.this, mAuth.getUid().toString(), Toast.LENGTH_LONG).show();

                                Intent intent = new Intent(MainActivity.this, Chats.class);
                                startActivity(intent);

                                finish();
                            }

                            else {

                                Toast.makeText(MainActivity.this, "Log In Failed", Toast.LENGTH_LONG).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {

                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Toast.makeText(MainActivity.this, "Log In Failed"+e.toString(), Toast.LENGTH_LONG).show();
                        }
                    });
                }

                else {

                    Log.d("loginClickElse", emailInput);
                    Toast.makeText(MainActivity.this, "Input Fields Missing!", Toast.LENGTH_LONG).show();
                }
            }
        });

        // Set onClickListener for the toggle button
        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toggle password visibility
                if (passwordEditText.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                    // Password is visible, hide it
                    passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                } else {
                    // Password is hidden, show it
                    passwordEditText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }
                // Move the cursor to the end of the text to maintain the current cursor position
                passwordEditText.setSelection(passwordEditText.getText().length());
            }
        });

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
