package com.gadware.mvga.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Entity(tableName = "ServiceInfo",
        foreignKeys = {@ForeignKey(entity = TrainerInfo.class, parentColumns = "trainerId", childColumns = "trainerId",
                        onDelete = ForeignKey.CASCADE)})
public class ServiceInfo {
    @PrimaryKey(autoGenerate = true)
    private long serviceId;
    @ColumnInfo(index = true)
    private long trainerId;

    private String servName,description;

    public static List<ServiceInfo> getList() {
        List<ServiceInfo> reviewInfoList = new ArrayList<>();
        reviewInfoList.add(new ServiceInfo(1, 1,   "Weight Gain", "Gain Weight at any age with diet and workout"));
        reviewInfoList.add(new ServiceInfo(2, 2,   "Weight Gain", "Gain Weight at any age with diet and workout"));
        reviewInfoList.add(new ServiceInfo(3, 3,   "Weight Gain", "Gain Weight at any age with diet and workout"));
        reviewInfoList.add(new ServiceInfo(4, 1,   "Weight Loss", "Lose Weight at any age with diet and workout"));
        reviewInfoList.add(new ServiceInfo(5, 2,   "Weight Loss", "Lose Weight at any age with diet and workout"));
        reviewInfoList.add(new ServiceInfo(6, 2,   "Cardio Training", "Cardio training by workout"));
        reviewInfoList.add(new ServiceInfo(7, 3,   "Cardio Training", "Cardio training by workout"));
        reviewInfoList.add(new ServiceInfo(8, 3,   "Joint Pain Relief", "Joint pain solution by workout"));
        return reviewInfoList;
    }
}
