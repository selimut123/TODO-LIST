package edu.sjsu.android.finalproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;


public class ItemListFragment extends Fragment {
    ArrayList<TodoItem> tasks = new ArrayList<>();
    private final String AUTHORITY = "edu.sjsu.android.finalproject";
    private final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/TODO");
    private final Uri CONTENT_URI_ALLTODO = Uri.parse("content://" + AUTHORITY + "/ALLTODO");
    public SharedPreferences preferences;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemListFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = getContext().getSharedPreferences("CheckBox", Context.MODE_PRIVATE);
        // TODO : Get tasks from database
        tasks = new ArrayList<>();
        if(getArguments().getString("categoryID").equals("ALL")){
            try(Cursor c = getContext().getContentResolver().query(CONTENT_URI_ALLTODO, null, null, null, null)){
                readTasks(c, "");
            }
        } else if(getArguments().getString("categoryID").equals("INCOMPLETE")){
            try(Cursor c = getContext().getContentResolver().query(CONTENT_URI_ALLTODO, null, null, null, null)){
                readTasks(c, "incomplete");
            }
        }else if(getArguments().getString("categoryID").equals("COMPLETE")){
            try(Cursor c = getContext().getContentResolver().query(CONTENT_URI_ALLTODO, null, null, null, null)){
                readTasks(c, "complete");
            }
        }else{
            try(Cursor c = getContext().getContentResolver().query(CONTENT_URI, null, getArguments().getString("categoryID"), null, null)){
                readTasks(c, "");
            }
            assert getArguments() != null;
        }
    }

    public void readTasks(Cursor c, String type){
        if(c.moveToFirst()){
            do{
                int id = c.getColumnIndex("_id");
                int nameid = c.getColumnIndex("name");
                int dateid = c.getColumnIndex("date");
                String name = c.getString(nameid);
                String date = c.getString(dateid);
                String todo_id = c.getString(id);

                if(type.equals("complete")){
                    if(preferences.getBoolean(todo_id, false)){
                        tasks.add(new TodoItem(todo_id, name, date));
                    }
                }else if(type.equals("incomplete")){
                    if(!preferences.getBoolean(todo_id, false)){
                        tasks.add(new TodoItem(todo_id, name, date));
                    }
                }else{
                    tasks.add(new TodoItem(todo_id, name, date));
                }

            }while(c.moveToNext());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);
        view.findViewById(R.id.itemFAB).setOnClickListener(v->addTask());

        // Set the adapter
        if (view instanceof RelativeLayout) {
            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.itemList);
            recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
            ItemListAdapter adapter = new ItemListAdapter(tasks, ItemListFragment.this);
            recyclerView.setAdapter(adapter);
        }
        return view;
    }

    public void addTask(){
        Log.d("FAB", "TOUCHED");
        new IndividualItem().addDialog(getContext(), this);
    }

    public void onClick(int position) {
        Log.d("ItemListFragment", "TOUCHED");
        new IndividualItem().editDialog(position, getContext(), tasks, this);
    }

    public void onCheckClick(int position, CheckBox done){
        TodoItem item = tasks.get(position);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(item.getId(), done.isChecked());
        editor.apply();
    }
}