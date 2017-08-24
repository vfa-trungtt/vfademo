package com.asai24.golf.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.asai24.golf.Constant;
import com.asai24.golf.inputscore.R;


public class NetworkUtils {
    public static boolean isNetworkAvailable(Context context) {
        if (context == null) {
            return false;
        }
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static boolean showInternetConnectionErrorIfnoInternet(Context context) {

        if (isNetworkAvailable(context)) {

            return true;
        }

        showNoInternetAlert(context);

        return false;
    }

    public static void showNoInternetAlert(Context context) {

        if (context == null)
            return;
       /* Toast.makeText(context, context.getString(R.string.no_internet_connectivity_msg), Toast.LENGTH_SHORT).show();*/
    }

    public static AlertDialog DialogNoNetWork(final Context context){
        AlertDialog  dialogNetWork =  new AlertDialog.Builder(context).setIcon(
                R.drawable.alert_dialog_icon).setTitle(
                R.string.warning).setMessage(
                R.string.network_erro_or_not_connet).setPositiveButton(
                R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int whichButton) {
                        dialog.dismiss();
                    }
                }).create();
        return dialogNetWork;
    }
    public static String errorRequest(final Context context,Constant.ErrorServer errorServer){
        String message = "";
        if(errorServer == Constant.ErrorServer.ERROR_CONNECT){
            message = context.getString(R.string.no_internet_connectivity_msg);
        }
        else if(errorServer == Constant.ErrorServer.ERROR_CONNECT_TIMEOUT){
            message = context.getString(R.string.error_connect_timeout);

        }else if(errorServer == Constant.ErrorServer.ERROR_SOCKET_TIMEOUT) {
            message = context.getString(R.string.error_socket_timeout);

        }
        return message;
    }
}
