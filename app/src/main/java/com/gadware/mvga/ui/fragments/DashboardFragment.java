package com.gadware.mvga.ui.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.gadware.mvga.adapters.BookingAdapter;
import com.gadware.mvga.databinding.FragmentDashboardBinding;
import com.gadware.mvga.models.SubscriptionInfo;
import com.gadware.mvga.models.UserInfo;
import com.gadware.mvga.ui.activities.AddNewReview;
import com.gadware.mvga.ui.activities.MainActivity;
import com.gadware.mvga.vm.BookingViewModel;
import com.gadware.mvga.vm.SubscriptionViewModel;
import com.gadware.mvga.vm.UserViewModel;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    BookingViewModel bookingViewModel;
    UserViewModel userViewModel;
    SubscriptionViewModel subscriptionViewModel;
    BookingAdapter adapter;
    AlertDialog alert11;
    Disposable dx;
    String subtype, balance;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentDashboardBinding.inflate(inflater, container, false);

        bookingViewModel = new ViewModelProvider(this).get(BookingViewModel.class);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        subscriptionViewModel = new ViewModelProvider(this).get(SubscriptionViewModel.class);
        binding.recycler.setLayoutManager(new LinearLayoutManager(requireContext()));

        //if no subscription, dialog show

        GetUserInfo();

        return binding.getRoot();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void GetMyServices() {
        dx = bookingViewModel.getModelList(MainActivity.userId).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(reviewInfoModels -> {
                    adapter = new BookingAdapter(requireContext(), reviewInfoModels, this::RemoveBookingDialog);
                    binding.recycler.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                });
    }

    private void RemoveBookingDialog(long bookingId, long trainerId) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(requireContext());
        builder1.setTitle("Leave service");
        builder1.setMessage("Do you want to leave this service");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                (dialog, id) -> {
                    RemoveBooking(bookingId, trainerId);
                    dialog.cancel();
                });

        builder1.setNegativeButton(
                "No",
                (dialog, id) -> dialog.cancel());

        alert11 = builder1.create();
        alert11.show();
        //review dialog after deletion


    }

    private void GetSubsInfo() {
        subscriptionViewModel.getUserInfo(MainActivity.userId).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new SingleObserver<SubscriptionInfo>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onSuccess(@NonNull SubscriptionInfo model) {
                subtype = model.getSubName();
                binding.subsTv.setText("Subscription: "+subtype);

                GetMyServices();
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }
        });
    }

    private void GetUserInfo() {
        userViewModel.getUserInfo(MainActivity.userId).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new SingleObserver<UserInfo>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onSuccess(@NonNull UserInfo model) {
                balance = model.getBalance();
                balance = model.getBalance();
                binding.blncTv.setText("My Balance: "+balance+" $");
                binding.validTv.setText("Membership till: "+model.getValidity());
                GetSubsInfo();
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }
        });
    }

    private void ShowReviewDialog(long trainerId) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(requireContext());
        builder1.setTitle("Rating service");
        builder1.setMessage("Do you want to Rate this service");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                (dialog, id) -> {
                    Intent intent = new Intent(requireContext(), AddNewReview.class);
                    intent.putExtra("t_id", trainerId);
                    startActivity(intent);
                    dialog.cancel();
                });

        builder1.setNegativeButton(
                "No",
                (dialog, id) -> dialog.cancel());

        alert11 = builder1.create();
        alert11.show();
        //review dialog after deletion


    }

    private void RemoveBooking(long bookingId, long trainerId) {
        Completable.fromAction(() -> bookingViewModel.deleteUser(bookingId)).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onComplete() {
                Toast.makeText(requireContext(), "Booking Successful", Toast.LENGTH_SHORT).show();
                ShowReviewDialog(trainerId);
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}