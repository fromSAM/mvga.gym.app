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
@Entity(tableName = "SubscriptionInfo",
        foreignKeys = {@ForeignKey(entity = UserInfo.class, parentColumns = "userId", childColumns = "userId",
                onDelete = ForeignKey.CASCADE),@ForeignKey(entity = PackageInfo.class, parentColumns = "pkgId", childColumns = "pkgId",
                onDelete = ForeignKey.CASCADE)})
public class SubscriptionInfo {
    @PrimaryKey(autoGenerate = true)
    private long subId;
    @ColumnInfo(index = true)
    private long pkgId;
    @ColumnInfo(index = true)
    private long userId;
    private String bill, referDiscount,validity;

    @Ignore
    public SubscriptionInfo(long pkgId, long userId, String bill, String referDiscount, String validity) {
        this.pkgId = pkgId;
        this.userId = userId;
        this.bill = bill;
        this.referDiscount = referDiscount;
        this.validity = validity;
    }
}
