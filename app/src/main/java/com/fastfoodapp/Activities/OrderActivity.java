package com.fastfoodapp.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fastfoodapp.Adapters.MyBtnClickListner;
import com.fastfoodapp.Adapters.MySwipeHepler;
import com.fastfoodapp.Adapters.RecyclerOrderAdapter;
import com.fastfoodapp.Models.Food;
import com.fastfoodapp.Models.Order;
import com.fastfoodapp.Models.StaticVariable;
import com.fastfoodapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderActivity extends AppCompatActivity {


    EditText numTable;
    RecyclerView recyclerOrder;
    Button confirmOrder, cancledOrder;
    private Toolbar toolbar;
    TextView title, totalPrice;
    ImageView goback;
    private Order order;
    RecyclerOrderAdapter myadapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        toolbar = findViewById(R.id.main_page_toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setTitle("");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
//        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);//
//        upArrow.setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);
//        getSupportActionBar().setHomeAsUpIndicator(upArrow);//
        title = findViewById(R.id.titleOfActivity);
        goback = findViewById(R.id.goback);
        title.setText("Confirmer l'Ordre");
        recyclerOrder = findViewById(R.id.listOrder);
        numTable = findViewById(R.id.numTable);
        confirmOrder = findViewById(R.id.confOrder);
        cancledOrder = findViewById(R.id.canceledOrder);
        totalPrice = findViewById(R.id.totalOrder);
        this.order = StaticVariable.order;
        cancledOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder adb = new AlertDialog.Builder(OrderActivity.this);
                adb.setTitle("Confirmation");
                adb.setMessage("Vous êtes sûr de annuler cet order ?");
                adb.setNegativeButton("Cancel", null);
                adb.setPositiveButton("Oui",
                        new AlertDialog.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                order.getFoods().clear();
                                newCmd();
                            }
                        }).show();
                return;
            }
        });

        confirmOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (numTable.getText().toString().equals("")) {
                    Toast.makeText(OrderActivity.this, "Entrer le numéro de la table d'abord !", Toast.LENGTH_SHORT).show();
                    return;
                }
                AlertDialog.Builder adb = new AlertDialog.Builder(OrderActivity.this);
                adb.setTitle("Confirmation");
                adb.setMessage("Vous êtes sûr de confirmer cet order ?");
                adb.setNegativeButton("Cancel", null);
                adb.setPositiveButton("Oui",
                        new AlertDialog.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // envoyer cmd
                                if (numTable.getText().toString().equals("")) {
                                    Toast.makeText(OrderActivity.this, "Entrer le numéro de la table d'abord !", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                sendCommand(order);
                            }
                        }).show();
            }
        });
        setupRecyclerOrdersView();
//        recyclerOrder.setOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                myadapter.notifyDataSetChanged();
//            }
//        });
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            recyclerOrder.setLayoutManager(new GridLayoutManager(this, 2));
        }

        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void sendCommand(Order orderCmd) {
        orderCmd.setNumTab(numTable.getText().toString());
        SimpleDateFormat df = new SimpleDateFormat("ddMMyyyyHHmmss");
        Date c = new Date();
        orderCmd.setId(df.format(c) + StaticVariable.username);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String URL = StaticVariable.ipAddress + "insertCommand.php";
        URL = URL.replace(" ", "");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, response -> {
            Log.i("VOLLEY RESPONSE ", "response order : " + response);
            try {
                JSONObject res = new JSONObject(response);
                if (res.getInt("value") == 1) {
                    for (Food food : orderCmd.getFoods())
                        sendDetail(orderCmd, food);
                }
                //order.getFoods().clear();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            error.printStackTrace();
            Toast.makeText(OrderActivity.this, "Error !! Try again ..", Toast.LENGTH_SHORT).show();
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<>();
                parameters.put("id", orderCmd.getId());
                parameters.put("price", orderCmd.getTotal().toString());
                parameters.put("num_tab", orderCmd.getNumTab());
                parameters.put("username", StaticVariable.username);
                return parameters;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void sendDetail(Order cmd, Food food) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String URL = StaticVariable.ipAddress + "insertCommandDetail.php";
        URL = URL.replace(" ", "");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, response -> {
            Log.i("VOLLEY RESPONSE ", "response order : " + response);
            try {
                JSONObject res = new JSONObject(response);
                if (res.getInt("value") == 1) {
                    StaticVariable.count++;
                    if (StaticVariable.count == cmd.getFoods().size()) {
                        Toast.makeText(OrderActivity.this, "Commande envoyé ! ", Toast.LENGTH_SHORT).show();
                        newCmd();
                    }
                }
                //order.getFoods().clear();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            error.printStackTrace();
            Toast.makeText(OrderActivity.this, "Error !! Try again ..", Toast.LENGTH_SHORT).show();
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<>();

                parameters.put("id_bon", cmd.getId());
                parameters.put("name", food.getName());
                parameters.put("quantity", String.valueOf(food.getQuantity()));
                parameters.put("priceUnity", food.getPriceUnity().toString());
                parameters.put("priceTotal", food.getPriceTotal().toString());
                return parameters;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void setupRecyclerOrdersView() {
//        foods.add(new Food(1, "Checken Burger", new Category(), R.drawable.burger, "description", 2, BigDecimal.valueOf(350.00)));
//        foods.add(new Food(1, "Royal Pizza", new Category(), R.drawable.tortilla, "description", 1, BigDecimal.valueOf(850.00)));
//        foods.add(new Food(1, "Lemon juice", new Category(), R.drawable.chawarma, "description", 2, BigDecimal.valueOf(200.00)));
//        foods.add(new Food(1, "Checken Burger", new Category(), R.drawable.burger, "description", 2, BigDecimal.valueOf(350.00)));
//        foods.add(new Food(1, "Royal Pizza", new Category(), R.drawable.tortilla, "description", 1, BigDecimal.valueOf(850.00)));
//        foods.add(new Food(1, "Lemon juice", new Category(), R.drawable.chawarma, "description", 2, BigDecimal.valueOf(200.00)));
        myadapter = new RecyclerOrderAdapter(this, this.order);
        myadapter.setTotal(this.totalPrice);
        myadapter.setActivity(this);
        recyclerOrder.setLayoutManager(new LinearLayoutManager(this));
        recyclerOrder.setAdapter(myadapter);
        myadapter.setRecyclerOrder(recyclerOrder);
        System.out.println("total is : " + this.order.getTotal());
        order.calculateTotal();
        totalPrice.setText(String.valueOf(this.order.getTotal()));
//        ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
//            @Override
//            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
//                return false;
//            }
//
//            @Override
//            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
//                switch (direction) {
//                    case ItemTouchHelper.LEFT:
//                        System.out.println("wooow");
//                        break;
//                    case ItemTouchHelper.RIGHT:
//
//                        break;
//                }
//            }
//        };

//        MySwipeHepler mySwipeHepler = new MySwipeHepler(this, recyclerOrder, 200) {
//            @Override
//            public void instantiateMyBtn(RecyclerView.ViewHolder viewHolder, List<MySwipeHepler.MyBtn> buffer) {
//                buffer.add(new MyBtn(OrderActivity.this, "DELETE",
//                        R.drawable.ic_checklist, R.drawable.item_orange, 30, new MyBtnClickListner() {
//                    //  Color.parseColor("#FF3C30")
//                    @Override
//                    public void onClick(int pos) {
//                        //Toast.makeText(OrderActivity.this, "DELETED ! ", Toast.LENGTH_SHORT).show();
//                        AlertDialog.Builder adb = new AlertDialog.Builder(OrderActivity.this);
//                        adb.setTitle("Confirmation");
//                        adb.setMessage("Vous êtes sûr de supprimer l'objet ");
//                        adb.setNegativeButton("Cancel", null);
//                        adb.setPositiveButton("Oui",
//                                new AlertDialog.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        if (order.getFoods().size() == 1)
//                                            newCmd();
//                                        order.getFoods().remove(pos);
//                                        myadapter.notifyDataSetChanged();
//                                    }
//                                }).show();
//                    }
//                }));
//            }
//        };

//        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
//        itemTouchHelper.attachToRecyclerView(recyclerOrder);
//        new ItemTouchHelper(new ItemTouchHelper.Callback() {
//            @Override
//            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
//                return 0;
//            }
//
//            @Override
//            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
//                System.out.println("Hmmmmmmmm");
//                return false;
//            }
//
//            @Override
//            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
//
//            }
//        }).attachToRecyclerView(recyclerOrder);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //menuInflater.inflate(R.menu.munu_tool_bar, menu);
        getMenuInflater().inflate(R.menu.munu_tool_bar, menu);
        MenuItem item = menu.findItem(R.id.confirmOrder);
        item.setVisible(false);
        item = menu.findItem(R.id.parametres);
        item.setVisible(false);
        item = menu.findItem(R.id.refresh);
        item.setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            recyclerOrder.setLayoutManager(new GridLayoutManager(this, 2));
        }

    }

    private void newCmd() {
        StaticVariable.count = 0;
        StaticVariable.order = new Order();
        StaticVariable.foods = new ArrayList<>();
        StaticVariable.categories = new ArrayList<>();
        Intent intent = new Intent(this, MainActivity.class);
        this.finish();
//        StaticVariable.mainActivity.setupRecyclerCategoriesView();
//        StaticVariable.mainActivity.setupRecyclerFoodsView();
        this.startActivity(intent);
    }
}