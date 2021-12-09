package com.gadware.mvga.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.gadware.mvga.databinding.ActivityRegisterBinding;
import com.gadware.mvga.models.SubscriptionInfo;
import com.gadware.mvga.models.UserInfo;
import com.gadware.mvga.vm.SubscriptionViewModel;
import com.gadware.mvga.vm.UserViewModel;

import java.util.Objects;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class RegisterActivity extends AppCompatActivity {
    ActivityRegisterBinding binding;
    private String name, email, pass, age, height, weight, address;
    UserViewModel userViewModel;
    SubscriptionViewModel subscriptionViewModel;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        subscriptionViewModel = new ViewModelProvider(this).get(SubscriptionViewModel.class);

        sharedPref = getSharedPreferences("MVGA", Context.MODE_PRIVATE);
        editor = sharedPref.edit();


        binding.haveAcc.setOnClickListener(v -> {
            startActivity(new Intent(this, Authentication.class));
        });

        binding.sendBtn.setOnClickListener(v -> {
            if (Validate() == 1) {
                binding.loadingBar2.setVisibility(View.VISIBLE);
                try {
                    CreateAccount();
                } catch (Exception e) {
                    binding.loadingBar2.setVisibility(View.INVISIBLE);
                    Toast.makeText(this, "Error.!\n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private int Validate() {
        name = binding.etName.getText().toString();
        if (name.isEmpty()) {
            binding.etName.setError("Invalid");
            Toast.makeText(RegisterActivity.this, "Name Empty", Toast.LENGTH_SHORT).show();

            return 0;
        } else {
            binding.etName.setError(null);
        }

        email = binding.etEmail.getText().toString();
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etEmail.setError("Invalid");
            Toast.makeText(RegisterActivity.this, "Invalid Email", Toast.LENGTH_SHORT).show();

            return 0;
        } else {
            binding.etEmail.setError(null);
        }
        pass = binding.etPassword.getText().toString();
        if (pass.isEmpty() || pass.length() < 6) {
            binding.etPassword.setError("Invalid");
            Toast.makeText(RegisterActivity.this, "Pass Invalid", Toast.LENGTH_SHORT).show();

            return 0;
        } else {
            binding.etPassword.setError(null);
        }
        age = binding.etAge.getText().toString();
        if (age.isEmpty()) {
            binding.etAge.setError("Invalid");
            Toast.makeText(RegisterActivity.this, "Age Empty", Toast.LENGTH_SHORT).show();

            return 0;
        } else {
            binding.etAge.setError(null);
        }
        height = binding.etHeight.getText().toString();
        if (height.isEmpty()) {
            binding.etHeight.setError("Invalid");
            Toast.makeText(RegisterActivity.this, "Height Empty", Toast.LENGTH_SHORT).show();

            return 0;
        } else {
            binding.etHeight.setError(null);
        }

        weight = binding.etWeight.getText().toString();
        if (weight.isEmpty()) {
            binding.etWeight.setError("Invalid");
            Toast.makeText(RegisterActivity.this, "Weight Empty", Toast.LENGTH_SHORT).show();

            return 0;
        } else {
            binding.etWeight.setError(null);
        }

        address = binding.etAddress.getText().toString();
        if (address.isEmpty()) {
            binding.etAddress.setError("Invalid");
            Toast.makeText(RegisterActivity.this, "Address Empty", Toast.LENGTH_SHORT).show();

            return 0;
        } else {
            binding.etAddress.setError(null);
        }

        if (!binding.chkTerms.isChecked()){
            Toast.makeText(this, "Accept Terms and Conditions", Toast.LENGTH_SHORT).show();
            return 0;
        }
        return 1;
    }


    private void CreateAccount() {
        userViewModel.insertSingleUser(new UserInfo(name, email, pass, age, height, weight, address, "0", "0", null)).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new SingleObserver<Long>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onSuccess(@NonNull Long aLong) {
                editor.putLong("userId", aLong);
                editor.apply();
                AddSubscription(aLong);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                binding.loadingBar2.setVisibility(View.INVISIBLE);
                if (Objects.requireNonNull(e.getMessage()).contains("UNIQUE constraint failed")) {
                    Toast.makeText(RegisterActivity.this, "Error.!\n" + "Email already used", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    private void AddSubscription(long userId) {
        Completable.fromAction(() -> subscriptionViewModel.insertUser(new SubscriptionInfo(1, userId, "none", "none", "none"))).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onComplete() {


                long adminId = sharedPref.getLong("admin", -1);
                if (adminId == -1) {
                    editor.putLong("admin", userId);
                    editor.apply();
                }

                Toast.makeText(RegisterActivity.this, "Successful...", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                finish();
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }
        });

    }

}