
package com.example.bulletinboard.ui.home;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bulletinboard.ui.post.PostAdapter;
import com.example.bulletinboard.R;
import com.example.bulletinboard.SharedPreferencesManager;
import com.example.bulletinboard.data.model.Post;
import com.example.bulletinboard.databinding.FragmentHomeBinding;
import com.example.bulletinboard.ui.login.LoginActivity;
import com.example.bulletinboard.ui.post.PostFragment;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment implements PostAdapter.MyButtonClickListener  {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;
    private FragmentHomeBinding binding;
    private BulletinBoardViewModel viewModel;
    private PostAdapter adapter;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private String myLocation;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(BulletinBoardViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

        viewModel.getPosts();

        RecyclerView recyclerView = binding.recyclerViewPosts;
        EditText searchEditText = binding.editTextSearch;


        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            getCurrentLocation();
        }
        adapter = new PostAdapter(viewModel,myLocation, this); // Create your adapter
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


        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String searchText = s.toString().trim();
                adapter.filter(searchText);
            }
        });

        binding.myLocationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    adapter.filterByCurrentLocation();
                } else {
                    adapter.setPosts(viewModel.getPostsLiveData().getValue());
                }
            }
        });

        binding.logoutButton.setOnClickListener(view -> {

            SharedPreferencesManager.clearUser(getActivity().getApplicationContext());
            Intent intent = new Intent(view.getContext(), LoginActivity.class);
            view.getContext().startActivity(intent);
        });

        return binding.getRoot();
    }

    public HomeFragment() {
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

    @Override
    public void OnSharePostButtonClicked(Post post) {
        shareViaWhatsApp(post);
    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        LocationRequest locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10000) // Update every 10 seconds
                .setFastestInterval(5000); // Fastest update interval

        LocationCallback locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    // Handle the received location
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    myLocation =  reverseGeocode(latitude,longitude);
                    adapter.setCurrentLocation(myLocation);
                }
            }
        };
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());

    }

    private void shareViaWhatsApp(Post post) {
        try {
            Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
            whatsappIntent.setType("text/plain");
            whatsappIntent.setPackage("com.whatsapp"); // Specify WhatsApp package name

            whatsappIntent.putExtra(Intent.EXTRA_TEXT, post.getTitle()+"\n" + post.getDescription()+"\n"+post.getCreatorName());

            startActivity(whatsappIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getActivity(), "WhatsApp is not installed.", Toast.LENGTH_SHORT).show();
        }
    }


    private String reverseGeocode(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        List<Address> addresses;

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);

            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                return address.getAddressLine(0);
            } else {
                return "Address not found";
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Error during reverse geocoding";
        }
    }
}