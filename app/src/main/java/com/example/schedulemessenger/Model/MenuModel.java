package com.example.schedulemessenger.Model;

public class MenuModel {
    String name;
    int icon;
    int textColor;
    int navId;

    public MenuModel(String name, int icon, int textColor, int navId) {
        this.name = name;
        this.icon = icon;
        this.textColor = textColor;
        this.navId = navId;
    }

    public String getName() {
        return name;
    }

    public int getIcon() {
        return icon;
    }

    public int getTextColor() {
        return textColor;
    }

    public int getNavId() {
        return navId;
    }
}
