package vfa.vflib.utils;

import android.os.Debug;
import android.util.Log;

import java.lang.reflect.Field;

public class LogUtils {
	
	public static final String DEFAULT_TAG = "Nifty";
	public static boolean IS_DEBUG = true;
	
	public static long L_LastMemoryUsed = 0;
	public static void debug(String msg){
		if(IS_DEBUG){
			Log.d(DEFAULT_TAG,msg);
		}
	}
	
	public static void warning(String msg){
		if(IS_DEBUG){
			Log.w(DEFAULT_TAG,msg);
		}
	}
	
	public static void info(String msg){
		if(IS_DEBUG){
			Log.i(DEFAULT_TAG,msg);
		}
	}
	
	public static void info(Object obj){
		if(IS_DEBUG){
			for(Field field:obj.getClass().getDeclaredFields()){
				info(""+field.getName()+":");
//				field.get
			}
//			Log.i(DEFAULT_TAG,msg);
		}
	}
	
	public static void error(String msg){
		if(IS_DEBUG){
			Log.e(DEFAULT_TAG,msg);
		}
	}

    public static void error(Exception e){
        if(IS_DEBUG){
            Log.e(DEFAULT_TAG,e.toString());
        }
    }
	
	public static void debug(String tag, String msg){
		if(IS_DEBUG){
			Log.d(tag,msg);
		}
	}
	public static void info(String tag, String msg){
		if(IS_DEBUG){
			Log.i(tag,msg);
		}
	}
	
	public static void error(String tag, String msg){
		if(IS_DEBUG){
			Log.e(tag,msg);
		}
	}
	public static void memoryUsedInfo()
	{
		if(!IS_DEBUG)
			return;
		long lMemUsedInBytes = Debug.getNativeHeapAllocatedSize();
		int iMemUsedInMB = (int) (lMemUsedInBytes/ 1048576L);
		int iMemUseInKB = (int) (lMemUsedInBytes / 1024L);

		if(lMemUsedInBytes > L_LastMemoryUsed){
			Log.d(DEFAULT_TAG,"Memory++" + (lMemUsedInBytes - L_LastMemoryUsed) + " bytes");
		}else{
			Log.d(DEFAULT_TAG,"Memory--" + (L_LastMemoryUsed - lMemUsedInBytes) + " bytes");
		}
		
		Runtime rt = Runtime.getRuntime();
		long maxMem =  rt.maxMemory()/ 1048576L;
		L_LastMemoryUsed = lMemUsedInBytes;		
		Log.e(DEFAULT_TAG,"Memory has used:"+iMemUsedInMB + "/" + maxMem + "MB,"+iMemUseInKB+"KB, "+lMemUsedInBytes+"Bytes.");
		
	}
	
	public static void memoryUsedInfo(String msg)
	{
		if(!IS_DEBUG)
			return;
		long lMemUsedInBytes = Debug.getNativeHeapAllocatedSize();
		int iMemUsedInMB = (int) (lMemUsedInBytes/ 1048576L);
		int iMemUseInKB = (int) (lMemUsedInBytes / 1024L);

		if(lMemUsedInBytes > L_LastMemoryUsed){
			Log.d(DEFAULT_TAG,"Memory++" + (lMemUsedInBytes - L_LastMemoryUsed) + " bytes");
		}else{
			Log.d(DEFAULT_TAG,"Memory--" + (L_LastMemoryUsed - lMemUsedInBytes) + " bytes");
		}
		
		L_LastMemoryUsed = lMemUsedInBytes;
		Log.e(DEFAULT_TAG,msg+":Memory has used:"+iMemUsedInMB + "MB, "+iMemUseInKB+"KB, "+lMemUsedInBytes+"Bytes.");
	}
}
