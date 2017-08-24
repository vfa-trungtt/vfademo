package com.asai24.golf.domain;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by CongVC on 7/2/13.
 */
public class CourseLiveObj implements Serializable {

    private String id;
    private String name;
    private ArrayList<Hole> arrHoles;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Hole> getArrHoles() {
        return arrHoles;
    }

    public void setArrHoles(ArrayList<Hole> arrHoles) {
        this.arrHoles = arrHoles;
    }
}
