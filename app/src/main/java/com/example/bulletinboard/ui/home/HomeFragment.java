
package com.example.bulletinboard.ui.home;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bulletinboard.PostAdapter;
import com.example.bulletinboard.PostResult;
import com.example.bulletinboard.R;
import com.example.bulletinboard.ResponseResult;
import com.example.bulletinboard.SharedPreferencesManager;
import com.example.bulletinboard.data.model.Post;
import com.example.bulletinboard.data.model.User;
import com.example.bulletinboard.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {


    private FragmentHomeBinding binding;

    private BulletinBoardViewModel viewModel;
    private PostAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(BulletinBoardViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);



        // Fetch posts
        viewModel.getPosts();

        RecyclerView recyclerView = binding.recyclerViewPosts;

        adapter = new PostAdapter(); // Create your adapter
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));



        viewModel.getResponseResult().observe(getActivity(), postResult -> {
            if (postResult == null) {
                return;
            }
            if (postResult.getError() != null) {
                //showLoginFailed(responseResult.getError());
            }
            if (postResult.getSuccess() != null) {
                updateUiWithPosts(postResult.getSuccess());
                recyclerView.setAdapter(adapter);
            }

        });


        return binding.getRoot();
    }

    private void updateUiWithPosts(List<Post> posts) {
        adapter.setPosts(posts);

    }
}