package vfa.vfdemo.networks;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import vfa.vflib.utils.LogUtils;

public class VolleyHelper {

    public static final int REQUEST_SUCCESSFULL = 0;
    public static final int REQUEST_FAILS       = 1;
    public interface BaseRestListener{
        public void onRequestCompleted(int status,String response);
    }

    public static void doGETrequest(Context context, final String url,final BaseRestListener listener){
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                LogUtils.info(url+"==response:"+response);
                if(listener != null){
                    listener.onRequestCompleted(REQUEST_SUCCESSFULL,response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtils.error(url+"==error:"+error.toString());
                if(listener != null){
                    listener.onRequestCompleted(REQUEST_FAILS,null);
                }
            }
        });
        queue.add(stringRequest);
    }
}
