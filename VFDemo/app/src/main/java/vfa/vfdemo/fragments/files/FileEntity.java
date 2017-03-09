package vfa.vfdemo.fragments.files;

import java.io.File;

/**
 * Created by Vitalify on 3/8/17.
 */

public class FileEntity {

    public String fileName;
    public String filePath;
    public String fileType;

    public File file;

    public FileEntity(){

    }

    public boolean isDirectory(){
        File f = new File(filePath);
        return f.isDirectory();
    }

}
