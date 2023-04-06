package com.example.briefingaboutitapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.briefingaboutitapp.databinding.FragmentEditPhotoBinding;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import Entities.Article;
import Entities.Image;
import Utils.DesignUtils;
import Utils.EntitiesUtils;


public class EditPhotoFragment extends Fragment {

    private FragmentEditPhotoBinding binding;

    private ActivityResultLauncher<String> galleryLauncher;

    private Image myImage;

    private Bitmap localImageBitmap;

    private ImageView imageView;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(false);

        binding = FragmentEditPhotoBinding.inflate(inflater, container, false);
        return binding.getRoot();


    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        int imagePosition = Integer.parseInt(requireArguments().getString("ImageObjectPosition"));

        //gets the images from the article
        EntitiesUtils articleUtils = new EntitiesUtils(getContext());
        Article article = articleUtils.getArticleFromShPref();
        List<Image> images = article.getImages();

        //getting image
        this.myImage = images.get(imagePosition);

        //set editable text
        EditText title = (EditText) binding.getRoot().getViewById(R.id.photo_description_to_edit);
        title.setText(this.myImage.getImageName());

        //elsewhere onClick
        title.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                hideKeyboard();
            }
        });

        //set ticked for blur or not
        boolean isTicked = myImage.isToBlur();
        CheckBox mCheckBox = binding.getRoot().findViewById(R.id.checkForBlurringEdit);
        mCheckBox.setChecked(isTicked);


        //set image
        Bitmap imageBitmap = convertStringToBitmap(myImage.getPhoto());
        this.localImageBitmap = imageBitmap;
        imageView = binding.getRoot().findViewById(R.id.image_view_edit);
        imageView.setImageBitmap(imageBitmap);


        //pick another image
        Button pickAnotherPhotoButton = binding.getRoot().findViewById(R.id.pick_other_image);
        galleryLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(),
                result -> {
                    if(result!= null){
                        Bitmap photoBitmap = null;

                        //convert uri to bitmap
                        try {
                            photoBitmap = BitmapFactory.decodeStream(binding.getRoot().getContext()
                                    .getContentResolver().openInputStream(result));


                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }

                        this.localImageBitmap = photoBitmap;
                        imageView.setImageBitmap(this.localImageBitmap);

                    }

                });
        pickAnotherPhotoButton.setOnClickListener(view12 -> galleryLauncher.launch("image/*"));

        //submit image class button
        binding.submitMyEditedPhotoButton.setOnClickListener(aView1 -> {
            if(binding.photoDescriptionToEdit.getText().toString().trim().equals("")){
                Toast.makeText(getContext(), "Pick title/description for photo!", Toast.LENGTH_SHORT).show();
            }
            else {
                //edit the current article photos list
                String imageName = binding.photoDescriptionToEdit.getText().toString().trim();
                boolean to_blur = binding.checkForBlurringEdit.isChecked();
                Image newImage = new Image(myImage.getId(), imageName, convertBitmapToString(localImageBitmap), "", to_blur, new ArrayList<>());
                article.updateImage(newImage);
                articleUtils.updateArticleShPref(article);

                //announce the edit
                Toast.makeText(binding.getRoot().getContext(), "Photo edited!", Toast.LENGTH_SHORT).show();

                //navigate to edit image title
                NavHostFragment.findNavController(EditPhotoFragment.this)
                        .navigate(R.id.action_editPhotoFragment_to_ThirdFragment);
            }
        });

        //delete photo button
        binding.deletePhoto.setOnClickListener(aView2 -> {

            //delete the current image
            article.deleteImage(myImage);
            articleUtils.updateArticleShPref(article);

            //announce the delete
            Toast.makeText(binding.getRoot().getContext(), "Photo deleted!", Toast.LENGTH_SHORT).show();

            //navigate to edit image title
            NavHostFragment.findNavController(EditPhotoFragment.this)
                    .navigate(R.id.action_editPhotoFragment_to_ThirdFragment);
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

    private static String convertBitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    public static Bitmap convertStringToBitmap(String string) {
        byte[] byteArray1;
        byteArray1 = Base64.decode(string, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(byteArray1, 0,
                byteArray1.length);
    }

}