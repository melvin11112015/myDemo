package com.scancode.widget;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.scancode.R;

public class DialogMessage extends AlertDialog {

	private Context context;
	
	private CancelHttp cancenHttp;//按返回键盘的时候取消网络请求
	
	private String tag;
	public DialogMessage(Context context) {
		super(context);
		this.createDialog(context);
		this.context = context;
	}

	public Dialog createDialog(Context context) {
		dialog = new Dialog(context, R.style.dialog);
		View view = View.inflate(context, R.layout.loading_meaasge, null);
		dialog.setContentView(view);
		mTitle = (TextView) view.findViewById(R.id.loading_tv);
		this.context = context;
		return dialog;
	}

	private Dialog dialog;
	TextView mTitle;


	public void dissmissDialog() {
		if (cancenHttp != null) {
			cancenHttp.cancel(tag);
		}
		if (dialog != null) {
			dialog.dismiss();
			dialog = null;
		}
	}

	public void setMessage(String msg){
    	mTitle.setText(msg);
    }
	
	public void setBack(String tag, CancelHttp cancenHttp){
		this.tag = tag;
		this.cancenHttp = cancenHttp;
	}
	
	public void showDialog() {
		setCanceledOnTouchOutside(false);
		if (dialog != null) {
			dialog.setCanceledOnTouchOutside(false);
			dialog.show();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
			dissmissDialog();
            return true;
        }
        return super.onKeyDown(keyCode, event);
	}
	
	public interface CancelHttp{
		public void cancel(String tag);
	}
}
