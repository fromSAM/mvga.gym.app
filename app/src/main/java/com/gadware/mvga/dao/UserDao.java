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
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(TrainerInfo model);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Single<Long> insertSingleUser(UserInfo model);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUserList(List<UserInfo> modelList);

    @Query("Select * from UserInfo where userId=:id")
    Single<UserInfo> getUserInfo(long id);

    @Query("Update UserInfo set balance=balance+:balance where userId=:id")
    void updateBalance(long id,long balance);

    @Query("Select userId from UserInfo where email=:email and pass=:pass")
    Single<Long> getUserid(String email,String pass);




    @Query("Delete  from UserInfo where userId=:id")
    void deleteUser(long id);


}
