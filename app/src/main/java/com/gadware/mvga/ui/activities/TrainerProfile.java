package com.gadware.mvga.ui.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.gadware.mvga.adapters.ReviewAdapter;
import com.gadware.mvga.adapters.ServiceAdapter;
import com.gadware.mvga.databinding.ActivityTrainerProfileBinding;
import com.gadware.mvga.models.SubscriptionInfoModel;
import com.gadware.mvga.models.TrainerInfo;
import com.gadware.mvga.vm.ReviewViewModel;
import com.gadware.mvga.vm.ServiceViewModel;
import com.gadware.mvga.vm.SubscriptionViewModel;
import com.gadware.mvga.vm.TrainerViewModel;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class TrainerProfile extends AppCompatActivity implements ServiceAdapter.BookInterface {

    ActivityTrainerProfileBinding binding;
    TrainerViewModel trainerViewModel;
    SubscriptionViewModel subscriptionViewModel;
    ReviewViewModel reviewViewModel;
    ServiceViewModel serviceViewModel;
    ReviewAdapter adapter;
    ServiceAdapter adapter2;
    Disposable dx;
    private long trainerId,userId;
    String trainerName,subtype;
    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTrainerProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sharedPref = getSharedPreferences("MVGA", Context.MODE_PRIVATE);
        userId = sharedPref.getLong("userId", -1);

        reviewViewModel = new ViewModelProvider(this).get(ReviewViewModel.class);
        serviceViewModel = new ViewModelProvider(this).get(ServiceViewModel.class);
        trainerViewModel = new ViewModelProvider(this).get(TrainerViewModel.class);
        subscriptionViewModel = new ViewModelProvider(this).get(SubscriptionViewModel.class);


        binding.recycler.setLayoutManager(new LinearLayoutManager(this));
        binding.recycler2.setLayoutManager(new LinearLayoutManager(this));


        trainerId = getIntent().getLongExtra("id", -1);


        GetTrainerInfo();
    }

    private void GetTrainerInfo() {
        trainerViewModel.getUserInfo(trainerId).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new SingleObserver<TrainerInfo>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onSuccess(@NonNull TrainerInfo model) {
                trainerName = model.getTrainerName();
                binding.nameTv.setText(model.getTrainerName());
                binding.emailTv.setText(model.getTrainerEmail());
                binding.expTv.setText(model.getExperience() + " years");
                GetReviews();
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void GetServiceList() {
        dx = serviceViewModel.getUserInfo(trainerId).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(reviewInfoModels -> {
                    adapter2 = new ServiceAdapter(this, reviewInfoModels, this);
                    binding.recycler2.setAdapter(adapter2);
                    adapter2.notifyDataSetChanged();
                    GetSubsInfo();
                });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void GetReviews() {
        dx = reviewViewModel.getReviewInfoModelList(trainerId).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(reviewInfoModels -> {
                    adapter = new ReviewAdapter(this, reviewInfoModels);
                    binding.recycler.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    GetServiceList();
                });
    }
    private void GetSubsInfo() {
        subscriptionViewModel.getUserInfo(userId).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new SingleObserver<SubscriptionInfoModel>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onSuccess(@NonNull SubscriptionInfoModel model) {
                subtype=model.getSubName();
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }
        });
    }

    @Override
    public void BookService(long serviceId, String serv, String desc) {

        if (subtype.equals("Pay Per View")) {
            Toast.makeText(this, "Available for Monthly and Yearly Subscribers", Toast.LENGTH_SHORT).show();
        } else if (subtype.equals("none")) {
            Toast.makeText(this, "Add a subscription first", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(this, AddBooking.class);
            intent.putExtra("t_id", trainerId);
            intent.putExtra("s_id", serviceId);
            intent.putExtra("trainer", trainerName);
            intent.putExtra("serv", serv);
            intent.putExtra("desc", desc);
            startActivity(intent);
        }
    }
}