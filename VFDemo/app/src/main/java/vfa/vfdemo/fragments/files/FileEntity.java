package vfa.vfdemo.fragments.files;

import java.io.File;

/**
 * Created by Vitalify on 3/8/17.
 */

public class FileEntity {

    public String fileName;
    public String filePath;
    public String fileType;


    public boolean isSelected = false;
    public File file;

    public FileEntity(){

    }

    public boolean isDirectory(){
        File f = new File(filePath);

        return f.isDirectory();
    }

    public String getExtension(){
        File f = new File(filePath);
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 && i < s.length() - 1) {
            ext = s.substring(i + 1).toLowerCase();
        }
        return ext;
    }

}
