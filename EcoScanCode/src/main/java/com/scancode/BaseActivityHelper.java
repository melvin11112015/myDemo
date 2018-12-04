package com.scancode;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Window;
import android.widget.Toast;

import com.scancode.webservice.AsyncCaller;
import com.scancode.webservice.BaseWebservice.OnCallbackListener;
import com.scancode.webservice.WebServiceTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BaseActivityHelper {

	private Context context;

	// webservice请求任务列表
	private List<WebServiceTask> taskList = new ArrayList<WebServiceTask>();

	// 支持Fragment加入任务保存列表
	private List<WebServiceTask> savedTaskList;

	private ProgressDialog mSpinner;

	public BaseActivityHelper(Context context) {
		this.context = context;
	}

	/**
	 * 打开加载中对话框
	 */
	public void showSpinner(Context ctx) {
		if (mSpinner == null) {
			mSpinner = new ProgressDialog(ctx);
			mSpinner.requestWindowFeature(Window.FEATURE_NO_TITLE);
			mSpinner.setMessage("正在处理中，请稍候�?");
		}

		if (!mSpinner.isShowing()) {
			mSpinner.show();
		}
	}

	/**
	 * 关闭加载中对话框
	 */
	public void dismissSpinner() {
		if (mSpinner != null && mSpinner.isShowing()) {
			mSpinner.dismiss();
		}
	}

	/**
	 * 获取用户Id
	 * 
	 * @return
	 */
	public String getUserId() {
		return context.getSharedPreferences("user", Context.MODE_PRIVATE)
				.getString("webUserId", null);
	}

	/**
	 * 取得任务id
	 * 
	 * @return
	 */
	public String getTaskId() {
		return context.getSharedPreferences("customer", Context.MODE_PRIVATE)
				.getString("taskId", null);
	}

	/**
	 * 获取客户Id
	 * 
	 * @return
	 */
	public String getCustomerId() {
		return context.getSharedPreferences("customer", Context.MODE_PRIVATE)
				.getString("customer_id", null);
	}

	public void clearAllWebServiceTask_Fragment() {
		if (savedTaskList != null) {
			savedTaskList.clear();
			savedTaskList = null;
		}
	}

	public void startAllWebServiceTask() {
		// 重新�?���?��下载任务
		for (WebServiceTask task : taskList) {
			if (task.task_state != 2) {
				task.start();
			}
		}
	}

	public void startAllWebServiceTask_Fragment() {
		// 重新�?���?��下载任务
		if (savedTaskList != null) {
			for (int i = savedTaskList.size() - 1; i >= 0; i--) {
				WebServiceTask task = savedTaskList.remove(i);
				if (task.task_state != 2) {
					task.start();
				}
			}
			savedTaskList = null;
		}
	}

	public void stopAllWebServiceTask() {
		// 停止�?��下载任务
		for (WebServiceTask task : taskList) {
			task.stop();
		}
	}

	public void stopAllWebServiceTask_Fragment() {
		// 停止�?��下载任务
		if (savedTaskList != null) {
			savedTaskList.clear();
			savedTaskList = null;
		}
		if (taskList.size() == 0) {
			return;
		}
		savedTaskList = new ArrayList<WebServiceTask>(taskList.size());
		for (WebServiceTask task : taskList) {
			if (task.task_state == 2) { // 已调用成功则不加�?
				continue;
			}
			savedTaskList.add(task);
			task.stop();
		}
	}

	/**
	 * 
	* @Title: requestWebService 
	* @Description: TODO	调用webServices
	* @param @param serviceName
	* @param @param params
	* @param @param callbackListener    设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void requestWebService(String serviceName,
			Map<String, Object> params, OnCallbackListener callbackListener) {
		WebServiceTask task = new WebServiceTask();
		task.serviceName = serviceName;
		task.parames = params;
		task.callbackListener = callbackListener;

		taskList.add(task);
		task.start();
	}

	/**
	 * 处理webservice返回(验证)
	 * 
	 * @param res
	 * @param state
	 * @return
	 */
	public String processWebserviceDataToString(Object res, int state) {
		return _processWebserviceData(res, state, 1);
	}

	/**
	 * 处理webservice返回数据(返回数组)
	 * 
	 * @param res
	 * @param state
	 * @return
	 */
	public JSONObject processWebserviceData(Object res, int state) {
		String _res = _processWebserviceData(res, state, 1);
		return _processWebserviceData(_res, 1);
	}

	/**
	 * 处理webservice返回数据(返回对象)
	 * 
	 * @param res
	 * @param state
	 * @return
	 */
	public JSONObject processWebserviceDataNotArray(Object res, int state) {
		String _res = _processWebserviceData(res, state, 2);
		return _processWebserviceData(_res, 2);
	}

	private JSONObject _processWebserviceData(String result, int type) {
		if (TextUtils.isEmpty(result)) {
			return null;
		}
		try {
			if (type == 1) {
				JSONArray arr = new JSONArray(result);
				if (arr == null || arr.length() <= 0) {
					return null;
				}
				return arr.getJSONObject(0);
			} else {
				return new JSONObject(result);
			}
		} catch (JSONException e) {
			toast("程序处理错误, 请联系管理员");
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * 处理webservice返回数据
	 */
	private String _processWebserviceData(Object res, int state, int type) {
		if (state == AsyncCaller.NETERROR) {
			toast("网络异常, 请检查网络是否可用");
			return null;
		} else if (state == AsyncCaller.FAIL) {
			toast("服务器返回失败");
			return null;
		}

		String result = (String) res;
		if (TextUtils.isEmpty(result) || state != AsyncCaller.SUCCESS
				|| "fail".equals(result)) {
			toast("服务器异常? 请联系管理员!");
			return null;
		}

		return (String) res;
	}

	/**
	 * 提示信息
	 * 
	 * @param msg
	 */
	protected void toast(String msg) {
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}

	/**
	 * 为所有跳转构造Intent
	 */
	public Intent createIntent(Class<? extends Activity> targetClass,
			Bundle extras) {
		Intent intent = new Intent();
		intent.setClass(context, targetClass);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		if (extras != null) {
			intent.putExtras(extras);
		}
		return intent;
	}

}
