package com.asai24.golf.object;

/**
 * Created by huynq on 12/15/16.
 */
public class ObjItemThumbMovie {
   private String program_code;
    private String program_name;
    private String category_code;
    private String category_name;
    private String thumbnail;
    private String cref;
    private String spec;

    public ObjItemThumbMovie() {
    }

    public void setProgram_code(String program_code) {
        this.program_code = program_code;
    }

    public void setProgram_name(String program_name) {
        this.program_name = program_name;
    }

    public void setCategory_code(String category_code) {
        this.category_code = category_code;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public void setCref(String cref) {
        this.cref = cref;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getProgram_code() {
        return program_code;
    }

    public String getProgram_name() {
        return program_name;
    }

    public String getCategory_code() {
        return category_code;
    }

    public String getCategory_name() {
        return category_name;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getCref() {
        return cref;
    }

    public String getSpec() {
        return spec;
    }
}
