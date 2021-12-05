package com.gadware.mvga.vm;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.gadware.mvga.models.UserInfo;
import com.gadware.mvga.storage.AppDatabase;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;


public class UserViewModel extends AndroidViewModel {
    AppDatabase appDatabase;

    public UserViewModel(@NonNull Application application) {
        super(application);
        appDatabase = AppDatabase.getDatabase(application);
    }

    public Single<Long> insertSingleUser(UserInfo model) {
        return appDatabase.userDao().insertSingleUser(model);
    }

    public void insertUserList(List<UserInfo> modelList) {
        appDatabase.userDao().insertUserList(modelList);
    }

    public Single<UserInfo> getUserInfo(long id) {
        return appDatabase.userDao().getUserInfo(id);
    }

    public void updateBalance(long id,long b) {
         appDatabase.userDao().updateBalance(id,b);
    }

    public Single<Long> getUserid(String email,String pass) {
        return appDatabase.userDao().getUserid(email,pass);
    }



    public void deleteUser(long id) {
        appDatabase.userDao().deleteUser(id);
    }


}
