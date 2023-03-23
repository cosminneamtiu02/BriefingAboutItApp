package com.example.briefingaboutitapp;

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
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.briefingaboutitapp.databinding.FragmentFirstBinding;

import Entities.Article;
import Utils.DesignUtils;
import Utils.EntitiesUtils;


public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;
    private String titleHeading = "h1";
    private String titleText = "";
    private TextView titleDesign;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        titleDesign = view.findViewById(R.id.title_design_display);

        //goes to second fragment
        binding.goToEditArticleText.setOnClickListener(view1 -> {

            if(!titleText.equals("")) {
                //update the article with the new data
                EntitiesUtils articleUtils = new EntitiesUtils(view.getContext());
                Article article = articleUtils.getArticleFromShPref();
                article.setTitleText(this.titleText);
                article.setTitleHeader(this.titleHeading);
                articleUtils.updateArticleShPref(article);

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
            EntitiesUtils articleUtils = new EntitiesUtils(view.getContext());
            articleUtils.dropArticleFromShPref();

            //navigate back to main page
            Intent goToMainActivity = new Intent(getContext(), MainActivity.class);
            this.startActivity(goToMainActivity);
            Toast.makeText(this.getContext(),"Article deleted!",Toast.LENGTH_SHORT).show();
        });


    }

    private void setTitleDesign(){
        String titleHTML = "<" + titleHeading + ">" + titleText + "</" + titleHeading + ">";
        titleDesign.setText(Html.fromHtml(titleHTML, Html.FROM_HTML_MODE_LEGACY));
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

}