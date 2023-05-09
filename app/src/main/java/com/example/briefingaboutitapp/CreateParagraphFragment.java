package com.example.briefingaboutitapp;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.briefingaboutitapp.databinding.FragmentCreateParagraphBinding;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.gson.Gson;

import java.util.Objects;
import java.util.UUID;

import Entities.Article;
import Entities.Paragraph;
import Entities.Title;
import Utils.DesignUtils;
import Utils.EntitiesUtils;
import Utils.FirebaseDataBindings;
import Utils.FirestoreUtils;

public class CreateParagraphFragment extends Fragment {

    private FragmentCreateParagraphBinding binding;

    private Paragraph paragraph;

    private Article article;
    private ListenerRegistration listener;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(false);

        binding = FragmentCreateParagraphBinding.inflate(inflater, container, false);

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

            if (snapshot != null && snapshot.exists()) {
                progressDialog.dismiss();
                this.article = snapshot.toObject(Article.class);
            }
        });
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle titleBundle = getArguments();

        if(titleBundle != null) {

            Gson gson = new Gson();
            String paragraphAsText = requireArguments().getString("paragraph");
            this.paragraph = gson.fromJson(paragraphAsText, Paragraph.class);

            binding.paragraphDescription.setText(this.paragraph.getParagraphDescription());
            binding.paragraphText.setText(this.paragraph.getParagraphText());
            setTitleDesign(this.paragraph.getParagraphTitle().getHeader(), this.paragraph.getParagraphTitle().getTitleText());

        }

        binding.submitParagraph.setOnClickListener(create -> {

            String paragraphDescription = binding.paragraphDescription.getText().toString().trim();
            String paragraphText = binding.paragraphText.getText().toString().trim();
            String id = UUID.randomUUID().toString();

            if(paragraphDescription.equals("")){
                Toast.makeText(binding.getRoot().getContext(), "Paragraph description is compulsory.", Toast.LENGTH_SHORT).show();
            }else{

                //paragraph creation
                if(this.paragraph == null){
                    this.paragraph = new Paragraph(id, new Title(), paragraphDescription, paragraphText);
                }
                else {
                    this.paragraph.setParagraphDescription(paragraphDescription);
                    this.paragraph.setParagraphText(paragraphText);
                }

                //save the paragraph to temporary article object
                article.addNewParagraph(paragraph);
                FirestoreUtils articleDBObject = new FirestoreUtils(article, binding.getRoot().getContext());
                articleDBObject.commitArticle(binding.getRoot().getContext());
                listener.remove();

                //navigation back to article creation main path
                NavHostFragment.findNavController(CreateParagraphFragment.this)
                        .navigate(R.id.action_createParagraphFragment_to_SecondFragment);
            }

        });

        binding.discardParagraph.setOnClickListener(delete -> {

            listener.remove();

            NavHostFragment.findNavController(CreateParagraphFragment.this)
                .navigate(R.id.action_createParagraphFragment_to_SecondFragment);
        });

        binding.addParagraphTitleLabelButton.setOnClickListener(updateTitle -> {

            String paragraphDescription = binding.paragraphDescription.getText().toString().trim();
            String paragraphText = binding.paragraphText.getText().toString().trim();
            String id = UUID.randomUUID().toString();

            //paragraph creation
            if(this.paragraph == null){
                this.paragraph = new Paragraph(id, new Title(), paragraphDescription, paragraphText);
            }
            else {
                this.paragraph.setParagraphDescription(paragraphDescription);
                this.paragraph.setParagraphText(paragraphText);
            }

            Bundle bundle = new Bundle();
            Gson gson = new Gson();
            bundle.putString("paragraph", gson.toJson(this.paragraph));

            NavHostFragment.findNavController(CreateParagraphFragment.this)
                    .navigate(R.id.action_createParagraphFragment_to_editParagraphTitleFragment, bundle);
        });

        binding.paragraphText.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                hideKeyboard();
            }
        });

        binding.paragraphDescription.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                hideKeyboard();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void hideKeyboard(){
        DesignUtils keyboard = new DesignUtils(binding.getRoot());
        keyboard.closeKeyBoard();
    }

    private void setTitleDesign(String titleHeading, String titleText){
        String titleHTML = "<" + titleHeading + ">" + titleText + "</" + titleHeading + ">";
        TextView titleDesign = binding.getRoot().findViewById(R.id.subtitle_design_display);
        titleDesign.setText(Html.fromHtml(titleHTML, Html.FROM_HTML_MODE_LEGACY));
    }
}