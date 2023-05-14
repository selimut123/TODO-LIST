package edu.sjsu.android.finalproject;

import android.graphics.PorterDuff;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;

public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.ViewHolder> {
    private final List<CategoryItem> categories;
    CategoryListFragment categoryListFragment;

    public CategoryListAdapter(List<CategoryItem> categories, CategoryListFragment categoryListFragment) {
        this.categories = categories;
        this.categoryListFragment = categoryListFragment;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_row, parent, false);
        return new ViewHolder(view, categoryListFragment);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        // TODO : get this data from database
        CategoryItem item = categories.get(position);
        holder.image.setBackgroundResource(item.getBackgroundImageID());   // From Database
        Log.d("id1", item.getImageID() + "");
        Log.d("id2", R.drawable.ic_home_foreground + "");
        holder.image.setImageDrawable(categoryListFragment.getContext().getDrawable(item.getImageID())); // From Database
//        holder.image.setColorFilter(categoryListFragment.getContext().getColor(item.getColorID()), PorterDuff.Mode.SRC_IN);
        holder.name.setText(item.getName());
        holder.tasks.setText(item.getLength() + " tasks");
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final ShapeableImageView image;
        public final TextView name;
        public final TextView tasks;

        public ViewHolder(View binding, CategoryListFragment categoryListFragment) {
            super(binding);
            image = (ShapeableImageView) binding.findViewById(R.id.image);
            name = (TextView) binding.findViewById(R.id.category);
            tasks = (TextView) binding.findViewById(R.id.tasks);
            binding.setOnClickListener(v ->   {
                categoryListFragment.onClick(this.getAdapterPosition());
            });
            binding.setOnLongClickListener(v -> {
                categoryListFragment.onHold(this.getAdapterPosition());
                return true;
            });
        }
    }
}