package com.gadware.mvga.ui.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialog;
import androidx.lifecycle.ViewModelProvider;

import com.gadware.mvga.databinding.ActivityPaymentBinding;
import com.gadware.mvga.databinding.DialogReferenceBinding;
import com.gadware.mvga.models.PackageInfo;
import com.gadware.mvga.utils.MonthType;
import com.gadware.mvga.utils.RackMonthPicker;
import com.gadware.mvga.vm.SubscriptionViewModel;
import com.gadware.mvga.vm.UserViewModel;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class Payment extends AppCompatActivity {
    private static final String TAG = "BtnChk";
    ActivityPaymentBinding binding;
    ArrayAdapter<String> adapter;
    ArrayAdapter<String> adapter2;

    Disposable dx;
    private AlertDialog alertDialog;
    public Calendar myCalendar = Calendar.getInstance();
    public Calendar now = Calendar.getInstance();
    public int yr = now.get(Calendar.YEAR);
    public int mnth = now.get(Calendar.MONTH);
    public int day = now.get(Calendar.DAY_OF_MONTH);

    private long ReferId = -1;


    Map<String, String> chargeMap = new HashMap<>();
    Map<String, String> discMap = new HashMap<>();
    Map<String, PackageInfo> pkgMap = new HashMap<>();

    long userId, myBalance;
    long am, remB, ch, refDiscount;

    boolean discountFlag = false, RefDiscount = false, paypayFlag = false;

    SharedPreferences sharedPref;
    UserViewModel userViewModel;
    SubscriptionViewModel subscriptionViewModel;

    final PackageInfo[] subInfo = new PackageInfo[1];

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPaymentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        subscriptionViewModel = new ViewModelProvider(this).get(SubscriptionViewModel.class);

        sharedPref = getSharedPreferences("MVGA", Context.MODE_PRIVATE);
        userId = sharedPref.getLong("userId", -1);


        binding.etCvv.setFilters(new InputFilter[]{new InputFilter.LengthFilter(3)});
        binding.tvCardNumber.setFilters(new InputFilter[]{new InputFilter.LengthFilter(19)});


        GetPkgList();

        binding.tvSubType.setOnItemClickListener((adapterView, view, pos, id) -> {
            subInfo[0] = pkgMap.get(adapterView.getItemAtPosition(pos));

            assert subInfo[0] != null;
            binding.tvCharge.setText(subInfo[0].getCharge());
            binding.tvDiscount.setText(subInfo[0].getDiscount());
            if (subInfo[0].getPkgId() != 4) {
                binding.refBtn.setVisibility(View.GONE);
                discountFlag = false;
            } else {
                binding.refBtn.setVisibility(View.VISIBLE);
                discountFlag = true;
            }
        });

        binding.tvCardType.setOnItemClickListener((adapterView, view, pos, id) -> {
            String ct = (String) adapterView.getItemAtPosition(pos);

            if (ct.equals("Paypal")) {
                binding.cardPayLay.setVisibility(View.GONE);
                binding.paypalLay.setVisibility(View.VISIBLE);
                paypayFlag = true;
            } else {
                binding.cardPayLay.setVisibility(View.VISIBLE);
                binding.paypalLay.setVisibility(View.GONE);
                paypayFlag = false;
            }
        });


        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new String[]{  "Credit Card","Debit Card","Paypal"});
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        binding.tvCardType.setAdapter(adapter);
        binding.tvCardType.setText(binding.tvCardType.getAdapter().getItem(0).toString());
        adapter.getFilter().filter(null);


        binding.tvCardNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()==4||s.length()==9||s.length()==14){
                    //binding.tvCardNumber.setText(s.toString()+"-");
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        binding.etDate.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                ShowDatePicker();
            }
            return false;
        });

        binding.refBtn.setOnClickListener(v -> {
            ShowReferenceDialog();
        });

        binding.nextBtn.setOnClickListener(v -> {
            if (Validate2() == 1) {
                binding.billLay.setVisibility(View.GONE);
                binding.paymentLay.setVisibility(View.VISIBLE);
                Objects.requireNonNull(getSupportActionBar()).setTitle("Payment");
                if (discountFlag) {
                    long d = Long.parseLong(subInfo[0].getDiscount());
                    if (RefDiscount) {
                        d += Long.parseLong(binding.tvRefDiscount.getText().toString());
                    }
                    binding.tvBill.setText(String.valueOf(Long.parseLong(subInfo[0].getCharge()) - d));
                } else {
                    binding.tvBill.setText(String.valueOf(Long.parseLong(subInfo[0].getCharge())));
                }
            }
        });

        binding.doneBtn.setOnClickListener(v -> {
            if (Validate() == 1) {
                UpdateSubType();
            }
        });

    }

    private void GetPkgList() {
        dx = subscriptionViewModel.getPkgList().observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(modelList -> {

                    for (PackageInfo pk : modelList) {
                        pkgMap.put(pk.getSubName(), pk);
                    }

                    adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new String[]{"Yearly", "Monthly", "Pay Per View"});
                    adapter2.setDropDownViewResource(android.R.layout.simple_list_item_1);
                    binding.tvSubType.setAdapter(adapter2);
                    binding.tvSubType.setText(binding.tvSubType.getAdapter().getItem(0).toString());
                    adapter2.getFilter().filter(null);


                    subInfo[0] = pkgMap.get("Yearly");

                    assert subInfo[0] != null;
                    binding.tvCharge.setText(subInfo[0].getCharge());
                    binding.tvDiscount.setText(subInfo[0].getDiscount());

                    adapter2.getFilter().filter(null);

                    GetBalance();
                });
    }

    private void UpdateSubType() {
        Completable.fromAction(() -> subscriptionViewModel.updateSubType(userId, subInfo[0].getPkgId())).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onComplete() {
                Log.d(TAG, "sub type updated");
                UpdateBalance();
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Toast.makeText(Payment.this, "Error.!\n" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void UpdateBalance() {
        Completable.fromAction(() -> userViewModel.updateBalance(userId, remB)).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onComplete() {
                if (RefDiscount) {
                    ReturnBalance(ReferId, refDiscount);
                } else {
                    Toast.makeText(Payment.this, "Payment Successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Payment.this, MainActivity.class));
                    finish();
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Toast.makeText(Payment.this, "Error.!\n" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void UpdateCounter() {
        Completable.fromAction(() -> userViewModel.updateCounter(userId)).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
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
                Toast.makeText(Payment.this, "Error.!\n" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void ReturnBalance(long id, long b) {
        Completable.fromAction(() -> userViewModel.updateBalance(id, b)).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onComplete() {
                UpdateCounter();
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }
        });
    }

    private int Validate() {
        String type;

        type = binding.tvPayAmount.getText().toString();
        if (type.isEmpty()) {
            binding.tvPayAmount.setError("invalid");
            return 0;
        } else {
            try {
                am = Long.parseLong(type);
                remB = am - ch;
                am += myBalance;
            } catch (Exception e) {
                Log.d(TAG, "conversion failed");
                Toast.makeText(this, "Error...\n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                binding.tvPayAmount.setError("invalid");
                return 0;
            }
        }




        binding.tvPayAmount.setError(null);

        type = binding.tvBill.getText().toString();
        ch = Long.parseLong(type);

        long r = am - ch;

        Log.d(TAG, remB + "--" + ch + "--" + am);
        if (r < 0) {
            binding.tvPayAmount.setError("less than charge");
            return 0;
        } else {
            binding.tvPayAmount.setError(null);
        }


        if (paypayFlag) {
            type = binding.tvPaypal.getText().toString();
            if (type.isEmpty()|| !Patterns.EMAIL_ADDRESS.matcher(type).matches()) {
                binding.tvPaypal.setError("invalid");
                return 0;
            } else {
                binding.tvPaypal.setError(null);
                return 1;
            }
        }



        type = binding.tvCardNumber.getText().toString();
        if (type.isEmpty()) {
            binding.tvCardNumber.setError("invalid");
            Log.d(TAG, "card err");
            return 0;
        } else {
            binding.tvCardNumber.setError(null);
        }
        type = binding.tvName.getText().toString();
        if (type.isEmpty()) {
            binding.tvName.setError("invalid");
            Log.d(TAG, "name err");
            return 0;
        } else {
            binding.tvName.setError(null);
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

    private int Validate2() {
        String type;

//
//        type = binding.tvCardNumber.getText().toString();
//        if (type.isEmpty()) {
//            binding.tvCardNumber.setError("invalid");
//            return 0;
//        } else {
//            binding.tvCardNumber.setError(null);
//        }


        return 1;
    }

    private void ShowDatePicker() {

        new RackMonthPicker(this)
                .setMonthType(MonthType.TEXT)
                .setLocale(Locale.ENGLISH)
                .setPositiveButton((month, startDate, endDate, year, monthLabel) -> {
                    System.out.println(month);
                    System.out.println(startDate);
                    System.out.println(endDate);
                    System.out.println(year);
                    System.out.println(monthLabel);

                    binding.etDate.setText(month + "/" + year);

                })
                .setNegativeButton(AppCompatDialog::dismiss).show();

    }

    private void ShowReferenceDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        DialogReferenceBinding binding = DialogReferenceBinding.inflate(LayoutInflater.from(this), null, false);

        dialogBuilder.setView(binding.getRoot());


        binding.doneBtn.setOnClickListener(v -> {
            String refid = binding.tvRefId.getText().toString();
            if (refid.isEmpty()) {
                binding.tvRefId.setError("empty");
                return;
            } else {
                binding.tvRefId.setError(null);
            }
            try {
                CheckRefId(refid);
            } catch (Exception e) {

            }

        });


        alertDialog = dialogBuilder.create();
        alertDialog.show();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

    }

    private void CheckRefId(String refid) {

        userViewModel.getUserid(refid).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).
                subscribe(new SingleObserver<Long>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull Long aLong) {
                        ReferId = aLong;
                        GetSubType();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });
    }

    private void GetBalance() {

        userViewModel.getBalance(userId).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).
                subscribe(new SingleObserver<String>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull String aLong) {
                        myBalance = Long.parseLong(aLong);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });
    }

    private void GetSubType() {

        subscriptionViewModel.getSubType(ReferId).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).
                subscribe(new SingleObserver<Long>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull Long v) {
                        if (v==4) {

                            //update amount for ref
                            //reduce bill for new
                            long fc = Long.parseLong(subInfo[0].getCharge());
                            long mf = fc / 12;
                            mf *= 3;
                            long dv = (mf * 20) / 100;
                            binding.tvRefDiscount.setText(String.valueOf(dv));
                            binding.tvRefDiscount.setVisibility(View.VISIBLE);
                            RefDiscount = true;
                            Toast.makeText(Payment.this, "Congratulations, You got Discount", Toast.LENGTH_SHORT).show();
                        } else {
                            RefDiscount = false;
                            Toast.makeText(Payment.this, "Discount not available", Toast.LENGTH_SHORT).show();
                        }
                        alertDialog.dismiss();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });
    }
}