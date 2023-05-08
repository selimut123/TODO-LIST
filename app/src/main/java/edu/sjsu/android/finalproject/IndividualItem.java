package edu.sjsu.android.finalproject;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class IndividualItem {
    private final String AUTHORITY = "edu.sjsu.android.finalproject";
    private final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/TODO");
    public void showDialog(int position, Context context) {
        //Alert
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        final View popup = inflater.inflate(R.layout.item_edit_popup, null);

        // Set Name
        /* TODO :
              Save name into Database
          */
        ((TextView)popup.findViewById(R.id.in_name)).setText("");

        // Set Date
        /* TODO :
              Set an intial date
              Save date into Database
          */
        TextView inDate = ((TextView)popup.findViewById(R.id.in_date));
        inDate.setOnClickListener(v -> changeDate(context, inDate));

        // Set Category
        /* TODO :
              Replace temporary with Database
              Save categories into Database
          */
        String[] tempCategories = {
                "Select Category",
                "Category1", "Category2", "Category3", "Category4", "Category5",
                "Category6", "Category7", "Category8", "Category9", "Category10",
        };
        ArrayList<ItemState> listItems = new ArrayList<>();
        for (String s:tempCategories) {
            ItemState iS = new ItemState();
            iS.setCategory(s);
            iS.setSelected(false);
            listItems.add(iS);
        }
        IndividualItemAdapter individualItemAdapter = new IndividualItemAdapter(popup.getContext(), 0, listItems);
        Spinner spinner = (Spinner)popup.findViewById(R.id.in_category);
        spinner.setAdapter(individualItemAdapter);

        // Misc
        builder.setView(popup);
        builder.setPositiveButton("Save", (dialog, id) -> {
            ContentValues val = new ContentValues();
            val.put("name", ((TextView)popup.findViewById(R.id.in_name)).getText().toString());
            val.put("date", inDate.getText().toString());
            ArrayList<String> cat = new ArrayList<>();
            for(ItemState s : listItems){
                if(s.isSelected()){
                    cat.add(s.getCategory());
                }
            }

            TodoDB.cat = cat;
            // update Database
            if (context.getContentResolver().insert(CONTENT_URI, val) != null)
                Toast.makeText(context, "Task Added", Toast.LENGTH_SHORT).show();
        });
        builder.setNegativeButton("Cancel", (dialog, id) -> {
            // When user selects no, do nothing
        });
        builder.create().show();
    }

    private void changeDate(Context context, TextView textView) {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR); // current year
        int mMonth = c.get(Calendar.MONTH); // current month
        int mDay = c.get(Calendar.DAY_OF_MONTH); // current day

        // date picker dialog
        DatePickerDialog dpDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                 textView.setText(i + "/" + (i1 + 1) + "/" + i2);
            }
        }, mYear, mMonth, mDay);
        dpDialog.show();
    }

    public class ItemState {
        private String category;
        private boolean selected;

        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }
        public boolean isSelected() { return selected; }

        public void setSelected(boolean selected) { this.selected = selected; }
    }
}

