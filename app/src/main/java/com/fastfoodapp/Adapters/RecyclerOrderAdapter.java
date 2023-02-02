package com.fastfoodapp.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.fastfoodapp.Activities.MainActivity;
import com.fastfoodapp.Activities.OrderActivity;
import com.fastfoodapp.Models.Food;
import com.fastfoodapp.Models.Order;
import com.fastfoodapp.R;

import java.util.List;

/**
 * Created by Aws on 11/03/2018.
 */

public class RecyclerOrderAdapter extends RecyclerView.Adapter<RecyclerOrderAdapter.MyViewHolder> {

    private Context mContext;
    private List<Food> foodList;
    //RequestOptions option;
    private Order order;
    private Activity activity;
    private TextView total;
    RecyclerView recyclerOrder;


    public RecyclerOrderAdapter(Context mContext, Order order) {
        this.mContext = mContext;
        // Request option for Glide
        //option = new RequestOptions().centerCrop().placeholder(R.drawable.loading_shape).error(R.drawable.loading_shape);
        this.order = order;
        this.foodList = this.order.getFoods();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {

        View view;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.item_order, parent, false);
        final MyViewHolder viewHolder = new MyViewHolder(view);
        viewHolder.plusQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                foodList.get(viewHolder.getAdapterPosition()).setQuantity(foodList.get(viewHolder.getAdapterPosition()).getQuantity() + 1);
                foodList.get(viewHolder.getAdapterPosition()).calculatePriceTotal();
                order.calculateTotal();
                total.setText(order.getTotal().toString());
                viewHolder.quantity.setText(String.valueOf(foodList.get(viewHolder.getAdapterPosition()).getQuantity()));
                viewHolder.totalPrice.setText(foodList.get(viewHolder.getAdapterPosition()).getPriceTotal().toString());
            }
        });
        viewHolder.minusQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (foodList.get(viewHolder.getAdapterPosition()).getQuantity() > 0) {
                    if (foodList.get(viewHolder.getAdapterPosition()).getQuantity() == 1) {
                        if (foodList.size() == 1) {
                            AlertDialog.Builder adb = new AlertDialog.Builder(mContext);
                            adb.setTitle("Confirmation");
                            adb.setMessage("Vous êtes sûr de annuler cet order ?");
                            adb.setNegativeButton("Cancel", null);
                            adb.setPositiveButton("Oui",
                                    new AlertDialog.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            foodList.clear();
                                            newCmd();
                                        }
                                    }).show();
                            return;
                        }
                        // confirmation
                        AlertDialog.Builder adb = new AlertDialog.Builder(mContext);
                        adb.setTitle("Confirmation");
                        adb.setMessage("Vous êtes sûr de supprimer l'objet ");
                        adb.setNegativeButton("Cancel", null);
                        adb.setPositiveButton("Oui",
                                new AlertDialog.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        foodList.remove(viewHolder.getAdapterPosition());
                                        RecyclerOrderAdapter.this.notifyDataSetChanged();
                                    }
                                }).show();
                        return;
                    }
                    foodList.get(viewHolder.getAdapterPosition()).setQuantity(foodList.get(viewHolder.getAdapterPosition()).getQuantity() - 1);
                    foodList.get(viewHolder.getAdapterPosition()).calculatePriceTotal();
                    order.calculateTotal();
                    total.setText(order.getTotal().toString());
                    viewHolder.quantity.setText(String.valueOf(foodList.get(viewHolder.getAdapterPosition()).getQuantity()));
                    viewHolder.totalPrice.setText(foodList.get(viewHolder.getAdapterPosition()).getPriceTotal().toString());
                }
            }
        });

        return viewHolder;
    }

    private void newCmd() {
        Intent intent = new Intent(activity, MainActivity.class);
        this.activity.finish();
        this.activity.startActivity(intent);
    }

//    @Override
//    public int getItemViewType(int position) {
//        if(list.get(position).isShowMenu()){
//            return SHOW_MENU;
//        }else{
//            return HIDE_MENU;
//        }
//    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        //Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), android.R.anim.slide_in_left);
        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.fade_trans_anim);
        Animation animation2 = AnimationUtils.loadAnimation(mContext, R.anim.fade_scale_animation);
        holder.nameFood.setText(foodList.get(position).getName());
        holder.priceFood.setText(foodList.get(position).getPriceUnity().toString());
        holder.totalPrice.setText(foodList.get(position).getPriceTotal().toString());
        holder.quantity.setText(String.valueOf(foodList.get(position).getQuantity()));
        //holder.image.setImageResource(foodList.get(position).getImg());
        holder.image.setImageResource(R.drawable.burger);
        //holder.image.setAnimation(AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.fade_trans_anim));
        holder.view_container.startAnimation(animation);
//        holder.contentText.startAnimation(animation2);
//        holder.contentBtn.startAnimation(animation2);
        // Load Image from the internet and set it into Imageview using Glide
        //Glide.with(mContext).load(mData.get(position).getImage_url()).apply(option).into(holder.layout_thumbnail);
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        LinearLayout view_container, contentBtn, contentText;
        TextView nameFood, priceFood, quantity, totalPrice;
        ImageView image;
        RelativeLayout plusQuantity, minusQuantity;

        public MyViewHolder(View itemView) {
            super(itemView);
            view_container = itemView.findViewById(R.id.container);
            contentText = itemView.findViewById(R.id.contentText);
            contentBtn = itemView.findViewById(R.id.contentBtn);
            nameFood = itemView.findViewById(R.id.nameFood);
            priceFood = itemView.findViewById(R.id.priceFood);
            totalPrice = itemView.findViewById(R.id.totalPrice);
            image = itemView.findViewById(R.id.imgPlat);
            plusQuantity = itemView.findViewById(R.id.plusQuantity);
            quantity = itemView.findViewById(R.id.quantity);
            minusQuantity = itemView.findViewById(R.id.minusQuantity);
        }
    }


    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public RecyclerView getRecyclerOrder() {
        return recyclerOrder;
    }

    public void setRecyclerOrder(RecyclerView recyclerOrder) {
        this.recyclerOrder = recyclerOrder;
    }

    //    @Override
//    public int getViewTypeCount() {
//        return getCount();
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//        return position;
//    }


    public TextView getTotal() {
        return total;
    }

    public void setTotal(TextView total) {
        this.total = total;
    }
}
