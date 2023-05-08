package edu.sjsu.android.finalproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class TodoDB extends SQLiteOpenHelper {
    static String TABLE_NAME = "Todo";
    protected static String ID = "_id";
    protected static String ID2 = "_id";
    protected static String NAME = "name";
    protected static String DATE = "date";

    static String TABLE_NAME2 = "Category";

    protected static String CATEGORY = "category";
    protected static ArrayList<String> cat;

    public void setCategories(ArrayList<String> cat){
        this.cat = cat;
    }

    public TodoDB(@Nullable Context context) {
        super(context, TABLE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sq) {
        String command = String.format("CREATE TABLE %s ("
                        + "%s INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + "%s STRING NOT NULL, "
                        + "%s STRING NOT NULL);",
                TABLE_NAME, ID, NAME, DATE);

        String command2 = "CREATE TABLE Todocategory(todo_id int NOT NULL, category varchar(50) NOT NULL, FOREIGN KEY(todo_id) REFERENCES Todo(todo_id));";

        String command3 = String.format("CREATE TABLE %s ("
                        + "%s INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + "%s STRING NOT NULL);",
                TABLE_NAME2, ID2, CATEGORY);

        sq.execSQL(command);
        sq.execSQL(command2);
        sq.execSQL(command3);
        Log.d("Test", "test");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sq, int i, int i1) {
        sq.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        sq.execSQL("DROP TABLE IF EXISTS Todocategory");
        sq.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME2);
        onCreate(sq);
    }

    public long insert(ContentValues values){
        SQLiteDatabase db = getWritableDatabase();
        long inserted = db.insert(TABLE_NAME, null, values);

        for(String cat : cat){
            ContentValues val = new ContentValues();
            val.put("todo_id", inserted);
            val.put("category", cat);
            db.insert("Todocategory",null, val);
        }
        return inserted;
    }

    public long insertCategory(ContentValues values){
        SQLiteDatabase db = getWritableDatabase();
        return db.insert(TABLE_NAME2, null, values);
    }

    public Cursor getAllTodo(String sortOrder){
        SQLiteDatabase database = getWritableDatabase();

        return database.query(TABLE_NAME, new String[]{ID, NAME, DATE}, null, null, null, null, sortOrder);
    }

    public Cursor getTodo(){
        SQLiteDatabase db = getWritableDatabase();
        return db.rawQuery("SELECT * FROM Todo JOIN Todocategory ON Todo.id == Todocategory.id ORDER BY Todo.id", null);
    }

//    public int deleteAll(){
//        SQLiteDatabase db = getWritableDatabase();
//        return db.delete(TABLE_NAME, null, null);
//    }

}
