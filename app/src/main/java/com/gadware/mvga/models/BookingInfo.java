package com.gadware.mvga.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Entity(tableName = "BookingInfo",
        foreignKeys = {@ForeignKey(entity = TrainerInfo.class, parentColumns = "trainerId", childColumns = "trainerId",
                onDelete = ForeignKey.CASCADE), @ForeignKey(entity = UserInfo.class, parentColumns = "userId", childColumns = "userId",
                onDelete = ForeignKey.CASCADE), @ForeignKey(entity = ServiceInfo.class, parentColumns = "serviceId", childColumns = "serviceId",
                onDelete = ForeignKey.CASCADE)})
public class BookingInfo {
    @PrimaryKey(autoGenerate = true)
    private long bookingId;
    @ColumnInfo(index = true)
    private long trainerId;
    @ColumnInfo(index = true)
    private long userId;
    @ColumnInfo(index = true)
    private long serviceId;

    private String  duration;
    private long sTime;
    @Ignore

    public BookingInfo(long trainerId, long userId, long serviceId,  String duration, long sTime) {
        this.trainerId = trainerId;
        this.userId = userId;
        this.serviceId = serviceId;
        this.duration = duration;
        this.sTime = sTime;
    }
}
