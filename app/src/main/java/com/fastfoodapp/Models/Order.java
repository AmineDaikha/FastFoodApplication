package com.fastfoodapp.Models;

import java.math.BigDecimal;
import java.util.ArrayList;

public class Order {

    private String id;
    private String numTab;
    private ArrayList<Food> foods = new ArrayList<>();
    private BigDecimal total;

    public String getNumTab() {
        return numTab;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setNumTab(String numTab) {
        this.numTab = numTab;
    }

    public ArrayList<Food> getFoods() {
        return foods;
    }

    public void setFoods(ArrayList<Food> foods) {
        this.foods = foods;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public void calculateTotal() {
        total = new BigDecimal("0.00");
        for (Food food : foods) {
            food.calculatePriceTotal();
            total = total.add(food.getPriceTotal());
        }
    }
}
