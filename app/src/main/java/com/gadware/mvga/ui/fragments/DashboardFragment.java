package com.gadware.mvga.ui.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.gadware.mvga.models.SubscriptionInfoModel;
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

    private static final String TAG = "DBTest";
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



        return binding.getRoot();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void GetMyServices() {
        dx = bookingViewModel.getModelList(MainActivity.userId).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(reviewInfoModels -> {
                    if (reviewInfoModels.size() > 0) {
                        adapter = new BookingAdapter(requireContext(), reviewInfoModels, (bookingId, trainerId) -> RemoveBookingDialog( bookingId,  trainerId));
                        binding.recycler.setVisibility(View.VISIBLE);
                        binding.noServTv.setVisibility(View.GONE);
                        binding.recycler.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    } else {
                        binding.noServTv.setVisibility(View.VISIBLE);
                        binding.recycler.setVisibility(View.GONE);
                    }
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
                });

        builder1.setNegativeButton(
                "No",
                (dialog, id) -> alert11.dismiss());

        alert11 = builder1.create();
        alert11.show();

    }

    private void GetSubsInfo() {
        subscriptionViewModel.getUserInfo(MainActivity.userId).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new SingleObserver<SubscriptionInfoModel>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onSuccess(@NonNull SubscriptionInfoModel model) {
                subtype = model.getSubName();
                binding.subsTv.setText("Subscription: "+subtype);
                binding.validTv.setText("Membership till: "+model.getValidity());
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
                binding.blncTv.setText("My Balance: "+balance+" $");
                binding.refIdTv.setText("My Ref. Id: "+model.getEmail());
                binding.refCountTv.setText("Ref. Counter: "+model.getReferCount());
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
                    alert11.dismiss();
                    Intent intent = new Intent(requireContext(), AddNewReview.class);
                    intent.putExtra("t_id", trainerId);
                    requireContext().startActivity(intent);

                });

        builder1.setNegativeButton(
                "No",
                (dialog, id) -> alert11.cancel());

        alert11 = builder1.create();
        alert11.show();

    }

    private void RemoveBooking(long bookingId, long trainerId) {
        Completable.fromAction(() -> bookingViewModel.deleteUser(bookingId)).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onComplete() {
                alert11.dismiss();
                Toast.makeText(requireContext(), "Booking Successful", Toast.LENGTH_SHORT).show();
                ShowReviewDialog(trainerId);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                alert11.dismiss();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "user id: "+MainActivity.userId);
        GetUserInfo();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}