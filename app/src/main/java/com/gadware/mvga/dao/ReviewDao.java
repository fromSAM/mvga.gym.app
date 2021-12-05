package com.gadware.mvga.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.gadware.mvga.models.ReviewInfo;
import com.gadware.mvga.models.ReviewInfoModel;
import com.gadware.mvga.models.TrainerInfo;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;

@Dao
public interface ReviewDao {



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Single<Long> insertSingleUser(ReviewInfo model);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUserList(List<ReviewInfo> modelList);

    @Query("Select reviewId,userId,trainerId,rating,date,review,UserInfo.userName from ReviewInfo join UserInfo on UserInfo.userId=ReviewInfo.userId where trainerId=:id")
    Flowable<List<ReviewInfoModel>> getReviewInfoModelList(long id);


}
