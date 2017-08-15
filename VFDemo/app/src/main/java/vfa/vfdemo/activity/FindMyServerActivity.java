package vfa.vfdemo.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import vfa.vfdemo.ActivitySlideMenu;
import vfa.vfdemo.fragments.VFFragDemoList;
import vfa.vfdemo.fragments.hdiapp.FragHost;
import vfa.vfdemo.networks.VolleyHelper;
import vfa.vfdemo.utils.VFTask;
import vfa.vflib.utils.LogUtils;


public class FindMyServerActivity extends ActivitySlideMenu {

    ArrayList<String> listHost = new ArrayList<>();
    private int hostPort = 80;
    int countThread;

    Handler handler;
    String hostUrl;
    boolean hasFound = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragHost fg = new FragHost();
        setRootFragment(fg);
        for (int i = 2;i<255;i++){
            String SERVER_IP = "192.168.5."+i;
            tryHost(SERVER_IP);
            if(hasFound){
                break;
            }
        }
        handler = new Handler();

    }

    private void tryHost(final String hostip){
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    countThread++;

                    Socket clientSocket;
                    InetAddress serverAddr = InetAddress.getByName(hostip);
                    clientSocket = new Socket(serverAddr, hostPort);
                    String hostName = serverAddr.getHostName();
                    LogUtils.debug("found server:"+hostip+" ,name:"+hostName);
                    listHost.add(hostip);

                    final String host = "http://"+hostip+"/apk";
                    VolleyHelper.doGETrequest(FindMyServerActivity.this, host, new VolleyHelper.BaseRestListener() {
                        @Override
                        public void onRequestCompleted(int status, String response) {
                            if(status == VolleyHelper.REQUEST_SUCCESSFULL){
                                hasFound = true;
                                hostUrl = "http://"+hostip;
                                FindMyServerActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ((FragHost)getRootFragment()).loadUrl(hostUrl);
                                    }
                                });
                            }
                        }
                    });
                    clientSocket.close();

                } catch (Exception e){
//                    e.printStackTrace();
                }

                if(countThread >= 254){
                    LogUtils.info("Scan completed.");
                }
            }
        });
        th.start();

    }

    class ScanHostTask extends VFTask{

        @Override
        protected Void doInBackground(Void... params) {
            boolean exitScan = false;
            int startOctet = 254/2;
            String ipMaskSub = "192.168.5";
            String SERVER_IP = "192.168.5."+startOctet;
            Socket clientSocket;

            while (!exitScan){
                try{

                    if(startOctet <= 1){
                        exitScan = true;
                    }

                    InetAddress serverAddr = InetAddress.getByName(SERVER_IP);
                    clientSocket = new Socket(serverAddr, 80);
                    String host = serverAddr.getHostName();
                    LogUtils.debug("found server:"+SERVER_IP+" ,name:"+host);


                    clientSocket.close();
//                    exitScan = true;
                    startOctet--;
                    SERVER_IP = "192.168.5."+startOctet;

                } catch (Exception e){
                    e.printStackTrace();
                    startOctet--;

                    SERVER_IP = "192.168.5."+startOctet;
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }
}