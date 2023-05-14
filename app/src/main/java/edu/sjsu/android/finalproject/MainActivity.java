package edu.sjsu.android.finalproject;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

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
import android.view.Menu;
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

        drawer = findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(this, drawer, R.string.open, R.string.close);
        drawer.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Menu sub = addMenuItemInNavMenuDrawer();

        NavigationView navigationView = (NavigationView) findViewById(R.id.drawer_nav);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int id = item.getItemId();
                if(id == R.id.home){
                    NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
                    assert navHostFragment != null;
                    NavController controller = navHostFragment.getNavController();
                    controller.navigate(R.id.action_global_categoryListFragment2);
                }
                else{
                    NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
                    assert navHostFragment != null;
                    NavController controller = navHostFragment.getNavController();
                    int index = -1;
                    Log.d("Test", String.valueOf(sub.size()));
                    for (int i=0; i<sub.size(); i++) {
                        if (sub.getItem(i).equals(item)) index = i; break;
                    }
                    Log.d("Test", String.valueOf(index));
                    Bundle bundle = new Bundle();
                    bundle.putString("categoryID", String.valueOf(index + 1));
                    controller.navigate(R.id.action_global_listFragment, bundle);
                }
                drawer.closeDrawers();
                return true;
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

    private Menu addMenuItemInNavMenuDrawer(){
        NavigationView navigationView = (NavigationView) findViewById(R.id.drawer_nav);

        Menu menu = navigationView.getMenu();
        Menu submenu = menu.addSubMenu("Categories");

        for (CategoryItem category : CategoryListFragment.categories){
            submenu.add(category.getName());
        }

        navigationView.invalidate();

        return submenu;
    }
}