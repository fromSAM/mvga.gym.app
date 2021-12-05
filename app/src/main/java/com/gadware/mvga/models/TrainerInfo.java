package com.gadware.mvga.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Entity
public class TrainerInfo {
    @PrimaryKey(autoGenerate = true)
    private long trainerId;
    private String trainerName,trainerEmail,experience;
    private byte[] image;

    public static List<TrainerInfo> getList(){
        List<TrainerInfo> trainerInfoList=new ArrayList<>();
        trainerInfoList.add(new TrainerInfo(1, "Mr. AAA","demo1@mail.com","4",null));
        trainerInfoList.add(new TrainerInfo(2, "Mr. BBB","demo2@mail.com","3",null));
        trainerInfoList.add(new TrainerInfo(3, "Mr. CCC","demo3@mail.com","2",null));

        return trainerInfoList;
    }
}
