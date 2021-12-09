package com.gadware.mvga.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.gadware.mvga.databinding.FragmentProfileBinding;
import com.gadware.mvga.models.UserInfo;
import com.gadware.mvga.ui.activities.MainActivity;
import com.gadware.mvga.vm.UserViewModel;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ProfileFragment extends Fragment {


    private FragmentProfileBinding binding;
    UserViewModel userViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        GetUserInfo();

        return binding.getRoot();
    }

    private void GetUserInfo() {
        userViewModel.getUserInfo(MainActivity.userId).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new SingleObserver<UserInfo>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onSuccess(@NonNull UserInfo model) {
                binding.nameTv.setText(model.getUserName());
                binding.emailTv.setText(model.getEmail());
                binding.addressTv.setText("Address: "+model.getAddress());
                binding.etHeight.setText("Height: "+model.getHeight());
                binding.etWeight.setText("Weight: "+model.getWeight());
                binding.etAge.setText("Age: "+model.getAge());
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //binding = null;
    }
}