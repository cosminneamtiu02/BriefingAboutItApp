package com.example.briefingaboutitapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class PhotoBlurringMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_blurring_menu);

        FloatingActionButton createArticle = findViewById(R.id.return_to_edit_article_menu);
        createArticle.setOnClickListener(view -> {

            //navigate to the article creation intent
            Intent goToArticleCreation = new Intent(this, ArticleEditMenuActivity.class);
            this.startActivity(goToArticleCreation);
        });
    }
}