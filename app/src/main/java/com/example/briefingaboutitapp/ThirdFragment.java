package com.example.briefingaboutitapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Objects;

import Entities.Article;
import Entities.Image;
import Utils.EntitiesUtils;
import Utils.FirebaseDataBindings;
import Utils.ImagesAdapter.ImagesAdapter;
import Utils.LambdaClient;

public class ThirdFragment extends Fragment {

    private FragmentThirdBinding binding;

    private ActivityResultLauncher<String> galleryLauncher;

    private ListenerRegistration listener;

    private Article article;

    private ArrayList<Image> images;

    private String id;

    RecyclerView recyclerView;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(false);

        binding = FragmentThirdBinding.inflate(inflater, container, false);


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
        this.id = articleID;
        // retrieve object from Firestore
        FirebaseDataBindings dbBinding = new FirebaseDataBindings();
        FirebaseFirestore db = dbBinding.getDatabaseReference();
        DocumentReference docRef = db.collection("Users").document(author).collection("Articles").document(articleID);

        listener = docRef.addSnapshotListener((snapshot, e) -> {

            if (snapshot != null && snapshot.exists() && e == null) {

                article = snapshot.toObject(Article.class);
                if (article != null) {

                    this.images = article.getImages();
                    if (binding != null) {
                        recyclerView = binding.getRoot().findViewById(R.id.recyclerview);
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext()));
                        ImagesAdapter imagesAdapter = new ImagesAdapter(images);
                        recyclerView.setAdapter(imagesAdapter);

                        imagesAdapter.setOnClickListener(position -> {

                            //package object uri
                            Image tempImage = article.getImages().get(position);
                            Gson gson = new Gson();
                            String imageAsJSON = gson.toJson(tempImage);
                            Bundle bundle = new Bundle();
                            bundle.putString("ImageToEdit", imageAsJSON);
                            bundle.putString("ArticleId", this.id);
                            Log.d("articleID", this.id);

                            //navigate to edit image title
                            NavHostFragment.findNavController(ThirdFragment.this)
                                    .navigate(R.id.action_ThirdFragment_to_editPhotoFragment, bundle);

                        });

                        progressDialog.dismiss();
                    }
                }
            }
        });


        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        binding.returnToEditText.setOnClickListener(view1 -> NavHostFragment.findNavController(ThirdFragment.this)
                .navigate(R.id.action_ThirdFragment_to_SecondFragment));

        Button photoPickerButton = view.findViewById(R.id.photo_picker_button);
        galleryLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(),
                result -> {
                    listener.remove();
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

        photoPickerButton.setOnClickListener(view12 -> {
            if(this.images.size() < 2){
                galleryLauncher.launch("image/*");
            }
            else{
                Toast.makeText(binding.getRoot().getContext(), "Photos limit reached!", Toast.LENGTH_SHORT).show();
            }
        });

        binding.submitForm.setOnClickListener(view1 -> {


            listener.remove();

            LambdaClient myLambdaClient = new LambdaClient(binding.getRoot().getContext());

            Thread thread = new Thread(() -> Log.d("LambdaResponse", myLambdaClient.invokeLambda("Test")));
            thread.start();


            Toast.makeText(binding.getRoot().getContext(), "Article submitted successfully", Toast.LENGTH_SHORT).show();

            Intent goToMainActivity = new Intent(getContext(), MainActivity.class);
            this.startActivity(goToMainActivity);


        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}