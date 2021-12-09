package com.gadware.mvga.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.gadware.mvga.models.TrainerInfo;
import com.gadware.mvga.models.UserInfo;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;

@Dao
public interface UserDao {



    @Insert()
    Single<Long> insertSingleUser(UserInfo model);

    @Insert()
    void insertUserList(List<UserInfo> modelList);

    @Query("Select * from UserInfo where userId=:id")
    Single<UserInfo> getUserInfo(long id);

    @Query("Update UserInfo set balance=balance+:balance where userId=:id")
    void updateBalance(long id,long balance);

    @Query("Update UserInfo set referCount=referCount+1 where userId=:id")
    void updateCounter(long id);

    @Query("Select userId from UserInfo where email=:email and pass=:pass")
    Single<Long> getUserid(String email,String pass);

    @Query("Select distinct email from UserInfo")
    Single<List<String>> getEmails();

    @Query("Select userId from UserInfo where email=:refId")
    Single<Long> getUserid(String refId);
    @Query("Select pass from UserInfo where email=:refId")
    Single<String> getUserPass(String refId);

    @Query("Select balance from UserInfo where userId=:userId")
    Single<String> getBalance(long userId);

    @Query("Delete  from UserInfo where userId=:id")
    void deleteUser(long id);


}
