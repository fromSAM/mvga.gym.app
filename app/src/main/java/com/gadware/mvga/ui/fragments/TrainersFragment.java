package com.gadware.mvga.ui.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.gadware.mvga.adapters.TrainersAdapter;
import com.gadware.mvga.databinding.FragmentTrainersBinding;
import com.gadware.mvga.vm.TrainerViewModel;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class TrainersFragment extends Fragment {


    private FragmentTrainersBinding binding;
    TrainerViewModel trainerViewModel;
    TrainersAdapter adapter;
    Disposable dx;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentTrainersBinding.inflate(inflater, container, false);
        trainerViewModel = new ViewModelProvider(this).get(TrainerViewModel.class);
        binding.recycler.setLayoutManager(new LinearLayoutManager(requireContext()));

        GetTrainersList();

        return binding.getRoot();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void GetTrainersList() {
        dx = trainerViewModel.getAllUser().observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(trainerInfos -> {
                    adapter = new TrainersAdapter(requireContext(), trainerInfos);
                    binding.recycler.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        dx.dispose();
        binding = null;
    }
}