package com.example.briefingaboutitapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import Utils.AuthUtils;
import Utils.LogInAndRegistrationDataValidator;

public class RegisterActivity extends AppCompatActivity {

    private TextView emailField;
    private TextView passwordField;
    private TextView reenterPasswordField;
    private AuthUtils register;

    private final LogInAndRegistrationDataValidator dataValidator = new LogInAndRegistrationDataValidator();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        // Email validation Listener
        emailField = findViewById(R.id.editTextTextEmailAddress);
        emailField.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable email) {
                if(dataValidator.isEmailValid(email.toString().trim())){
                    emailField.setError(null);
                }
            }

            public void beforeTextChanged(CharSequence email, int start,
                                          int count, int after) {

            }

            public void onTextChanged(CharSequence email, int start,
                                      int before, int count) {

                if(!dataValidator.isEmailValid(email.toString().trim())){
                    emailField.setError("Wrong E-mail format!");
                }

            }
        });

        // Password validation Listener
        passwordField = findViewById(R.id.editTextTextPassword);
        passwordField.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable password) {
                if(dataValidator.isPasswordValid(password.toString())){
                    passwordField.setError(null);
                }
            }

            public void beforeTextChanged(CharSequence password, int start,
                                          int count, int after) {

            }

            public void onTextChanged(CharSequence password, int start,
                                      int before, int count) {

                if(!dataValidator.isPasswordValid(password.toString())){
                    passwordField.setError("Password must be at least 8 to 20 characters long, contain at least 1 capital letter, at least 1 lowercase letter, one digit and at least one special character like ! @ # & ( )");
                }

            }
        });

        // Reenter password validation Listener
        reenterPasswordField = findViewById(R.id.editTextTextReenterPassword);
        reenterPasswordField.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable reenteredPassword) {
                if(dataValidator.doPasswordsCoincide(reenteredPassword.toString(), passwordField.getText().toString())){
                    reenterPasswordField.setError(null);
                }
            }

            public void beforeTextChanged(CharSequence reenteredPassword, int start,
                                          int count, int after) {

            }

            public void onTextChanged(CharSequence reenteredPassword, int start,
                                      int before, int count) {

                if(!dataValidator.doPasswordsCoincide(reenteredPassword.toString(), passwordField.getText().toString())){
                    reenterPasswordField.setError("Passwords don't match!");
                }

            }
        });

        //initiate registration
        Button registerButton = findViewById(R.id.register_button);
        passwordField = findViewById(R.id.editTextTextPassword);
        reenterPasswordField = findViewById(R.id.editTextTextReenterPassword);
        registerButton.setOnClickListener(view -> {

            boolean emailOK = false;
            boolean passwordOK = false;
            boolean reenteredPasswordOK = false;

            String password = passwordField.getText().toString();
            String email = emailField.getText().toString().trim();

            if(emailField.getText().toString().trim().equals("")){
                emailField.setError("Email is required!");
            }
            else if(!dataValidator.isEmailValid(emailField.getText().toString().trim())){
                Toast.makeText(getApplicationContext(),"Check Warnings!",Toast.LENGTH_SHORT).show();
            }
            else{
                emailOK = true;
            }

            if(passwordField.getText().toString().equals("")) {
                passwordField.setError("Password is required!");
            }
            else if(!dataValidator.isPasswordValid(passwordField.getText().toString())) {
                Toast.makeText(getApplicationContext(),"Check Warnings!",Toast.LENGTH_SHORT).show();
            }
            else{
                passwordOK = true;
            }

            if(passwordField.getText().toString().equals("")) {
                passwordField.setError("Reentering password is required!");
            }
            else if(!dataValidator.doPasswordsCoincide(passwordField.getText().toString(), reenterPasswordField.getText().toString())) {
                Toast.makeText(getApplicationContext(),"Check Warnings!",Toast.LENGTH_SHORT).show();
            }
            else{
                reenteredPasswordOK = true;
            }

            if(emailOK && passwordOK && reenteredPasswordOK){
                register = new AuthUtils();
                register.register(this, email, password);
            }

        });

        //go to login intent
        Button loginButton = findViewById(R.id.back_to_login_button);
        loginButton.setOnClickListener(view -> {
            Intent goBackToLogIn = new Intent(this, LogInActivity.class);
            finish();
            this.startActivity(goBackToLogIn);
        });
    }
}