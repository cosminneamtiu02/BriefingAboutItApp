package com.example.briefingaboutitapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.gson.Gson;

import java.util.List;
import java.util.stream.Collectors;

import Entities.Article;
import Entities.Image;
import Utils.EntitiesUtils;
import Utils.FirebaseDataBindings;
import Utils.PhotoAdapter.PhotoAdapter;

public class PhotoBlurringMenu extends AppCompatActivity {

    RecyclerView recyclerView;

    private Article article;

    private List<Image> images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_blurring_menu);

        ProgressBar progressBar = new ProgressBar(this, null, android.R.attr.progressBarStyleLarge);
        progressBar.setIndeterminate(true);
        progressBar.setVisibility(View.VISIBLE);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(progressBar);
        AlertDialog progressDialog = builder.create();
        progressDialog.show();

        //gets temp article
        EntitiesUtils articleUtils = new EntitiesUtils(this);
        String author = articleUtils.getEmailFromShPref();

        //get article id
        String articleID = articleUtils.getArticleUUIDFromShPref();
        // retrieve object from Firestore
        FirebaseDataBindings dbBinding = new FirebaseDataBindings();
        FirebaseFirestore db = dbBinding.getDatabaseReference();
        DocumentReference docRef = db.collection("Users").document(author).collection("Articles").document(articleID);

        //navigate to the article creation intent
        ListenerRegistration listener = docRef.addSnapshotListener((snapshot, e) -> {

            if (snapshot != null && snapshot.exists() && e == null) {

                article = snapshot.toObject(Article.class);
                if (article != null) {

                    this.images = article.getImages().stream()
                            .filter(Image::isToBlur)
                            .collect(Collectors.toList());


                    recyclerView = findViewById(R.id.image_blurring_recycle_view);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(this));
                    PhotoAdapter imagesAdapter = new PhotoAdapter(this.images);
                    recyclerView.setAdapter(imagesAdapter);

                    imagesAdapter.setOnClickListener(position -> {

                        Log.d("clicked", position + "");

                    });


                }
            }
        });

        progressDialog.dismiss();

        FloatingActionButton createArticle = findViewById(R.id.return_to_edit_article_menu);
        createArticle.setOnClickListener(view -> {

            //navigate to the article creation intent
            Intent goToArticleCreation = new Intent(this, ArticleEditMenuActivity.class);
            this.startActivity(goToArticleCreation);

            listener.remove();
        });
    }
}