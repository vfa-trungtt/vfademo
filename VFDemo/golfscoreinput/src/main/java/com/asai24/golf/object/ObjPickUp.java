package com.asai24.golf.object;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by huynq on 12/21/16.
 */

public class ObjPickUp implements Serializable{
    // Error and Type
    public static  enum ViewType{
        VIEW_1,
        VIEW_2,
        ADD,
        APP_VADOR
    }
    // View 1
    private int page;
    private int total_pages;
    @SerializedName("news")
    private ArrayList<ObjPickUpItem> lstView1 = new ArrayList<>();
    // list for display on view
    private ArrayList<ObjPickUpItem> lstDisplay = new ArrayList<>();
    public ObjPickUp() {
    }


    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(int total_pages) {
        this.total_pages = total_pages;
    }


    public ArrayList<ObjPickUpItem> getLstView1() {
        return lstView1;
    }

    public void setLstView1(ArrayList<ObjPickUpItem> lstView1) {
        this.lstView1 = lstView1;
    }

    public void setViewTypeForList(ViewType viewType, ArrayList<ObjPickUpItem> lst){
        for (ObjPickUpItem ob:lst) {
            ob.setViewType(viewType);
        }

    }
// list for display on view
    public ArrayList<ObjPickUpItem> getLstDisplay() {
        return lstDisplay;
    }

    public void setLstDisplay(ArrayList<ObjPickUpItem> lstDisplay) {
        this.lstDisplay = lstDisplay;
    }
    // add item into list
    public void addItemDisplay(ArrayList<ObjPickUpItem> arrayList){
        this.lstDisplay.addAll(arrayList);
    }
    public void addItemDisPlayAtPosition(int position, ArrayList<ObjPickUpItem> arrayList){
        for(int i =0; i< arrayList.size(); i++){
            this.lstDisplay.add(position, arrayList.get(i));
            position++;
        }

    }
    public void addItemDisPlayAtPosition(int position, ObjPickUpItem ob){
            this.lstDisplay.add(position, ob);

    }
    public void addItemDisPlayMax(int position, ArrayList<ObjPickUpItem> arrayList,int count){
        for(int i =0; i< count; i++){
            this.lstDisplay.add(position, arrayList.get(i));
            position++;
        }

    }
    public ObjPickUpItem getItemDisplayAtPosition(int position){
        return lstDisplay.get(position);
    }
}
