package com.gadware.mvga.storage;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;


import com.gadware.mvga.dao.BookingDao;
import com.gadware.mvga.dao.ReviewDao;
import com.gadware.mvga.dao.ServiceDao;
import com.gadware.mvga.dao.SubscriptionDao;
import com.gadware.mvga.dao.TrainerDao;
import com.gadware.mvga.dao.UserDao;
import com.gadware.mvga.models.BookingInfo;
import com.gadware.mvga.models.ReviewInfo;
import com.gadware.mvga.models.ServiceInfo;
import com.gadware.mvga.models.SubscriptionInfo;
import com.gadware.mvga.models.TrainerInfo;
import com.gadware.mvga.models.UserInfo;
import com.gadware.mvga.utils.Converters;

import java.util.concurrent.Executors;

@TypeConverters({Converters.class})
@Database(entities = {TrainerInfo.class, ReviewInfo.class, UserInfo.class, ServiceInfo.class, SubscriptionInfo.class, BookingInfo.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "GADWARE_MVGA_DEMO";

    private static AppDatabase instance;

    public static synchronized AppDatabase getDatabase(Context context) {
        if (instance == null) {
            instance = buildDatabase(context);
        }
        return instance;
    }


    public abstract TrainerDao trainerDao();
    public abstract ReviewDao reviewDao();
    public abstract ServiceDao serviceDao();
    public abstract SubscriptionDao subscriptionDao();
    public abstract BookingDao bookingDao();
    public abstract UserDao userDao();



    private static AppDatabase buildDatabase(final Context context) {
        return Room.databaseBuilder(context,
                AppDatabase.class,
                DATABASE_NAME).fallbackToDestructiveMigration()
                .addCallback(new Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                        Executors.newSingleThreadScheduledExecutor().execute(() -> {
                            getDatabase(context).trainerDao().insertUserList(TrainerInfo.getList());
                        });
                    }
                }).build();
    }

}

