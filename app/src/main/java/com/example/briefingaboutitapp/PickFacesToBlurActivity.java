package com.example.briefingaboutitapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import Entities.Article;
import Entities.Face;
import Entities.Image;
import Utils.EntitiesUtils;
import Utils.FaceAdapter;
import Utils.FaceBlur;
import Utils.FirebaseDataBindings;
import Utils.FirestoreUtils;

public class PickFacesToBlurActivity extends AppCompatActivity {

    private FaceAdapter imagesAdapter;

    private List<Image> allImages;

    private String imageID;

    private String imageToBlur;

    private Image myImage;

    private Article article;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_faces_to_blur);

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

                article = snapshot.toObject(Article.class);
                if (article != null) {

                    SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                    imageID = sh.getString("ImageID", "");

                    allImages = article.getImages();

                    myImage = article.getImages().stream()
                            .filter(image -> imageID.equals(image.getId()))
                            .findFirst()
                            .orElse(null);

                    List<Face> faces = Objects.requireNonNull(myImage).getFaces();

                    imageToBlur = Objects.requireNonNull(myImage).getPhoto();

                    RecyclerView recyclerView = findViewById(R.id.recyclerview_faces);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
                    imagesAdapter = new FaceAdapter(faces, getBaseContext());
                    recyclerView.setAdapter(imagesAdapter);

                }
            }
        });

        progressDialog.dismiss();

        FloatingActionButton returnToImageViewOnClick = findViewById(R.id.return_to_face_blur_menu);
        returnToImageViewOnClick.setOnClickListener(onClick ->{

            listener.remove();

            Intent returnToImageOnClick = new Intent(this, ImageViewOnClickActivity.class);
            startActivity(returnToImageOnClick);
        });

        FloatingActionButton submit = findViewById(R.id.submit_to_blur);
        submit.setOnClickListener(onClick ->{

            listener.remove();

            List<Face> facesToBlur = imagesAdapter.getCheckedFaceList();

            Bitmap blurredImage = FaceBlur.blurFaceKMax(imageToBlur, facesToBlur);

            myImage.setImageBlurred(convertBitmapToString(blurredImage));

            allImages.removeIf(image -> image.getId().equals(imageID));

            allImages.add(myImage);

            article.setImages((ArrayList<Image>) allImages);

            FirestoreUtils firestoreUtils = new FirestoreUtils(article, getBaseContext());

            firestoreUtils.deleteArticle(getBaseContext(), null);
            firestoreUtils.commitArticle(getBaseContext(), null);

            Toast.makeText(getBaseContext(),"Image blurred successfully!",Toast.LENGTH_SHORT).show();

            Intent submit_to_blur = new Intent(this, ImageViewOnClickActivity.class);
            startActivity(submit_to_blur);
        });
    }

    private static String convertBitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }
}