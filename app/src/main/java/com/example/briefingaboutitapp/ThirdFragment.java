package com.example.briefingaboutitapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.briefingaboutitapp.databinding.FragmentThirdBinding;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import Entities.Article;
import Entities.Image;
import Utils.EntitiesUtils;
import Utils.FirestoreUtils;
import Utils.ImagesAdapter.ImagesAdapter;

public class ThirdFragment extends Fragment {

    private FragmentThirdBinding binding;

    private ActivityResultLauncher<String> galleryLauncher;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(false);

        binding = FragmentThirdBinding.inflate(inflater, container, false);

        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        //gets the images from the article
        AtomicReference<EntitiesUtils> articleUtils = new AtomicReference<>(new EntitiesUtils(getContext()));
        Article article = articleUtils.get().getArticleFromShPref();
        List<Image> images = article.getImages();

        // Add the following lines to create RecyclerView
        RecyclerView recyclerView = binding.getRoot().findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext()));
        ImagesAdapter imagesAdapter = new ImagesAdapter(images);
        recyclerView.setAdapter(imagesAdapter);

        imagesAdapter.setOnClickListener(position -> {

            //package object uri
            Bundle bundle = new Bundle();
            bundle.putString("ImageObjectPosition", String.valueOf(position));

            //navigate to edit image title
            NavHostFragment.findNavController(ThirdFragment.this)
                    .navigate(R.id.action_ThirdFragment_to_editPhotoFragment, bundle);

        });


        binding.returnToEditText.setOnClickListener(view1 -> NavHostFragment.findNavController(ThirdFragment.this)
                .navigate(R.id.action_ThirdFragment_to_SecondFragment));

        Button photoPickerButton = view.findViewById(R.id.photo_picker_button);
        galleryLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(),
                result -> {
                    if(result!= null){
                        Bitmap photoBitmap;
                        String photoBitmapAsString = "";
                        //convert uri to bitmap
                        try {
                            photoBitmap = BitmapFactory.decodeStream(binding.getRoot().getContext()
                                    .getContentResolver().openInputStream(result));

                            //convert bitmap to string to be passed
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            photoBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                            byte[] byteArray = stream.toByteArray();
                            photoBitmapAsString = Base64.encodeToString(byteArray, Base64.DEFAULT);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }

                        //package image uri
                        Bundle bundle = new Bundle();
                        bundle.putString("imageBitmap", photoBitmapAsString);

                        //navigate to image display and set title with parceled uri
                        NavController navController = Navigation.findNavController(view);
                        navController.navigate(R.id.pickedImageFragment, bundle);
                    }
                    else{
                        Toast.makeText(getContext(), "Please select an image!", Toast.LENGTH_SHORT).show();
                    }
                });

        photoPickerButton.setOnClickListener(view12 -> galleryLauncher.launch("image/*"));

        binding.submitForm.setOnClickListener(view1 -> {

            Article myArticle = articleUtils.get().getArticleFromShPref();

            FirestoreUtils fsUtils = new FirestoreUtils(myArticle);

            if(fsUtils.commitArticle(this.getContext())){
                //remove created article from shared preferences
                articleUtils.get().dropArticleFromShPref();

                Intent goToMainActivity = new Intent(getContext(), MainActivity.class);
                this.startActivity(goToMainActivity);

            }

        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}