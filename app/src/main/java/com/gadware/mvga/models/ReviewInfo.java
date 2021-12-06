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
@Entity(tableName = "ReviewInfo",
        foreignKeys = {@ForeignKey(entity = UserInfo.class, parentColumns = "userId",
                childColumns = "userId", onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = TrainerInfo.class, parentColumns = "trainerId", childColumns = "trainerId",
                        onDelete = ForeignKey.CASCADE)})
public class ReviewInfo {
    @PrimaryKey(autoGenerate = true)
    private long reviewId;
    @ColumnInfo(index = true)
    private long userId;
    @ColumnInfo(index = true)
    private long trainerId;
    private double rating;
    private String date,review;

    public ReviewInfo(long userId, long trainerId, double rating, String date, String review) {
        this.userId = userId;
        this.trainerId = trainerId;
        this.rating = rating;
        this.date = date;
        this.review = review;
    }

    public static List<ReviewInfo> getList() {
        List<ReviewInfo> reviewInfoList = new ArrayList<>();
        reviewInfoList.add(new ReviewInfo(1, 1, 1, 3, "10-10-20", "Good"));
        reviewInfoList.add(new ReviewInfo(2, 2, 2, 4.5, "12-11-21", "Best Ever"));
        reviewInfoList.add(new ReviewInfo(3, 3, 1, 5.0, "23-06-20", "Excellent"));
        reviewInfoList.add(new ReviewInfo(4, 1, 2, 3.5, "08-05-19", "Satisfied"));
        reviewInfoList.add(new ReviewInfo(6, 3, 3, 4, "11-07-21", "Superb"));
        reviewInfoList.add(new ReviewInfo(7, 1, 3, 3.8, "16-10-20", "Nice"));
        reviewInfoList.add(new ReviewInfo(9, 2, 1, 4.2, "29-04-21", "Great Service"));

        return reviewInfoList;
    }
}
