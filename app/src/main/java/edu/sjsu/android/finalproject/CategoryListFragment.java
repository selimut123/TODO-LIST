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
import android.widget.EditText;
import android.widget.ImageView;
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
    ArrayList<CategoryItem> categories = new ArrayList<>();
    private final String AUTHORITY = "edu.sjsu.android.finalproject";
    private final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/CATEGORY");
    private final Uri CONTENT_URI2 = Uri.parse("content://" + AUTHORITY + "/TODOLEN");

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

                    String len = "0";
                    try(Cursor c2 = getContext().getContentResolver().query(CONTENT_URI2, null, cat, null, null)) {
                        if (c2.moveToFirst()) {
                            int lenid = c2.getColumnIndex("len");
                            len = c2.getString(lenid);
                        }
                    }

                    categories.add(new CategoryItem(cat, name, len));
                }while(c.moveToNext());
            }
        }
//        Log.d("testing", String.valueOf(categories.size()));
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
        CategoryItem item = categories.get(position);
        bundle.putString("categoryID", item.getId());
        controller.navigate(R.id.action_categoryListFragment_to_itemListFragment, bundle);
    }

//    public void openDialog(View view){
//        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService( Context.LAYOUT_INFLATER_SERVICE );
//        final View popup = inflater.inflate(R.layout.category_popup, null);
//
//        // Set Name
//        /* TODO :
//              Save name into Database
//          */
//        ((TextView)popup.findViewById(R.id.in_category)).setText("");
//
//        builder.setView(popup);
//        builder.setPositiveButton("Save", (dialog, id) -> {
//
//            ContentValues val = new ContentValues();
//            val.put("category", ((TextView)popup.findViewById(R.id.in_category)).getText().toString());
//            if (getContext().getContentResolver().insert(CONTENT_URI, val) != null)
//                Toast.makeText(getContext(), "Category Added", Toast.LENGTH_SHORT).show();
//        });
//        builder.setNegativeButton("Cancel", (dialog, id) -> {
//            // When user selects no, do nothing
//        });
//        builder.create().show();
//    }

    public void onHold(int position) {
        Log.d("CategoryListFragment", "HOLD");
        showEditDialog(position);
        //Toast.makeText(getContext(), "Long Hold", Toast.LENGTH_SHORT).show();
    }

    public void showEditDialog(int position) {
        // Alert
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        final View popup = inflater.inflate(R.layout.category_edit_popup, null);
        CategoryItem category = categories.get(position);
        // Set Image
        /* TODO : Get image from Database and External Storage ;
                  Remove images from storage when no categories use them */
        ImageView iView = (ImageView)popup.findViewById(R.id.category_image);
        iView.setImageResource(R.mipmap.ic_launcher_round);
        iView.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Changing Image", Toast.LENGTH_SHORT).show();
        });

        // Set Category
        // TODO : Get Category name from Database or onClick
        ((EditText)popup.findViewById(R.id.category_name)).setText(category.getName());

        // Buttons
        builder.setNeutralButton("Delete", (dialog, id) -> {
            // TODO : Ask for confirmation ; remove from database
        });
        builder.setNegativeButton("Cancel", (dialog, id) -> {
            // When user selects, do nothing
        });
        builder.setPositiveButton("Save", (dialog, id) -> {
            // TODO : Save image and new name into database : Update everywhere
            ContentValues val = new ContentValues();
            val.put("category", ((TextView)popup.findViewById(R.id.category_name)).getText().toString());

            if (getContext().getContentResolver().update(CONTENT_URI, val, category.getId(), null) > 0){
                Toast.makeText(getContext(), "Category Updated", Toast.LENGTH_SHORT).show();
                this.getActivity().finish();

                this.getActivity().overridePendingTransition(0,0);
                this.getActivity().startActivity(this.getActivity().getIntent());
                this.getActivity().overridePendingTransition(0,0);
            }

        });

        // Misc
        builder.setView(popup);
        builder.create().show();
    }
}