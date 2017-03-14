package vfa.vfdemo.fragments.nifty;

import com.nifty.cloud.mb.core.NCMBObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vitalify on 3/14/17.
 */

public class SchoolEntity {
    public String SchoolName;

    public static List<SchoolEntity> getListFromQuery(List<NCMBObject> list){
        List<SchoolEntity> schools = new ArrayList<>();
        for(NCMBObject obj:list){
            SchoolEntity school = new SchoolEntity();
            school.SchoolName   = obj.getString("school_name");
            schools.add(school);
        }
        return schools;
    }
}
