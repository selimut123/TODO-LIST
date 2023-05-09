package edu.sjsu.android.finalproject;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.Calendar;

public class IndividualItem {
    private final String AUTHORITY = "edu.sjsu.android.finalproject";
    private final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/TODO");
    public void showDialog(int position, Context context, ArrayList<TodoItem> tasks, ItemListFragment itemListFragment) {
        //Alert
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        final View popup = inflater.inflate(R.layout.item_edit_popup, null);

        TodoItem task = tasks.get(position);
        // Set Name
        /* TODO :
              Save name into Database
          */
        ((TextView)popup.findViewById(R.id.in_name)).setText(task.getName());

        // Set Date
        /* TODO :
              Set an intial date
              Save date into Database
          */
        TextView inDate = ((TextView)popup.findViewById(R.id.in_date));
        inDate.setText(task.getDate());
        inDate.setOnClickListener(v -> changeDate(context, inDate));

        // Set Category
        /* TODO :
              Replace temporary with Database
              Save categories into Database
          */
//        String[] tempCategories = {
//                "Select Category",
//                "Category1", "Category2", "Category3", "Category4", "Category5",
//                "Category6", "Category7", "Category8", "Category9", "Category10",
//        };
//        ArrayList<ItemState> listItems = new ArrayList<>();
//        for (String s:tempCategories) {
//            ItemState iS = new ItemState();
//            iS.setCategory(s);
//            iS.setSelected(false);
//            listItems.add(iS);
//        }
//        IndividualItemAdapter individualItemAdapter = new IndividualItemAdapter(popup.getContext(), 0, listItems);
//        Spinner spinner = (Spinner)popup.findViewById(R.id.in_category);
//        spinner.setAdapter(individualItemAdapter);

        // Misc
        builder.setView(popup);
        builder.setPositiveButton("Save", (dialog, id) -> {
            ContentValues val = new ContentValues();
            val.put("name", ((TextView)popup.findViewById(R.id.in_name)).getText().toString());
            val.put("date", inDate.getText().toString());

            if (context.getContentResolver().update(CONTENT_URI, val, task.getId(), null) > 0){
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

