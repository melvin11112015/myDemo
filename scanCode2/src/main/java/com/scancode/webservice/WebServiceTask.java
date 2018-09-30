package com.scancode.webservice;

import java.util.Map;

import android.os.Build;

import com.scancode.webservice.BaseWebservice.OnCallbackListener;


public class WebServiceTask {
	public String serviceName;
	public OnCallbackListener callbackListener; 
	public Map<String, Object> parames;
	
	public volatile int task_state = 0;		// not run, 1 running, 2 finish
	
	private WebServiceAsyncTask task;
	
	public void start() {
		if(task != null) {
			task.cancel(true);
			task = null;
		}
		task = new WebServiceAsyncTask(serviceName, parames, new OnCallbackListener() {
			@Override
			public void onCallback(Object result, int state) {
				task_state = 2;	// finish...
				System.out.println("task finishing...");
				if(callbackListener != null) {
					callbackListener.onCallback(result, state);
				}
			}
		});
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			task.executeOnExecutor(BaseWebservice.threadPool);
		} else {
			task.execute();
		}
		task_state = 1;	// running
	}
	
	public void stop() {
		if(task != null) {
			task.cancel(true);
			task = null;
		}
	}
	
}
