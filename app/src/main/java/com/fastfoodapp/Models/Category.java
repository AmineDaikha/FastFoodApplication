package com.fastfoodapp.Models;

public class Category {

    private int id;
    private String name;
    private int image = -1;
    private int seq;
    private boolean selected;
    private boolean exist;

    public Category(int id, String name, int image) {
        this.id = id;
        this.name = name;
        this.image = image;
    }

    public Category() {
    }

    public Category(int img) {
        this.image = img;
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

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public boolean isExist() {
        return exist;
    }

    public void setExist(boolean exist) {
        this.exist = exist;
    }
}
