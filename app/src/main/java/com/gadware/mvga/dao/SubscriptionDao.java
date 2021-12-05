package com.gadware.mvga.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.gadware.mvga.models.ServiceInfo;
import com.gadware.mvga.models.SubscriptionInfo;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;

@Dao
public interface SubscriptionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Single<Long> insertUser(SubscriptionInfo model);

    @Query("Select * from SubscriptionInfo where userId=:id")
    Single<SubscriptionInfo> getUserInfo(long id);

    @Query("Delete  from SubscriptionInfo where subId=:id")
    void deleteUser(long id);
}
