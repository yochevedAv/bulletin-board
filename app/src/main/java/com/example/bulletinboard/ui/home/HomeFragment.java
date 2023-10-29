
package com.example.bulletinboard.ui.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
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
import com.example.bulletinboard.ui.createPost.CreatePostActivity;
import com.example.bulletinboard.ui.login.LoginActivity;
import com.example.bulletinboard.ui.post.PostFragment;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements PostAdapter.OnUpdateButtonClickListener  {


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

        adapter = new PostAdapter(viewModel, this); // Create your adapter
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

        viewModel.getDeletePostResult().observe(getActivity(), success -> {
            if (success) {
                viewModel.getPosts();
            } else {
                // Handle failed post deletion
            }
        });


        binding.logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferencesManager.clearUser(getActivity().getApplicationContext());


                Intent intent = new Intent(view.getContext(), LoginActivity.class);
                view.getContext().startActivity(intent);
            }
        });



        return binding.getRoot();
    }

    private void updateUiWithPosts(List<Post> posts) {
        adapter.setPosts(posts);

    }

    @Override
    public void onUpdateButtonClicked(Post post) {
        Fragment postFragment = new PostFragment();
        Bundle args = new Bundle();
        args.putString("action", "edit");
        args.putSerializable("post", post);
        postFragment.setArguments(args);
        FragmentTransaction transaction =getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, postFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void onUpdateButtonClick(Post post) {

    }
}