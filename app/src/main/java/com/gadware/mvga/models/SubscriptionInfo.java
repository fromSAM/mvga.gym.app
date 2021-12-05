package com.gadware.mvga.models;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Entity(tableName = "SubscriptionInfo",
        foreignKeys = {@ForeignKey(entity = UserInfo.class, parentColumns = "userId", childColumns = "userId",
                onDelete = ForeignKey.CASCADE)})
public class SubscriptionInfo {
    @PrimaryKey(autoGenerate = true)
    private long subId, userId;
    private String subName, charge, discount;

    @Ignore
    public SubscriptionInfo(long userId, String subName, String charge, String discount) {
        this.userId = userId;
        this.subName = subName;
        this.charge = charge;
        this.discount = discount;
    }

    public static List<SubscriptionInfo> getList() {
        List<SubscriptionInfo> subscriptionInfos = new ArrayList<>();
        subscriptionInfos.add(new SubscriptionInfo(1, 1, "Pay Per View", "2$", "none"));
        subscriptionInfos.add(new SubscriptionInfo(2, 2, "Monthly", "40$", "none"));
        subscriptionInfos.add(new SubscriptionInfo(3, 3, "Yearly", "400$", "20$"));
        return subscriptionInfos;
    }
}
