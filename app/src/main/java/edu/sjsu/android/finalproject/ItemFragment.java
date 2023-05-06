package edu.sjsu.android.finalproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


/**
 * A fragment representing a list of Items.
 */
public class ItemFragment extends Fragment {

    ArrayList<String> CategoryNames = new ArrayList<>();
    ArrayList<Integer> CategoryNumTasks = new ArrayList<>();
    //MyItemRecyclerViewAdapter adapter;
    RecyclerView recyclerView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemFragment() {
    }

    @SuppressWarnings("unused")
//    public static ItemFragment newInstance(int columnCount) {
//        ItemFragment fragment = new ItemFragment();
//        Bundle args = new Bundle();
//        args.putInt(ARG_COLUMN_COUNT, columnCount);
//        fragment.setArguments(args);
//        return fragment;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);

        //TODO: get # of categories
        for (int i = 0; i <= 3; i++) {
            CategoryNames.add("Category " + i);
        }
        //todo: get # tasks in each category
        for (int i = 0; i <= 3; i++) {
            CategoryNumTasks.add(i);
        }
        //adapter = new MyItemRecyclerViewAdapter(CategoryNames, CategoryNumTasks);
        recyclerView = (RecyclerView) view;
        //recyclerView.setAdapter(adapter);
        return view;
    }
}