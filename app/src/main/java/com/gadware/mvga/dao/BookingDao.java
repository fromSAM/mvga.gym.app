package com.gadware.mvga.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.gadware.mvga.models.BookingInfo;
import com.gadware.mvga.models.BookingInfoModel;
import com.gadware.mvga.models.ReviewInfo;
import com.gadware.mvga.models.ReviewInfoModel;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;

@Dao
public interface BookingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(BookingInfo model);

    @Query("Select BookingInfo.bookingId,BookingInfo.trainerId,BookingInfo.userId,BookingInfo.serviceId,BookingInfo.sTime,BookingInfo.duration," +
            "TrainerInfo.trainerName,Serviceinfo.servName,Serviceinfo.description " +
            "from BookingInfo join TrainerInfo on TrainerInfo.trainerId=BookingInfo.trainerId " +
            "join Serviceinfo on Serviceinfo.serviceId=BookingInfo.serviceId  where userId=:id")
    Flowable<List<BookingInfoModel>> getModelList(long id);

    @Query("Delete  from BookingInfo where bookingId=:id")
    void deleteUser(long id);
}
