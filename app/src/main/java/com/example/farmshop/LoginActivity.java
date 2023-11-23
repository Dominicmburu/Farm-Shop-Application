package com.example.farmshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils; // Import TextUtils
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    EditText emailEditText, passwordEditText;
    Button loginButton;
    TextView toSignupTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        toSignupTextView = findViewById(R.id.toSignupTextView);

        toSignupTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Initialize the database helper in your LoginActivity
                DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext(), "FarmAppDB", null, 1);

                // Get user input
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    // Check if email or password is empty
                    Toast.makeText(getApplicationContext(), "Please fill in all the fields.", Toast.LENGTH_SHORT).show();
                } else {
                    // Verify the user's login credentials using getUserByEmail
                    User user = dbHelper.getUserByEmail(email, password);

                    if (user != null) {
                        // Successful login
                        Toast.makeText(getApplicationContext(), "Login successful.", Toast.LENGTH_SHORT).show();

                        SharedPreferences sharedPreferences = getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("email", email);
                        // to save our data with key and value.
                        editor.apply();


                        // Navigate to the main app activity (FarmProduceActivity)
                        // Create an intent to start the FarmProduceActivity
                        Intent intent = new Intent(getApplicationContext(), FarmProduceActivity.class);

                        // Start the FarmProduceActivity
                        startActivity(intent);

                    } else {
                        // Login failed
                        // Handle the error (e.g., incorrect email or password)
                        Toast.makeText(getApplicationContext(), "Login failed. Please check your email and password.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
