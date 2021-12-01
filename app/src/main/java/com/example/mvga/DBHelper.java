package com.example.mvga;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DBNAME= "gym.db";
    public DBHelper(Context context) {

        super(context, "gym.db", null , 1);
    }

    @Override
    public void onCreate(SQLiteDatabase DB) {
        DB.execSQL("create Table Customer(uname TEXT primary key,pass TEXT,mail TEXT,name TEXT,gender TEXT, packages TEXT)");
        // DB.execSQL("create Table Trainerdetails(trainername TEXT primary key, email TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase DB, int oldVersion, int newVersion) {
        DB.execSQL("drop Table if exists Customer");
    }
    public Boolean insertcustomerdata(String uname, String pass, String mail, String name, String gender, String packages )
    {
        SQLiteDatabase DB= this.getWritableDatabase();
        ContentValues contentValues= new ContentValues();
        contentValues.put("uname",uname);
        contentValues.put("pass",pass);
        contentValues.put("mail",mail);
        contentValues.put("name",name);
        contentValues.put("gender",gender);
        contentValues.put("packages",packages);

        long result=DB.insert("Customer", null, contentValues);

        if(result==-1)
            return false;
        else
            return true;
    }
    public Boolean checkuname(String uname){
        SQLiteDatabase DB=this.getWritableDatabase();
        Cursor cursor=DB.rawQuery("select * from Customer where uname= ?", new String[]{uname});

        if(cursor.getCount()>0)
            return true;
        else
            return false;
    }

    public Boolean checkunamepass(String uname, String pass){
        SQLiteDatabase DB=this.getWritableDatabase();
        Cursor cursor=DB.rawQuery("select * from Customer where uname= ? and pass= ?", new String[]{uname,pass});

        if(cursor.getCount()>0)
            return true;
        else
            return false;
    }
    /*public Cursor getAllData(String username){
        SQLiteDatabase DB=this.getWritableDatabase();
        Cursor res=DB.rawQuery("select * from Userdetails where username= ?", new String[]{username});
        return res;
    }*/


}

