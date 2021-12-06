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
public class PackageInfo {
    @PrimaryKey(autoGenerate = true)
    private long pkgId;
    private String subName, charge, discount;

    public static List<PackageInfo> getList() {
        List<PackageInfo> subscriptionInfos = new ArrayList<>();
        subscriptionInfos.add(new PackageInfo(1, "none", "none", "none"));
        subscriptionInfos.add(new PackageInfo(2, "Pay Per View", "2", "none"));
        subscriptionInfos.add(new PackageInfo(3, "Monthly", "40", "none"));
        subscriptionInfos.add(new PackageInfo(4, "Yearly", "400", "20"));
        return subscriptionInfos;
    }
}
