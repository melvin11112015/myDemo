package com.scancode.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class AlertUtils {
	
	
	public static void alert(String msg,Context ctx) {

		AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
		builder.setTitle("提示");
		builder.setMessage(msg);
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		});
		builder.show().setCanceledOnTouchOutside(false);;
	}
	
	public static void alert(String msg,Context ctx,DialogInterface.OnClickListener listerner) {	
		alert("提示",msg,ctx,listerner);
	}
	
	/**
	 * 带确定，取消的提示框
	 * @param title  提示头
	 * @param msg   标题
	 * @param ctx   context
	 * @param positive   确定事件
	 * @param positives 取消事件
	 */
    public static void alert(String title,
    		String msg,
    		Context ctx,
    		String positiveText,
    		String negativeText,
    		DialogInterface.OnClickListener positive,
    		DialogInterface.OnClickListener negative){
    	AlertDialog.Builder builder=new AlertDialog.Builder(ctx);
    	builder.setTitle(title);
    	builder.setMessage(msg);
    	builder.setCancelable(false);
    	builder.setPositiveButton(positiveText, positive);
    	builder.setNegativeButton(negativeText, negative);
    	builder.show().setCanceledOnTouchOutside(false);
    }
	
	public static void alert(String title,String msg,Context ctx,DialogInterface.OnClickListener listerner) {
		AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
		builder.setTitle(title);
		builder.setMessage(msg);
		builder.setCancelable(false);
		builder.setPositiveButton("确定", listerner);
		builder.show().setCanceledOnTouchOutside(false);
	}
	/**
	 * 带确定，取消的提示框
	 * @param title  提示头
	 * @param msg   标题
	 * @param ctx   context
	 * @param positive   确定事件
	 * @param positives 取消事件
	 */
    public static void alert(String title,
    		String msg,
    		Context ctx,
    		DialogInterface.OnClickListener positive,
    		DialogInterface.OnClickListener negative){
    	AlertDialog.Builder builder=new AlertDialog.Builder(ctx);
    	builder.setTitle(title);
    	builder.setMessage(msg);
    	builder.setCancelable(false);
    	builder.setPositiveButton("确定", positive);
    	builder.setNegativeButton("取消", negative);
    	builder.show().setCanceledOnTouchOutside(false);;
    }
    
    
}
