package com.gadware.mvga.vm;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.gadware.mvga.models.ReviewInfo;
import com.gadware.mvga.models.ReviewInfoModel;
import com.gadware.mvga.models.TrainerInfo;
import com.gadware.mvga.storage.AppDatabase;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;


public class ReviewViewModel extends AndroidViewModel {
    AppDatabase appDatabase;

    public ReviewViewModel(@NonNull Application application) {
        super(application);
        appDatabase = AppDatabase.getDatabase(application);
    }

    public Single<Long> insertSingleUser(ReviewInfo model) {
       return appDatabase.reviewDao().insertSingleUser(model);
    }

    public void insertUserList(List<ReviewInfo> modelList) {
         appDatabase.reviewDao().insertUserList(modelList);
    }



    public Flowable<List<ReviewInfoModel>> getReviewInfoModelList(long id){
        return appDatabase.reviewDao().getReviewInfoModelList(id);
    }



}
