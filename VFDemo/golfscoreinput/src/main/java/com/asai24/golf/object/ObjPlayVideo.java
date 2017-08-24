package com.asai24.golf.object;

import android.os.Build;

import com.asai24.golf.utils.YgoLog;

import java.util.HashMap;
import java.util.Locale;

import static com.asai24.golf.Constant.URL_VIDEO_LIVE;
import static com.asai24.golf.Constant.URL_VIDEO_PLAY;


public class ObjPlayVideo {
    private final String TAG = ObjPlayVideo.class.getName();
    private HashMap<String,String> list = new HashMap<>();

    public ObjPlayVideo(){
        list = new HashMap<>();
        list.put("4.0","and040");
        list.put("4.1","and041");
        list.put("4.2","and042");
        list.put("4.3","and043");
        list.put("4.4","and044");
        list.put("5.0","and050");
        list.put("5.1","and051");
        list.put("6.0","and060");
        list.put("6.1","and061");
        list.put("7.0","and070");
        list.put("7.1","and071");
    }

    public String getOSVersion(){
        String version = Build.VERSION.RELEASE;
        String sub_version = version.substring(0,3);
        String os = "";
        for ( String key : list.keySet() ) {
            if(key.equals(sub_version)){
                os = list.get(key);
                break;
            }
        }
        if(os.equals(""))
            os = list.get("4.0");

        return os;
    }
    public String getStringPlayVideo(String programCode, String license_type, String authen){
        String url = URL_VIDEO_PLAY+"?pcode="+programCode+"&c_type="+license_type+"&spos="+getOSVersion()+"&authentication_token="+authen;
        YgoLog.d(TAG,"URL: "+url);
       return url;
    }
    public String getStringLiveVideo(String programCode, String license_type, String authen){
        String url = URL_VIDEO_LIVE+"?pcode="+programCode+"&c_type="+license_type+"&spos="+getOSVersion()+"&authentication_token="+authen;
        YgoLog.d(TAG,"URL: "+url);
        return url;
    }
    private int MAX_LENGTH_JA = 6;
    private int MAX_LENGTH_EN = 12;
    public String handleTextLength(String title, String subTitle){
        String textTile;
        String m_title;
        String m_subTitle;
       if(Locale.getDefault().getLanguage().equals("ja")){
          m_title =  handleTextJA(title);
           m_subTitle = handleTextJA(subTitle);
       }else{
          m_title = handleTextEN(title);
           m_subTitle = handleTextEN(subTitle);
       }
       textTile = m_title+"\n"+m_subTitle;
        return textTile;
    }
    private String handleTextJA(String text){
        String textTile = text;
        if(calLengthText(text.trim()) > MAX_LENGTH_JA){
            textTile = text.substring(0,6) +"...";
        }
        return textTile;
    }
    private String handleTextEN(String text){
        String textTitle = text;
        String [] arrayText = text.trim().split(" ");
        String textBefore;
        if(arrayText.length > 1) {
            textTitle = arrayText[0];
            textBefore = textTitle;
            for(int i = 1; i< arrayText.length; i++){
                  if(calLengthText(textBefore) + calLengthText(arrayText[i]) < MAX_LENGTH_EN){
                      textTitle += " "+ arrayText[i];
                      textBefore += arrayText[i];
                  }else {
                      textTitle += "...";
                      break;
                  }

            }

        }

        return textTitle;
    }
    private int calLengthText(String text){
        return text.toCharArray().length;
    }
}
