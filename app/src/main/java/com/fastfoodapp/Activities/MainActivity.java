package com.fastfoodapp.Activities;

import static com.fastfoodapp.Models.StaticVariable.url_SERVER;
import static com.fastfoodapp.Models.StaticVariable.ipAddress;
import static com.fastfoodapp.Models.StaticVariable.username;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.fastfoodapp.Adapters.RecyclerCategoriesAdapter;
import com.fastfoodapp.Adapters.RecyclerFoodAdapter;
import com.fastfoodapp.DBLocal.DatabaseHalper;
import com.fastfoodapp.Models.Category;
import com.fastfoodapp.Models.Food;
import com.fastfoodapp.Models.Order;
import com.fastfoodapp.Models.StaticVariable;
import com.fastfoodapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;


public class MainActivity extends AppCompatActivity {


    RecyclerView recyclerCategory;
    RecyclerView recyclerFood;
    private Toolbar toolbar;
    private Order order;
    RecyclerFoodAdapter foodAdapter;
    RecyclerCategoriesAdapter categoriesAdapter;

    ArrayList<Category> categories = new ArrayList<>();
    ArrayList<Food> foods = new ArrayList<>();
    ArrayList<Category> categoriesAll = new ArrayList<>();
    ArrayList<Food> foodsAll = new ArrayList<>();
    TextView title;
    ImageView goback, refresh, logout, settings, commands;
    SharedPreferences mPrefs;
    DatabaseHalper db = new DatabaseHalper(this);

    private JsonArrayRequest request;
    private RequestQueue requestQueue;
    private static int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        recyclerCategory = findViewById(R.id.listCategories);
        recyclerFood = findViewById(R.id.listFood);
//        toolbar = findViewById(R.id.main_page_toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setTitle("");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        title = findViewById(R.id.titleOfActivity);
        goback = findViewById(R.id.goback);
        logout = findViewById(R.id.logout);
        refresh = findViewById(R.id.refresh);
        settings = findViewById(R.id.settings);
        commands = findViewById(R.id.commands);
        goback.setVisibility(View.GONE);
        title.setText("Catégories et Plats");
        mPrefs = this.getSharedPreferences("IP_S", 0);
        url_SERVER = mPrefs.getString("tag", "default_value_if_variable_not_found");
        username = mPrefs.getString("user", "default_value_if_variable_not_found");
        System.out.println("url_SERVER " + url_SERVER);
        url_SERVER = url_SERVER.replace(" ", "");
        ipAddress = "http://" + url_SERVER + "/pizzeria/";
        //Toast.makeText(this, ipAddress, Toast.LENGTH_LONG).show();
        System.out.println("ipAddress : " + ipAddress);
        StaticVariable.db = db;
        StaticVariable.mainActivity = this;
        this.order = StaticVariable.order;
        System.out.println("size orders : " + order.getFoods().size());
//        recyclerCategory.setLayoutManager(layoutManager);
        //recyclerFood.setLayoutManager(layoutManager);
//        recyclerCategory.setNestedScrollingEnabled(false);
//        recyclerFood.setNestedScrollingEnabled(false);
        System.out.println("size StaticVariable.categories : " + StaticVariable.categories.size());
        if (StaticVariable.categories.size() == 0) {
            categories = db.getCategories();
            categories.add(new Category(1, "Burger", R.drawable.ic_burger));
            categories.add(new Category(1, "Burger", R.drawable.ic_pizza));
            categories.add(new Category(1, "Burger", R.drawable.ic_drink));
            StaticVariable.categories = categories;
        } else
            categories = StaticVariable.categories;

        if (categories.size() != 0)
            categories.get(StaticVariable.select).setSelected(true);

        if (StaticVariable.order.getFoods().size() == 0) {
            if (categories.size() != 0)
                foods = db.getFoods(categories.get(0));
//            foods.add(new Food(1, "Checken Burger", categories.get(0), R.drawable.burger, "Pizza à pâte fine et croustillante richement garnie de tomates, de jambon, de champignons et de fromage, surgelée"));
//            foods.add(new Food(1, "Checken Burger", categories.get(0), R.drawable.tortilla, "Pizza à pâte fine et croustillante richement garnie de tomates, de jambon, de champignons et de fromage, surgelée"));
//            foods.add(new Food(1, "Checken Burger", categories.get(0), R.drawable.chawarma, "Pizza à pâte fine et croustillante richement garnie de tomates, de jambon, de champignons et de fromage, surgelée"));
            StaticVariable.foods = foods;
        } else
            foods = StaticVariable.foods;
        setupRecyclerCategoriesView();
        setupRecyclerFoodsView();
        DividerItemDecoration decoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerCategory.addItemDecoration(decoration);
        recyclerFood.addItemDecoration(decoration);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerCategory);

        ItemTouchHelper itemTouchHelper2 = new ItemTouchHelper(callback2);
        itemTouchHelper2.attachToRecyclerView(recyclerFood);

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count = 0;
                StaticVariable.db.updateCategories();
                StaticVariable.db.updateFoods();
                AlertDialog.Builder adb = new AlertDialog.Builder(MainActivity.this);
                adb.setTitle("Confirmation");
                adb.setMessage("Vous êtes sûr de adapter les listes des catégoris et des produits");
                adb.setNegativeButton("Cancel", null);
                adb.setPositiveButton("Oui",
                        new AlertDialog.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                getCategories();
                                //getCategoriesTest();
//                                try {
//                                    run();
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
                            }
                        }).show();
            }
        });

        commands.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MainActivity.this.order.getFoods().size() == 0) {
                    Toast.makeText(MainActivity.this, "Ajouter des produits d'abord !", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(MainActivity.this, OrderActivity.class);
                    startActivity(intent);
                }
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder adb = new AlertDialog.Builder(MainActivity.this);
                adb.setTitle("Confirmation");
                adb.setMessage("Vous êtes sûr de déconnecter depuis votre compte ?");
                adb.setNegativeButton("Cancel", null);
                adb.setPositiveButton("Oui",
                        new AlertDialog.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                                mPrefs.edit().clear().commit();
                                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                            }
                        }).show();
            }
        });
    }

    ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP |
            ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END, 0) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            int fromPosotion = viewHolder.getAdapterPosition();
            int toPosition = target.getAdapterPosition();
            Collections.swap(categories, fromPosotion, toPosition);
            recyclerCategory.getAdapter().notifyItemMoved(fromPosotion, toPosition);
            // update seq
            for (int i = 0; i < categories.size(); i++) {
                System.out.println("moving !!!");
                categories.get(i).setSeq(i);
                db.updateCategory(categories.get(i));
            }
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
//                switch (direction) {
//                    case ItemTouchHelper.LEFT:
//                        System.out.println("wooow");
//                        break;
//                    case ItemTouchHelper.RIGHT:
//
//                        break;
//                }
        }
    };

    ItemTouchHelper.SimpleCallback callback2 = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP |
            ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END, 0) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            int fromPosotion = viewHolder.getAdapterPosition();
            int toPosition = target.getAdapterPosition();
            Collections.swap(foods, fromPosotion, toPosition);
            recyclerFood.getAdapter().notifyItemMoved(fromPosotion, toPosition);
            for (int i = 0; i < foods.size(); i++) {
                foods.get(i).setSeq(i);
                db.updateFood(foods.get(i));
            }
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
//                switch (direction) {
//                    case ItemTouchHelper.LEFT:
//                        System.out.println("wooow");
//                        break;
//                    case ItemTouchHelper.RIGHT:
//
//                        break;
//                }
        }
    };

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setupRecyclerCategoriesView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.munu_tool_bar, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.confirmOrder:
                if (this.order.getFoods().size() == 0) {
                    Toast.makeText(this, "Ajouter des produits d'abord !", Toast.LENGTH_SHORT).show();
                    break;
                }
                Intent intent = new Intent(MainActivity.this, OrderActivity.class);
                startActivity(intent);
                break;

            case R.id.parametres:
                finish();
                intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
                break;

            case R.id.refresh:
                count = 0;
                StaticVariable.db.updateCategories();
                StaticVariable.db.updateFoods();
                AlertDialog.Builder adb = new AlertDialog.Builder(this);
                adb.setTitle("Confirmation");
                adb.setMessage("Vous êtes sûr de adapter les listes des catégoris et des produits");
                adb.setNegativeButton("Cancel", null);
                adb.setPositiveButton("Oui",
                        new AlertDialog.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                getCategories();
                                //getCategoriesTest();
//                                try {
//                                    run();
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
                            }
                        }).show();

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private final OkHttpClient client = new OkHttpClient();

    public void run() throws Exception {
        String JSON_URL = "http://192.168.1.106:80/pizzeria/getCategories.php";
        Request request = new Request.Builder()
                .url(JSON_URL)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull okhttp3.Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful())
                        throw new IOException("Unexpected code " + response);

                    Headers responseHeaders = response.headers();
                    for (int i = 0, size = responseHeaders.size(); i < size; i++) {
                        System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
                    }

                    System.out.println("response httpok" + responseBody.string());
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void getCategoriesTest() {


        OkHttpClient client = new OkHttpClient();
        String JSON_URL = ipAddress + "getCategories.php";
        Request request = new Request.Builder()
                .url(JSON_URL)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull okhttp3.Response response) throws IOException {
                if (response.isSuccessful()) {
                    String myResp = response.body().string();
                    System.out.println("finalResp : " + myResp);
                }
            }
        });

//        try (okhttp3.Response response = client.newCall(request).execute()) {
//            return response.body().string();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        return;
    }


    private void getCategories() {
        //String JSON_URL = "http://192.168.0.120/resto/getCategories.php";
        String JSON_URL = ipAddress + "getCategories.php";
        JSON_URL = JSON_URL.replace(" ", "");
        System.out.println("catt : " + JSON_URL);
        request = new JsonArrayRequest(JSON_URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                System.out.println("REsponse categories : " + response);
                categories.clear();
                StaticVariable.categories.clear();
                //ArrayList<Category> categories = new ArrayList<>();
                //StaticVariable.db.updateCategories();
                JSONObject jsonObject = null;
                if (response.length() == 0) {
                    AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                    alertDialog.setTitle("Alert");
                    alertDialog.setMessage("Pas de données !");
                    alertDialog.setIcon(android.R.drawable.ic_dialog_info);
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    return;
                                }
                            });
                    alertDialog.show();
                } else {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            jsonObject = response.getJSONObject(i);
                            Category category = new Category();
                            category.setId(jsonObject.getInt("id"));
                            category.setName(jsonObject.getString("name"));
                            categories.add(category);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
//                    for (Category category : categoriesDB)
//                        StaticVariable.db.deleteCategory(category);

                    for (int i = 0; i < categories.size(); i++) {
                        categories.get(i).setSeq(i);
                        categories.get(i).setImage(i % StaticVariable.imgCategories.length);
                        //categories.get(i).setImage(0);
                        System.out.println("insert? " + StaticVariable.db.insertCategory(categories.get(i)));
                    }
                    StaticVariable.db.getCategories();

                    for (Category category : categories)
                        getFoods(category);
//                    getFoods();
//                    System.out.println("size for delete : " + StaticVariable.db.getFoodsForDelete().size());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("errrrrrrrrrrrrrr : " + error.getMessage());
                Log.d("Volley", "Error StackTrace:" + error.getStackTrace());
                Toast.makeText(MainActivity.this, "Pas de connexion " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(request);

    }

    private void getFoods() {
        String JSON_URL = ipAddress + "getFoods.php";
        JSON_URL = JSON_URL.replace(" ", "");
        request = new JsonArrayRequest(JSON_URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                foods.clear();
                System.out.println("REsponse food : " + response);
                //StaticVariable.db.updateFoods();
                ArrayList<Food> foodsBD = new ArrayList<>();
                ArrayList<Food> foodsDBLocal = StaticVariable.db.getFoods();
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        Food food = new Food();
                        food.setId(jsonObject.getInt("id"));
                        food.setName(jsonObject.getString("name"));
                        food.setDescription(jsonObject.getString("name"));
                        food.setPriceUnity(new BigDecimal(jsonObject.getString("price")));
                        food.setCategory(StaticVariable.db.getOneCategory(jsonObject.getString("id_category")));
                        foods.add(food);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                for (Food food : foodsDBLocal)
                    StaticVariable.db.deleteFood(food);
                for (int i = 0; i < foods.size(); i++) {
                    foods.get(i).setSeq(i);
                    foods.get(i).setImg(i % StaticVariable.imgFoods.length);
                    StaticVariable.db.insertFood(foods.get(i));
                }

                setupRecyclerFoodsView();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("errrrrrrrrrrrrrr");
                Toast.makeText(MainActivity.this, "Pas de connexion", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(request);
        categoriesAdapter.notifyDataSetChanged();
        foodAdapter.notifyDataSetChanged();
    }

    private void getFoods(Category category) {
        String JSON_URL = ipAddress + "getFoods.php?id=" + category.getName();
        JSON_URL = JSON_URL.replace(" ", "");
        request = new JsonArrayRequest(JSON_URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                foods.clear();
                System.out.println("REsponse food : " + response);
                //StaticVariable.db.updateFoods();
                ArrayList<Food> foodsBD = new ArrayList<>();
                ArrayList<Food> foodsDBLocal = StaticVariable.db.getFoods(category);
                JSONObject jsonObject = null;
                count++;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        Food food = new Food();
                        food.setId(jsonObject.getInt("id"));
                        food.setName(jsonObject.getString("name"));
                        food.setDescription(jsonObject.getString("name"));
                        food.setPriceUnity(new BigDecimal(jsonObject.getString("price")));
                        food.setCategory(StaticVariable.db.getOneCategory(jsonObject.getString("id_category")));
                        foods.add(food);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
//                for (Food food : foodsDBLocal)
//                    StaticVariable.db.deleteFood(food);
                for (int i = 0; i < foods.size(); i++) {
                    foods.get(i).setSeq(i);
                    foods.get(i).setImg(i % StaticVariable.imgFoods.length);
                    StaticVariable.db.insertFood(foods.get(i));
                }
//                StaticVariable.select = 0;
//                categories = StaticVariable.db.getCategories();
//                foods = StaticVariable.db.getFoods(categories.get(StaticVariable.select));
//                setupRecyclerCategoriesView();
//                setupRecyclerFoodsView();

                if (categories.size() == count) {
                    categories.clear();
                    StaticVariable.categories.clear();
                    refresh();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("errrrrrrrrrrrrrr");
                Toast.makeText(MainActivity.this, "Pas de connexion", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(request);
        categoriesAdapter.notifyDataSetChanged();
        foodAdapter.notifyDataSetChanged();
    }

    public void setupRecyclerFoodsView() {

        foodAdapter = new RecyclerFoodAdapter(this, foods, "", this.order);
        //recyclerFood.setLayoutManager(new LinearLayoutManager(this));
        recyclerFood.setLayoutManager(new LinearLayoutManager(this));
        recyclerFood.setAdapter(foodAdapter);
        categoriesAdapter.setRecyclerFoodAdapter(foodAdapter);
//        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
//            //Do some stuff
//            recyclerFood.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));
//        }
//        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            //Do some stuff
//
//        }
        recyclerFood.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));
    }

    public void setupRecyclerCategoriesView() {
        categoriesAdapter = new RecyclerCategoriesAdapter(this, categories, "");
        recyclerCategory.setLayoutManager(new LinearLayoutManager(this));
        recyclerCategory.setAdapter(categoriesAdapter);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerCategory.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));
        }

    }

    private void refresh() {
        Intent intent = new Intent(this, MainActivity.class);
        this.finish();
        this.startActivity(intent);
    }
}