package com.gadware.mvga.ui.activities;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.gadware.mvga.adapters.BookingAdapter;
import com.gadware.mvga.databinding.ActivityAddBookingBinding;
import com.gadware.mvga.models.BookingInfo;
import com.gadware.mvga.models.BookingInfoModel;
import com.gadware.mvga.vm.BookingViewModel;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class AddBooking extends AppCompatActivity {
    ActivityAddBookingBinding binding;

    List<BookingInfoModel> myTimes=new ArrayList<>();
    List<BookingInfoModel> trTimes=new ArrayList<>();
    private long trainerId, userId, serviceId;

    private long stLong = 0, dtLong = 0;

    private String date, sTime,
            duration, serv, desc, trainer;
    ArrayAdapter<String> adapter;

    final Calendar myCalendar = Calendar.getInstance();

    Calendar now = Calendar.getInstance();

    int hour = now.get(Calendar.HOUR_OF_DAY);
    int minte = now.get(Calendar.MINUTE);
    int yr = now.get(Calendar.YEAR);
    int mnth = now.get(Calendar.MONTH);
    int day = now.get(Calendar.DAY_OF_MONTH);
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

        GetTRServices();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new String[]{"1", "2"});
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        binding.etDuration.setAdapter(adapter);
        binding.etDuration.setText(binding.etDuration.getAdapter().getItem(0).toString());
        adapter.getFilter().filter(null);


        binding.etStartTime.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                ShowDatePicker(binding.tvDate, new SimpleDateFormat("dd-MMM, yyyy", Locale.US));
            }
            return false;
        });
        binding.tvDate.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                ShowDatePicker(binding.tvDate, new SimpleDateFormat("dd-MMM, yyyy", Locale.US));
            }
            return false;
        });


        binding.doneBtn.setOnClickListener(v -> {
            Log.d("TimeCheck", "Validate: " + stLong);
            if (Validate() == 1) {
                try {
                    AddNewBooking();
                } catch (Exception e) {
                    Toast.makeText(AddBooking.this, "Error.!\n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });


    }


    private int Validate() {


        date = binding.tvDate.getText().toString();
        if (date.isEmpty()) {
            binding.tvDate.setError("invalid");
            Toast.makeText(AddBooking.this, "date Empty", Toast.LENGTH_SHORT).show();

            return 0;
        } else {
            binding.tvDate.setError(null);
        }

        sTime = binding.etStartTime.getText().toString();
        if (sTime.isEmpty()) {
            binding.etStartTime.setError("invalid");
            Toast.makeText(AddBooking.this, "time Empty", Toast.LENGTH_SHORT).show();
            return 0;
        } else {
            binding.etStartTime.setError(null);
        }

        duration = binding.etDuration.getText().toString();

        return CalcConflicts();

    }

    private void AddNewBooking() {
        Completable.fromAction(() -> bookingViewModel.insertSingleUser(new BookingInfo(trainerId, userId, serviceId,  duration, stLong))).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
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
                Toast.makeText(AddBooking.this, "Error.!\n" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void ShowDatePicker(MaterialAutoCompleteTextView tvDate, SimpleDateFormat sdf) {
        DatePickerDialog nDate = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            myCalendar.set(Calendar.HOUR_OF_DAY, 0);
            myCalendar.set(Calendar.MINUTE, 0);
            myCalendar.set(Calendar.SECOND, 0);


            tvDate.setText(sdf.format(myCalendar.getTime()));
            dtLong = myCalendar.getTimeInMillis();
            ShowTimePicker(binding.etStartTime, new SimpleDateFormat("hh:mm a"), dtLong);
        }, yr, mnth, day);
        nDate.show();
    }

    private void ShowTimePicker(MaterialAutoCompleteTextView etStartTime, SimpleDateFormat stf, long ml) {
        TimePickerDialog nTime = new TimePickerDialog(this, (view, hourOfDay, minute) -> {

            Calendar c = Calendar.getInstance();
//Set time in milliseconds
            c.setTimeInMillis(ml);
            int mYear = c.get(Calendar.YEAR);
            int mMonth = c.get(Calendar.MONTH);
            int d = c.get(Calendar.DAY_OF_MONTH);

            myCalendar.set(Calendar.YEAR, mYear);
            myCalendar.set(Calendar.MONTH, mMonth);
            myCalendar.set(Calendar.DAY_OF_MONTH, d);
            myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            myCalendar.set(Calendar.MINUTE, minute);
            myCalendar.set(Calendar.SECOND, 0);

            //myCalendar.setTimeInMillis(ml);

            etStartTime.setText(stf.format(myCalendar.getTime()));
            stLong = myCalendar.getTimeInMillis();
        }, hour, minte, false);
        nTime.show();
    }


    private void GetTRServices() {
       bookingViewModel.getTrainerBookingListList(trainerId).observeOn(AndroidSchedulers.mainThread())
               .subscribeOn(Schedulers.io()).subscribe(new SingleObserver<List<BookingInfoModel>>() {
           @Override
           public void onSubscribe(@NonNull Disposable d) {

           }

           @Override
           public void onSuccess(@NonNull List<BookingInfoModel> bookingInfoModels) {
               trTimes.addAll(bookingInfoModels);
               GetMYServices();
           }

           @Override
           public void onError(@NonNull Throwable e) {

           }
       });
    }
    private void GetMYServices() {
        bookingViewModel.getMyBookingListList(userId).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new SingleObserver<List<BookingInfoModel>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onSuccess(@NonNull List<BookingInfoModel> bookingInfoModels) {
                myTimes.addAll(bookingInfoModels);

            }

            @Override
            public void onError(@NonNull Throwable e) {

            }
        });
    }
    private int CalcConflicts() {
        for (BookingInfoModel model:trTimes){
            long ot=model.getSTime()+(3600000*Integer.parseInt(model.getDuration()));
            long ct=stLong+(3600000*Integer.parseInt(binding.etDuration.getText().toString()));
            if ((stLong<=ot&&stLong>=model.getSTime())||(ct>=ot&&ct<=model.getSTime())){
                binding.tvDate.setError("change");
                binding.etStartTime.setError("change");
                Toast.makeText(AddBooking.this, "Trainer has a booking from "+new Date(model.getSTime())+" to "+new Date(ot), Toast.LENGTH_SHORT).show();
                return 0;
            }
        }
        for (BookingInfoModel model:myTimes){
            long ot=model.getSTime()+(3600000*Integer.parseInt(model.getDuration()));
            long ct=stLong+(3600000*Integer.parseInt(binding.etDuration.getText().toString()));
            if ((stLong<=ot&&stLong>=model.getSTime())||(ct>=ot&&ct<=model.getSTime())){
                Toast.makeText(AddBooking.this, "You have a booking from "+new Date(model.getSTime())+" to "+new Date(ot), Toast.LENGTH_SHORT).show();
                binding.tvDate.setError("change");
                binding.etStartTime.setError("change");
                return 0;
            }
        }
        return 1;
    }
}