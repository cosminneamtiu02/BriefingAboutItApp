package com.example.briefingaboutitapp;

import android.content.Context;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.briefingaboutitapp.databinding.FragmentEditParagraphTitleBinding;
import com.google.gson.Gson;

import java.util.Objects;

import Entities.Paragraph;
import Entities.Title;
import Utils.DesignUtils;

public class CreateParagraphTitleFragment extends Fragment {

    private FragmentEditParagraphTitleBinding binding;

    private TextView titleDesign;

    private String titleHeading = "h1";
    private String titleText = "";

    private Paragraph paragraph;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(false);


        binding = FragmentEditParagraphTitleBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        titleDesign = view.findViewById(R.id.title_design_display);

        //getting paragraph from bundle
        Gson gson = new Gson();
        this.paragraph = gson.fromJson(requireArguments().getString("paragraph"), Paragraph.class);

        //setting possible subject areas
        this.titleText = this.paragraph.getParagraphTitle().getTitleText();
        this.titleHeading = this.paragraph.getParagraphTitle().getHeader();

        //setting the already inputted heading
        RadioGroup radioGroup = binding.getRoot().findViewById(R.id.title_heading_radio_group);
        if(Objects.equals(this.titleHeading, "h1")){
            radioGroup.check(R.id.radio_h1);
        }
        else if(Objects.equals(this.titleHeading, "h2")){
            radioGroup.check(R.id.radio_h2);
        }
        else if(Objects.equals(this.titleHeading, "h3")){
            radioGroup.check(R.id.radio_h3);
        }
        else{
            radioGroup.check(R.id.radio_h4);
        }
        setTitleDesign();

        binding.editTextTitle.setText(this.titleText);


        //hide keyboard on click elsewhere(do not forget to copy:
        // android:clickable="true"
        // android:focusableInTouchMode="true" )
        binding.editTextTitle.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                hideKeyboard();
            }
        });

        //listens to the state of the currently set heading
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

        //change title display on title text change
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

        //navigate to title editing
        binding.submitTitle.setOnClickListener(navigate -> {

            //package title and heading
            Bundle bundle = new Bundle();

            Title title = new Title(this.titleHeading, this.titleText);
            this.paragraph.setParagraphTitle(title);
            bundle.putString("paragraph", gson.toJson(this.paragraph));

            NavHostFragment.findNavController(CreateParagraphTitleFragment.this)
                .navigate(R.id.action_editParagraphTitleFragment_to_createParagraphFragment, bundle);});


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