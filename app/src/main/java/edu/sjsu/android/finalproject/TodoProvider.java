package edu.sjsu.android.finalproject;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;

public class TodoProvider extends ContentProvider {

    TodoDB db;

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
        return db.getAllTodo(sortOrder);
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long inserted = db.insert(values);
        if(inserted > 0){
            Uri _uri = ContentUris.withAppendedId(uri, inserted);
            getContext().getContentResolver().notifyChange(_uri,null);
            return _uri;
        }
        throw new SQLException("Failed to add a record into" + uri);
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
