package com.example.farmshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SignUpActivity extends AppCompatActivity {

    EditText emailEditText, passwordEditText;
    Button signupButton;
    TextView toLoginTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        signupButton = findViewById(R.id.signupButton);
        toLoginTextView = findViewById(R.id.toLoginTextView);

        toLoginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Initialize the database helper in your SignUpActivity
                DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext(), "FarmAppDB", null, 1);
                SQLiteDatabase db = dbHelper.getWritableDatabase();

                // Get user input
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                // Validate and insert user data
                if (validateInput(email, password)) {
                    // Check if the email is already registered (duplicate email)
                    Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_USERS + " WHERE " +
                            DatabaseHelper.COLUMN_EMAIL + " = ?", new String[]{email});

                    if (cursor.getCount() > 0) {
                        // Duplicate email found
                        Toast.makeText(getApplicationContext(), "Email already registered. Please use a different email.", Toast.LENGTH_SHORT).show();
                    } else {
                        // Email is not a duplicate, proceed with registration
                        long newRowId = dbHelper.insertUser(email, password);

                        if (newRowId != -1) {
                            // User successfully registered
                            Toast.makeText(getApplicationContext(), "Registration successful. Please log in.", Toast.LENGTH_SHORT).show();
                            // Navigate to the login page
                            startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                        } else {
                            // Registration failed
                            // Handle the error (e.g., database insertion failed)
                            Toast.makeText(getApplicationContext(), "Registration failed. Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    cursor.close();
                }

                db.close();
            }
        });





    }

    private boolean validateInput(String email, String password) {
        if (email.isEmpty() || !isValidEmail(email)) {
            // Invalid email address
            Toast.makeText(this, "Please enter a valid email address.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (password.isEmpty() || password.length() < 6) {
            // Invalid password (e.g., less than 6 characters)
            Toast.makeText(this, "Please enter a password with at least 6 characters.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    // Helper method to validate email format
    private boolean isValidEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        return email.matches(emailPattern);
    }

}
