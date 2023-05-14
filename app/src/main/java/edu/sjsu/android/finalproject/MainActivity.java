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
    DrawerLayout drawer;
    NavigationView navView;
    ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpDrawerNavigation();
    }

    private void setUpDrawerNavigation() {
        // Set up UI
        drawer = findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(this, drawer, R.string.open, R.string.close);
        drawer.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navView = findViewById(R.id.drawer_nav);

        // Set up onClick Navigation
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch(item.getItemId()){
                    case (R.id.home):
                    {
                        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
                        assert navHostFragment != null;
                        NavController controller = navHostFragment.getNavController();
                        controller.navigate(R.id.action_global_categoryListFragment2);
                        drawer.closeDrawer(GravityCompat.START, true);
                        break;
                    }
                    case (R.id.about):
                    {
                        Toast.makeText(MainActivity.this, "about", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case (R.id.task_visibility):
                    {
                        switch (item.getTitle().toString()) {
                            case ("All Tasks") : {
                                item.setTitle("Incomplete Tasks");

                                NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
                                assert navHostFragment != null;
                                NavController controller = navHostFragment.getNavController();
                                Bundle bundle = new Bundle();
                                bundle.putString("categoryID", "ALL");
                                controller.navigate(R.id.action_global_listFragment, bundle);
                                break;
                            }
                            case ("Incomplete Tasks"): {
                                item.setTitle("Completed Tasks");

                                NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
                                assert navHostFragment != null;
                                NavController controller = navHostFragment.getNavController();
                                Bundle bundle = new Bundle();
                                bundle.putString("categoryID", "INCOMPLETE");
                                controller.navigate(R.id.action_global_listFragment, bundle);
                                break;
                            }
                            case ("Completed Tasks"): {
                                item.setTitle("All Tasks");
                                NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
                                assert navHostFragment != null;
                                NavController controller = navHostFragment.getNavController();
                                Bundle bundle = new Bundle();
                                bundle.putString("categoryID", "COMPLETE");
                                controller.navigate(R.id.action_global_listFragment, bundle);
                            } break;
                        }
                        Toast.makeText(MainActivity.this, "task visibility", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    default : {
                        // Get position of item in menu
                        int index = item.getOrder();
                        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
                        assert navHostFragment != null;
                        NavController controller = navHostFragment.getNavController();
                        Bundle bundle = new Bundle();
                        bundle.putString("categoryID", String.valueOf(index));
                        controller.navigate(R.id.action_global_listFragment, bundle);
                        Toast.makeText(MainActivity.this, "clicked category : " + index, Toast.LENGTH_SHORT).show();
                    }
                }
                drawer.closeDrawers();
                return false;
            }
        });
        addMenuItemInNavMenuDrawer();
    }

    private void addMenuItemInNavMenuDrawer(){
        NavigationView navigationView = (NavigationView) findViewById(R.id.drawer_nav);

        Menu menu = navigationView.getMenu();
        Menu submenu = menu.addSubMenu("Categories");
        for(CategoryItem item : CategoryListFragment.categories){
            submenu.add(Menu.NONE, Menu.NONE, Integer.parseInt(item.getId()), item.getName());
        }
        navigationView.invalidate();
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
        Log.d("test", "running");
    }

}