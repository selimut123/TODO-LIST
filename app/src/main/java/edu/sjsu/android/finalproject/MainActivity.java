package edu.sjsu.android.finalproject;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    private final String AUTHORITY = "edu.sjsu.android.finalproject";
    private final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    DrawerLayout drawer;
    NavigationView navView;
    ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawer = findViewById(R.id.drawer_layout);
        navView = findViewById(R.id.drawer_nav);
        drawerToggle = new ActionBarDrawerToggle(this, drawer, R.string.open, R.string.close);
        drawer.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch(item.getItemId()){
                    case (R.id.home):
                    {
                        Toast.makeText(MainActivity.this, "home", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case (R.id.completedtask):
                    {
                        Toast.makeText(MainActivity.this, "completed task", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case (R.id.about):
                    {
                        Toast.makeText(MainActivity.this, "about", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case (R.id.login):
                    {
                        Toast.makeText(MainActivity.this, "log in", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case (R.id.signout):
                    {
                        Toast.makeText(MainActivity.this, "sign out", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
                return false;
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(drawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
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