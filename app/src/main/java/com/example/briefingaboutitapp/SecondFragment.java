package com.example.briefingaboutitapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.briefingaboutitapp.databinding.FragmentSecondBinding;

import java.util.List;
import java.util.Objects;

import Entities.Article;
import Entities.Paragraph;
import Utils.EntitiesUtils;
import Utils.ParagraphsAdapter.ParagraphsAdapter;

public class SecondFragment extends Fragment {

    private FragmentSecondBinding binding;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(false);

        binding = FragmentSecondBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //gets the images from the article
        EntitiesUtils articleUtils = new EntitiesUtils(getContext());
        Article article = articleUtils.getArticleFromShPref();
        List<Paragraph> paragraphs = article.getParagraphs();

        // Add the following lines to create RecyclerView
        RecyclerView recyclerView = binding.getRoot().findViewById(R.id.paragraph_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext()));
        ParagraphsAdapter paragraphsAdapter = new ParagraphsAdapter(paragraphs);
        recyclerView.setAdapter(paragraphsAdapter);

        binding.createParagraphButton.setOnClickListener(view13 -> NavHostFragment.findNavController(SecondFragment.this)
                .navigate(R.id.action_SecondFragment_to_createParagraphFragment));

        binding.goToPicturesPick.setOnClickListener(view1 -> NavHostFragment.findNavController(SecondFragment.this)
                .navigate(R.id.action_SecondFragment_to_ThirdFragment));

        binding.returnToTitleEdit.setOnClickListener(view12 -> NavHostFragment.findNavController(SecondFragment.this)
                .navigate(R.id.action_SecondFragment_to_FirstFragment));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}