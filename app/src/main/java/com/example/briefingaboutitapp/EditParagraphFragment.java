package com.example.briefingaboutitapp;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.gson.Gson;

import java.util.Objects;
import java.util.UUID;

import Entities.Paragraph;
import Entities.Title;
import Utils.DesignUtils;
import Utils.FirebaseDataBindings;


public class EditParagraphFragment extends Fragment {

    private FragmentEditParagraphBinding binding;

    private Paragraph paragraph;

    private String paragraphPosition;

    private String articleId;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(false);

        binding = FragmentEditParagraphBinding.inflate(inflater, container, false);

        Gson gson = new Gson();
        String ParagraphAsJSON = requireArguments().getString("ParagraphToEdit");
        paragraphPosition = requireArguments().getString("paragraphPosition");
        articleId = requireArguments().getString("ArticleID");

        this.paragraph = gson.fromJson(ParagraphAsJSON, Paragraph.class);

        binding.paragraphDescription.setText(this.paragraph.getParagraphDescription());
        binding.paragraphText.setText(this.paragraph.getParagraphText());
        setTitleDesign(this.paragraph.getParagraphTitle().getHeader(), this.paragraph.getParagraphTitle().getTitleText());

        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



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

                SharedPreferences shPref = binding.getRoot().getContext().getSharedPreferences("MySharedPref", MODE_PRIVATE);
                String emailFromLogin = shPref.getString("email", "");

                FirebaseDataBindings dbBinding = new FirebaseDataBindings();
                DocumentReference documentRef = dbBinding.getDatabaseReference().
                        collection("Users" ).
                        document(emailFromLogin).
                        collection("Articles").
                        document(articleId);

                Gson gson = new Gson();
                SharedPreferences sh = binding.getRoot().getContext().getSharedPreferences("MySharedPref", MODE_PRIVATE);
                String articleAsJSON = sh.getString("paragraphString", "");
                Paragraph paragraphToRemove = gson.fromJson(articleAsJSON, Paragraph.class);

                // Update the object at the specified index in the array field
                documentRef.update("paragraphs", FieldValue.arrayRemove(paragraphToRemove));

                // add the same updated paragraph
                documentRef.update("paragraphs", FieldValue.arrayUnion(paragraph));


                //navigation back to article creation main path
                NavHostFragment.findNavController(EditParagraphFragment.this)
                        .navigate(R.id.action_editParagraphFragment_to_SecondFragment);
            }

        });

        binding.discardParagraph.setOnClickListener(
                delete -> {
                    //update paragraphs
                    SharedPreferences shPref = binding.getRoot().getContext().getSharedPreferences("MySharedPref", MODE_PRIVATE);
                    String emailFromLogin = shPref.getString("email", "");


                    FirebaseDataBindings dbBinding = new FirebaseDataBindings();
                    DocumentReference documentRef = dbBinding.getDatabaseReference().
                            collection("Users" ).
                            document(emailFromLogin).
                            collection("Articles").
                            document(articleId);

                    Gson gson = new Gson();
                    SharedPreferences sh = binding.getRoot().getContext().getSharedPreferences("MySharedPref", MODE_PRIVATE);
                    String articleAsJSON = sh.getString("paragraphString", "");
                    Paragraph paragraphToRemove = gson.fromJson(articleAsJSON, Paragraph.class);

                    // Update the object at the specified index in the array field
                    documentRef.update("paragraphs", FieldValue.arrayRemove(paragraphToRemove));


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
            bundle.putString("ParagraphToEdit", gson.toJson(this.paragraph));
            bundle.putString("ArticleID", articleId);
            bundle.putString("paragraphPosition", paragraphPosition);

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