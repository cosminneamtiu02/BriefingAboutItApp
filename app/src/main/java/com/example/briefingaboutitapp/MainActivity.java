package com.example.briefingaboutitapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;
import java.util.UUID;

import Entities.Article;
import Utils.AuthUtils;
import Utils.EntitiesUtils;
import Utils.FirestoreUtils;

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

            //create the new article
            SharedPreferences sh = getApplicationContext().getSharedPreferences("MySharedPref", MODE_PRIVATE);
            Article newArticle = new Article(sh.getString("email", ""));
            FirestoreUtils articleDBObject = new FirestoreUtils(newArticle);
            articleDBObject.commitArticle(getApplicationContext());

            //store temp article with id
            EntitiesUtils setArticleUUID = new EntitiesUtils(getApplicationContext());
            setArticleUUID.setArticleUUIDToShPref(newArticle.getArticleId());
            Log.d("ArticleCreateID", newArticle.getArticleId());

            //navigate to the article creation intent
            Intent goToArticleCreation = new Intent(this, CreateArticle.class);
            this.startActivity(goToArticleCreation);
        });
    }
}