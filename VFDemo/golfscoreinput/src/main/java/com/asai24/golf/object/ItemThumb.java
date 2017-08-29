package com.asai24.golf.object;

/**
 * Created by VFA on 6/28/17.
 */

public class ItemThumb {
    boolean selected = false;
    String itemName;
    int thumbResourceId;

    public ItemThumb(String itemName, int thumbResourceId) {
        this.itemName = itemName;
        this.thumbResourceId = thumbResourceId;
    }

    public ItemThumb(boolean isSelected, String itemName, int thumbResourceId) {
        selected = isSelected;
        this.itemName = itemName;
        this.thumbResourceId = thumbResourceId;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getThumbResourceId() {
        return thumbResourceId;
    }

    public void setThumbResourceId(int thumbResourceId) {
        this.thumbResourceId = thumbResourceId;
    }
}
