package com.gadware.mvga.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.gadware.mvga.databinding.ActivityAuthenticationBinding;
import com.gadware.mvga.vm.UserViewModel;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class Authentication extends AppCompatActivity {
    ActivityAuthenticationBinding binding;
    UserViewModel userViewModel;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAuthenticationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sharedPref = getSharedPreferences("MVGA", Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        binding.signupBtn.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
        });

        binding.doneBtn.setOnClickListener(v -> {
            String email = binding.tvEmail.getText().toString();
            if (email.isEmpty()|| !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.tvEmail.setError("invalid");
                Toast.makeText(Authentication.this, "Email Empty", Toast.LENGTH_SHORT).show();

                return;
            } else {
                binding.tvEmail.setError(null);
            }

            String pass = binding.tvPass.getText().toString();
            if (pass.isEmpty()) {
                binding.tvPass.setError("invalid");
                Toast.makeText(Authentication.this, "Password Empty", Toast.LENGTH_SHORT).show();
                return;
            } else {
                if (pass.length() < 6) {
                    Toast.makeText(Authentication.this, "Password min length 6", Toast.LENGTH_SHORT).show();
                    return;
                }
                binding.tvPass.setError(null);
            }
            LoginUser(email, pass);
        });
    }

    private void LoginUser(String email, String pass) {
        userViewModel.getUserid(email,pass).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).
                subscribe(new SingleObserver<Long>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull Long aLong) {
                        editor.putLong("userId",aLong);
                        editor.apply();
                        Toast.makeText(Authentication.this, "Successful...", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Authentication.this,MainActivity.class));
                        finish();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Toast.makeText(Authentication.this, "Error.!\n" + "Email and or password invalid", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}