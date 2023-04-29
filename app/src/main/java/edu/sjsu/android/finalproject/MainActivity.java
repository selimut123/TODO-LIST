package edu.sjsu.android.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private final String AUTHORITY = "edu.sjsu.android.finalproject";
    private final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void addToDo(View view) {
        ContentValues values = new ContentValues();
        //Putting the value according to the Input UI ID
//        values.put("name", binding.nameID.getText().toString());
//        values.put("date", binding.dateID.getText().toString());
//        values.put("category", binding.categoryID.getText().toString());
        // Toast message if successfully inserted
        if (getContentResolver().insert(CONTENT_URI, values) != null)
            Toast.makeText(this, "Student Added", Toast.LENGTH_SHORT).show();
    }

    public void getAllStudents(View view){
        try(Cursor c = getContentResolver().query(CONTENT_URI, null, null, null, "name")){
            if(c.moveToFirst()){
                do{
                    for(int i = 0; i < c.getColumnCount(); i++){
                        // display each result into the listview
                        String result = c.getString(i);
                        // later implement here
                    }
                }while(c.moveToNext());
            }
        }
    }
}