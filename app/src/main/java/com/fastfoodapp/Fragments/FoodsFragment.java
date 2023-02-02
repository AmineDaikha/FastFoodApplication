package com.fastfoodapp.Fragments;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.fastfoodapp.Adapters.CategoryImageAdapter;
import com.fastfoodapp.Adapters.FoodImageAdapter;
import com.fastfoodapp.Dialogs.DialogEditImage;
import com.fastfoodapp.Models.Category;
import com.fastfoodapp.Models.Food;
import com.fastfoodapp.Models.StaticVariable;
import com.fastfoodapp.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FoodsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FoodsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    GridView gridView;

    public FoodsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FoodsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FoodsFragment newInstance(String param1, String param2) {
        FoodsFragment fragment = new FoodsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_categoris, container, false);
        gridView = view.findViewById(R.id.listImgCategories);
        ArrayList<Food> foods = StaticVariable.db.getFoods();
        FoodImageAdapter foodImageAdapter = new FoodImageAdapter((Activity) view.getContext(), R.layout.item_category, foods);
//        foods.add(new Food(1, "Checken Burger", new Category(), R.drawable.burger, "Pizza à pâte fine et croustillante richement garnie de tomates, de jambon, de champignons et de fromage, surgelée"));
//        foods.add(new Food(1, "Checken Burger", new Category(), R.drawable.tortilla, "Pizza à pâte fine et croustillante richement garnie de tomates, de jambon, de champignons et de fromage, surgelée"));
//        foods.add(new Food(1, "Checken Burger", new Category(), R.drawable.chawarma, "Pizza à pâte fine et croustillante richement garnie de tomates, de jambon, de champignons et de fromage, surgelée"));
        gridView.setAdapter(foodImageAdapter);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            gridView.setNumColumns(4);
        }
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                DialogEditImage dialogChoose = new DialogEditImage(getContext(), "food", (Activity) view.getContext());
                dialogChoose.setFood(foods.get(position));
                dialogChoose.setFoodImageAdapter(foodImageAdapter);
                dialogChoose.show();
                return false;
            }
        });
        return view;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            gridView.setNumColumns(4);
        }
    }
}