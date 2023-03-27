package com.example.briefingaboutitapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.briefingaboutitapp.databinding.FragmentThirdBinding;

import java.util.List;

import Entities.Article;
import Entities.Image;
import Utils.EntitiesUtils;
import Utils.ImagesAdapter.ImagesAdapter;

public class ThirdFragment extends Fragment {

    private FragmentThirdBinding binding;

    private ActivityResultLauncher<String> galleryLauncher;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentThirdBinding.inflate(inflater, container, false);

        //gets the images from the article
        EntitiesUtils articleUtils = new EntitiesUtils(getContext());
        Article article = articleUtils.getArticleFromShPref();
        List<Image> images = article.getImages();

        // Add the following lines to create RecyclerView
        RecyclerView recyclerView = binding.getRoot().findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext()));
        recyclerView.setAdapter(new ImagesAdapter(images));

        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        binding.returnToEditText.setOnClickListener(view1 -> NavHostFragment.findNavController(ThirdFragment.this)
                .navigate(R.id.action_ThirdFragment_to_SecondFragment));

        Button photoPickerButton = view.findViewById(R.id.photo_picker_button);
        galleryLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(),
                result -> {
                    if(result!= null){

                        //package image uri
                        Bundle bundle = new Bundle();
                        bundle.putString("imageURI", result.toString());

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
            Intent goToMainActivity = new Intent(getContext(), MainActivity.class);
            this.startActivity(goToMainActivity);
            Toast.makeText(this.getContext(),"Article submitted successfully!",Toast.LENGTH_SHORT).show();

        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}