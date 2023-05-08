package edu.sjsu.android.finalproject;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class TodoItem{

    private final String name;
    private final String date;

    public TodoItem(String name, String date) {
        this.name = name;
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public String getName() {
        return name;
    }

}
