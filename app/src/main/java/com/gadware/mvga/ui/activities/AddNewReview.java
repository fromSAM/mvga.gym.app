package com.gadware.mvga.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;


import com.gadware.mvga.databinding.ActivityAddNewReviewBinding;
import com.gadware.mvga.models.ReviewInfo;
import com.gadware.mvga.vm.ReviewViewModel;
import com.gadware.mvga.vm.SubscriptionViewModel;
import com.gadware.mvga.vm.UserViewModel;

import java.sql.Date;
import java.util.Objects;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class AddNewReview extends AppCompatActivity {
    ActivityAddNewReviewBinding binding;

    ReviewViewModel reviewViewModel;
    private long trainerId;
    private String  guestId,guestName, review, rating;
    float rat = 0;
    public long userId;
    SharedPreferences sharedPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddNewReviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        trainerId=getIntent().getLongExtra("t_id",-1);
        reviewViewModel = new ViewModelProvider(this).get(ReviewViewModel.class);

        sharedPref = getSharedPreferences("MVGA", Context.MODE_PRIVATE);
        userId = sharedPref.getLong("userId", -1);

        binding.btnCheckin.setOnClickListener(v -> {
            if (Validate()==1){
                AddReview();
            }
        });

    }

    private void AddReview() {
        Completable.fromAction(() -> reviewViewModel.insertSingleUser(new ReviewInfo(userId,trainerId,rat,String.valueOf(new Date(System.currentTimeMillis())),review))).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onComplete() {
                Toast.makeText(AddNewReview.this, "Successful", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(AddNewReview.this,MainActivity.class));
                finish();
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Toast.makeText(AddNewReview.this, "Error.!\n" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private int Validate() {

        review = binding.inputReview.getText().toString();
        if (review.isEmpty()) {
            binding.nameLayout.setError("invalid");
            return 0;
        } else {
            binding.nameLayout.setError(null);
        }

        rat = binding.ratingBar.getRating();
        if (rat == 0) {
            Toast.makeText(this, "Rating empty", Toast.LENGTH_SHORT).show();
            return 0;
        } else {
            rating = String.valueOf(rat);
        }

        return 1;
    }
}