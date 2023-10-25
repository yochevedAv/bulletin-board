package com.example.bulletinboard.ui.createPost;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.bulletinboard.MainActivity;
import com.example.bulletinboard.R;
import com.example.bulletinboard.ResponseResult;
import com.example.bulletinboard.SharedPreferencesManager;
import com.example.bulletinboard.data.model.User;
import com.example.bulletinboard.databinding.ActivityCreatePostBinding;
import com.example.bulletinboard.ui.registration.RegistrationFormState;
import com.example.bulletinboard.ui.registration.RegistrationResult;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CreatePostActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private CreatePostViewModel createPostViewModel;
    private ActivityCreatePostBinding binding;
    private TextInputEditText dateEditText;
    private TextInputEditText locationEditText;

    private FusedLocationProviderClient fusedLocationProviderClient;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;
    private static final int REQUEST_LOCATION_UPDATE = 1;

    private String myLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCreatePostBinding.inflate(getLayoutInflater());
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        setContentView(binding.getRoot());

        createPostViewModel = new ViewModelProvider(this, new createPostViewModelFactory(this)).get(CreatePostViewModel.class);

        final TextInputEditText titleEditText = binding.editTextTitle;
        dateEditText = binding.editTextDate;
        final TextInputEditText creatorNameEditText = binding.editTextCreatorName;
        locationEditText = binding.editTextLocation;
        final TextInputLayout locationTextField = binding.textFieldLocation;
        final TextInputEditText descriptionEditText = binding.editTextDescription;
        final Button createPostButton = binding.buttonCreatePost;


        createPostViewModel.getCreatePostFormState().observe(this, new Observer<CreatePostFormState>() {
            @Override
            public void onChanged(@Nullable CreatePostFormState createPostFormState) {
                if (createPostFormState == null) {
                    return;
                }
                createPostButton.setEnabled(createPostFormState.isDataValid());

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

        createPostViewModel.getCreatePostResult().observe(this, new Observer<ResponseResult>() {
            @Override
            public void onChanged(@Nullable ResponseResult createPostResult) {
                if (createPostResult == null) {
                    return;
                }
                if (createPostResult.getError() != null) {
                    Toast.makeText(getApplicationContext(), "Failed to save post", Toast.LENGTH_LONG).show();
                }
                if (createPostResult.getSuccess() != null) {
                    finish();
                }
                setResult(Activity.RESULT_OK);

                //Complete and destroy login activity once successful
                finish();
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
                createPostViewModel.CreatePostDataChanged(titleEditText.getText().toString(),creatorNameEditText.getText().toString(),dateEditText.getText().toString(),locationEditText.getText().toString(),descriptionEditText.getText().toString());
            }
        };


        titleEditText.addTextChangedListener(afterTextChangedListener);
        creatorNameEditText.addTextChangedListener(afterTextChangedListener);
        dateEditText.addTextChangedListener(afterTextChangedListener);
        locationEditText.addTextChangedListener(afterTextChangedListener);
        descriptionEditText.addTextChangedListener(afterTextChangedListener);

        // Set the current date in the dateEditText field
        setCurrentDate();


        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            getCurrentLocation();
        }

        locationTextField.setEndIconOnClickListener(view -> editLocation(view));

        createPostButton.setOnClickListener(v -> {

            User user = SharedPreferencesManager.getUser(this);

            createPostViewModel.createPost(user.get_id(),titleEditText.getText().toString(),creatorNameEditText.getText().toString(),dateEditText.getText().toString(),locationEditText.getText().toString(),descriptionEditText.getText().toString());

            finish();
        });
    }


    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();

                        String address = reverseGeocode(latitude, longitude);
                        myLocation = address;
                        locationEditText.setText(myLocation);
                    }
                });
    }

    // Function to set the current date in the dateEditText field
    private void setCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        dateEditText.setText(dateFormat.format(calendar.getTime()));
    }

    private String reverseGeocode(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
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


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getCurrentLocation();
                } else {
                }
                break;
            }
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

        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            mapActivityResultLauncher.launch(mapIntent);
        } else {
        }
    }


    private static final int REQUEST_CODE_MAP = 123;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_LOCATION_UPDATE) {
            if (resultCode == RESULT_OK) {
                // Get the updated location from the data
                String updatedLocation = data.getStringExtra("location");
                if (updatedLocation != null) {
                    // Update the location EditText with the new location
                    EditText locationEditText = findViewById(R.id.editTextLocation);
                    locationEditText.setText(updatedLocation);
                }
            }
        }

        if (requestCode == REQUEST_LOCATION_UPDATE && resultCode == RESULT_OK) {
            // Handle the chosen location data from the 'data' intent
            String chosenLocation = data.getDataString();
            locationEditText.setText(chosenLocation);
        }
    }

    private boolean isPackageInstalled(String packageName) {
        try {
            getPackageManager().getPackageInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    private final ActivityResultLauncher<Intent> mapActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                }
            }
    );

}
