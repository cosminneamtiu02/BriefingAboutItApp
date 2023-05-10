package com.example.briefingaboutitapp;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.briefingaboutitapp.databinding.FragmentSecondBinding;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.gson.Gson;

import java.util.List;
import java.util.Objects;

import Entities.Article;
import Entities.Paragraph;
import Utils.EntitiesUtils;
import Utils.FirebaseDataBindings;
import Utils.ParagraphsAdapter.ParagraphsAdapter;

public class SecondFragment extends Fragment {

    private FragmentSecondBinding binding;

    private List<Paragraph> paragraphs;

    private ListenerRegistration listener;

    private Article article;

    private RecyclerView recyclerView;

    private ParagraphsAdapter paragraphsAdapter;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(false);

        binding = FragmentSecondBinding.inflate(inflater, container, false);
        ProgressBar progressBar = new ProgressBar(binding.getRoot().getContext(), null, android.R.attr.progressBarStyleLarge);
        progressBar.setIndeterminate(true);
        progressBar.setVisibility(View.VISIBLE);
        AlertDialog.Builder builder = new AlertDialog.Builder(binding.getRoot().getContext());
        builder.setView(progressBar);
        AlertDialog progressDialog = builder.create();
        progressDialog.show();

        //gets temp article
        EntitiesUtils articleUtils = new EntitiesUtils(getContext());
        String author = articleUtils.getEmailFromShPref();

        //get article id
        String articleID = articleUtils.getArticleUUIDFromShPref();

        // retrieve object from Firestore
        FirebaseDataBindings dbBinding = new FirebaseDataBindings();
        FirebaseFirestore db = dbBinding.getDatabaseReference();
        DocumentReference docRef = db.collection("Users").document(author).collection("Articles").document(articleID);

        listener = docRef.addSnapshotListener((snapshot, e) -> {

            if (snapshot != null && snapshot.exists() && e == null) {

                article = snapshot.toObject(Article.class);
                if (article != null) {

                    this.paragraphs = article.getParagraphs();
                    if (binding != null) {
                        recyclerView = binding.getRoot().findViewById(R.id.paragraph_recyclerview);
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext()));
                        paragraphsAdapter = new ParagraphsAdapter(paragraphs);
                        recyclerView.setAdapter(paragraphsAdapter);

                        paragraphsAdapter.setOnClickListener(position -> {

                            //package object
                            Paragraph paragraphToEdit = this.paragraphs.get(position);
                            Gson gson = new Gson();
                            Bundle bundle = new Bundle();
                            String paragraphString = gson.toJson(paragraphToEdit);
                            bundle.putString("ParagraphToEdit", paragraphString);
                            bundle.putString("ArticleID", this.article.getArticleId());
                            bundle.putString("paragraphPosition", String.valueOf(position));

                            SharedPreferences sh = binding.getRoot().getContext().getSharedPreferences("MySharedPref", MODE_PRIVATE);
                            SharedPreferences.Editor myEdit = sh.edit();
                            myEdit.putString("paragraphString", paragraphString);
                            myEdit.apply();

                            //navigate to edit image title
                            NavHostFragment.findNavController(SecondFragment.this)
                                    .navigate(R.id.action_SecondFragment_to_editParagraphFragment, bundle);

                        });

                        progressDialog.dismiss();
                    }
                }
            }
        });


        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        binding.createParagraphButton.setOnClickListener(view13 -> {

            listener.remove();

            NavHostFragment.findNavController(SecondFragment.this)
                .navigate(R.id.action_SecondFragment_to_createParagraphFragment);
        });

        binding.goToPicturesPick.setOnClickListener(view1 -> {

            listener.remove();

            NavHostFragment.findNavController(SecondFragment.this)
                .navigate(R.id.action_SecondFragment_to_ThirdFragment);

        });

        binding.returnToTitleEdit.setOnClickListener(view12 -> {

            listener.remove();

            NavHostFragment.findNavController(SecondFragment.this)
                .navigate(R.id.action_SecondFragment_to_FirstFragment);

        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

}