package com.example.briefingaboutitapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import Utils.LogInAndRegistrationDataValidator;

public class LogInActivity extends AppCompatActivity {
    private TextView emailField;
    private TextView passwordField;

    private final LogInAndRegistrationDataValidator dataValidator = new LogInAndRegistrationDataValidator();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        // Email validation Listener
        emailField = (EditText)findViewById(R.id.editTextTextEmailAddress);
        emailField.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable email) {
                if(dataValidator.isEmailValid(email.toString().trim())){
                    emailField.setError(null);
                    emailField.clearFocus();
                }
            }

            public void beforeTextChanged(CharSequence email, int start,
                                          int count, int after) {            }

            public void onTextChanged(CharSequence email, int start,
                                      int before, int count) {

                if(!dataValidator.isEmailValid(email.toString().trim())){
                    emailField.setError("Wrong E-mail format!");
                }

            }
        });

        //initiate log in
        Button logInButton = findViewById(R.id.log_in_button);
        passwordField = (EditText)findViewById(R.id.editTextTextPassword);
        logInButton.setOnClickListener(view -> {

            boolean emailOK = false;
            boolean passwordOK = false;

            if(emailField.getText().toString().trim().equals("")){
                emailField.setError("Email is required!");
            }
            else if(!dataValidator.isEmailValid(emailField.getText().toString().trim())){
                Toast.makeText(getApplicationContext(),"Check E-mail Warning!",Toast.LENGTH_SHORT).show();
            }
            else{
                emailOK = true;
            }

            if(passwordField.getText().toString().equals("")) {
                passwordField.setError("Password is required!");
            }
            else{
                passwordOK = true;
            }

            if(emailOK && passwordOK){
                //TODO login
            }

        });

        //go to registration intent
        Button registerButton = findViewById(R.id.register_button);
        registerButton.setOnClickListener(view -> {
            Intent goToRegistration = new Intent(this, RegisterActivity.class);
            finish();
            this.startActivity(goToRegistration);
        });
    }
}