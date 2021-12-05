package com.gadware.mvga.ui.activities;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.gadware.mvga.databinding.ActivityPaymentBinding;
import com.gadware.mvga.vm.UserViewModel;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;

import java.util.Calendar;
import java.util.Locale;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class Payment extends AppCompatActivity {
    ActivityPaymentBinding binding;
    ArrayAdapter<String> adapter;

    public Calendar myCalendar = Calendar.getInstance();
    public Calendar now = Calendar.getInstance();
    public int yr = now.get(Calendar.YEAR);
    public int mnth = now.get(Calendar.MONTH);
    public int day = now.get(Calendar.DAY_OF_MONTH);

    long userId;
    long am;

    SharedPreferences sharedPref;
    UserViewModel userViewModel;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPaymentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        sharedPref = getSharedPreferences("MVGA", Context.MODE_PRIVATE);
        userId = sharedPref.getLong("userId", -1);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new String[]{"Paypal", "Debit Card", "Credit Card"});
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        binding.tvCardType.setAdapter(adapter);
        binding.tvCardType.setText(binding.tvCardType.getAdapter().getItem(0).toString());
        adapter.getFilter().filter(null);


        binding.etDate.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                ShowDatePicker(binding.etDate, new SimpleDateFormat("dd-MMM-yyyy", Locale.US));
            }
            return false;
        });


        binding.doneBtn.setOnClickListener(v -> {
            if (Validate() == 1) {

                UpdateBalance();
            }
        });

    }

    private void UpdateBalance() {
        Completable.fromAction(() -> userViewModel.updateBalance(userId, am)).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onComplete() {
                Toast.makeText(Payment.this, "Payment Successful", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Payment.this, MainActivity.class));
                finish();
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }
        });
    }

    private int Validate() {
        String type;


        type = binding.tvCardNumber.getText().toString();
        if (type.isEmpty()) {
            binding.tvCardNumber.setError("invalid");
            return 0;
        } else {
            binding.tvCardNumber.setError(null);
        }
        type = binding.tvName.getText().toString();
        if (type.isEmpty()) {
            binding.tvName.setError("invalid");
            return 0;
        } else {
            binding.tvName.setError(null);
        }
        type = binding.tvAmount.getText().toString();
        if (type.isEmpty()) {
            binding.tvAmount.setError("invalid");
            return 0;
        } else {
            try {
                am = Long.parseLong(type);
            } catch (Exception e) {
                Toast.makeText(this, "error...\n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                binding.tvAmount.setError(null);
            }
        }
        type = binding.etCvv.getText().toString();
        if (type.isEmpty()) {
            binding.etCvv.setError("invalid");
            return 0;
        } else {
            binding.etCvv.setError(null);
        }
        type = binding.etDate.getText().toString();
        if (type.isEmpty()) {
            binding.etDate.setError("invalid");
            return 0;
        } else {
            binding.etDate.setError(null);
        }

        return 1;
    }

    private void ShowDatePicker(MaterialAutoCompleteTextView textView, SimpleDateFormat sdf) {

        DatePickerDialog nDate = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            textView.setText(sdf.format(myCalendar.getTimeInMillis()));

        }, yr, mnth, day);
        nDate.show();

    }
}