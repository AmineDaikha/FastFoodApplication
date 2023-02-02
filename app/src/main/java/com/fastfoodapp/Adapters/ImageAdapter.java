package com.fastfoodapp.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.fastfoodapp.Models.Category;
import com.fastfoodapp.Models.StaticVariable;
import com.fastfoodapp.R;

import java.util.List;

public class ImageAdapter extends ArrayAdapter<Category> {

    private List<Category> items;
    Activity activity;
    private int itemResourceId;
    TextView title;
    ImageView img;
    private String type;

    public ImageAdapter(Activity activity, int itemResourceId, List<Category> items, String type) {
        super(activity, itemResourceId, items);
        this.items = items;
        this.activity = activity;
        this.itemResourceId = itemResourceId;
        this.type = type;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = activity.getLayoutInflater();
            convertView = inflater.inflate(itemResourceId, parent, false);
            convertView.setTag(this);
        }
        Category category = items.get(position);
        title = convertView.findViewById(R.id.titleCategory);
        title.setVisibility(View.GONE);
        img = convertView.findViewById(R.id.imgCategory);
        title.setText(category.getName());
        // type
        if (this.type.equals("category"))
            img.setImageResource(StaticVariable.imgCategories[position]);
        else
            img.setImageResource(StaticVariable.imgFoods[position]);

        return convertView;
    }

    @Override
    public int getViewTypeCount() {
        return getCount();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
