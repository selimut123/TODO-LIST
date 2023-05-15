package edu.sjsu.android.finalproject;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class TodoItem{

    private final String name;
    private final String date;
    private final String id;

    public TodoItem(String id, String name, String date) {
        this.name = name;
        this.date = date;
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public String getName() {
        return name;
    }

    public String getId(){return id;}

}
