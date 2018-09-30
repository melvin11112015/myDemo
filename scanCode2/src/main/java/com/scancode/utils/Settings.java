package com.scancode.utils;

import com.scancode.BaseApplication;
import com.scancode.R;

public class Settings {
	/**
	 * 获取命名空间
	 * @return
	 */
	public static String getWSNameSpace() {
		return BaseApplication.getInstance().getResources()
						.getString(R.string.ws_server_namespace);
	}
	
	/**
	 * 获取url
	 * @return
	 */
	public static String getWSURL() {
//		return BaseApplication.getInstance().getResources()
//				.getString(R.string.ws_server_url);
		return SharedPreferencesHelper.getAddress();
	}
	
	/**
	 * 获取action前缀
	 * @return
	 */
	public static String getWSActionPrefix() {
		return BaseApplication.getInstance().getResources()
				.getString(R.string.ws_server_action_prefix);
	}
	
}
