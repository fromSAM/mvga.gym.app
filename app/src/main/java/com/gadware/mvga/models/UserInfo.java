package com.gadware.mvga.models;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Entity
public class UserInfo {
    @PrimaryKey(autoGenerate = true)
    private long userId;
    private String userName, email, pass, age, height, weight, address, balance, referCount;
    private byte[] image;

    @Ignore
    public UserInfo(String userName, String email, String pass, String age, String height, String weight, String address, String balance, String referCount, byte[] image) {
        this.userName = userName;
        this.email = email;
        this.pass = pass;
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.address = address;
        this.balance = balance;
        this.referCount = referCount;
        this.image = image;
    }


    public static List<UserInfo> getList() {
        List<UserInfo> userInfoList = new ArrayList<>();
        userInfoList.add(new UserInfo(1, "Mr. Xyz", "user1@mail.com", "12345", "22", "5.11F", "65", "A road, B city", "0", String.valueOf(new Date(System.currentTimeMillis())), null));
        userInfoList.add(new UserInfo(2, "Mr. ABC", "user2@mail.com", "12345", "25", "6.1F", "69", "B road, C city", "0", String.valueOf(new Date(System.currentTimeMillis())), null));
        userInfoList.add(new UserInfo(3, "Mr. PQR", "user3@mail.com", "12345", "19", "5.6F", "58", "B road, B city", "0", String.valueOf(new Date(System.currentTimeMillis())), null));
        return userInfoList;
    }
}
