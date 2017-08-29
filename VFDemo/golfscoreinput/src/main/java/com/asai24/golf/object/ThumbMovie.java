package com.asai24.golf.object;

import android.content.Context;

/**
 * Created by huynq on 3/4/16.
 */
public class ThumbMovie {
    private String programCode;
    private String programName;
    private String categoryCode;
    private String categoryName;
    private String thumbnail;
    private int cref;
    private String spec;

    public String getProgramCode(){return programCode;}
    public String getProgramName(){return programName;}
    public String getCategoryCode(){return categoryCode;}
    public String getCategoryName(){return categoryName;}
    public String getThumbnail(){return thumbnail;}
    public int getCref(){return cref;}
    public String getSpec(){return spec;}

    public void setProgramCode(String mProgramCode){this.programCode = mProgramCode;}
    public void setProgramName(String mProgramName){this.programName = mProgramName;}
    public void setCategoryCode(String mCatergoryCode){this.categoryCode =mCatergoryCode;}
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
    public void setCref(int cref) {
        this.cref = cref;
    }
    public void setSpec(String spec) {
        this.spec = spec;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
    public ThumbMovie(String programCode, String programName, String categoryCode, String categoryName, String thumbnail, int cref, String spec){
        this.programCode = programCode;
        this.programName = programName;
        this.categoryCode = categoryCode;
        this.categoryName = categoryName;
        this.thumbnail = thumbnail;
        this.cref = cref;
        this.spec = spec;
    }
    public ThumbMovie(String programCode, String programName){
        this.programCode = programCode;
        this.programName = programName;
        this.categoryCode = "";
        this.categoryName = "";
        this.thumbnail = "";
        this.cref = 0;
        this.spec = "";
    }
    public void onItemClick(Context context){
        //Intent intent = new Intent()
    }

}
