package com.example.bulletinboard.ui.post;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.bulletinboard.R;
import com.example.bulletinboard.server.result.ResponseResult;
import com.example.bulletinboard.SharedPreferencesManager;
import com.example.bulletinboard.data.model.Post;
import com.example.bulletinboard.data.model.User;
import com.example.bulletinboard.databinding.FragmentPostBinding;
import com.example.bulletinboard.ui.home.HomeFragment;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class PostFragment extends Fragment {

    //edit = 1 new = 0
    private boolean action;

    private Post editPost;

    private CreatePostViewModel createPostViewModel;
    private FragmentPostBinding binding;
    private TextInputEditText dateEditText;
    private TextInputEditText locationEditText;

    private FusedLocationProviderClient fusedLocationProviderClient;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;

    private String myLocation;

    public PostFragment()
    {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle args = getArguments();

        if (args != null) {
            String actionValue = args.getString("action");
            action = actionValue.equals("edit")?true:false;
            if(action){
                editPost =  (Post) args.getSerializable("post");
            }
        }

        binding = FragmentPostBinding.inflate(inflater, container, false);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        createPostViewModel = new ViewModelProvider(this, new createPostViewModelFactory(getContext())).get(CreatePostViewModel.class);

        final TextInputEditText titleEditText = binding.editTextTitle;
        dateEditText = binding.editTextDate;
        final TextInputEditText creatorNameEditText = binding.editTextCreatorName;
        locationEditText = binding.editTextLocation;
        final TextInputLayout locationTextField = binding.textFieldLocation;
        final TextInputEditText descriptionEditText = binding.editTextDescription;
        final Button savePostButton = binding.buttonSavePost;


        if(action){
            binding.setEditPost(editPost);
        }


        createPostViewModel.getCreatePostFormState().observe(getActivity(), new Observer<CreatePostFormState>() {
            @Override
            public void onChanged(@Nullable CreatePostFormState createPostFormState) {
                if (createPostFormState == null) {
                    return;
                }
                savePostButton.setEnabled(createPostFormState.isDataValid());

                if (createPostFormState.getTitleError() != null) {
                    titleEditText.setError(getString(createPostFormState.getTitleError()));
                }
                if (createPostFormState.getCreatorNameError() != null) {
                    creatorNameEditText.setError(getString(createPostFormState.getCreatorNameError()));
                }
                if (createPostFormState.getDescriptionError() != null) {
                    descriptionEditText.setError(getString(createPostFormState.getDescriptionError()));
                }
                if (createPostFormState.getDateError() != null) {
                    dateEditText.setError(getString(createPostFormState.getDateError()));
                }
                if (createPostFormState.getLocationError() != null) {
                    locationTextField.setError(getString(createPostFormState.getLocationError()));
                }
            }
        });

        createPostViewModel.getCreatePostResult().observe(getActivity(), new Observer<ResponseResult>() {
            @Override
            public void onChanged(@Nullable ResponseResult createPostResult) {
                if (createPostResult == null) {
                    return;
                }
                if (createPostResult.getError() != null) {
                    Toast.makeText(getContext(), "Failed to save post", Toast.LENGTH_LONG).show();
                }
                if (createPostResult.getSuccess() != null) {
                    moveToMainFragment();
                }

            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                createPostViewModel.CreatePostDataChanged(titleEditText.getText().toString(), creatorNameEditText.getText().toString(), dateEditText.getText().toString(), locationEditText.getText().toString(), descriptionEditText.getText().toString());
            }
        };

        titleEditText.addTextChangedListener(afterTextChangedListener);
        creatorNameEditText.addTextChangedListener(afterTextChangedListener);
        dateEditText.addTextChangedListener(afterTextChangedListener);
        locationEditText.addTextChangedListener(afterTextChangedListener);
        descriptionEditText.addTextChangedListener(afterTextChangedListener);

        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            getCurrentLocation();
        }

        locationTextField.setEndIconOnClickListener(view -> editLocation(view));

        setCurrentDate();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonSavePost.setOnClickListener(v -> {
            User user = SharedPreferencesManager.getUser(getActivity());
            if (!action) {
                createPostViewModel.createPost(user.getId(), binding.editTextTitle.getText().toString(), binding.editTextCreatorName.getText().toString(), dateEditText.getText().toString(), locationEditText.getText().toString(), binding.editTextDescription.getText().toString());
                moveToMainFragment();
            }
            else{
                createPostViewModel.editPost(user.getId(), binding.editTextTitle.getText().toString(), binding.editTextCreatorName.getText().toString(), dateEditText.getText().toString(), locationEditText.getText().toString(), binding.editTextDescription.getText().toString(),editPost.getId());
                moveToMainFragment();
            }
        });
    }

    private void moveToMainFragment() {

        Fragment homeFragment = new HomeFragment();
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, homeFragment);
        transaction.addToBackStack(null);
        transaction.commit();
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
                    if(locationEditText.getText().toString().equals("")) {
                        locationEditText.setText(myLocation);
                    }
                }
            }
        };
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());

    }

    // Function to set the current date in the dateEditText field
    private void setCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        dateEditText.setText(dateFormat.format(calendar.getTime()));
        binding.setCurrentDate(dateFormat.format(calendar.getTime()));
        Log.d("dateEditText",dateEditText.getText().toString());
    }

    private String reverseGeocode(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        List<Address> addresses;

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);

            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                // Get various address components, e.g., address, city, country, etc.
                return address.getAddressLine(0);
            } else {
                return "Address not found";
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Error during reverse geocoding";
        }
    }

    public void editLocation(View view) {
        String location = myLocation; // Provide the location or coordinates you want to show in Google Maps

        Uri locationUri = Uri.parse("geo:0,0?q=" + location);

        Intent mapIntent = new Intent(Intent.ACTION_VIEW, locationUri);

        if (isPackageInstalled("org.openstreetmap.android")) {
            mapIntent.setPackage("org.openstreetmap.android");
        } else {
            mapIntent.setPackage("com.google.android.apps.maps");
        }

        if (mapIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            mapActivityResultLauncher.launch(mapIntent);
        } else {
        }
    }


    private boolean isPackageInstalled(String packageName) {
        try {
            getActivity().getPackageManager().getPackageInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    private final ActivityResultLauncher<Intent> mapActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                }
            }
    );
}