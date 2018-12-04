package com.scancode.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Tools {

	public static boolean isEmpty(Object input) {
		if (input instanceof String) {
			return isEmpty((String) input);
		}
		if (input instanceof List<?>) {
			return input == null || ((List<?>) input).isEmpty();
		}
		if (input instanceof Collection<?>) {
			return input == null || ((Collection<?>) input).isEmpty();
		}
		return input == null;
	}
	
	public static boolean isNotEmpty(Object input){
		return !isEmpty(input);
	}
	
	private static boolean isEmpty(String input) {
		return input == null || input.trim().length() == 0;
	}
	
    /** 获取Android本机IP地址     **/
	public static String getLocalIpAddress() {
		try {            
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {                
				NetworkInterface intf = en.nextElement();                
				for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {                    
					InetAddress inetAddress = enumIpAddr.nextElement();                    
					if (!inetAddress.isLoopbackAddress()) {                        
						return inetAddress.getHostAddress().toString();                    
					}                
				}            
			}        
		} 
		catch (SocketException ex) {            
			Log.e("WifiPreference IpAddress", ex.toString());        
		}        
		return null;
	}
	/**
	 * 获取手机唯一标识码
	 * @return
	 */
	public static String getDeviceid(Context context) {
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		// 获取设备编号
		String tmpId = tm.getDeviceId();
		if(tmpId.length() >= 20)
			tmpId = tmpId.substring(0, 20);
		else if(tmpId.length() >= 10)
			tmpId = tmpId.substring(0, 10);
		return tmpId;
	}
	
	/**
	 * 字符串转小数，默认保留两�?
	 * @param str
	 * @return 
	 * reference:http://zhujianjia.iteye.com/blog/1061319
	 */
	public static final BigDecimal converStringToDecimal(String str){
		BigDecimal bd = new BigDecimal(str);
		bd = bd.setScale(2,BigDecimal.ROUND_HALF_UP);
		return bd;
	}
	
	/**
	 * 字符串转小数，默认保�?�?
	 * @param str
	 * @return
	 */
	public static final BigDecimal converStringToDecimalAsInt(String str){
		BigDecimal bd = new BigDecimal(str);
		bd = bd.setScale(0,BigDecimal.ROUND_HALF_UP);
		return bd;
	}
	
	/**
	 * 字符串转小数，默认保�?�?
	 * @param str
	 * @return
	 */
	public static final BigDecimal converStringToDecimalAsNum(String str){
		BigDecimal bd = new BigDecimal(str);
		bd = bd.setScale(1,BigDecimal.ROUND_HALF_UP);
		return bd;
	}
	
	//图片转base64
	public static final String base64(String strPath){
		String baseStr = null;
		try {
			InputStream in = new FileInputStream(strPath);
			byte[] bytes = new byte[in.available()];
			in.read(bytes);
			in.close();
			byte[] bate = Base64.encode(bytes, Base64.DEFAULT);
			baseStr = new String(bate, "UTF-8");
		} catch (Exception e) {	
			e.printStackTrace();
		}	
		return baseStr;	
	}
	//图片转base64
	public static final String base64(Bitmap bitmap){
		String baseStr = null;
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
			InputStream sbs = new ByteArrayInputStream(baos.toByteArray());
			byte[] bytes = new byte[sbs.available()];
			sbs.read(bytes);
			sbs.close();
			byte[] bate = Base64.encode(bytes, Base64.DEFAULT);
			baseStr = new String(bate, "UTF-8");
		} catch (Exception e) {	
			e.printStackTrace();
		}	
		return baseStr;	
	}
	
	public static String UUID(){
		return java.util.UUID.randomUUID().toString().replace("-", "");
	}
	
	public static String getNowTime(){
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String date = sDateFormat.format(new java.util.Date());
		return date;
	}
	
	public static boolean isEmail(String email){  
		if (null==email || "".equals(email)) return false;	
		//Pattern p = Pattern.compile("\\w+@(\\w+.)+[a-z]{2,3}"); //简单匹配  
		Pattern p =  Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");//复杂匹配  
		Matcher m = p.matcher(email);  
		return m.matches();  
	}
	
	public static String getSDPath(){
		try{
	       File sdDir = null; 
	       boolean sdCardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
	//判断sd卡是否存在 
	       if   (sdCardExist)   
	       {                               
	         sdDir = Environment.getExternalStorageDirectory();//获取跟目录 
	      }   
	       return sdDir.toString(); 
		}catch(Exception e){
			e.printStackTrace();
		}
		return "";
	}
	
}
