package com.example.briefingaboutitapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ImageViewOnClickActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view_on_click);


        Button toBlurClick = findViewById(R.id.select_picture_to_blur);
        toBlurClick.setOnClickListener(click -> {
            Intent goToPickFacesBlur = new Intent(this, PickFacesToBlurActivity.class);
            startActivity(goToPickFacesBlur);
        });

        Button seeBlurredPicture = findViewById(R.id.see_blurred_picture);
        seeBlurredPicture.setOnClickListener(click -> {
            Intent goToSeeBlurredPhoto = new Intent(this, BlurredPhotoDisplay.class);
            startActivity(goToSeeBlurredPhoto);
        });


        FloatingActionButton createArticle = findViewById(R.id.returnToSelectImage);
        createArticle.setOnClickListener(view -> {

            //navigate to the article creation intent
            Intent goToArticleCreation = new Intent(this, PhotoBlurringMenu.class);
            this.startActivity(goToArticleCreation);

        });
    }
}