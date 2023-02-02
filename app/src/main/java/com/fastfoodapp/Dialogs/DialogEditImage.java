package com.fastfoodapp.Dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.fastfoodapp.Adapters.CategoryImageAdapter;
import com.fastfoodapp.Adapters.FoodImageAdapter;
import com.fastfoodapp.Adapters.ImageAdapter;
import com.fastfoodapp.Models.Category;
import com.fastfoodapp.Models.Food;
import com.fastfoodapp.Models.StaticVariable;
import com.fastfoodapp.R;

import java.util.ArrayList;

public class DialogEditImage extends Dialog {

    private Activity activity;
    private String type;
    private ArrayList<Category> categories = new ArrayList<>();
    private Category category;
    private Food food;
    private CategoryImageAdapter categoryImageAdapter;
    private FoodImageAdapter foodImageAdapter;


    public DialogEditImage(@NonNull Context context, String type, Activity activity) {
        super(context);
        this.type = type;
        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_change_image);
        GridView gridView = findViewById(R.id.listImg);
        ImageAdapter imageAdapter;

        if (this.type.equals("food"))
            for (int img = 0; img < StaticVariable.imgFoods.length; img++)
                categories.add(new Category(img));
        else if (this.type.equals("category"))
            for (int img = 0; img < StaticVariable.imgCategories.length; img++)
                categories.add(new Category(img));
        //        if (this.type.equals("food"))
//            for (int img : StaticVariable.imgFoods)
//                categories.add(new Category(img));
//        else if (this.type.equals("category"))
//            for (int img : StaticVariable.imgCategories)
//                categories.add(new Category(img));
        imageAdapter = new ImageAdapter(this.activity, R.layout.item_category, categories, this.type);
        gridView.setAdapter(imageAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (type.equals("food")) {
                    System.out.println("my img : " + categories.get(position).getImage());
                    food.setImg(categories.get(position).getImage());
                    //StaticVariable.imgFoods[food.getImg()]
                    StaticVariable.db.updateFood(food);
                    foodImageAdapter.notifyDataSetChanged();
                } else if (type.equals("category")) {
                    category.setImage(categories.get(position).getImage());
                    System.out.println("update : " + StaticVariable.db.updateCategory(category));
                    categoryImageAdapter.notifyDataSetChanged();
                }
                dismiss();
            }
        });

        if (this.activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            gridView.setNumColumns(4);
        }
    }


    void refresh() {
        Intent intent = activity.getIntent();
        activity.finish();
        activity.startActivity(intent);
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Food getFood() {
        return food;
    }

    public void setFood(Food food) {
        this.food = food;
    }

    public CategoryImageAdapter getCategoryImageAdapter() {
        return categoryImageAdapter;
    }

    public void setCategoryImageAdapter(CategoryImageAdapter categoryImageAdapter) {
        this.categoryImageAdapter = categoryImageAdapter;
    }

    public FoodImageAdapter getFoodImageAdapter() {
        return foodImageAdapter;
    }

    public void setFoodImageAdapter(FoodImageAdapter foodImageAdapter) {
        this.foodImageAdapter = foodImageAdapter;
    }
}
