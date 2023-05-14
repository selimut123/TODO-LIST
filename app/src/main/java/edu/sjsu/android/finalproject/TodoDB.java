package edu.sjsu.android.finalproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Map;

public class TodoDB extends SQLiteOpenHelper {
    static String TABLE_NAME = "Todo";
    protected static String ID = "_id";
    protected static String ID2 = "_id";
    protected static String NAME = "name";
    protected static String DATE = "date";
    protected static String IMAGE = "image";
    protected static String COLOR = "color";
    protected static String BACKGROUNDCOLOR = "backgroundcolor";

    static String TABLE_NAME2 = "Category";

    protected static String CATEGORY = "category";
    protected static ArrayList<String> cat;

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


        String command2 = String.format("CREATE TABLE %s ("
                        + "%s INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + "%s INT NOT NULL, "
                        + "%s INT NOT NULL, "
                        + "%s INT NOT NULL, "
                        + "%s STRING NOT NULL);",
                TABLE_NAME2, ID2, IMAGE, COLOR, BACKGROUNDCOLOR, CATEGORY);

        String command3 = "CREATE TABLE Todocategory(todo_id int NOT NULL, category_id int NOT NULL, FOREIGN KEY(todo_id) REFERENCES Todo(todo_id), FOREIGN KEY(category_id) REFERENCES Category(category_id));";

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

        for(String cat_id : cat){
            ContentValues val = new ContentValues();
            val.put("todo_id", inserted);
            val.put("category_id", Integer.valueOf(cat_id));
            db.insert("Todocategory",null, val);
        }
        return inserted;
    }

    public long insertCategory(ContentValues values){
        SQLiteDatabase db = getWritableDatabase();
        return db.insert(TABLE_NAME2, null, values);
    }

    public Cursor getEveryTodo(String sortOrder){
        SQLiteDatabase database = getWritableDatabase();
        return database.query(TABLE_NAME, new String[]{ID, NAME, DATE}, null, null, null, null, sortOrder);
    }

    public Cursor getAllTodo(String sortOrder, String cat_id){
        SQLiteDatabase database = getWritableDatabase();

        return database.rawQuery("SELECT DISTINCT Todo._id, Todo.name, Todo.date FROM Category JOIN Todocategory ON Category._id == Todocategory.category_id JOIN Todo ON Todocategory.todo_id == Todo._id WHERE Category._id == " + cat_id.trim() + ";", null);
    }

    public Cursor getCatIDFromTodo(String sortOrder, String todo_id){
        SQLiteDatabase database = getWritableDatabase();
        return database.rawQuery("SELECT category_id FROM Todocategory WHERE Todocategory.todo_id == " + todo_id.trim() + ";", null);
    }

    public Cursor getAllCat(String sortOrder){
        SQLiteDatabase database = getWritableDatabase();
        return database.query(TABLE_NAME2, new String[]{ID, IMAGE, COLOR, BACKGROUNDCOLOR, CATEGORY}, null, null, null, null, sortOrder);
    }

    public Cursor getTodoLength(String cat_id){
        SQLiteDatabase database = getWritableDatabase();
        return database.rawQuery("SELECT COUNT(Todo_id) AS len FROM Todocategory WHERE Todocategory.category_id == " + cat_id.trim() + ";", null);
    }

    public Cursor getEveryTodoLength(){
        SQLiteDatabase database = getWritableDatabase();
        return database.rawQuery("SELECT COUNT(_id) AS len FROM Todo;", null);
    }

    public int updateTodo(ContentValues val, String todo_id){
        SQLiteDatabase database = getWritableDatabase();

        database.delete("Todocategory", "todo_id == " + todo_id.trim(), null);

        for(String cat_id : cat){
            ContentValues val2 = new ContentValues();
            val2.put("todo_id", todo_id);
            val2.put("category_id", Integer.valueOf(cat_id));
            database.insert("Todocategory",null, val2);
        }

        return database.update(TABLE_NAME, val,"_id == " + todo_id.trim(),null);
    }

    public int updateCat(ContentValues val, String cat_id){
        SQLiteDatabase database = getWritableDatabase();
        return database.update(TABLE_NAME2, val,"_id == " + cat_id.trim(),null);
    }

    public int deleteCategory(String cat_id){
        SQLiteDatabase db = getWritableDatabase();
        db.delete("Todocategory", "category_id == " + cat_id.trim(), null);
        return db.delete(TABLE_NAME2, "_id == " + cat_id.trim(), null);
    }

    public int deleteTodo(String todo_id){
        SQLiteDatabase db = getWritableDatabase();
        db.delete("Todocategory", "todo_id == " + todo_id.trim(), null);
        return db.delete(TABLE_NAME, "_id == " + todo_id.trim(), null);
    }

}
