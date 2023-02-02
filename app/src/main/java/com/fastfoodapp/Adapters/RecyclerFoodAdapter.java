package com.fastfoodapp.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fastfoodapp.Models.Food;
import com.fastfoodapp.Models.Order;
import com.fastfoodapp.Models.StaticVariable;
import com.fastfoodapp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aws on 11/03/2018.
 */

public class RecyclerFoodAdapter extends RecyclerView.Adapter<RecyclerFoodAdapter.MyViewHolder> {

    private Context mContext;
    private List<Food> foodList;
    //RequestOptions option;
    private String type;
    private Order order;
    private Activity activity;


    public RecyclerFoodAdapter(Context mContext, List<Food> zoneList, String type, Order order) {
        this.mContext = mContext;
        this.foodList = zoneList;
        this.order = order;
        // Request option for Glide
        //option = new RequestOptions().centerCrop().placeholder(R.drawable.loading_shape).error(R.drawable.loading_shape);
        this.type = type;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {

        View view;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.item_food, parent, false);
        final MyViewHolder viewHolder = new MyViewHolder(view);
        viewHolder.plusQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                foodList.get(viewHolder.getAdapterPosition()).setQuantity(foodList.get(viewHolder.getAdapterPosition()).getQuantity() + 1);
                foodList.get(viewHolder.getAdapterPosition()).calculatePriceTotal();
                viewHolder.quantity.setText(String.valueOf(foodList.get(viewHolder.getAdapterPosition()).getQuantity()));
            }
        });
        viewHolder.minusQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (foodList.get(viewHolder.getAdapterPosition()).getQuantity() > 0) {
                    foodList.get(viewHolder.getAdapterPosition()).setQuantity(foodList.get(viewHolder.getAdapterPosition()).getQuantity() - 1);
                    foodList.get(viewHolder.getAdapterPosition()).calculatePriceTotal();
                    viewHolder.quantity.setText(String.valueOf(foodList.get(viewHolder.getAdapterPosition()).getQuantity()));
                    // remove from the listFood
                    if (foodList.get(viewHolder.getAdapterPosition()).getQuantity() == 0) {
                        viewHolder.addToCmd.setBackgroundTintList(mContext.getResources().getColorStateList(R.color.black));
                        viewHolder.addToCmd.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_shopping, 0, 0, 0);
                        foodList.get(viewHolder.getAdapterPosition()).setInOrder(false);
                        order.getFoods().remove(foodList.get(viewHolder.getAdapterPosition()));

                        viewHolder.addToCmd.setTextColor(mContext.getResources().getColor(R.color.white));
                        viewHolder.addToCmd.setText("Ajouter");
                        viewHolder.addToCmd.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_shopping, 0, 0, 0);
                        foodList.get(viewHolder.getAdapterPosition()).setQuantity(0);
                        viewHolder.quantity.setText("0");
                    }
                }
            }
        });

        viewHolder.addToCmd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!foodList.get(viewHolder.getAdapterPosition()).isInOrder()) {
                    //if (foodList.get(viewHolder.getAdapterPosition()).getQuantity() > 0) {
                    viewHolder.addToCmd.setBackgroundTintList(mContext.getResources().getColorStateList(R.color.white));
                    viewHolder.addToCmd.setTextColor(mContext.getResources().getColor(R.color.black));
                    viewHolder.addToCmd.setText("Supprimer");
                    viewHolder.addToCmd.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_remove_shopping, 0, 0, 0);
                    foodList.get(viewHolder.getAdapterPosition()).setInOrder(true);//viewHolder.inOrder = true;
                    order.getFoods().add(foodList.get(viewHolder.getAdapterPosition()));
                    if (foodList.get(viewHolder.getAdapterPosition()).getQuantity() == 0) {
                        foodList.get(viewHolder.getAdapterPosition()).setQuantity(foodList.get(viewHolder.getAdapterPosition()).getQuantity() + 1);
                        foodList.get(viewHolder.getAdapterPosition()).calculatePriceTotal();
                        viewHolder.quantity.setText(String.valueOf(foodList.get(viewHolder.getAdapterPosition()).getQuantity()));
                    }
                    //} else{
                    //Toast.makeText(mContext, "Ajouter la quantitée !", Toast.LENGTH_SHORT).show();

                } else {
                    viewHolder.addToCmd.setBackgroundTintList(mContext.getResources().getColorStateList(R.color.black));
                    viewHolder.addToCmd.setTextColor(mContext.getResources().getColor(R.color.white));
                    viewHolder.addToCmd.setText("Ajouter");
                    viewHolder.addToCmd.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_shopping, 0, 0, 0);
                    foodList.get(viewHolder.getAdapterPosition()).setInOrder(false);
                    //viewHolder.inOrder = false;
                    foodList.get(viewHolder.getAdapterPosition()).setQuantity(0);
                    order.getFoods().remove(foodList.get(viewHolder.getAdapterPosition()));
                    viewHolder.quantity.setText("0");
                }
            }
        });

//        viewHolder.view_container.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (type.equals("magazin"))
//                    return;
//                Intent i = new Intent(mContext, MagazinActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("magazin", clientList.get(viewHolder.getAdapterPosition()));
//                i.putExtras(bundle);
//                mContext.startActivity(i);
//            }
//        });
//        viewHolder.view_container.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//                return false;
//            }
//        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        //holder.image.setImageResource(foodList.get(position).getImg());
        holder.image.setImageResource(StaticVariable.imgFoods[foodList.get(position).getImg()]);
        holder.titleFood.setText(foodList.get(position).getName());
        holder.descFood.setText(foodList.get(position).getDescription());
        holder.priceFood.setText(foodList.get(position).getPriceUnity().toString());
        holder.quantity.setText(String.valueOf(foodList.get(position).getQuantity()));
        Food selfFood = foodList.get(position);
        for (Food food : StaticVariable.foods)
            if (food.getId() == selfFood.getId())
                selfFood = food;

        if (!selfFood.isInOrder()) {
            holder.addToCmd.setText("Ajouter");
            holder.addToCmd.setBackgroundTintList(mContext.getResources().getColorStateList(R.color.black));
            holder.addToCmd.setTextColor(mContext.getResources().getColor(R.color.white));
            holder.addToCmd.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_shopping, 0, 0, 0);
        } else {
            holder.addToCmd.setText("Supprimer");
            holder.addToCmd.setBackgroundTintList(mContext.getResources().getColorStateList(R.color.white));
            holder.addToCmd.setTextColor(mContext.getResources().getColor(R.color.black));
            holder.addToCmd.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_remove_shopping, 0, 0, 0);
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.fade_trans_anim);
        holder.view_container.startAnimation(animation);
        //holder.image.setImageResource(foodList.get(position).getImg());

        // Load Image from the internet and set it into Imageview using Glide
        //Glide.with(mContext).load(mData.get(position).getImage_url()).apply(option).into(holder.layout_thumbnail);
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView titleFood, descFood, priceFood, quantity;
        ImageView image;
        RelativeLayout view_container;
        RelativeLayout plusQuantity, minusQuantity;
        Button addToCmd;

        public MyViewHolder(View itemView) {
            super(itemView);
            view_container = itemView.findViewById(R.id.container);
            titleFood = itemView.findViewById(R.id.titleFood);
            descFood = itemView.findViewById(R.id.descFood);
            priceFood = itemView.findViewById(R.id.priceFood);
            image = itemView.findViewById(R.id.imgPlat);
            plusQuantity = itemView.findViewById(R.id.plusQuantity);
            quantity = itemView.findViewById(R.id.quantity);
            minusQuantity = itemView.findViewById(R.id.minusQuantity);
            addToCmd = itemView.findViewById(R.id.addToCmd);
        }
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public List<Food> getFoodList() {
        return foodList;
    }

    public void setFoodList(List<Food> foodList) {
        this.foodList = foodList;
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
}
