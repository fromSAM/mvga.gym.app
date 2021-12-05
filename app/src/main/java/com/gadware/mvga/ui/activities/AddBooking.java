package com.gadware.mvga.ui.activities;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
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

import com.gadware.mvga.databinding.ActivityAddBookingBinding;
import com.gadware.mvga.models.BookingInfo;
import com.gadware.mvga.vm.BookingViewModel;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;

import java.util.Calendar;
import java.util.Locale;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class AddBooking extends AppCompatActivity {
    ActivityAddBookingBinding binding;

    private long trainerId, userId, serviceId;


    private String sTime, duration, serv, desc, trainer;
    ArrayAdapter<String> adapter;

    final Calendar myCalendar = Calendar.getInstance();

    Calendar now = Calendar.getInstance();

    int hour = now.get(Calendar.HOUR_OF_DAY);
    int minte = now.get(Calendar.MINUTE);

    BookingViewModel bookingViewModel;


    SharedPreferences sharedPref;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddBookingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        bookingViewModel = new ViewModelProvider(this).get(BookingViewModel.class);


        sharedPref = getSharedPreferences("MVGA", Context.MODE_PRIVATE);
        userId = sharedPref.getLong("userId", -1);

        trainerId = getIntent().getLongExtra("t_id", -1);
        serviceId = getIntent().getLongExtra("s_id", -1);
        serv = getIntent().getStringExtra("serv");
        desc = getIntent().getStringExtra("desc");
        trainer = getIntent().getStringExtra("trainer");

        binding.tvDesc.setText(desc);
        binding.tvTrainer.setText(trainer);
        binding.tvService.setText(serv);


        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new String[]{"1 hour", "2 hour"});
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        binding.etDuration.setAdapter(adapter);
        binding.etDuration.setText(binding.etDuration.getAdapter().getItem(0).toString());
        adapter.getFilter().filter(null);


        binding.etStartTime.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                ShowTimePicker(binding.etStartTime, new SimpleDateFormat("hh:mm a", Locale.US));
            }
            return false;
        });


        binding.doneBtn.setOnClickListener(v -> {
            if (Validate() == 1) {
                AddNewBooking();
            }
        });


    }

    private int Validate() {
        sTime = binding.etStartTime.getText().toString();
        if (sTime.isEmpty()) {
            binding.etStartTime.setError("invalid");
            return 0;
        } else {
            binding.etStartTime.setError(null);
        }

        duration = binding.etDuration.getText().toString();
        return 0;
    }

    private void AddNewBooking() {
        Completable.fromAction(() -> bookingViewModel.insertSingleUser(new BookingInfo(trainerId, userId, serviceId, sTime, duration))).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onComplete() {
                Toast.makeText(AddBooking.this, "Booking Successful", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(AddBooking.this, MainActivity.class));
                finish();
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }
        });
    }

    private void ShowTimePicker(MaterialAutoCompleteTextView etStartTime, SimpleDateFormat stf) {
        TimePickerDialog nTime = new TimePickerDialog(this, (view, hourOfDay, minute) -> {
            myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            myCalendar.set(Calendar.MINUTE, minute);


            etStartTime.setText(stf.format(myCalendar.getTime()));
        }, hour, minte, false);
        nTime.show();
    }
}