package com.example.bulletinboard.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.bulletinboard.SharedPreferencesManager;
import com.example.bulletinboard.databinding.FragmentHomeBinding;
import com.example.bulletinboard.ui.createPost.CreatePostActivity;
import com.example.bulletinboard.ui.login.LoginActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textHome;
        final FloatingActionButton createPostButton = binding.createPostButton;
        final FloatingActionButton logoutButton = binding.logoutButton;

        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        createPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), CreatePostActivity.class);
                view.getContext().startActivity(intent);
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferencesManager.clearUser(view.getContext());

                Intent intent = new Intent(view.getContext(), LoginActivity.class);
                view.getContext().startActivity(intent);
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}