package edu.sjsu.android.finalproject;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemListFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO : Get tasks from database
        tasks = new ArrayList<>();
        try(Cursor c = getContext().getContentResolver().query(CONTENT_URI, null, getArguments().getString("categoryID"), null, null)){
            if(c.moveToFirst()){
                do{
                    int id = c.getColumnIndex("_id");
                    int nameid = c.getColumnIndex("name");
                    int dateid = c.getColumnIndex("date");
                    String name = c.getString(nameid);
                    String date = c.getString(dateid);
                    String todo_id = c.getString(id);

                    tasks.add(new TodoItem(todo_id, name, date));
                }while(c.moveToNext());
            }
        }
        assert getArguments() != null;
        Log.d("testing1", "" + getArguments().getString("categoryID"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);

        // Set the adapter
        if (view instanceof RelativeLayout) {
            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.itemList);
            recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
            ItemListAdapter adapter = new ItemListAdapter(tasks, ItemListFragment.this);
            recyclerView.setAdapter(adapter);
        }
        return view;
    }

    public void onClick(int position) {
        Log.d("ItemListFragment", "TOUCHED");
        new IndividualItem().showDialog(position, getContext(), tasks, this);
    }
}