package edu.sjsu.android.finalproject;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private final String AUTHORITY = "edu.sjsu.android.finalproject";
    private final Uri CONTENT_URI_TODO = Uri.parse("content://" + AUTHORITY + "/TODO");
    private final Uri CONTENT_URI_CAT = Uri.parse("content://" + AUTHORITY + "/CATEGORY");

    private LocalDate newDate;
    DrawerLayout drawer;
    NavigationView navView;
    ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.add_FAB).setOnClickListener(this::addToDo);
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

    private void changeDate(Context context, TextView textView) {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR); // current year
        int mMonth = c.get(Calendar.MONTH); // current month
        int mDay = c.get(Calendar.DAY_OF_MONTH); // current day

        // date picker dialog
        DatePickerDialog dpDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @SuppressLint("NewApi")
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                textView.setText(year + "/" + (month + 1) + "/" + day);
                newDate = LocalDate.of(year, month + 1, day);
            }
        }, mYear, mMonth, mDay);
        dpDialog.show();
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

    @Override
    protected void onPause() {
        super.onPause();
    }

    public void addToDo(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = (LayoutInflater) MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View popup = inflater.inflate(R.layout.item_add_popup, null);
        TextView inDate = ((TextView)popup.findViewById(R.id.item_add_modal_date));
        inDate.setOnClickListener(v -> changeDate(MainActivity.this, inDate));
        Spinner spinner = (Spinner)popup.findViewById(R.id.item_add_modal_category_spinner);
        ArrayList<IndividualItem.ItemState> listItems = new ArrayList<>();
        /* TODO :
              Replace temporary with Database
              Save categories into Database
              Depending on how the previous two todos are implemented, append R.string.add_new_category to the end of the spinner
          */
//        ArrayList<String> tempCategories = new ArrayList<>();
//        tempCategories.add("Select Category");

        IndividualItem.ItemState iS = new IndividualItem.ItemState();
        iS.setCategory("Select Category");
        iS.setSelected(false);
        listItems.add(iS);
        try(Cursor c = getContentResolver().query(CONTENT_URI_CAT, null, null, null, null)){
            if(c.moveToFirst()){
                do{
                    int catid = c.getColumnIndex("category");
                    String cat = c.getString(catid);

                    int cat_id = c.getColumnIndex("_id");
                    String id = c.getString(cat_id);

                    iS = new IndividualItem.ItemState();
                    iS.setId(id);
                    iS.setCategory(cat);
                    iS.setSelected(false);
                    listItems.add(iS);

//                    tempCategories.add(cat);
                }while(c.moveToNext());
            }
        }
        iS = new IndividualItem.ItemState();
        iS.setCategory(getString(R.string.add_new_category));
        iS.setSelected(false);
        listItems.add(iS);
//        tempCategories.add(getString(R.string.add_new_category));
//        String[] tempCategories = {
//                "Select Category",
//                "Category1", "Category2", "Category3", "Category4", "Category5",
//                "Category6", "Category7", "Category8", "Category9", getString(R.string.add_new_category)
//        };
//        for (String s:tempCategories) {
//        }
        builder.setTitle(R.string.add_popup_title);
        builder.setNegativeButton("Cancel", ((dialogInterface, i) -> {
            newDate = null;
            dialogInterface.dismiss();
        }));
        builder.setPositiveButton("Add", (dialog, id) ->{
            //todo: add new event to db
            //
            // YT:
            // The event name is stored in *new_item_name*
            // selected date is stored in a private variable *newDate*
            // ^ not sure if storing this info in a private variable is a good idea but can't think
            //   of any problem at this point (cancelling will reset this variable (see couple lines
            //   before this comment in setNegativeButton))
            // As Bryan implemented, the selected categories can be retrieved by iterating over the list
            String new_item_name = ((EditText)popup.findViewById(R.id.item_add_modal_item_name)).getText().toString();
            ContentValues val = new ContentValues();
            val.put("name", new_item_name);
            val.put("date", inDate.getText().toString());

            ArrayList<String> selected_cats = new ArrayList<>();
            for(IndividualItem.ItemState i: listItems){
                if(i.isSelected()){
                    selected_cats.add(i.getId());
                }
            }

            TodoDB.cat = selected_cats;
            // update Database
            if (getContentResolver().insert(CONTENT_URI_TODO, val) != null){
                Toast.makeText(this, "Task Added", Toast.LENGTH_SHORT).show();
                finish();
                overridePendingTransition(0,0);
                startActivity(getIntent());
                overridePendingTransition(0,0);
            }

//            Log.d("TAG", "addItem: \n\tname: "+ new_item_name + "\n\tdate: " + newDate.toString() + "\n\tcategories: " + String.join(" ", selected_cats));
        });
        builder.setView(popup);
        AlertDialog alert = builder.create();

        IndividualItemAdapter individualItemAdapter = new IndividualItemAdapter(popup.getContext(), 0, listItems);
        spinner.setAdapter(individualItemAdapter);
        Context cont = this;
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                String selectedItem = ((IndividualItem.ItemState)parent.getItemAtPosition(position)).getCategory();
                Log.d("TAG", "onItemSelected: " + selectedItem);
                if(selectedItem.equals(getString(R.string.add_new_category)))
                {
//                    Log.d("TAG", "getCustomView: new feature");
                    android.app.AlertDialog.Builder builder2 = new android.app.AlertDialog.Builder(popup.getContext());
                    LayoutInflater inflater = (LayoutInflater) popup.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    final View add_cat_popup = inflater.inflate(R.layout.category_add_popup, null);
                    builder2.setTitle("Add a new category");
                    builder2.setNegativeButton("Cancel", ((dialogInterface, i) -> {
                        dialogInterface.dismiss();
                    }));
                    builder2.setPositiveButton("Add", (dialogInterface, i) ->{
                        //todo: add new category to db and to the spinner
                        String new_cat_name = ((EditText)add_cat_popup.findViewById(R.id.cat_add_modal_cat_name)).getText().toString();
                        ContentValues val = new ContentValues();
                        val.put("category", new_cat_name);

                        if (getContentResolver().insert(CONTENT_URI_CAT, val) != null){
                            Toast.makeText(cont, "Category Added", Toast.LENGTH_SHORT).show();
                            alert.dismiss();
                            finish();
                            overridePendingTransition(0,0);
                            startActivity(getIntent());
                            overridePendingTransition(0,0);
                        }
//                        Log.d("TAG", "addCategory: "+ new_cat_name);
                    });
                    builder2.setView(add_cat_popup);
                    builder2.create().show();
                }
            }
            public void onNothingSelected(AdapterView<?> parent){
            }
        });


        alert.show();
    }


//    public void getAllStudents(View view){
//        try(Cursor c = getContentResolver().query(CONTENT_URI, null, null, null, "name")){
//            if(c.moveToFirst()){
//                do{
//                    for(int i = 0; i < c.getColumnCount(); i++){
//                        // display each result into the listview
//                        String result = c.getString(i);
//                        // later implement here
//                    }
//                }while(c.moveToNext());
//            }
//        }
//    }
}