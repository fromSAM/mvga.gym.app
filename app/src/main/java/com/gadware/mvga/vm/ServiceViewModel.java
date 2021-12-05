package com.gadware.mvga.vm;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.gadware.mvga.models.ReviewInfo;
import com.gadware.mvga.models.ReviewInfoModel;
import com.gadware.mvga.models.ServiceInfo;
import com.gadware.mvga.storage.AppDatabase;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;


public class ServiceViewModel extends AndroidViewModel {
    AppDatabase appDatabase;

    public ServiceViewModel(@NonNull Application application) {
        super(application);
        appDatabase = AppDatabase.getDatabase(application);
    }

    public void insertUserList(List<ServiceInfo> modelList) {
         appDatabase.serviceDao().insertUserList(modelList);
    }

    public Flowable<List<ServiceInfo>> getUserInfo(long id){
        return appDatabase.serviceDao().getUserInfo(id);
    }

    public void deleteUser(long id){
         appDatabase.serviceDao().deleteUser(id);
    }


}
