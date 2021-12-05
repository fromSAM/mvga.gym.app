package com.gadware.mvga.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;


import com.gadware.mvga.models.TrainerInfo;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;

@Dao
public interface TrainerDao {



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Single<Long> insertSingleUser(TrainerInfo model);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUserList(List<TrainerInfo> modelList);

    @Query("Select * from TrainerInfo where trainerId=:id")
    Single<TrainerInfo> getUserInfo(long id);

    @Query("Select * from TrainerInfo")
    Flowable<List<TrainerInfo>> getAllUser();

    @Query("Delete  from TrainerInfo where trainerId=:id")
    void deleteUser(long id);


}
