package com.example.bulletinboard.ui.registration;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bulletinboard.MainActivity;
import com.example.bulletinboard.R;
import com.example.bulletinboard.ResponseResult;
import com.example.bulletinboard.SharedPreferencesManager;
import com.example.bulletinboard.data.model.User;
import com.example.bulletinboard.databinding.ActivityRegistrationBinding;
import com.google.android.material.textfield.TextInputEditText;

public class RegistrationActivity extends AppCompatActivity {


    private RegistrationViewModel registrationViewModel;
    private ActivityRegistrationBinding binding;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRegistrationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        registrationViewModel = new ViewModelProvider(this, new RegistrationViewModelFactory())
                .get(RegistrationViewModel.class);

        final TextInputEditText emailEditText = binding.emailEditText;
        final TextInputEditText passwordEditText = binding.passwordEditText;
        final TextInputEditText usernameEditText = binding.usernameEditText;
        final Button loginButton = binding.loginButton;
        final ProgressBar loadingProgressBar = binding.loading;

        registrationViewModel.getRegistrationFormState().observe(this, new Observer<RegistrationFormState>() {
            @Override
            public void onChanged(@Nullable RegistrationFormState registrationFormState) {
                if (registrationFormState == null) {
                    return;
                }
                loginButton.setEnabled(registrationFormState.isDataValid());
                if (registrationFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(registrationFormState.getUsernameError()));
                }
                if (registrationFormState.getEmailError() != null) {
                    emailEditText.setError(getString(registrationFormState.getEmailError()));
                }
                if (registrationFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(registrationFormState.getPasswordError()));
                }
            }
        });

        registrationViewModel.getResponseResult().observe(this, new Observer<ResponseResult>() {
            @Override
            public void onChanged(@Nullable ResponseResult responseResult) {
                if (responseResult == null) {
                    return;
                }
                loadingProgressBar.setVisibility(View.GONE);
                if (responseResult.getError() != null) {
                    showLoginFailed(responseResult.getError());
                }
                if (responseResult.getSuccess() != null) {
                    updateUiWithUser(responseResult.getSuccess());
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
                registrationViewModel.RegistrationDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString(),emailEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        emailEditText.addTextChangedListener(afterTextChangedListener);

        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    registrationViewModel.register(usernameEditText.getText().toString(),
                            passwordEditText.getText().toString(),emailEditText.getText().toString());
                }
                return false;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                registrationViewModel.register(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString(),emailEditText.getText().toString());
            }
        });
    }

    private void updateUiWithUser(User model) {

        SharedPreferencesManager.saveUser(getApplicationContext(), model);

        String welcome = getString(R.string.welcome) + model.getUserName();
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
        openMainActivity();
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }

    private void openMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}