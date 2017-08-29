package com.asai24.golf.object;

import android.content.Context;

import com.asai24.golf.Constant;
import com.asai24.golf.common.Distance;
import com.asai24.golf.listener.ServiceListener;
import com.asai24.golf.utils.YgoLog;

/**
 * Created by akato_vf on 2017/03/29.
 */

public class GuestUser {
    public interface ListenerGuestUser{
        void onPreGuestUser();
         void onSuccessGuestUser();
         void onErrorGuestUser(Constant.ErrorServer errorServer);
    }
    private String auth_token;

    public static String KEY_IS_USER_GUEST = "KEY_IS_USER_GUEST";
    public static boolean isUserGuest(){
//        return GolfApplication.getPreferences().getBoolean(KEY_IS_USER_GUEST,false);
        return true;
    }
    public static void saveUserGuest(){
//        GolfApplication.getPreferences().edit().putBoolean(KEY_IS_USER_GUEST,true).commit();
    }
    public static void removeUserGuest(){
//        GolfApplication.getPreferences().edit().putBoolean(KEY_IS_USER_GUEST,false).commit();
    }

    public GuestUser(String tokenGuest) {
        this.auth_token = tokenGuest;
    }

    public String getTokenGuest() {
        return auth_token;
    }

    public void setTokenGuest(String tokenGuest) {
        this.auth_token = tokenGuest;
    }
//
//    public static void getUserToken (final Context context, APIInterfaceImpl service, final ListenerGuestUser listenerGuestUser){
//        service.getTokenUserGuest(context, new ServiceListener<GuestUser>() {
//            @Override
//            public void onPreExecute() {
//                listenerGuestUser.onPreGuestUser();
//            }
//
//            @Override
//            public void onPostExecute(GuestUser guestUser) {
//                YgoLog.d("TAG_GUEST_USER",String.format("Token Guest user : %s",guestUser.getTokenGuest()));
//                Distance.saveYourGolfAccountToken(context,guestUser.getTokenGuest());
//                GuestUser.saveUserGuest();
//                listenerGuestUser.onSuccessGuestUser();
//            }
//
//            @Override
//            public void onError(Constant.ErrorServer errorServer) {
//                YgoLog.d("TAG_GUEST_USER",String.format("Get token Guest is Error with errorServer : %s", errorServer));
//                Distance.saveYourGolfAccountToken(context,"");
//
//                listenerGuestUser.onErrorGuestUser(errorServer);
//            }
//        });
//    }

}
