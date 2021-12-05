package com.gadware.mvga.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.gadware.mvga.models.ReviewInfo;
import com.gadware.mvga.models.ReviewInfoModel;
import com.gadware.mvga.models.ServiceInfo;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;

@Dao
public interface ServiceDao {



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUserList(List<ServiceInfo> modelList);

    @Query("Select * from ServiceInfo where trainerId=:id")
    Flowable<List<ServiceInfo>> getUserInfo(long id);

    @Query("Delete  from ServiceInfo where serviceId=:id")
    void deleteUser(long id);
}
