package com.example.bulletinboard.ui.post;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bulletinboard.LocationHelper;
import com.example.bulletinboard.R;
import com.example.bulletinboard.data.model.Post;
import com.example.bulletinboard.ui.home.BulletinBoardViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

import com.example.bulletinboard.databinding.ItemPostBinding;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    private List<Post> posts;
    private List<Post> filteredPosts;
    private BulletinBoardViewModel viewModel;

    private String myLocation;


    public PostAdapter(BulletinBoardViewModel viewModel, String myLocation, MyButtonClickListener listener ) {
        this.viewModel = viewModel;
        this.myButtonClickListener = listener;
        this.myLocation = myLocation;

    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
        this.filteredPosts = new ArrayList<>(posts);
        notifyDataSetChanged();
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
        Post post = filteredPosts.get(position);
        holder.bind(post, viewModel);

        holder.binding.editButton.setOnClickListener(view -> {

            if (myButtonClickListener != null) {
                myButtonClickListener.onUpdateButtonClicked(post);
            }
        });

        holder.binding.shareButton.setOnClickListener(view -> {
            if(myButtonClickListener != null) {
                myButtonClickListener.OnSharePostButtonClicked(post);
            }
        });
    }

    public void filter(String text) {
        if (filteredPosts!=null) {
            filteredPosts.clear();
        }
        if (text.isEmpty()) {
            filteredPosts.addAll(posts);
        } else {
            text = text.toLowerCase();
            for (Post post : posts) {
                if (post.getTitle().toLowerCase().contains(text) ||
                        post.getDescription().toLowerCase().contains(text) ||
                        post.getCreatorName().toLowerCase().contains(text) ||
                        post.getDate().toLowerCase().contains(text)) {
                    filteredPosts.add(post);
                }
            }
        }
        notifyDataSetChanged();
    }

    public void filterByCurrentLocation() {
        filteredPosts.clear();
        for (Post post : posts) {
            if (post.getLocation().contains(myLocation)) {
                filteredPosts.add(post);
            }
        }

        setPosts(filteredPosts);


        notifyDataSetChanged();
    }

    public void setCurrentLocation(String location) {
        this.myLocation = location;
        notifyDataSetChanged();
    }

    public interface MyButtonClickListener {
        void onUpdateButtonClicked(Post post);

        void OnSharePostButtonClicked(Post post);
    }


    private MyButtonClickListener myButtonClickListener;

    @Override
    public int getItemCount() {
        return filteredPosts.size();
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
