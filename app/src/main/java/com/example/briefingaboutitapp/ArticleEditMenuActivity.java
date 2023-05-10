package com.example.briefingaboutitapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import Utils.EntitiesUtils;
import Utils.FirebaseDataBindings;

public class ArticleEditMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_edit_menu);

        Bundle extras = getIntent().getExtras();
        if(extras !=null) {
            String value = extras.getString("articleTitle");
            TextView articleTitleTextView = findViewById(R.id.article_title_label);
            articleTitleTextView.setText(value);
        }

        Button startEdits = findViewById(R.id.edit_article);
        startEdits.setOnClickListener( onClick -> {
            Intent goToArticleCreation = new Intent(this, CreateArticle.class);
            this.startActivity(goToArticleCreation);
        });

        Button blurFacesInPhotos = findViewById(R.id.article_photo_blur);
        blurFacesInPhotos.setOnClickListener( onClick -> {
            Intent returnToMainActivity = new Intent(this, PhotoBlurringMenu.class);
            startActivity(returnToMainActivity);
        });

        Button doneButton = findViewById(R.id.submit_my_edited_article);
        doneButton.setOnClickListener( onClick -> {
            Intent returnToMainActivity = new Intent(this, MainActivity.class);
            startActivity(returnToMainActivity);
        });

        Button deleteButton = findViewById(R.id.delete_edited_article);
        deleteButton.setOnClickListener( onClick -> {

            //get creator
            SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
            String email = sh.getString("email", "");

            //get article id
            EntitiesUtils shPref = new EntitiesUtils(getApplicationContext());
            String articleId = shPref.getArticleUUIDFromShPref();

            //delete going on
            FirebaseDataBindings dbBinding = new FirebaseDataBindings();
            FirebaseFirestore db = dbBinding.getDatabaseReference();
            db.collection("Users").document(email).collection("Articles").document(articleId).delete();

            //return to intent
            Intent returnToMainActivity = new Intent(this, MainActivity.class);
            startActivity(returnToMainActivity);
        });
    }
}