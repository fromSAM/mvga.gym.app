package com.gadware.mvga.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(access = AccessLevel.PUBLIC)

public class BookingInfoModel {
    private long bookingId;
    @ColumnInfo(index = true)
    private long trainerId;
    @ColumnInfo(index = true)
    private long userId;
    @ColumnInfo(index = true)
    private long serviceId;

    private String sTime, duration,trainerName,servName,description;
}
