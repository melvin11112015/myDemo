package com.scancode.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.scancode.BaseApplication;
import com.scancode.R;
import com.scancode.initview.User;

/**
 * SharedPreferences帮助类
 */
public final class SharedPreferencesHelper {
	
	private static final String _default_shared_preferences_name = "scan.sdb";
	
	private static User user;
	
	/**
	 * 获取默认的SharedPreferences
	 * @return
	 */
	public static SharedPreferences get() {
		return get(_default_shared_preferences_name);
	}
	
	/**
	 * 获取给定名称的SharedPreferences
	 * @param name
	 * @return
	 */
	public static SharedPreferences get(String name) {
		return get(name, Context.MODE_PRIVATE);
	}
	
	/**
	 * 获取给定名称的SharedPreferences
	 * @param name
	 * @param mode
	 * @return
	 */
	public static SharedPreferences get(String name, int mode) {
		return get((Context)BaseApplication.getInstance(), name, mode);
	}
	
	/**
	 * 获取给定名称的SharedPreferences
	 * @param ctx
	 * @param name
	 * @param mode
	 * @return
	 */
	public static SharedPreferences get(Context ctx, String name, int mode) {
		return ctx.getSharedPreferences(name, mode);
	}

	public static void addUser(User user){
		if (user == null) {
			return;
		}
		get().edit().putString("username", user.getUserName()).commit();
		get().edit().putString("userid", user.getUserId()).commit();
		get().edit().putString("account", user.getAccount()).commit();
		get().edit().putString("password", user.getPassword()).commit();
	}
	
	public static User getUser(){
		if (user != null) {
			return user;
		}
		user = new User();
		user.setUserName(get().getString("username", ""));
		user.setUserId(get().getString("userid", ""));
		user.setAccount(get().getString("account", ""));
		user.setPassword(get().getString("password", ""));
		return user;
	}
	
	public static String getIp(){
		return get().getString("ip", BaseApplication.getInstance().getResources().getString(R.string.ws_service_ip));
	}
	
	public static String getPort(){
		return get().getString("port", BaseApplication.getInstance().getResources().getString(R.string.ws_service_port));
	}
	
	public static String getAddress(){
//		return "http://"+getIp()+":"+getPort()+"/ECOService/BarcodeFunc.asmx";
		return "http://"+getIp()+":"+getPort()+"/BarcodeFunc.asmx";
		
	}
}
