package com.example.briefingaboutitapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import com.example.briefingaboutitapp.databinding.FragmentThirdBinding;
import com.google.gson.Gson;

import Entities.Image;

public class ThirdFragment extends Fragment {

    private FragmentThirdBinding binding;

    private ActivityResultLauncher<String> galleryLauncher;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentThirdBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        try {
            Gson gson = new Gson();

                if (gson.fromJson(requireArguments().getString("IMAGE"), Image.class) != null) {
                    Image myImage = gson.fromJson(requireArguments().getString("IMAGE"), Image.class);
                    Log.d("test", myImage.getImageName());
                    Log.d("test", myImage.getId());
                    Log.d("test", myImage.getPhoto().toString());
                    Log.d("test", String.valueOf(myImage.isBlurred()));
                    Log.d("test", String.valueOf(myImage.isToBlur()));
                }

        }
        catch (Exception e){
            Log.d("test", "image still null");
        }

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