package com.gadware.mvga.utils;

import androidx.room.TypeConverter;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Objects;


public class Converters {
    @TypeConverter
    public static Date fromLongToDate(Long value) {
        //DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy",Locale.US);
        //
//        Date date =  new  Date(value);
//
//
//        try {
//            date =  dateFormat.parse(dateFormat.format(date));
//        } catch (java.text.ParseException e) {
//            e.printStackTrace();
//        }

        //return date;

        return value == null ? null : new Date(value);
    }

    //@TypeConverter
    public static String fromLongToDateString(Long value) {
        DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy HH:mm a", Locale.US);
        return value == null ? null : dateFormat.format(new Date(value));
    }

    public static String fromLongToTs(Long value) {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm a", Locale.US);
        return value == null ? null : dateFormat.format(new Date(value));
    }

    //@TypeConverter
    public static String fromLongToDs(Long value) {
        DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.US);
        return value == null ? null : dateFormat.format(new Date(value));
    }

    public static String fromLongToDS(String value) {
        DateFormat f1 = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        DateFormat f2 = new SimpleDateFormat("dd MMM yyyy", Locale.US);
        String x=null;
        try {
            x=f2.format(Objects.requireNonNull(f1.parse(value)));
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        return x;
    }
//    @TypeConverter//remove annotation and try
//    public static String fromLongToTimeString(Long value) {
//        DateFormat timeFormat = new SimpleDateFormat("HH:mm a", Locale.US);
//        return value == null ? null : timeFormat.format(new Date(value));
//    }

    @TypeConverter
    public static Long dateToLong(Date date) {
        return date == null ? null : date.getTime();
    }
}