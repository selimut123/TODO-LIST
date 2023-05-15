package edu.sjsu.android.finalproject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.PorterDuff;
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
import android.widget.ToggleButton;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;


public class CategoryListFragment extends Fragment {
    static ArrayList<CategoryItem> categories = new ArrayList<>();

    private final String AUTHORITY = "edu.sjsu.android.finalproject";
    private final Uri CONTENT_URI_CAT = Uri.parse("content://" + AUTHORITY + "/CATEGORY");
    private final Uri CONTENT_URI_CATLEN = Uri.parse("content://" + AUTHORITY + "/TODOLEN");
    private final Uri CONTENT_URI_ALLTODOLEN= Uri.parse("content://" + AUTHORITY + "/ALLTODOLEN");

    private static int imageID = 0;
    private static int colorID = 0;
    private static int backgroundColorID = 0;

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
        try(Cursor c = getContext().getContentResolver().query(CONTENT_URI_CAT, null, null, null, "category")){
            if(c.moveToFirst()){
                do{
                    int nameID = c.getColumnIndex("category");
                    String name = c.getString(nameID);

                    int catid = c.getColumnIndex("_id");
                    String cat = c.getString(catid);

                    String len = "0";
                    try(Cursor c2 = getContext().getContentResolver().query(CONTENT_URI_CATLEN, null, cat, null, "category")) {
                        if (c2.moveToFirst()) {
                            int lenid = c2.getColumnIndex("len");
                            len = c2.getString(lenid);
                        }
                    }

                    int colorid = c.getColumnIndex("color");
                    int imageid = c.getColumnIndex("image");
                    int backgroundId = c.getColumnIndex("backgroundcolor");

                    int color = c.getInt(colorid);
                    int image = c.getInt(imageid);
                    int background = c.getInt(backgroundId);

                    categories.add(new CategoryItem(cat, name, len, image, color, background));
                }while(c.moveToNext());
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category_list, container, false);
        view.findViewById(R.id.categoryFAB).setOnClickListener(v->openDialog());

        final View global = view.findViewById(R.id.ALLTODO);

        ShapeableImageView iView = global.findViewById(R.id.ALL_image);
        iView.setBackgroundResource(R.color.blue);
        iView.setImageResource(R.drawable.ic_important_foreground);
        iView.setColorFilter(getContext().getColor(R.color.white), PorterDuff.Mode.SRC_IN);

        ((TextView) global.findViewById(R.id.ALL_category)).setText("ALL TASKS");

        String len = "0";
        try(Cursor c = getContext().getContentResolver().query(CONTENT_URI_ALLTODOLEN, null, null, null, "category")){
            if(c.moveToFirst()){
                do{
                    int id = c.getColumnIndex("len");
                    len = c.getString(id);

                }while(c.moveToNext());
            }
        }

        ((TextView) global.findViewById(R.id.ALL_tasks)).setText(len + " tasks");

        global.setOnClickListener(v -> getAllTodo());

        if(view instanceof RelativeLayout){
            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.categoryList);
            recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
            CategoryListAdapter adapter = new CategoryListAdapter(categories, CategoryListFragment.this);
            recyclerView.setAdapter(adapter);
        }

        return view;
    }

    public void getAllTodo(){
        NavController controller = NavHostFragment.findNavController(this);
        Bundle bundle = new Bundle();
        bundle.putString("categoryID", "ALL");
        controller.navigate(R.id.action_categoryListFragment_to_itemListFragment, bundle);
    }

    public void onClick(int position) {
        Log.d("CategoryListFragment", "TOUCHED");
        NavController controller = NavHostFragment.findNavController(this);
        Bundle bundle = new Bundle();
        CategoryItem item = categories.get(position);
        bundle.putString("categoryID", item.getId());
        controller.navigate(R.id.action_categoryListFragment_to_itemListFragment, bundle);
    }

    @SuppressLint("ResourceType")
    public void openDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        final View popup = inflater.inflate(R.layout.category_add_popup, null);

        imageID = R.drawable.ic_home_foreground;
        colorID = R.color.white;
        backgroundColorID = R.color.blue;

        ShapeableImageView iView = (ShapeableImageView)popup.findViewById(R.id.category_image);
        iView.setBackgroundResource(backgroundColorID);   // From Database
        iView.setImageDrawable(getContext().getDrawable(imageID)); // From Database
        iView.setColorFilter(getContext().getColor(colorID), PorterDuff.Mode.SRC_IN);

        iView.setOnClickListener(v -> {
            editImageDialog(getContext(), iView);
        });

        ((EditText)popup.findViewById(R.id.category_name)).setText("");

        AlertDialog.Builder builder2 = new AlertDialog.Builder(getContext());
        final View popup2 = inflater.inflate(R.layout.validation_alert, null);
        ((TextView)popup2.findViewById(R.id.validation_text)).setText("Please enter the correct Input! Categories can't have the same name as others and Length should be less than 15 characters");

        builder2.setView(popup2);
        builder2.setPositiveButton("Ok", (dialog,id) -> {

        });
        AlertDialog valid = builder2.create();

        builder.setView(popup);
        builder.setPositiveButton("Save", (dialog, id) -> {
            //todo: add new category to db and to the spinner
            String new_cat_name = ((EditText)popup.findViewById(R.id.category_name)).getText().toString();
            for(CategoryItem item : categories){
                if(item.getName().equals(new_cat_name) || validation(new_cat_name) || new_cat_name.toUpperCase().equals("ALL TASKS")){
                    valid.show();
                    builder.create().dismiss();
                    return;
                }
            }
            ContentValues val = new ContentValues();
            val.put("image", imageID);
            val.put("color", colorID);
            val.put("backgroundcolor", backgroundColorID);
            val.put("category", new_cat_name);

            if (getContext().getContentResolver().insert(CONTENT_URI_CAT, val) != null){
                Toast.makeText(getContext(), "Category Added", Toast.LENGTH_SHORT).show();
                this.getActivity().finish();
                this.getActivity().overridePendingTransition(0,0);
                startActivity(this.getActivity().getIntent());
                this.getActivity().overridePendingTransition(0,0);
            }
        });
        builder.setNegativeButton("Cancel", (dialog, id) -> {
            // When user selects no, do nothing
        });
        builder.create().show();
    }

    public boolean validation(String str){
        return (!str.matches("[\\w ]+")) || str.length() > 15;
    }

    public void onHold(int position) {
        Log.d("CategoryListFragment", "HOLD");
        showEditDialog(position);
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
        ShapeableImageView iView = (ShapeableImageView)popup.findViewById(R.id.category_image);
        iView.setBackgroundResource(category.getBackgroundImageID());   // From Database
        iView.setImageDrawable(getContext().getDrawable(category.getImageID())); // From Database
        iView.setColorFilter(getContext().getColor(category.getColorID()), PorterDuff.Mode.SRC_IN);

        imageID = category.getImageID();
        colorID = category.getColorID();
        backgroundColorID = category.getBackgroundImageID();

        iView.setOnClickListener(v -> {
            editImageDialog(getContext(), iView);
        });

        // Set Category
        // TODO : Get Category name from Database or onClick
        ((EditText)popup.findViewById(R.id.category_name)).setText(category.getName());

        AlertDialog.Builder builder2 = new AlertDialog.Builder(getContext());
        final View popup2 = inflater.inflate(R.layout.validation_alert, null);
        ((TextView)popup2.findViewById(R.id.validation_text)).setText("Please enter the correct Input! Categories can't have the same name as others and Length should be less than 15 characters");

        builder2.setView(popup2);
        builder2.setPositiveButton("Ok", (dialog,id) -> {

        });
        AlertDialog valid = builder2.create();

        // Buttons
        builder.setNeutralButton("Delete", (dialog, id) -> {
            // TODO : Ask for confirmation ; remove from database
            if(getContext().getContentResolver().delete(CONTENT_URI_CAT, category.getId(), null) > 0){
                Toast.makeText(getContext(), "Category Deleted", Toast.LENGTH_SHORT).show();
                this.getActivity().finish();

                this.getActivity().overridePendingTransition(0,0);
                this.getActivity().startActivity(this.getActivity().getIntent());
                this.getActivity().overridePendingTransition(0,0);
            }
        });
        builder.setNegativeButton("Cancel", (dialog, id) -> {
            // When user selects, do nothing
        });
        builder.setPositiveButton("Save", (dialog, id) -> {
            // TODO : Save image and new name into database : Update everywhere
            String cat = ((TextView)popup.findViewById(R.id.category_name)).getText().toString();
            for(CategoryItem item : categories){
                if(item.getName().equals(category.getName())) continue;
                if(item.getName().equals(cat) || validation(cat)){
                    valid.show();
                    builder.create().dismiss();
                    return;
                }
            }
            ContentValues val = new ContentValues();
            val.put("image", imageID);
            val.put("color", colorID);
            val.put("backgroundcolor", backgroundColorID);
            val.put("category", cat);

            if (getContext().getContentResolver().update(CONTENT_URI_CAT, val, category.getId(), null) > 0){
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

    private static void changeColor(Context context, ShapeableImageView iView, int res, boolean foreground) {
        if (foreground) {
            iView.setColorFilter(context.getColor(res), PorterDuff.Mode.SRC_IN);
            colorID = res;
        }
        else {
            iView.setBackgroundResource(res);
            backgroundColorID = res;
        }
    }

    static private void editImageDialog(Context context, ShapeableImageView oView) {
        // Alert
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        final View popup = inflater.inflate(R.layout.image_select, null);

        // Set Image
        ShapeableImageView iView = (ShapeableImageView)popup.findViewById(R.id.base_image);
        iView.setBackground(oView.getBackground());
        iView.setImageDrawable(oView.getDrawable());
        iView.setColorFilter(oView.getColorFilter());


        // Change Icon :
        popup.findViewById(R.id.icon_home).setOnClickListener(
                v -> {
                    iView.setImageResource(R.drawable.ic_home_foreground);
                    imageID = R.drawable.ic_home_foreground;
                });
        popup.findViewById(R.id.icon_people).setOnClickListener(
                v -> {
                    iView.setImageResource(R.drawable.ic_people_foreground);
                    imageID = R.drawable.ic_people_foreground;
                });
        popup.findViewById(R.id.icon_work).setOnClickListener(
                v -> {
                    iView.setImageResource(R.drawable.ic_work_foreground);
                    imageID = R.drawable.ic_work_foreground;
                });
        popup.findViewById(R.id.icon_important).setOnClickListener(
                v -> {
                    iView.setImageResource(R.drawable.ic_important_foreground);
                    imageID = R.drawable.ic_important_foreground;
                });
        popup.findViewById(R.id.icon_star).setOnClickListener(
                v -> {
                    iView.setImageResource(R.drawable.ic_star_foreground);
                    imageID = R.drawable.ic_star_foreground;
                });
        popup.findViewById(R.id.icon_money).setOnClickListener(
                v -> {
                    iView.setImageResource(R.drawable.ic_money_foreground);
                    imageID = R.drawable.ic_money_foreground;
                });
        popup.findViewById(R.id.icon_calender).setOnClickListener(
                v -> {
                    iView.setImageResource(R.drawable.ic_calender_foreground);
                    imageID = R.drawable.ic_calender_foreground;
                });
        popup.findViewById(R.id.icon_medical).setOnClickListener(
                v -> {
                    iView.setImageResource(R.drawable.ic_medical_foreground);
                    imageID = R.drawable.ic_medical_foreground;
                });
        popup.findViewById(R.id.icon_misc).setOnClickListener(
                v -> {
                    iView.setImageResource(R.drawable.ic_misc_foreground);
                    imageID = R.drawable.ic_misc_foreground;
                });

        ToggleButton toggle = (ToggleButton) popup.findViewById(R.id.toggle_icon_edit);
        toggle.setChecked(true);

        // Change background/foreground color
        popup.findViewById(R.id.icon_white).setOnClickListener(
                v -> changeColor(context, iView, R.color.white, toggle.isChecked())
        );
        popup.findViewById(R.id.icon_red).setOnClickListener(
                v -> changeColor(context, iView, R.color.red, toggle.isChecked())
        );
        popup.findViewById(R.id.icon_orange).setOnClickListener(
                v -> changeColor(context, iView, R.color.orange, toggle.isChecked())
        );
        popup.findViewById(R.id.icon_yellow).setOnClickListener(
                v -> changeColor(context, iView, R.color.yellow, toggle.isChecked())
        );
        popup.findViewById(R.id.icon_green).setOnClickListener(
                v -> changeColor(context, iView, R.color.green, toggle.isChecked())
        );
        popup.findViewById(R.id.icon_teal).setOnClickListener(
                v -> changeColor(context, iView, R.color.teal, toggle.isChecked())
        );
        popup.findViewById(R.id.icon_blue).setOnClickListener(
                v -> changeColor(context, iView, R.color.blue, toggle.isChecked())
        );
        popup.findViewById(R.id.icon_purple).setOnClickListener(
                v -> changeColor(context, iView, R.color.purple, toggle.isChecked())
        );
        popup.findViewById(R.id.icon_pink).setOnClickListener(
                v -> changeColor(context, iView, R.color.pink, toggle.isChecked())
        );
        popup.findViewById(R.id.icon_black).setOnClickListener(
                v -> changeColor(context, iView, R.color.black, toggle.isChecked())
        );

        // Buttons
        builder.setNegativeButton("Cancel", (dialog, id) -> {
            // Do nothing if canceling
        });
        builder.setPositiveButton("Save", (dialog, id) -> {
            oView.setBackground(iView.getBackground());
            oView.setImageDrawable(iView.getDrawable());
            oView.setColorFilter(iView.getColorFilter());
        });

        // Misc
        builder.setView(popup);
        builder.create().show();
    }
}