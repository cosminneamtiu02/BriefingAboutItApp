package com.example.briefingaboutitapp;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ImageViewOnClickActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view_on_click);


        Button toBlurClick = findViewById(R.id.select_picture_to_blur);
        toBlurClick.setOnClickListener(click -> {

        });

        Button seeBlurredPicture = findViewById(R.id.see_blurred_picture);
        seeBlurredPicture.setOnClickListener(click -> {

        });


        FloatingActionButton createArticle = findViewById(R.id.returnToSelectImage);
        createArticle.setOnClickListener(view -> {

            //navigate to the article creation intent
            Intent goToArticleCreation = new Intent(this, PhotoBlurringMenu.class);
            this.startActivity(goToArticleCreation);

        });
    }
}