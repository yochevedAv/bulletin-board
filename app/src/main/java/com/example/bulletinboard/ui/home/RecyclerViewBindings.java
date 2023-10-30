package com.example.bulletinboard.ui.home;

import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bulletinboard.data.model.Post;
import com.example.bulletinboard.ui.post.PostAdapter;

import java.util.List;

public class RecyclerViewBindings {
    @BindingAdapter("app:items")
    public static void setItems(RecyclerView recyclerView, List<Post> items) {
        PostAdapter adapter = (PostAdapter) recyclerView.getAdapter();
        if (adapter != null) {
            adapter.setPosts(items);
        }
    }
}
