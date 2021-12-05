package com.gadware.mvga.vm;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;


import com.gadware.mvga.models.TrainerInfo;
import com.gadware.mvga.storage.AppDatabase;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;


public class TrainerViewModel extends AndroidViewModel {
    AppDatabase appDatabase;

    public TrainerViewModel(@NonNull Application application) {
        super(application);
        appDatabase = AppDatabase.getDatabase(application);
    }

    public Single<Long> insertSingleUser(TrainerInfo model) {
       return appDatabase.trainerDao().insertSingleUser(model);
    }

    public void insertUserList(List<TrainerInfo> modelList) {
         appDatabase.trainerDao().insertUserList(modelList);
    }

    public Single<TrainerInfo> getUserInfo(long id){
        return appDatabase.trainerDao().getUserInfo(id);
    }

    public Flowable<List<TrainerInfo>> getAllUser(){
        return appDatabase.trainerDao().getAllUser();
    }

    public void deleteUser(long id){
         appDatabase.trainerDao().deleteUser(id);
    }


}
