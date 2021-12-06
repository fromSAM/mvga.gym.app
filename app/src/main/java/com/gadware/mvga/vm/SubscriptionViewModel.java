package com.gadware.mvga.vm;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.gadware.mvga.models.PackageInfo;
import com.gadware.mvga.models.SubscriptionInfo;
import com.gadware.mvga.models.SubscriptionInfoModel;
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

    public void insertPkgList(List<PackageInfo> modelList) {
        appDatabase.subscriptionDao().insertPkgList(modelList);
    }

    public void insertUser(SubscriptionInfo model) {
        appDatabase.subscriptionDao().insertUser(model);
    }

    public Single<SubscriptionInfoModel> getUserInfo(long id) {
        return appDatabase.subscriptionDao().getUserInfo(id);
    }

    public Flowable<List<PackageInfo>> getPkgList() {
        return appDatabase.subscriptionDao().getPkgList();
    }

    public Single<Long> getSubType(long id) {
        return appDatabase.subscriptionDao().getSubType(id);
    }

    public void updateSubType(long id, long pkgId) {
        appDatabase.subscriptionDao().updateSubType(id, pkgId);
    }



    public void updatePkgDiscount(long id,String charge,String discount) {
        appDatabase.subscriptionDao().updatePkgDiscount(id,charge, discount);
    }

    public void deleteUser(long id) {
        appDatabase.subscriptionDao().deleteUser(id);
    }


}
