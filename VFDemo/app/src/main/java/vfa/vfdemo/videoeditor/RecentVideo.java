package vfa.vfdemo.videoeditor;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import vfa.vfdemo.AppSettings;
import vn.hdisoft.hdilib.utils.LogUtils;

/**
 * Created by trungtt on 8/30/17.
 * Manager recent list entry has used by users
 */

public class RecentVideo {

    public int maxCount = 10;
    private Context _context;

    public RecentVideo(Context context){
        _context = context;
    }

    public void add(String path){
        if(getList().contains(path)){
            LogUtils.info("this path has exist...");
            return;
        }
        try{
            JSONArray arrRecent = new JSONArray();
            JSONObject entry = new JSONObject();
            entry.put("path_movie",path);
            arrRecent.put(entry);

            AppSettings.setString(_context,"crecent_movies",arrRecent.toString());

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public List<String> getList(){
        List<String> recentList = new ArrayList<>();
        try{
            String json = AppSettings.getString(_context,"crecent_movies");
            JSONArray arrRecent = new JSONArray(json);
            for(int i = 0;i < arrRecent.length();i++){
                JSONObject entry = arrRecent.getJSONObject(i);
                String moviePath = entry.getString("path_movie");
                recentList.add(moviePath);
                LogUtils.info("recents:"+i+":"+moviePath);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return recentList;
    }

    public void clear(){
        AppSettings.setString(_context,"crecent_movies","");
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void remove(int index){
        try{
            String json = AppSettings.getString(_context,"crecent_movies");
            JSONArray arrRecent = new JSONArray(json);
            for(int i = 0;i < arrRecent.length();i++){
                if(i == index){
                    arrRecent.remove(i);
                    break;
                }

            }
            AppSettings.setString(_context,"crecent_movies",arrRecent.toString());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void validData(){

    }
}
