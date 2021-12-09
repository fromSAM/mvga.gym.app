package com.gadware.mvga.vm;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.gadware.mvga.models.BookingInfo;
import com.gadware.mvga.models.BookingInfoModel;
import com.gadware.mvga.storage.AppDatabase;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;


public class BookingViewModel extends AndroidViewModel {
    AppDatabase appDatabase;

    public BookingViewModel(@NonNull Application application) {
        super(application);
        appDatabase = AppDatabase.getDatabase(application);
    }

    public void insertSingleUser(BookingInfo model) {
        appDatabase.bookingDao().insertUser(model);
    }

    public Single<List<BookingInfoModel>> getTrainerBookingListList(long id){
        return appDatabase.bookingDao().getTrainerBookingListList(id);
    }
    public Single<List<BookingInfoModel>> getMyBookingListList(long id){
        return appDatabase.bookingDao().getMyBookingList(id);
    }
    public Flowable<List<BookingInfoModel>> getModelList(long id){
        return appDatabase.bookingDao().getMyBookingModelList(id);
    }
    public void deleteUser(long id){
         appDatabase.bookingDao().deleteUser(id);
    }
}
