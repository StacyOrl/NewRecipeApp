package com.stasyorl.recipeapp.Models;

public class CategoryModel {
    int pos;
    String name;
    boolean isChecked;

    public CategoryModel(int pos, String name, boolean isChecked) {
        this.pos = pos;
        this.name = name;
        this.isChecked = isChecked;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
