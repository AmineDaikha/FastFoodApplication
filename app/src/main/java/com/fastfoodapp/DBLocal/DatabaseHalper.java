package com.fastfoodapp.DBLocal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.fastfoodapp.Models.Category;
import com.fastfoodapp.Models.Food;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Amine Daikha on 13/04/2019.
 */

public class DatabaseHalper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "resto.db";
    public static final String CATEGORY = "gategory";
    public static final String CATEGORY_Col_1 = "ID";
    public static final String CATEGORY_Col_2 = "name";
    public static final String CATEGORY_Col_3 = "image";
    public static final String CATEGORY_Col_4 = "seq";

    public static final String FOOD = "food";
    public static final String FOOD_Col1 = "id";
    public static final String FOOD_Col2 = "name";
    public static final String FOOD_Col3 = "description";
    public static final String FOOD_Col4 = "image";
    public static final String FOOD_Col5 = "seq";
    public static final String FOOD_Col6 = "id_category";


    public DatabaseHalper(Context context) {
        //creat database
        super(context, DATABASE_NAME, null, 10);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //creat tables
        //db.execSQL("PRAGMA foreign_keys=ON");

        db.execSQL("create table " + CATEGORY + " (ID INTEGER PRIMARY KEY, " +
                "name TEXT, " +
                "image INTEGER, " +
                "seq INTEGER)");

        db.execSQL("create table " + FOOD + " (ID INTEGER PRIMARY KEY, " +
                "name TEXT, " +
                "description TEXT, " +
                "image INTEGER, " +
                "seq INTEGER," +
                "id_category TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + CATEGORY);
        db.execSQL("DROP TABLE IF EXISTS " + FOOD);
        onCreate(db);
    }

    public void updateCategories() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + CATEGORY);
        db.execSQL("create table " + CATEGORY + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, " +
                "image INTEGER, " +
                "seq INTEGER)");
    }

    public void updateFoods() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + FOOD);
        db.execSQL("create table " + FOOD + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, " +
                "description TEXT, " +
                "image INTEGER, " +
                "seq INTEGER," +
                "id_category TEXT)");
    }

    //insert category

    public boolean insertCategory(Category category) {
        System.out.println("insert image of : " + category.getImage());
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CATEGORY_Col_1, category.getId());
        contentValues.put(CATEGORY_Col_2, category.getName());
        contentValues.put(CATEGORY_Col_3, category.getImage());
        //contentValues.put(CATEGORY_Col_3, "1");
        contentValues.put(CATEGORY_Col_4, category.getSeq());
        long result = db.insert(CATEGORY, null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }

    //update category
    public boolean updateCategory(Category category) {
        System.out.println("update image of : " + category.getImage() + " id : " + category.getId());
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CATEGORY_Col_3, category.getImage());
        contentValues.put(CATEGORY_Col_4, category.getSeq());
        int i = db.update(CATEGORY, contentValues, "id = ?", new String[]{String.valueOf(category.getId())});
        System.out.println("val i : " + i);
        return true;
    }

    //view data
    public Cursor getAllCategories() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("select * from " + CATEGORY, null);
        return result;
    }

    public ArrayList<Category> getCategories() {
        ArrayList<Category> categories = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("select * from " + CATEGORY + " ORDER BY seq", null);
        while (result.moveToNext()) {
            Category category = new Category();
            category.setId(result.getInt(0));
            category.setName(result.getString(1));
            category.setImage(result.getInt(2));
            category.setSeq(result.getInt(3));
            System.out.println("get image of : " + category.getImage() + " id : " + category.getId() + " img : " + result.getInt(2));
            categories.add(category);
        }
        return categories;
    }


    public Category getOneCategory(String ID) {
        Category category = new Category();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("select * from " + CATEGORY + " where name = '" + ID + "'", null);
        while (result.moveToNext()) {
            category.setId(result.getInt(0));
            category.setName(result.getString(1));
            category.setImage(result.getInt(2));
            category.setSeq(result.getInt(3));
        }
        return category;
    }

    // delete category
    public int deleteCategory(Category category) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(CATEGORY, "id =?", new String[]{String.valueOf(category.getId())});
    }

    // delete food
    public int deleteFood(Food category) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(FOOD, "id =?", new String[]{String.valueOf(category.getId())});
    }

    // table note

    //insert Food
    public boolean insertFood(Food food) {
        System.out.println("image of food : " + food.getImg());
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(FOOD_Col1, food.getId());
        contentValues.put(FOOD_Col2, food.getName());
        contentValues.put(FOOD_Col3, food.getDescription());
        contentValues.put(FOOD_Col4, food.getImg());
        contentValues.put(FOOD_Col5, food.getSeq());
        contentValues.put(FOOD_Col6, food.getCategory().getName());
        long result = db.insert(FOOD, null, contentValues);
        //System.out.println("result insert : " + result);
        if (result == -1)
            return false;
        else
            return true;
    }

    // get foods
    public ArrayList<Food> getFoods(Category category) {
        ArrayList<Food> foods = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("select * from " + FOOD + " where " + FOOD_Col6 + " = '" + category.getName() + "' ORDER BY seq", null);
        while (result.moveToNext()) {
            Food food = new Food();
            food.setId(result.getInt(0));
            food.setName(result.getString(1));
            food.setDescription(result.getString(2));
            food.setImg(result.getInt(3));
            food.setSeq(result.getInt(4));
            food.setCategory(getOneCategory(result.getString(1)));
            //System.out.println("immmmm : " + food.getImg());
            foods.add(food);
        }
        System.out.println("size foodList db : " + foods.size());
        return foods;
    }

    public ArrayList<Food> getFoods() {
        ArrayList<Food> notes = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("select * from " + FOOD, null);
        while (result.moveToNext()) {
            Food food = new Food();
            food.setId(result.getInt(0));
            food.setName(result.getString(1));
            food.setDescription(result.getString(2));
            food.setImg(result.getInt(3));
            food.setSeq(result.getInt(4));
            food.setCategory(getOneCategory(result.getString(1)));
            notes.add(food);
        }
        return notes;
    }

    public ArrayList<Food> getFoodsForDelete() {
        ArrayList<Food> notes = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("select * from " + FOOD + " where id_category NOT IN " +
                "(select name from gategory)", null);
        while (result.moveToNext()) {
            Food food = new Food();
            food.setId(result.getInt(0));
            food.setName(result.getString(1));
            food.setDescription(result.getString(2));
            food.setImg(result.getInt(3));
            food.setSeq(result.getInt(4));
            food.setCategory(getOneCategory(result.getString(1)));
            notes.add(food);
        }
        return notes;
    }

    //update food
    public boolean updateFood(Food food) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(FOOD_Col4, food.getImg());
        contentValues.put(FOOD_Col5, food.getSeq());
        db.update(FOOD, contentValues, "id = ?", new String[]{String.valueOf(food.getId())});
        return true;
    }

    private String setRegulerTime(String time) {
        String bref[] = time.split(":");
        return bref[0] + ":" + bref[1];
    }
}
