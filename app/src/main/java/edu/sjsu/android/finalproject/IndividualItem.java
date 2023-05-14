package edu.sjsu.android.finalproject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;

public class IndividualItem {
    private final String AUTHORITY = "edu.sjsu.android.finalproject";
    private final Uri CONTENT_URI_TODO = Uri.parse("content://" + AUTHORITY + "/TODO");
    private final Uri CONTENT_URI_CAT = Uri.parse("content://" + AUTHORITY + "/CATEGORY");
    private final Uri CONTENT_URI_TODO_CATID = Uri.parse("content://" + AUTHORITY + "/TODO_CATID");
    private final Uri CONTENT_URI_ALLTODO = Uri.parse("content://" + AUTHORITY + "/ALLTODO");

    private LocalDate newDate;
    public void editDialog(int position, Context context, ArrayList<TodoItem> tasks, ItemListFragment itemListFragment) {
        //Alert
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        final View popup = inflater.inflate(R.layout.item_edit_popup, null);

        TodoItem task = tasks.get(position);
        // Set Name
        /* TODO :
              Save name into Database
          */
        ((TextView)popup.findViewById(R.id.item_add_modal_item_name)).setText(task.getName());

        // Set Date
        /* TODO :
              Set an intial date
              Save date into Database
          */
        TextView inDate = ((TextView)popup.findViewById(R.id.item_add_modal_date));
        inDate.setText(task.getDate());
        inDate.setOnClickListener(v -> changeDate(context, inDate));

        Spinner spinner = (Spinner)popup.findViewById(R.id.item_add_modal_category_spinner);
        ArrayList<IndividualItem.ItemState> listItems = new ArrayList<>();

        IndividualItem.ItemState iS = new IndividualItem.ItemState();
        iS.setCategory("Select Category");
        iS.setSelected(false);
        listItems.add(iS);
        try(Cursor c = context.getContentResolver().query(CONTENT_URI_CAT, null, null, null, null)){
            if(c.moveToFirst()){
                do{
                    int catid = c.getColumnIndex("category");
                    String cat = c.getString(catid);

                    int cat_id = c.getColumnIndex("_id");
                    String id = c.getString(cat_id);

                    iS = new IndividualItem.ItemState();
                    iS.setId(id);
                    iS.setCategory(cat);
                    iS.setSelected(false);

                    try(Cursor c2 = context.getContentResolver().query(CONTENT_URI_TODO_CATID, null, task.getId(), null, null)){
                        if(c2.moveToFirst()){
                            do{
                                int check_catid = c2.getColumnIndex("category_id");
                                String check_cat = c2.getString(check_catid);

                                if(id.equals(check_cat)){
                                    iS.setSelected(true);
                                }
                            }while(c2.moveToNext());
                        }
                    }

                    listItems.add(iS);

                }while(c.moveToNext());
            }
        }

        AlertDialog.Builder builder2 = new AlertDialog.Builder(itemListFragment.getContext());
        final View popup2 = inflater.inflate(R.layout.validation_alert, null);
        ((TextView)popup2.findViewById(R.id.validation_text)).setText("Please enter the correct Input! TODO can't have the same name as others, and make sure you pick a categories!");

        builder2.setView(popup2);
        builder2.setPositiveButton("Ok", (dialog,id) -> {

        });
        AlertDialog valid = builder2.create();

        builder.setPositiveButton("Save", (dialog, id) -> {
            ContentValues val = new ContentValues();
            String edit_name = ((TextView)popup.findViewById(R.id.item_add_modal_item_name)).getText().toString();
            val.put("name", edit_name);
            val.put("date", ((TextView)popup.findViewById(R.id.item_add_modal_date)).getText().toString());

            ArrayList<String> selected_cats = new ArrayList<>();
            for(IndividualItem.ItemState i: listItems){
                if(i.isSelected()){
                    selected_cats.add(i.getId());
                }
            }

            try(Cursor c = itemListFragment.getContext().getContentResolver().query(CONTENT_URI_ALLTODO, null, null, null, null)){
                if(c.moveToFirst()){
                    do{
                        int nameid = c.getColumnIndex("name");
                        String name = c.getString(nameid);
                        if(edit_name.equals(name) || validation(edit_name) || selected_cats.size() > 0){
                            valid.show();
                            builder.create().dismiss();
                            return;
                        }

                    }while(c.moveToNext());
                }
            }

            TodoDB.cat = selected_cats;

            if (context.getContentResolver().update(CONTENT_URI_TODO, val, task.getId(), null) > 0){
                Toast.makeText(context, "Task Updated", Toast.LENGTH_SHORT).show();
                itemListFragment.getActivity().finish();

                itemListFragment.getActivity().overridePendingTransition(0,0);
                itemListFragment.getActivity().startActivity(itemListFragment.getActivity().getIntent());
                itemListFragment.getActivity().overridePendingTransition(0,0);
            }
        });
        builder.setNegativeButton("Cancel", (dialog, id) -> {
            // When user selects no, do nothing
        });
        builder.setNeutralButton("Delete", (dialog,id) -> {
            if((context.getContentResolver().delete(CONTENT_URI_TODO, task.getId(), null) > 0)){
                Toast.makeText(context, "TODO Deleted", Toast.LENGTH_SHORT).show();
                itemListFragment.getActivity().finish();

                itemListFragment.getActivity().overridePendingTransition(0,0);
                itemListFragment.getActivity().startActivity(itemListFragment.getActivity().getIntent());
                itemListFragment.getActivity().overridePendingTransition(0,0);
            }
        });

        IndividualItemAdapter individualItemAdapter = new IndividualItemAdapter(popup.getContext(), 0, listItems);
        spinner.setAdapter(individualItemAdapter);
        builder.setView(popup);
        builder.create().show();
    }

    public void addDialog(Context context, ItemListFragment itemListFragment){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View popup = inflater.inflate(R.layout.item_add_popup, null);

        TextView inDate = ((TextView)popup.findViewById(R.id.item_add_modal_date));
        inDate.setOnClickListener(v -> changeDate(context, inDate));
        Spinner spinner = (Spinner)popup.findViewById(R.id.item_add_modal_category_spinner);
        ArrayList<IndividualItem.ItemState> listItems = new ArrayList<>();

        IndividualItem.ItemState iS = new IndividualItem.ItemState();
        iS.setCategory("Select Category");
        iS.setSelected(false);
        listItems.add(iS);
        try(Cursor c = context.getContentResolver().query(CONTENT_URI_CAT, null, null, null, null)){
            if(c.moveToFirst()){
                do{
                    int catid = c.getColumnIndex("category");
                    String cat = c.getString(catid);

                    int cat_id = c.getColumnIndex("_id");
                    String id = c.getString(cat_id);

                    iS = new IndividualItem.ItemState();
                    iS.setId(id);
                    iS.setCategory(cat);
                    iS.setSelected(false);
                    listItems.add(iS);

                }while(c.moveToNext());
            }
        }

        AlertDialog.Builder builder2 = new AlertDialog.Builder(itemListFragment.getContext());
        final View popup2 = inflater.inflate(R.layout.validation_alert, null);
        ((TextView)popup2.findViewById(R.id.validation_text)).setText("Please enter the correct Input! TODO can't have the same name as others, and make sure you pick a categories!");

        builder2.setView(popup2);
        builder2.setPositiveButton("Ok", (dialog,id) -> {

        });
        AlertDialog valid = builder2.create();

        builder.setTitle(R.string.add_popup_title);
        builder.setNegativeButton("Cancel", ((dialogInterface, i) -> {
            newDate = null;
            dialogInterface.dismiss();
        }));
        builder.setPositiveButton("Add", (dialog, id) ->{

            String new_item_name = ((EditText)popup.findViewById(R.id.item_add_modal_item_name)).getText().toString();

            ContentValues val = new ContentValues();
            val.put("name", new_item_name);
            val.put("date", inDate.getText().toString());

            ArrayList<String> selected_cats = new ArrayList<>();
            for(IndividualItem.ItemState i: listItems){
                if(i.isSelected()){
                    selected_cats.add(i.getId());
                }
            }

            try(Cursor c = itemListFragment.getContext().getContentResolver().query(CONTENT_URI_ALLTODO, null, null, null, null)){
                if(c.moveToFirst()){
                    do{
                        int nameid = c.getColumnIndex("name");
                        String name = c.getString(nameid);
                        if(new_item_name.equals(name) || validation(new_item_name) || selected_cats.size() == 0){
                            valid.show();
                            builder.create().dismiss();
                            return;
                        }

                    }while(c.moveToNext());
                }
            }

            TodoDB.cat = selected_cats;
            // update Database
            if (context.getContentResolver().insert(CONTENT_URI_TODO, val) != null){
                Toast.makeText(context, "Task Added", Toast.LENGTH_SHORT).show();
                itemListFragment.getActivity().finish();
                itemListFragment.getActivity().overridePendingTransition(0,0);
                itemListFragment.getActivity().startActivity(itemListFragment.getActivity().getIntent());
                itemListFragment.getActivity().overridePendingTransition(0,0);
            }

        });

        IndividualItemAdapter individualItemAdapter = new IndividualItemAdapter(popup.getContext(), 0, listItems);
        spinner.setAdapter(individualItemAdapter);
        builder.setView(popup);
        builder.create().show();
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

    public boolean validation(String str){
        return (!str.matches("[\\w ]+")) || str.length() > 15;
    }

    public static class ItemState {
        private String id;
        private String category;
        private boolean selected;

        public void setId(String id){
            this.id = id;
        }

        public String getId(){
            return this.id;
        }

        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }
        public boolean isSelected() { return selected; }

        public void setSelected(boolean selected) { this.selected = selected; }
    }
}

