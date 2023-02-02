package com.fastfoodapp.Models;

import java.math.BigDecimal;

public class Food {

    private int id;
    private String name;
    private String description;
    private Category category;
    private int seq;
    private int img;
    private int quantity = 0;
    boolean inOrder = false;
    private BigDecimal priceUnity = new BigDecimal("250.00");
    private BigDecimal priceTotal = new BigDecimal("0.00");

    public Food(){

    }

    public Food(int id, String name, Category idCategory, int img, String description) {
        this.id = id;
        this.name = name;
        this.category = idCategory;
        this.img = img;
        this.description = description;
    }

    public Food(int id, String name, Category idCategory, int img, String description, int quantity, BigDecimal priceUnity) {
        this.id = id;
        this.name = name;
        this.category = idCategory;
        this.img = img;
        this.description = description;
        this.quantity = quantity;
        this.priceUnity = priceUnity;
        calculatePriceTotal();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPriceUnity() {
        return priceUnity;
    }

    public void setPriceUnity(BigDecimal priceUnity) {
        this.priceUnity = priceUnity;
    }

    public BigDecimal getPriceTotal() {
        return priceTotal;
    }

    public void setPriceTotal(BigDecimal priceTotal) {
        this.priceTotal = priceTotal;
    }

    public void calculatePriceTotal() {
        priceTotal = priceUnity.multiply(BigDecimal.valueOf(quantity));
    }

    public boolean isInOrder() {
        return inOrder;
    }

    public void setInOrder(boolean inOrder) {
        this.inOrder = inOrder;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }
}
