package com.gadware.mvga.ui.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.gadware.mvga.R;
import com.gadware.mvga.databinding.ActivityMainBinding;
import com.gadware.mvga.models.PackageInfo;
import com.gadware.mvga.vm.ReviewViewModel;
import com.gadware.mvga.vm.ServiceViewModel;
import com.gadware.mvga.vm.SubscriptionViewModel;
import com.gadware.mvga.vm.TrainerViewModel;
import com.gadware.mvga.vm.UserViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    SubscriptionViewModel subscriptionViewModel;
    UserViewModel userViewModel;
    TrainerViewModel trainerViewModel;
    ServiceViewModel serviceViewModel;
    ReviewViewModel reviewViewModel;

    public static long userId;
    public static boolean firstLaunch = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        subscriptionViewModel = new ViewModelProvider(this).get(SubscriptionViewModel.class);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        trainerViewModel = new ViewModelProvider(this).get(TrainerViewModel.class);
        serviceViewModel = new ViewModelProvider(this).get(ServiceViewModel.class);
        reviewViewModel = new ViewModelProvider(this).get(ReviewViewModel.class);

        sharedPref = getSharedPreferences("MVGA", Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        userId = sharedPref.getLong("userId", -1);
        firstLaunch = sharedPref.getBoolean("firstLaunch", false);
        if (!firstLaunch) {
            InsertPkgList();
        } else if (userId == -1) {
            startActivity(new Intent(this, Authentication.class));
            finish();
        }
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
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
        Completable.fromAction(() -> subscriptionViewModel.insertPkgList(PackageInfo.getList())).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
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
        Completable.fromAction(() -> subscriptionViewModel.insertPkgList(PackageInfo.getList())).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
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
        Completable.fromAction(() -> subscriptionViewModel.insertPkgList(PackageInfo.getList())).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        InsertReviewList();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });
    }

    private void InsertReviewList() {
        Completable.fromAction(() -> subscriptionViewModel.insertPkgList(PackageInfo.getList())).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        editor.putBoolean("firstLaunch", true).apply();
                        Toast.makeText(MainActivity.this, "You are all set", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MainActivity.this, Authentication.class));
                        finish();

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });
    }

    @SuppressLint("UnsafeExperimentalUsageError")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.menu_signout) {
            editor.clear();
            editor.apply();
            startActivity(new Intent(this, Authentication.class));
            finish();
            return false;
        }
        if (item.getItemId() == R.id.menu_payment) {
            Intent intent = new Intent(this, Payment.class);
            startActivity(intent);
            return false;
        }
        if (item.getItemId() == R.id.menu_pkg_list) {
            startActivity(new Intent(this, PackageList.class));
            return false;
        }
        return super.onOptionsItemSelected(item);
    }
}