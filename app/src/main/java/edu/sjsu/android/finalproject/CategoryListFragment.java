package edu.sjsu.android.finalproject;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class CategoryListFragment extends Fragment {
    ArrayList<String> categories = new ArrayList<>();

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CategoryListFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO : Get categories from database
        categories = new ArrayList<>();
        for (int i=0; i<25; i++) { categories.add("Category " + i); }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category_list, container, false);

        if(view instanceof LinearLayout){
            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.categoryList);
            recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
            CategoryListAdapter adapter = new CategoryListAdapter(categories, CategoryListFragment.this);
            recyclerView.setAdapter(adapter);
        }

        return view;
    }

    public void onClick(int position) {
        Log.d("CategoryListFragment", "TOUCHED");
        NavController controller = NavHostFragment.findNavController(this);
        controller.navigate(R.id.action_categoryListFragment_to_itemListFragment);
    }
}