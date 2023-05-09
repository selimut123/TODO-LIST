package edu.sjsu.android.finalproject;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class TodoProvider extends ContentProvider {

    TodoDB db;
    private static final String AUTHORITY = "edu.sjsu.android.finalproject";
    private static final int TODO = 1;
    private static final int TODO_ID = 2;
    private static final int CATEGORY = 3;
    private static final int CATEGORY_ID = 4;
    private static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, "TODO",TODO);
        uriMatcher.addURI(AUTHORITY, "TODO/#",TODO_ID);
        uriMatcher.addURI(AUTHORITY, "CATEGORY", CATEGORY);
        uriMatcher.addURI(AUTHORITY, "CATEGORY/#", CATEGORY_ID);
    }

    @Override
    public boolean onCreate() {
        db = new TodoDB(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // TODO: Implement this to handle query requests from clients.
        sortOrder = sortOrder == null ? "_id" : sortOrder;
        switch (uriMatcher.match(uri)){
            case TODO:
                return db.getAllTodo(sortOrder, selection);
            case CATEGORY:
                return db.getAllCat(sortOrder);
            default: throw new SQLException("Failed to add a record into " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
//        long inserted = db.insert(values);
//        if(inserted > 0){
//            Uri _uri = ContentUris.withAppendedId(uri, inserted);
//            getContext().getContentResolver().notifyChange(_uri,null);
//            return _uri;
//        }
//        throw new SQLException("Failed to add a record into" + uri);
        Uri _uri = null;
        switch (uriMatcher.match(uri)){
            case TODO:
                long _ID1 = db.insert(values);
                if (_ID1 > 0) {
                    _uri = ContentUris.withAppendedId(uri, _ID1);
                    getContext().getContentResolver().notifyChange(_uri, null);
                }
                break;
            case CATEGORY:
                long _ID2 = db.insertCategory(values);
                if (_ID2 > 0) {
                    _uri = ContentUris.withAppendedId(uri, _ID2);
                    getContext().getContentResolver().notifyChange(_uri, null);
                }
                break;
            default: throw new SQLException("Failed to add a record into " + uri);
        }
        return _uri;
    }


    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        //return db.deleteAll();
        throw new UnsupportedOperationException("Not yet implemented");
    }

    //don't need to implement
    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getType(Uri uri) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
