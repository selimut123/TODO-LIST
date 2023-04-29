package edu.sjsu.android.finalproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class TodoDB extends SQLiteOpenHelper {
    static String TABLE_NAME = "Todo";
    protected static String ID = "_id";
    protected static String NAME = "name";
    protected static String DATE = "date";
    protected static String CATEGORY = "CATEGORY";

    public TodoDB(@Nullable Context context) {
        super(context, TABLE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sq) {
        String command = String.format("CREATE TABLE %s ("
                        + "%s INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + "%s STRING NOT NULL, "
                        + "%s STRING NOT NULL, "
                        + "%s STRING NOT NULL); ",
                TABLE_NAME, ID, NAME, DATE, CATEGORY);
        sq.execSQL(command);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sq, int i, int i1) {
        sq.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sq);
    }

    public long insert(ContentValues values){
        SQLiteDatabase db = getWritableDatabase();
        return db.insert(TABLE_NAME, null, values);
    }

    public Cursor getAllTodo(String orderBy){
        SQLiteDatabase database = getWritableDatabase();
        return database.query(TABLE_NAME,
                new String[]{ID, NAME, DATE, CATEGORY},
                null, null, null, null, orderBy);
    }

//    public int deleteAll(){
//        SQLiteDatabase db = getWritableDatabase();
//        return db.delete(TABLE_NAME, null, null);
//    }

}
