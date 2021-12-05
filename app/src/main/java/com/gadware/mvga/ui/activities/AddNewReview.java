package com.gadware.mvga.ui.activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.gadware.mvga.databinding.ActivityAddNewReviewBinding;

import java.util.Date;
import java.util.Objects;

public class AddNewReview extends AppCompatActivity {
    ActivityAddNewReviewBinding binding;


    private long trainerId;
    private String  guestId,guestName, review, rating;
    float rat = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddNewReviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        trainerId=getIntent().getLongExtra("t_id",-1);

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