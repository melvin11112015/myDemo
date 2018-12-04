package com.scancode;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import com.scancode.utils.Tools;
import com.scancode.webservice.BaseWebservice.OnCallbackListener;
import com.scancode.widget.DialogMessage;
import com.scancode.widget.HandyTextView;

import java.util.Map;

/**
 * @fileName BaseActivity.java
 * @description Activity基类
 * @author lk
 * @version 1.0
 */
public abstract class BaseActivity extends FragmentActivity {
	/** Appliction基类对象 **/
	protected BaseApplication mApplication;
	protected Activity mActivity;
	/** 上下文 **/
	public Context mContext;

	protected long exitTime = 0;
	protected DialogMessage dialogMessage;
	protected Dialog waitingDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 获取BaseApplication对象
		mApplication = BaseApplication.getInstance();
		mContext = this;
		BaseApplication.getInstance().addActivity(this);
		requestWindowFeature(Window.FEATURE_NO_TITLE);    
		// dialogMessage = new DialogMessage(this);
	}

	protected void showLoadingDialog(int msgId) {
		dialogMessage = new DialogMessage(this);
		dialogMessage.showDialog();
		dialogMessage.setMessage(getString(msgId));

	}

	protected void showLoadingDialog(String msg) {
		dialogMessage = new DialogMessage(this);
		dialogMessage.showDialog();
		dialogMessage.setMessage(msg);

	}

	protected void showLoadingDialog() {
		dialogMessage = new DialogMessage(this);
		dialogMessage.showDialog();

	}

	protected void dismissLoadingDialog() {
		if (dialogMessage != null) {
			dialogMessage.dissmissDialog();
		}
	}

	// Intent intnte;
	// /*启动LoadingActivity*/
	// protected void showloadingActivity(){
	// intnte = new Intent(mContext,LoadingDialog.class);
	// thread.start();
	// }
	//
	// protected void showloadingActivity(int msgId) {
	// intnte = new Intent(mContext,LoadingDialog.class);
	// intnte.putExtra("message", getString(msgId));
	// thread.start();
	//
	// }
	// protected void showloadingActivity(String msg) {
	// intnte = new Intent(mContext,LoadingDialog.class);
	// intnte.putExtra("message", msg);
	// // startActivity(serverIntent);
	// thread.start();
	// }
	//
	// public Thread thread = new Thread(){
	//
	// @Override
	// public void run() {
	// if (intnte == null) {
	// return;
	// }
	// startActivity(intnte);
	// }
	// };
	// protected void dismissLoadingActivity() {
	// Activity activity = BaseApplication.getInstance().getActivity();
	// //判断是否是LoadingActivity
	// if(null == activity || !(activity instanceof LoadingDialog)){
	// return;
	// }
	// activity.finish();
	// BaseApplication.getInstance().setActivity(null);
	// }

	// public BaseActivity(BaseApplication application, Activity activity,
	// Context context) {
	// mApplication = application;
	// mActivity = activity;
	// mContext = context;
	// }

	public BaseActivity() {

	}

	/** 界面数据初始化 **/
	protected abstract void initData();

	/** 短暂显示Toast提示(来自res) **/
	protected void showShortToast(int resId) {
		Toast.makeText(this, getString(resId), Toast.LENGTH_SHORT).show();
	}

	/** 短暂显示Toast提示(来自String) **/
	protected void showShortToast(String text) {
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
	}

	/** 长时间显示Toast提示(来自res) **/
	protected void showLongToast(int resId) {
		Toast.makeText(this, getString(resId), Toast.LENGTH_LONG).show();
	}

	/** 长时间显示Toast提示(来自String) **/
	protected void showLongToast(String text) {
		Toast.makeText(this, text, Toast.LENGTH_LONG).show();
	}

	/** Debug输出Log日志 **/
	protected void showLogDebug(String tag, String msg) {
		Log.d(tag, msg);
	}

	/** Error输出Log日志 **/
	protected void showLogError(String tag, String msg) {
		Log.e(tag, msg);
	}

	/** 通过Class跳转界面 **/
	protected void startActivity(Class<?> cls) {
		startActivity(cls, null);
	}

	/** 通过Class跳转界面 **/
	protected void startActivityForResult(Class<?> cls, int requestCode) {
		startActivityForResult(cls, null, requestCode);
	}

	/** 含有Bundle通过Class跳转界面 **/
	protected void startActivityForResult(Class<?> cls, Bundle bundle,
			int requestCode) {
		Intent intent = new Intent();
		intent.setClass(this, cls);
		if (bundle != null) {
			intent.putExtras(bundle);
		}
		startActivityForResult(intent, requestCode);
		overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
	}
	


	/** 含有Bundle通过Class跳转界面 **/
	protected void startActivity(Class<?> cls, Bundle bundle) {
		Intent intent = new Intent();
		intent.setClass(this, cls);
		if (bundle != null) {
			intent.putExtras(bundle);
		}
		startActivity(intent);
		overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
	}

	/** 通过Action跳转界面 **/
	protected void startActivity(String action) {
		startActivity(action, null);
	}

	/** 含有Bundle通过Action跳转界面 **/
	protected void startActivity(String action, Bundle bundle) {
		Intent intent = new Intent();
		intent.setAction(action);
		if (bundle != null) {
			intent.putExtras(bundle);
		}
		startActivity(intent);
		overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
	}

	/** 含有标题和内容的对话框 **/
	protected AlertDialog showAlertDialog(String title, String message) {
		AlertDialog alertDialog = new AlertDialog.Builder(this).setTitle(title)
				.setMessage(message).show();
		return alertDialog;
	}

	/** 显示自定义Toast提示(来自res) **/
	protected void showCustomToast(int resId) {
		View toastRoot = LayoutInflater.from(BaseActivity.this).inflate(
				R.layout.common_toast, null);
		((HandyTextView) toastRoot.findViewById(R.id.toast_text))
				.setText(getString(resId));
		Toast toast = new Toast(BaseActivity.this);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setView(toastRoot);
		toast.show();
	}

	/** 显示自定义Toast提示(来自String) **/
	protected void showCustomToast(String text) {
		View toastRoot = LayoutInflater.from(BaseActivity.this).inflate(
				R.layout.common_toast, null);
		((HandyTextView) toastRoot.findViewById(R.id.toast_text)).setText(text);
		Toast toast = new Toast(BaseActivity.this);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setView(toastRoot);
		toast.show();
	}

	/** 含有标题、内容、两个按钮的对话框 **/
	protected AlertDialog showAlertDialog(String title, String message,
			String positiveText,
			DialogInterface.OnClickListener onPositiveClickListener,
			String negativeText,
			DialogInterface.OnClickListener onNegativeClickListener) {
		AlertDialog alertDialog = new AlertDialog.Builder(this).setTitle(title)
				.setMessage(message)
				.setPositiveButton(positiveText, onPositiveClickListener)
				.setNegativeButton(negativeText, onNegativeClickListener)
				.show();
		return alertDialog;
	}

	/** 含有标题、内容、图标、两个按钮的对话框 **/
	protected AlertDialog showAlertDialog(String title, String message,
			int icon, String positiveText,
			DialogInterface.OnClickListener onPositiveClickListener,
			String negativeText,
			DialogInterface.OnClickListener onNegativeClickListener) {
		AlertDialog alertDialog = new AlertDialog.Builder(this).setTitle(title)
				.setMessage(message).setIcon(icon)
				.setPositiveButton(positiveText, onPositiveClickListener)
				.setNegativeButton(negativeText, onNegativeClickListener)
				.show();
		return alertDialog;
	}
	
	
	/** 含有标题、内容、一个确认按钮的对话框 **/
	protected AlertDialog showAlertDialog(String title, String message,
			String positiveText,
			DialogInterface.OnClickListener onPositiveClickListener
		    ) {
		AlertDialog alertDialog = new AlertDialog.Builder(this).setTitle(title)
				.setMessage(message)
				.setPositiveButton(positiveText, onPositiveClickListener)				
				.show();
		alertDialog.setCanceledOnTouchOutside(false);
		alertDialog.setCancelable(false);
		return alertDialog;
	}

	protected String getEditTextText(EditText et) {
		return Tools.isEmpty(et) ? "" : et.getText().toString();
	}

	protected void clearEditText(EditText et) {
		et.setText("");
	}

	/** 带有右进右出动画的退出 **/
	public void finish() {
		super.finish();
		overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
	}

	/** 默认退出 **/
	protected void defaultFinish() {
		super.finish();
	}

	protected void exit() {
		if ((System.currentTimeMillis() - exitTime) > 2000) {
			showCustomToast("再按一次退出程序");
			exitTime = System.currentTimeMillis();
		} else {
			BaseApplication.getInstance().exit();
		}
	}

	/**
	 * 获取登录用户ID
	 * 
	 * @return
	 */
	protected String getUserId() {
		return helper.getUserId();
	}

	protected BaseActivityHelper helper = new BaseActivityHelper(mContext);

	protected void requestWebService(String serviceName,
			Map<String, Object> params, OnCallbackListener callbackListener) {
		helper.requestWebService(serviceName, params, callbackListener);
	}

	public void stopWebService() {
		helper.stopAllWebServiceTask();
	}
	
	
	abstract protected Handler getHandler();
	
	protected synchronized boolean sendMessage(int nMSGID) {
		boolean ret = false;
		if (getHandler() == null) {
			return ret;
		}
		
		if (!getHandler().hasMessages(nMSGID)) {
			Message msg = new Message();
			msg.what = nMSGID;
			ret = getHandler().sendMessage(msg);
		} else {
			ret = getHandler().sendMessage(getHandler().obtainMessage(nMSGID));
		}
		return ret;
	}
}
