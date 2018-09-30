package com.scancode;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.Application;

import com.scancode.ui.LoginActivity;
import com.seuic.scanner.DecodeInfo;
import com.seuic.scanner.DecodeInfoCallBack;
import com.seuic.scanner.Scanner;
import com.seuic.scanner.ScannerFactory;
import com.seuic.scanner.ScannerKey;

public class BaseApplication extends Application {
	private static BaseApplication mBaseApplication;
	private List<Activity> activityList = new LinkedList<Activity>();
	private Scanner mScanner;
	
	public static BaseApplication getInstance() {
		return mBaseApplication;
	}
	public void addActivity(Activity activity) {
		activityList.add(activity);
	}
	public void exit() {
		for (Activity activity : activityList) {
			activity.finish();
		}
		System.exit(0);
	}
	
	public void exitToLoginActivity() {
		for (Activity activity : activityList) {
			if(!(activity instanceof LoginActivity))
				activity.finish();
		}
		System.exit(0);
	}
	@Override
	public void onCreate() {
		super.onCreate();
		mBaseApplication = this;
		initScan();
		mScanner.open();
	}
	
	public Scanner getScance(){
		if (mScanner != null) {
			initScan();
		}
		return mScanner;
	}
	public void initScan(){
		mScanner = ScannerFactory.getScanner(this);
		new Thread(runnable).start();
	}
	Runnable runnable = new Runnable() {

		@Override
		public void run() {
			try{
				int ret1 = ScannerKey.open();
				if (ret1 > -1) {
					while (true) {
						int ret = ScannerKey.getKeyEvent();
						if (ret > -1) {
							switch (ret) {
							case ScannerKey.KEY_DOWN:
								mScanner.startScan();
								break;
							case ScannerKey.KEY_UP:
								mScanner.stopScan();
								break;
							}
						}
					}
				}
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	};
	
	public void setListener(){
		mScanner.setDecodeInfoCallBack(new DecodeInfoCallBack() {
			@Override
			public void onDecodeComplete(DecodeInfo arg0) {
				
			}
		});
	}
}


