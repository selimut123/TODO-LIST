package edu.sjsu.android.finalproject;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class ItemListFragment extends Fragment {
    ArrayList<String> tasks = new ArrayList<>();

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
        for (int i=0; i<25; i++) { tasks.add("Task " + i); }
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
        new IndividualItem().showDialog(position, getContext());
    }
}