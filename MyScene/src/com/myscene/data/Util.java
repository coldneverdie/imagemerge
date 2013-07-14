package com.myscene.data;

import java.util.Calendar;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.util.Log;

public class Util {

	public static void Print(String tag,String log){
		Log.v(tag,log);
	}
	
	
	
	/**
     * 判断私有文件是否存在
     * @return
     */
    public static boolean checkPrivateExist(Context context,String fileName){
//    	System.out.println("检测是否存在");
            String [] fileNameArray = context.fileList();
            for(int i = 0; i< fileNameArray.length; i++){
                    if(fileNameArray[i].equals(fileName)){
                    	System.out.println("checkPrivateExist 存在");
                            return true;
                    }
            }
        	System.out.println("checkPrivateExist 不存在");
            return false;
    }
	public final static String sdcardPath = android.os.Environment.getExternalStorageDirectory().getPath();
	/**
	 * 检测sd卡是否可用
	 * 
	 * @return
	 */
	public static boolean checkSDcardAvailable() {
		return android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);
	}
	
	/**
	 * 检测网络状态，之前的方法被复用
	 * 
	 * @return
	 */
	public static boolean checkWifiAndGPRS(Context context) {
		// 检测网络连接
		ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) {
			return false;
		} else {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED
							|| info[i].getState() == State.CONNECTING) {
						return true;
					}
				}
			}
		}
		return false;
	}
	/**
	 * 获取系统当前时间
	 * @return
	 */
	public static String getCurrentTime(){
		StringBuilder sb = new StringBuilder();
		Calendar c = Calendar.getInstance();//可以对每个时间域单独修改
		int year = c.get(Calendar.YEAR); 
		int month = c.get(Calendar.MONTH)+1; 
		int date = c.get(Calendar.DATE); 
		int hour = c.get(Calendar.HOUR_OF_DAY); 
		int minute = c.get(Calendar.MINUTE); 
		sb.append(year);
		sb.append("-"+month);
		sb.append("-"+date);
		sb.append("-"+hour);
		sb.append("-"+minute);
		return sb.toString();
	}
	/**
	 * format 2012-10-10
	 * @return
	 */
	public static String getDate(){
		StringBuilder sb = new StringBuilder();
		Calendar c = Calendar.getInstance();//可以对每个时间域单独修改
		int year = c.get(Calendar.YEAR); 
		int month = c.get(Calendar.MONTH)+1; 
		int date = c.get(Calendar.DATE); 
		sb.append(year);
		sb.append("-"+month);
		sb.append("-"+date);
		return sb.toString();
	}
	/**
	 * format 10:10
	 * @return
	 */
	public static String getTime(){
		StringBuilder sb = new StringBuilder();
		Calendar c = Calendar.getInstance();//可以对每个时间域单独修改
		int hour = c.get(Calendar.HOUR_OF_DAY); 
		int minute = c.get(Calendar.MINUTE); 
		sb.append(hour);
		sb.append(":"+minute);
		return sb.toString();
	}
	/**
	 * 检测网络连接
	 * @param context
	 * @return
	 */
	public final static boolean checkInternet(Context context){
		// 检测网络连接
				ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
				if (connectivity == null) {
					return false;
				} else {
					NetworkInfo[] info = connectivity.getAllNetworkInfo();
					if (info != null) {
						for (int i = 0; i < info.length; i++) {
							if (info[i].getState() == NetworkInfo.State.CONNECTED
									|| info[i].getState() == State.CONNECTING) {
								return true;
							}
						}
					}
				}
				return false;
		
	}
}
