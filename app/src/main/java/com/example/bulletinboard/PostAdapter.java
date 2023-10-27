package com.example.bulletinboard;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bulletinboard.data.model.Post;
import com.example.bulletinboard.ui.home.BulletinBoardViewModel;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;

import java.util.List;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import com.example.bulletinboard.databinding.ItemPostBinding;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    private List<Post> posts;
    private BulletinBoardViewModel viewModel;

    public PostAdapter(BulletinBoardViewModel viewModel) {
        this.viewModel = viewModel;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
        notifyDataSetChanged(); // Notify the adapter that the data has changed
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPostBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_post,
                parent, false
        );

        return new PostViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.bind(post, viewModel);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {
        private ItemPostBinding binding;

        public PostViewHolder(ItemPostBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Post post, BulletinBoardViewModel viewModel) {
            binding.setPost(post);
            binding.setViewModel(viewModel);
            binding.executePendingBindings();

            // Initialize the MapView and handle map setup

            MapsInitializer.initialize(itemView.getContext());
            MapView mapView = binding.mapViewPostLocation;
            mapView.onCreate(null);
            mapView.onResume();

            mapView.getMapAsync(googleMap -> {
                // You can now interact with the GoogleMap instance
                if (post != null) {
                    LatLng latLng = LocationHelper.getLatLongFromLocation(itemView.getContext(), post.getLocation());
                    if (latLng != null) {
                        double latitude = latLng.latitude;
                        double longitude = latLng.longitude;

                        LatLng location = new LatLng(latitude, longitude);
                        MarkerOptions markerOptions = new MarkerOptions().position(location).title("My Location");

                        googleMap.addMarker(markerOptions);
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15)); // Adjust the zoom level as needed
                    }
                }
            });
        }

    }
}
