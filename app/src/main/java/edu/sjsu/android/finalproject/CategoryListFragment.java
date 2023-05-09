package edu.sjsu.android.finalproject;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;


public class CategoryListFragment extends Fragment {
    ArrayList<String> categories = new ArrayList<>();
    ArrayList<String> cat_id = new ArrayList<>();
    private final String AUTHORITY = "edu.sjsu.android.finalproject";
    private final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/CATEGORY");

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
        try(Cursor c = getContext().getContentResolver().query(CONTENT_URI, null, null, null, null)){
            if(c.moveToFirst()){
                do{
                    int nameID = c.getColumnIndex("category");
                    String name = c.getString(nameID);

                    int catid = c.getColumnIndex("_id");
                    String cat = c.getString(catid);

                    categories.add(name);
                    cat_id.add(cat);
                }while(c.moveToNext());
            }
        }
//        for (int i=0; i<25; i++) { categories.add("Category " + i); }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category_list, container, false);

        if(view instanceof RelativeLayout){
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
        Bundle bundle = new Bundle();
        bundle.putString("categoryID", cat_id.get(position));
        controller.navigate(R.id.action_categoryListFragment_to_itemListFragment, bundle);
    }

    public void openDialog(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        final View popup = inflater.inflate(R.layout.category_popup, null);

        // Set Name
        /* TODO :
              Save name into Database
          */
        ((TextView)popup.findViewById(R.id.in_category)).setText("");

        builder.setView(popup);
        builder.setPositiveButton("Save", (dialog, id) -> {

            ContentValues val = new ContentValues();
            val.put("category", ((TextView)popup.findViewById(R.id.in_category)).getText().toString());
            if (getContext().getContentResolver().insert(CONTENT_URI, val) != null)
                Toast.makeText(getContext(), "Category Added", Toast.LENGTH_SHORT).show();
        });
        builder.setNegativeButton("Cancel", (dialog, id) -> {
            // When user selects no, do nothing
        });
        builder.create().show();
    }
}