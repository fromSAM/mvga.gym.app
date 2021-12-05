package com.gadware.mvga.models;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(access = AccessLevel.PUBLIC)

public class ReviewInfoModel {

    private long reviewId;
    private long userId;
    private long trainerId;
    private double rating;
    private String date,review,userName;


}
