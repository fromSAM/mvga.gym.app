package com.gadware.mvga.ui.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.gadware.mvga.adapters.PackageAdapter;
import com.gadware.mvga.databinding.ActivityPackageListBinding;
import com.gadware.mvga.databinding.DialogPackageEditBinding;
import com.gadware.mvga.models.PackageInfo;
import com.gadware.mvga.vm.SubscriptionViewModel;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class PackageList extends AppCompatActivity implements PackageAdapter.EditInterface {
    ActivityPackageListBinding binding;
    PackageAdapter adapter;
    Disposable dx;
    AlertDialog alertDialog;
    SubscriptionViewModel subscriptionViewModel;
    SharedPreferences sharedPref;
    long adminId, userId;
    private boolean admin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPackageListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sharedPref = getSharedPreferences("MVGA", Context.MODE_PRIVATE);

        adminId = sharedPref.getLong("admin", -1);
        userId = sharedPref.getLong("userId", -1);
        if (adminId == userId) {
            admin = true;
        }

        subscriptionViewModel = new ViewModelProvider(this).get(SubscriptionViewModel.class);

        binding.recycler.setLayoutManager(new LinearLayoutManager(this));
        GetPkgList();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void GetPkgList() {
        dx = subscriptionViewModel.getPkgList().observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(modelList -> {
                    adapter = new PackageAdapter(this, admin, modelList, this);
                    binding.recycler.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                });
    }

    @Override
    public void EditPackageInfo(PackageInfo model) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        DialogPackageEditBinding binding = DialogPackageEditBinding.inflate(LayoutInflater.from(this), null, false);

        dialogBuilder.setView(binding.getRoot());

        binding.tvName.setText(model.getSubName());
        binding.tvCharge.setText(model.getCharge());
        binding.tvDiscount.setText(model.getDiscount());

        if (model.getPkgId() != 4) {
            binding.tvDiscount.setInputType(InputType.TYPE_NULL);
        }

        binding.closeBtn.setOnClickListener(v -> alertDialog.dismiss());

        binding.doneBtn.setOnClickListener(v -> {
            String ch = binding.tvCharge.getText().toString();
            if (ch.isEmpty()) {
                binding.tvCharge.setError("empty");
                Toast.makeText(this, "Charge Empty", Toast.LENGTH_SHORT).show();
                return;
            } else {
                binding.tvCharge.setError(null);
            }
            String ds = binding.tvDiscount.getText().toString();
            if (ds.isEmpty()) {
                binding.tvDiscount.setError("empty");
                Toast.makeText(this, "Discount Empty", Toast.LENGTH_SHORT).show();
                return;
            } else {
                binding.tvDiscount.setError(null);
            }
            UpdatePkgInfo(model.getPkgId(), ch, ds);

        });


        alertDialog = dialogBuilder.create();
        alertDialog.show();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }

    private void UpdatePkgInfo(long id, String charge, String discount) {
        Completable.fromAction(() -> subscriptionViewModel.updatePkgDiscount(id, charge, discount)).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onComplete() {
                Toast.makeText(PackageList.this, "Update Successful", Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }
        });
    }
}