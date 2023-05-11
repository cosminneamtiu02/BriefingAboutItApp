package com.example.briefingaboutitapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

import Entities.Article;
import Entities.Image;
import Utils.EntitiesUtils;
import Utils.FirebaseDataBindings;

public class BlurredPhotoDisplay extends AppCompatActivity {

    private Image image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blurred_photo_display);

        ProgressBar progressBar = new ProgressBar(this, null, android.R.attr.progressBarStyleLarge);
        progressBar.setIndeterminate(true);
        progressBar.setVisibility(View.VISIBLE);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(progressBar);
        AlertDialog progressDialog = builder.create();
        progressDialog.show();

        EntitiesUtils articleUtils = new EntitiesUtils(this);
        String author = articleUtils.getEmailFromShPref();

        //get article id
        String articleID = articleUtils.getArticleUUIDFromShPref();
        // retrieve object from Firestore
        FirebaseDataBindings dbBinding = new FirebaseDataBindings();
        FirebaseFirestore db = dbBinding.getDatabaseReference();
        DocumentReference docRef = db.collection("Users").document(author).collection("Articles").document(articleID);

        ListenerRegistration listener = docRef.addSnapshotListener((snapshot, e) -> {

            if (snapshot != null && snapshot.exists() && e == null) {

                Article article = snapshot.toObject(Article.class);
                if (article != null) {

                    SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                    String lookupID = sh.getString("ImageID", "");

                    image = article.getImages().stream()
                            .filter(image -> lookupID.equals(image.getId()))
                            .findFirst()
                            .orElse(null);
                    if(image != null){
                        if(image.getImageBlurred() != null){
                            Bitmap blurredImage = Image.convertStringToBitmap(image.getImageBlurred());
                            ImageView imageView = findViewById(R.id.blurred_image_view);
                            imageView.setImageBitmap(blurredImage);
                        }
                    }

                }
            }
        });

        progressDialog.dismiss();



        FloatingActionButton createArticle = findViewById(R.id.return_to_image_view_menu);
        createArticle.setOnClickListener(view -> {

            listener.remove();

            //navigate to the article creation intent
            Intent goToArticleCreation = new Intent(this, ImageViewOnClickActivity.class);
            this.startActivity(goToArticleCreation);

        });
    }

}