package com.example.briefingaboutitapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

import Utils.AuthUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //deflates default "BriefingAboutIt" bar
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        Objects.requireNonNull(getSupportActionBar()).hide();

        setContentView(R.layout.activity_main);

        //go to registration activity
        Button logOutButton = findViewById(R.id.log_out);
        logOutButton.setOnClickListener(view -> {
            AuthUtils database = new AuthUtils();
            database.logout(this);
        });

        FloatingActionButton createArticle = findViewById(R.id.createArticle);
        createArticle.setOnClickListener(view -> {
            Intent goToArticleCreation = new Intent(this, CreateArticle.class);
            this.startActivity(goToArticleCreation);
        });
    }
}