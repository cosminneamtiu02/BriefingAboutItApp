package com.example.briefingaboutitapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class ArticleEditMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_edit_menu);

        Button startEdits = findViewById(R.id.edit_article);
        startEdits.setOnClickListener( onClick -> {

        });

        Button doneButton = findViewById(R.id.submit_my_edited_article);
        doneButton.setOnClickListener( onClick -> {
            Intent returnToMainActivity = new Intent(this, MainActivity.class);
            startActivity(returnToMainActivity);
        });

        Button deleteButton = findViewById(R.id.delete_edited_article);
        deleteButton.setOnClickListener( onClick -> {
            Intent returnToMainActivity = new Intent(this, MainActivity.class);
            startActivity(returnToMainActivity);
        });
    }
}