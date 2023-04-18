package com.example.briefingaboutitapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.briefingaboutitapp.databinding.FragmentFirstBinding;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.Objects;

import Entities.Article;
import Entities.Title;
import Utils.DesignUtils;
import Utils.EntitiesUtils;
import Utils.FirebaseDataBindings;
import Utils.FirestoreUtils;


public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;
    private String titleHeading = "h1";
    private String titleText = "";
    private TextView titleDesign;
    private Article article;
    private ListenerRegistration listener;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {


        binding = FragmentFirstBinding.inflate(inflater, container, false);
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
                if (this.article != null) {
                    if(!Objects.equals(Objects.requireNonNull(article).getTitle().getTitleText(), "") && !Objects.equals(article.getTitle().getHeader(), "")){
                        EditText localEditText = binding.getRoot().findViewById(R.id.editTextTitle);
                        localEditText.setText(article.getTitle().getTitleText());

                        this.titleText = article.getTitle().getTitleText();

                        RadioGroup radioGroup = binding.getRoot().findViewById(R.id.title_heading_radio_group);
                        if(Objects.equals(article.getTitle().getHeader(), "h1")){
                            radioGroup.check(R.id.radio_h1);
                        }
                        else if(Objects.equals(article.getTitle().getHeader(), "h2")){
                            radioGroup.check(R.id.radio_h2);
                        }
                        else if(Objects.equals(article.getTitle().getHeader(), "h3")){
                            radioGroup.check(R.id.radio_h3);
                        }
                        else{
                            radioGroup.check(R.id.radio_h4);
                        }
                    }
                }
            }
        });

        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        titleDesign = view.findViewById(R.id.title_design_display);


        //goes to second fragment
        binding.goToEditArticleText.setOnClickListener(view1 -> {

            if(!titleText.equals("")) {
                //update the article with the new data

                Title title = new Title(this.titleHeading, this.titleText);

                article.setTitle(title);

                FirestoreUtils articleDBObject = new FirestoreUtils(article);
                articleDBObject.commitArticle(binding.getRoot().getContext());

                listener.remove();

                //navigate to second fragment
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
            else{
                Toast.makeText(view.getContext(), "Please set an article title.", Toast.LENGTH_SHORT).show();
            }
        });

        //hide keyboard on click elsewhere(do not forget to copy:
        // android:clickable="true"
        // android:focusableInTouchMode="true" )
        binding.editTextTitle.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                hideKeyboard();
            }
        });

        //listens to the state of the currently set heading
        RadioGroup radioGroup = view.findViewById(R.id.title_heading_radio_group);
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            if(checkedId == R.id.radio_h1){
                titleHeading = "h1";
                setTitleDesign();
                hideKeyboard();
            }
            else if(checkedId == R.id.radio_h2){
                titleHeading = "h2";
                setTitleDesign();
                hideKeyboard();
            }
            else if(checkedId == R.id.radio_h3){
                titleHeading = "h3";
                setTitleDesign();
                hideKeyboard();
            }
            else{
                titleHeading = "h4";
                setTitleDesign();
                hideKeyboard();
            }
        });

        // Email validation Listener
        TextView titleField = view.findViewById(R.id.editTextTitle);
        titleField.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable title) {

            }

            public void beforeTextChanged(CharSequence title, int start,
                                          int count, int after) {

            }

            public void onTextChanged(CharSequence title, int start,
                                      int before, int count) {
                titleText = title.toString().trim();
                setTitleDesign();
            }
        });

        //drops the current article edit
        binding.returnToMainActivity.setOnClickListener(view1 -> {

            //remove created article from shared preferences
            FirestoreUtils articleDBObject = new FirestoreUtils(article);
            articleDBObject.deleteArticle(binding.getRoot().getContext());
            listener.remove();

            //navigate back to main page
            Intent goToMainActivity = new Intent(getContext(), MainActivity.class);
            this.startActivity(goToMainActivity);
        });


    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void setTitleDesign(){
        String titleHTML = "<" + titleHeading + ">" + titleText + "</" + titleHeading + ">";
        titleDesign.setText(Html.fromHtml(titleHTML, Html.FROM_HTML_MODE_LEGACY));
    }

    private void hideKeyboard(){
        DesignUtils keyboard = new DesignUtils(binding.getRoot());
        keyboard.closeKeyBoard();
    }

}