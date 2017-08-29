package vn.hdisoft.networkwidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;



public class NetInfoWidget extends AppWidgetProvider {

	public static final String AC_SETTING_CLICKED    = "AC_SETTING_CLICKED";
    public static final String ACTION_REFRESH        = "AC_REFRESH";

	Context _context;
	
	@Override
	public void onEnabled(Context context) {
		super.onEnabled(context);
		_context = context;
	}
	
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);
//        LogUtils.info("Widget count 1:"+appWidgetIds.length);
		_context = context;		
		ComponentName thisWidget = new ComponentName(context,NetInfoWidget.class);
	    int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
//        LogUtils.info("Widget onUpdate");
//        LogUtils.info("Widget count:"+allWidgetIds.length);

	    for (int widgetId : allWidgetIds) {

	      RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.netinfo_widget);
	      // Set the text
	      String ip         = NetworkTools.wifiIpAddress(context);
	      String hotspot    = NetworkTools.wifiHotSpotName(context);

	      remoteViews.setTextViewText(R.id.tvIPAddress, ""+ip);
	      remoteViews.setTextViewText(R.id.tvWifiSID, ""+hotspot);

	      // Register an onClickListeneractivity_wifi_settings.xml
//	      Intent iSetting = new Intent(context, ActivityWifiSettings.class);
//	      PendingIntent piSetting = PendingIntent.getActivity(context, 0, iSetting, 0);

//            Intent iSetting = new Intent(AC_SETTING_CLICKED);
//            PendingIntent piSetting = PendingIntent.getBroadcast(_context,1,iSetting,PendingIntent.FLAG_UPDATE_CURRENT);
//	      remoteViews.setOnClickPendingIntent(R.id.ibSettings, piSetting);
	      
	      remoteViews.setOnClickPendingIntent(R.id.ibSettings, getPendingSelfIntent(context, AC_SETTING_CLICKED));
	      appWidgetManager.updateAppWidget(widgetId, remoteViews);
	      
	    }
	}
	
	protected PendingIntent getPendingSelfIntent(Context context, String action) {
        Intent intent = new Intent(context, getClass());
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }
	
	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);
//		LogUtils.debug("onReceive:"+intent.getAction());
		if (AC_SETTING_CLICKED.equals(intent.getAction())) {
//			LogUtils.info("click setting...");
            updateWidgets(context);
        }else if("android.net.wifi.STATE_CHANGE".equals(intent.getAction())){
            updateWidgets(context);
        }
	}

    public void updateWidgets(Context context) {
        Intent intent = new Intent(context.getApplicationContext(), getClass());
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        AppWidgetManager widgetManager = AppWidgetManager.getInstance(context);
        int[] ids = widgetManager.getAppWidgetIds(new ComponentName(context, getClass()));
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
//            widgetManager.notifyAppWidgetViewDataChanged(ids, android.R.id.list);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        context.sendBroadcast(intent);
    }

}
