package vfa.vfdemo.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vitalify on 3/1/17.
 */

public class ImageEntity {
    public String url;

    public static List<ImageEntity> getDemoList(){
        ArrayList<ImageEntity> arr = new ArrayList<>();
        ImageEntity image = new ImageEntity();
        image.url = "http://khoemoivui.com/wp-content/uploads/2014/03/anh-dep-hoa-lan.jpg";

        arr.add(image);

        image = new ImageEntity();
        image.url = "http://khoemoivui.com/wp-content/uploads/2014/03/hoa-lay-on.jpg";

        arr.add(image);

        image = new ImageEntity();
        image.url = "http://khoemoivui.com/wp-content/uploads/2014/03/jasmine-flower-hoa-nhai.jpg";

        arr.add(image);

        image = new ImageEntity();
        image.url = "http://khoemoivui.com/wp-content/uploads/2014/03/hoa-phi-yen-Delphiniums.jpg";

        arr.add(image);

        return arr;
    }
}
