package com.example.briefingaboutitapp;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.briefingaboutitapp.databinding.FragmentEditParagraphBinding;
import com.google.gson.Gson;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import Entities.Article;
import Entities.Paragraph;
import Entities.Title;
import Utils.DesignUtils;
import Utils.EntitiesUtils;


public class EditParagraphFragment extends Fragment {

    private FragmentEditParagraphBinding binding;

    private Paragraph paragraph;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(false);

        binding = FragmentEditParagraphBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle titleBundle = getArguments();

        if(titleBundle != null) {

            try {

                //get position
                int paragraphIndex = Integer.parseInt(requireArguments().getString("ParagraphObjectPosition"));

                //gets the images from the article
                EntitiesUtils articleUtils = new EntitiesUtils(getContext());
                Article article = articleUtils.getArticleFromShPref();
                List<Paragraph> paragraphs = article.getParagraphs();

                //get the paragraph
                this.paragraph = paragraphs.get(paragraphIndex);

            }catch (Exception e){

                //dependent on what package it receives
                Gson gson = new Gson();
                String paragraphAsText = requireArguments().getString("paragraph");
                this.paragraph = gson.fromJson(paragraphAsText, Paragraph.class);

            }


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
                EntitiesUtils articleUtils = new EntitiesUtils(view.getContext());
                Article article = articleUtils.getArticleFromShPref();
                article.updateParagraph(paragraph);
                articleUtils.updateArticleShPref(article);

                //navigation back to article creation main path
                NavHostFragment.findNavController(EditParagraphFragment.this)
                        .navigate(R.id.action_editParagraphFragment_to_SecondFragment);
            }

        });

        binding.discardParagraph.setOnClickListener(
                delete -> {

                    //save the paragraph to temporary article object
                    EntitiesUtils articleUtils = new EntitiesUtils(view.getContext());
                    Article article = articleUtils.getArticleFromShPref();
                    article.deleteParagraph(paragraph);
                    articleUtils.updateArticleShPref(article);

                    NavHostFragment.findNavController(EditParagraphFragment.this)
                .navigate(R.id.action_editParagraphFragment_to_SecondFragment);
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

            NavHostFragment.findNavController(EditParagraphFragment.this)
                    .navigate(R.id.action_editParagraphFragment_to_editParagraphTitleFragment2, bundle);
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