package vfa.vfdemo.networks;

import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import vfa.vflib.utils.LogUtils;


public class VFScanHost {

    private int hostPort;
    private String hostSub;

    ScanHostListener _listender;
    ArrayList<String> listHost = new ArrayList<>();

    private int countThread = 0;

    public interface ScanHostListener{
        public void onScanCompleted(List<String> listHost);
    }
    public VFScanHost(String hostSub,int port){
        this.hostSub = hostSub;
        this.hostPort = port;
    }

    public void doScan(ScanHostListener listener){
        _listender = listener;
        for (int i = 2;i<255;i++){
            String SERVER_IP = hostSub+i;
            tryHost(SERVER_IP);
        }
    }

    private void tryHost(final String hostip){
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                try{

                    Socket clientSocket;
                    InetAddress serverAddr = InetAddress.getByName(hostip);
                    clientSocket = new Socket(serverAddr, hostPort);
                    String host = serverAddr.getCanonicalHostName();
                    LogUtils.debug("found server:"+hostip+" ,name:"+host);
                    listHost.add(hostip);

                    clientSocket.close();
                    countThread++;

                } catch (Exception e){
                    countThread++;
                }
            }
        });
        th.start();

    }

}
