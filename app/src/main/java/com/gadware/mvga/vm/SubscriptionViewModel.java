package com.gadware.mvga.vm;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.gadware.mvga.models.ServiceInfo;
import com.gadware.mvga.models.SubscriptionInfo;
import com.gadware.mvga.storage.AppDatabase;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;


public class SubscriptionViewModel extends AndroidViewModel {
    AppDatabase appDatabase;

    public SubscriptionViewModel(@NonNull Application application) {
        super(application);
        appDatabase = AppDatabase.getDatabase(application);
    }

    public void insertUser(SubscriptionInfo model) {
         appDatabase.subscriptionDao().insertUser(model);
    }

    public Single<SubscriptionInfo> getUserInfo(long id){
        return appDatabase.subscriptionDao().getUserInfo(id);
    }

    public void deleteUser(long id){
         appDatabase.subscriptionDao().deleteUser(id);
    }


}
