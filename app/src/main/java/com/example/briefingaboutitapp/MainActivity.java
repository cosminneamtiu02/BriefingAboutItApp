package com.example.briefingaboutitapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import Entities.Article;
import Entities.Title;
import Utils.AuthUtils;
import Utils.EntitiesUtils;
import Utils.FirestoreUtils;
import Utils.PairsAdapter.PairAdapter;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ProgressBar progressBar = new ProgressBar(MainActivity.this, null, android.R.attr.progressBarStyleLarge);
        progressBar.setIndeterminate(true);
        progressBar.setVisibility(View.VISIBLE);
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setView(progressBar);
        AlertDialog progressDialog = builder.create();
        progressDialog.show();

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

        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        String emailFromLogin = sh.getString("email", "");

        FirestoreUtils myUtils = new FirestoreUtils(new Article(emailFromLogin), getApplicationContext());

        myUtils.getPath().get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<Pair<String, String>> pairs = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {

                    //fetching the article id and their title for display
                    Title title = document.get("title",Title.class);
                    pairs.add(new Pair<>(document.getId(), Objects.requireNonNull(title).getTitleText()));

                }

                pairs.sort(Comparator.comparing((Pair<String, String> p) -> p.second));


                //populate the RW with articles
                recyclerView = findViewById(R.id.recyclerview);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                PairAdapter pairAdapter = new PairAdapter(pairs);
                recyclerView.setAdapter(pairAdapter);
                progressDialog.dismiss();

                pairAdapter.setOnClickListener(position -> {
                    //set uuid in sh pref
                    EntitiesUtils setArticleUUID = new EntitiesUtils(getApplicationContext());
                    setArticleUUID.setArticleUUIDToShPref(pairs.get(position).first);

                    Intent goToArticleEdit = new Intent(this, ArticleEditMenuActivity.class);
                    goToArticleEdit.putExtra("articleTitle",pairs.get(position).second);
                    startActivity(goToArticleEdit);
                });

            } else {
                Log.d("FetchArticlesError", "Error getting documents: ", task.getException());
            }
        });

        FloatingActionButton createArticle = findViewById(R.id.createArticle);
        createArticle.setOnClickListener(view -> {

            //create the new article
            Article newArticle = new Article(sh.getString("email", ""));

            //store temp article with id
            EntitiesUtils setArticleUUID = new EntitiesUtils(getApplicationContext());
            setArticleUUID.setArticleUUIDToShPref(newArticle.getArticleId());

            //navigate to the article creation intent
            Intent goToArticleCreation = new Intent(this, CreateArticle.class);
            this.startActivity(goToArticleCreation);
        });
    }
}