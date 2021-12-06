package com.gadware.mvga.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.gadware.mvga.models.PackageInfo;
import com.gadware.mvga.models.ServiceInfo;
import com.gadware.mvga.models.SubscriptionInfo;
import com.gadware.mvga.models.SubscriptionInfoModel;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;

@Dao
public interface SubscriptionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPkgList(List<PackageInfo> modelList);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(SubscriptionInfo model);

    @Query("Select subId,pkgId,userId,subName,bill,referDiscount,validity from SubscriptionInfo join PackageInfo on PackageInfo.pkgId=SubscriptionInfo.pkgid where userId=:id")
    Single<SubscriptionInfoModel> getUserInfo(long id);

    @Query("Select * from PackageInfo where pkgId!=1")
    Flowable<List<PackageInfo>> getPkgList();

    @Query("Select pkgId from SubscriptionInfo where userId=:id")
    Single<Long> getSubType(long id);

    @Query("Update SubscriptionInfo set pkgId=:pkgId  where userId=:id")
    void updateSubType(long id,long pkgId);



    @Query("Update PackageInfo set discount=:discount,charge=:charge  where pkgId=:id")
    void updatePkgDiscount(long id,String charge,String discount);

    @Query("Delete  from SubscriptionInfo where subId=:id")
    void deleteUser(long id);
}
