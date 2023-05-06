package edu.sjsu.android.finalproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.ViewHolder> {

    private final List<String> tasks;
    ItemListFragment itemListFragment;

    public ItemListAdapter(List<String> tasks, ItemListFragment itemListFragment) {
        this.tasks = tasks;
        this.itemListFragment = itemListFragment;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_row, parent, false);

        return new ViewHolder(view, itemListFragment);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        /* TODO :
            get this data from database
         */
        String item = tasks.get(position);
        holder.task.setText(item);
        holder.date.setText("April" + position);
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final CheckBox done;
        public final TextView task;
        public final TextView date;

        public ViewHolder(View binding, ItemListFragment itemListFragment) {
            super(binding);
            done = (CheckBox) binding.findViewById(R.id.done);
            task = (TextView) binding.findViewById(R.id.task);
            date = (TextView) binding.findViewById(R.id.date);
            binding.setOnClickListener(v ->   {
                itemListFragment.onClick(this.getAdapterPosition());
            });
        }
    }
}