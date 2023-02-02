package com.fastfoodapp.Models;

import com.fastfoodapp.Activities.MainActivity;
import com.fastfoodapp.DBLocal.DatabaseHalper;
import com.fastfoodapp.R;

import java.util.ArrayList;

public class StaticVariable {

    public static Order order = new Order();
    public static String url_SERVER;
    public static String username;
    public static String ipAddress;
    public static ArrayList<Food> foods = new ArrayList<>();
    public static ArrayList<Category> categories = new ArrayList<>();
    public static DatabaseHalper db;
    public static MainActivity mainActivity;
    public static int select = 0;
    public static int count = 0;
    public static int[] imgFoods = {R.drawable.burger2, R.drawable.burger, R.drawable.burger3, R.drawable.burger4,
            R.drawable.takos, R.drawable.takos2, R.drawable.takos3, R.drawable.takos4,
            R.drawable.pizza, R.drawable.pizza2, R.drawable.pizza3, R.drawable.pizza4, R.drawable.pizza5, R.drawable.pizza6,
            R.drawable.plat, R.drawable.plat2, R.drawable.plat3, R.drawable.plat4, R.drawable.plat5, R.drawable.plat6, R.drawable.plat7, R.drawable.plat8,
            R.drawable.dessert, R.drawable.dessert2, R.drawable.dessert3, R.drawable.dessert4,
            R.drawable.drink, R.drawable.drink2};
    public static int[] imgCategories = {R.drawable.ic_burger, R.drawable.ic_burger2, R.drawable.ic_burger3, R.drawable.ic_burger4,
            R.drawable.ic_dessert, R.drawable.ic_dessert2, R.drawable.ic_drink, R.drawable.ic_drink2, R.drawable.ic_drink3,
            R.drawable.ic_pizza, R.drawable.ic_pizza2, R.drawable.ic_pizza3,
            R.drawable.ic_plat, R.drawable.ic_plat2};
}
