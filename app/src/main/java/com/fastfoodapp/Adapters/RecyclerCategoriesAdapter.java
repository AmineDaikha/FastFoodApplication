package com.fastfoodapp.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.fastfoodapp.Models.Category;
import com.fastfoodapp.Models.Food;
import com.fastfoodapp.Models.StaticVariable;
import com.fastfoodapp.R;

import java.util.Collections;
import java.util.List;

/**
 * Created by Aws on 11/03/2018.
 */

public class RecyclerCategoriesAdapter extends RecyclerView.Adapter<RecyclerCategoriesAdapter.MyViewHolder> {

    private Context mContext;
    private List<Category> categoryList;
    //RequestOptions option;
    private String type;
    private Activity activity;
    private RecyclerFoodAdapter recyclerFoodAdapter;


    public RecyclerCategoriesAdapter(Context mContext, List<Category> zoneList, String type) {
        this.mContext = mContext;
        this.categoryList = zoneList;
        // Request option for Glide
        //option = new RequestOptions().centerCrop().placeholder(R.drawable.loading_shape).error(R.drawable.loading_shape);
        this.type = type;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.item_category, parent, false);
        final MyViewHolder viewHolder = new MyViewHolder(view);
        viewHolder.view_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (type.equals("magazin"))
////                    return;
////                Intent i = new Intent(mContext, MagazinActivity.class);
////                Bundle bundle = new Bundle();
////                bundle.putSerializable("magazin", clientList.get(viewHolder.getAdapterPosition()));
////                i.putExtras(bundle);
////                mContext.startActivity(i);
                if (!categoryList.get(viewHolder.getAdapterPosition()).isSelected()) {
                    //viewHolder.content.setBackgroundResource(R.drawable.item_detail_selected);
                    viewHolder.content.setElevation(16);
                    categoryList.get(viewHolder.getAdapterPosition()).setSelected(true);
                    StaticVariable.select = viewHolder.getAdapterPosition();
                    for (int i = 0; i < categoryList.size(); i++)
                        if (i != viewHolder.getAdapterPosition()) {
                            categoryList.get(i).setSelected(false);
                        }
                    recyclerFoodAdapter.getFoodList().clear();
                    //for (Food food : )
                    recyclerFoodAdapter.setFoodList(StaticVariable.db.getFoods(categoryList.get(viewHolder.getAdapterPosition())));
                    System.out.println("size foodList apt : " + recyclerFoodAdapter.getFoodList().size());
                    recyclerFoodAdapter.getFoodList().add(new Food(1, "Checken Burger", categoryList.get(0), R.drawable.burger, "Pizza à pâte fine et croustillante richement garnie de tomates, de jambon, de champignons et de fromage, surgelée"));
                    recyclerFoodAdapter.getFoodList().add(new Food(1, "Checken Burger", categoryList.get(0), R.drawable.tortilla, "Pizza à pâte fine et croustillante richement garnie de tomates, de jambon, de champignons et de fromage, surgelée"));
                    recyclerFoodAdapter.getFoodList().add(new Food(1, "Checken Burger", categoryList.get(0), R.drawable.chawarma, "Pizza à pâte fine et croustillante richement garnie de tomates, de jambon, de champignons et de fromage, surgelée"));
                    recyclerFoodAdapter.notifyDataSetChanged();
                    notifyDataSetChanged();


                }
            }
        });
        viewHolder.view_container.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                return false;
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        System.out.println("image ; " + categoryList.get(position).getImage());
        holder.nameClient.setText(categoryList.get(position).getName());
        //holder.imageView.setImageResource(StaticVariable.imgCategories[categoryList.get(position).getImage()]);
        holder.imageView.setImageResource(StaticVariable.imgCategories[0]);
        if (categoryList.get(position).isSelected())
            holder.content.setBackgroundResource(R.drawable.item_detail_selected);
        else
            holder.content.setBackgroundResource(R.drawable.item_detail);
    }

    public void swapeItem(int fromPosition, int toPosition) {
        Collections.swap(categoryList, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
    }
//    public void setDataset(List<String> dataset) {
//        this.dataset = dataset;
//        notifyDataSetChanged();
//    }
//
//    public void swapItems(int itemAIndex, int itemBIndex) {
//        //make sure to check if dataset is null and if itemA and itemB are valid indexes.
//        String itemA = dataset.get(itemAIndex);
//        String itemB = dataset.get(itemBIndex);
//        dataset.set(itemAIndex, itemB);
//        dataset.set(itemBIndex, ItemA);
//
//        notifyDataSetChanged(); //This will trigger onBindViewHolder method from the adapter.
//    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView nameClient;
        ImageView imageView;
        LinearLayout view_container;
        RelativeLayout content;

        public MyViewHolder(View itemView) {
            super(itemView);
            view_container = itemView.findViewById(R.id.container);
            nameClient = itemView.findViewById(R.id.titleCategory);
            imageView = itemView.findViewById(R.id.imgCategory);
            content = itemView.findViewById(R.id.content);
        }
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public RecyclerFoodAdapter getRecyclerFoodAdapter() {
        return recyclerFoodAdapter;
    }

    public void setRecyclerFoodAdapter(RecyclerFoodAdapter recyclerFoodAdapter) {
        this.recyclerFoodAdapter = recyclerFoodAdapter;
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
