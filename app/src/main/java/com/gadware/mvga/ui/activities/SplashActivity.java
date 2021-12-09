package com.gadware.mvga.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.gadware.mvga.databinding.ActivitySplashBinding;
import com.gadware.mvga.models.PackageInfo;
import com.gadware.mvga.models.ReviewInfo;
import com.gadware.mvga.models.ServiceInfo;
import com.gadware.mvga.models.TrainerInfo;
import com.gadware.mvga.models.UserInfo;
import com.gadware.mvga.vm.ReviewViewModel;
import com.gadware.mvga.vm.ServiceViewModel;
import com.gadware.mvga.vm.SubscriptionViewModel;
import com.gadware.mvga.vm.TrainerViewModel;
import com.gadware.mvga.vm.UserViewModel;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SplashActivity extends AppCompatActivity {

    ActivitySplashBinding binding;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    SubscriptionViewModel subscriptionViewModel;
    UserViewModel userViewModel;
    TrainerViewModel trainerViewModel;
    ServiceViewModel serviceViewModel;
    ReviewViewModel reviewViewModel;

    public static boolean firstLaunch = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        subscriptionViewModel = new ViewModelProvider(this).get(SubscriptionViewModel.class);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        trainerViewModel = new ViewModelProvider(this).get(TrainerViewModel.class);
        serviceViewModel = new ViewModelProvider(this).get(ServiceViewModel.class);
        reviewViewModel = new ViewModelProvider(this).get(ReviewViewModel.class);

        sharedPref = getSharedPreferences("MVGA", Context.MODE_PRIVATE);
        editor = sharedPref.edit();


        firstLaunch = sharedPref.getBoolean("firstLaunch", true);
        if (firstLaunch) {
            InsertPkgList();
        } else {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
        }
    }

    private void InsertPkgList() {
        Completable.fromAction(() -> subscriptionViewModel.insertPkgList(PackageInfo.getList())).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        InsertUserList();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });
    }

    private void InsertUserList() {
        Completable.fromAction(() -> userViewModel.insertUserList(UserInfo.getList())).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        InsertTrainerList();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });
    }

    private void InsertTrainerList() {
        Completable.fromAction(() -> trainerViewModel.insertUserList(TrainerInfo.getList())).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        InsertServiceList();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });
    }

    private void InsertServiceList() {
        Completable.fromAction(() -> serviceViewModel.insertUserList(ServiceInfo.getList())).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        editor.putBoolean("firstLaunch", false).apply();
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        finish();
                       //InsertReviewList();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });
    }

    private void InsertReviewList() {
        Completable.fromAction(() -> reviewViewModel.insertUserList(ReviewInfo.getList())).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        editor.putBoolean("firstLaunch", false).apply();
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        finish();

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });
    }
}