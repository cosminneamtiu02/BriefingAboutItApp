package com.example.briefingaboutitapp;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.briefingaboutitapp.databinding.FragmentPickedImageBinding;

import java.util.UUID;

import Entities.Article;
import Entities.Image;
import Utils.DesignUtils;
import Utils.EntitiesUtils;

public class PickedImageFragment extends Fragment {
    private FragmentPickedImageBinding binding;
    private Uri imageUri;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentPickedImageBinding.inflate(inflater, container, false);
        return binding.getRoot();


    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.imageUri = Uri.parse(requireArguments().getString("imageURI"));

        //get passed image
        ImageView imageView = view.findViewById(R.id.image_view);
        imageView.setImageURI(this.imageUri);

        //hide keyboard on click elsewhere(do not forget to copy:
        // android:clickable="true"
        // android:focusableInTouchMode="true" )
        binding.photoDescription.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                hideKeyboard();
            }
        });

        binding.submitMyPhotoButton.setOnClickListener(view1 -> {
            if(binding.photoDescription.getText().toString().trim().equals("")){
                Toast.makeText(getContext(), "Pick title/description for photo!", Toast.LENGTH_SHORT).show();
            }
            else{
                //create the gotten image class
                String photoName = binding.photoDescription.getText().toString().trim();
                String imageID = UUID.randomUUID().toString();
                boolean to_blur = binding.checkForBlurring.isChecked();
                Image myImage = new Image(imageID, photoName, this.imageUri.toString(), false, to_blur);

                //save the photo to temporary article object
                EntitiesUtils articleUtils = new EntitiesUtils(view.getContext());
                Article article = articleUtils.getArticleFromShPref();
                article.addNewImage(myImage);
                articleUtils.updateArticleShPref(article);

                //navigate back to other fragment
                NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.ThirdFragment);
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
}