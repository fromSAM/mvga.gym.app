package com.gadware.mvga.models;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(access = AccessLevel.PUBLIC)

public class SubscriptionInfoModel {
    private long subId;
    private long pkgId;
    private long userId;
    private String subName,bill,referDiscount,validity;
}
