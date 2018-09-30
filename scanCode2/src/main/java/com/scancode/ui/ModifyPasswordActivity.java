package com.scancode.ui;

import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.scancode.BaseActivity;
import com.scancode.R;
import com.scancode.utils.SharedPreferencesHelper;
import com.scancode.webservice.AsyncCaller;
import com.scancode.webservice.BaseWebservice.OnCallbackListener;
import com.scancode.widget.HeaderView;

public class ModifyPasswordActivity extends BaseActivity {

	private EditText oldEt;
	private EditText newEt;
	private EditText new2Et;
	
	private HeaderView headerview;
	protected Handler getHandler() {
		// TODO Auto-generated method stub
		return handler;
	}
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				break;
			default:
				//Toast.makeText(About.this, "获取关于信息失败", Toast.LENGTH_SHORT).show();
				break;
			}
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_modidfy_psw);
		initData();
	}

	@Override
	protected void initData() {
		oldEt = (EditText) findViewById(R.id.old_psw_et);
		newEt = (EditText) findViewById(R.id.new_psw_et);
		new2Et = (EditText) findViewById(R.id.new_psw2_et);
		
		headerview = (HeaderView) findViewById(R.id.headerview);
		headerview.setOtherTextClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				modifyPsw();
			}
		});
	}
	
	private void modifyPsw(){
		String psw = oldEt.getText().toString().trim();
		String pswNew = newEt.getText().toString().trim();
		String pswNew2 = new2Et.getText().toString().trim();
		if (TextUtils.isEmpty(psw)) {
			showCustomToast("请输入旧密码");
			return;
		}
		if (TextUtils.isEmpty(pswNew)) {
			showCustomToast("请输入新密码");
			return;
		}
		if (TextUtils.isEmpty(pswNew2)) {
			showCustomToast("请输入确认密码");
			return;
		}
		if (!psw.equals(SharedPreferencesHelper.getUser().getPassword())) {
			showCustomToast("旧密码不正确");
			return;
		}
		if (!pswNew.equals(pswNew2)) {
			showCustomToast("新密码和确认密码不一致");
			return;
		}
		showLoadingDialog();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userid",
				SharedPreferencesHelper.getUser().getUserId());
		map.put("oldPassword", SharedPreferencesHelper.getUser().getPassword());
		map.put("newPassword", pswNew);
		requestWebService("ChangePassword", map, onBackListener);
	}
	
	private OnCallbackListener onBackListener = new OnCallbackListener() {

		@Override
		public void onCallback(Object result, int state) {
			dismissLoadingDialog();
			switch (state) {
			case AsyncCaller.SUCCESS:
				try {
					dismissLoadingDialog();
					String res = "" + result;
					if (TextUtils.isEmpty(res)) {
						showCustomToast("修密码失败，请重试！");
						return;
					}
					if (res.equals("-100")) {
						showCustomToast("没有公司名称");
						return;
					}
					if (res.equals("-99")) {
						showCustomToast("服务器异常，请重试！");
						return;
					}
					if (res.equals("1")) {
						showCustomToast("修改成功！");
						SharedPreferencesHelper.get().edit().putString("password", newEt.getText().toString().trim());
						finish();
						return;
					}
					showCustomToast("修改密码失败！");
					System.out.println(result);
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case AsyncCaller.FAIL:
				showShortToast("网络连接失败，请稍后重试！");
				break;
			case AsyncCaller.NETERROR:
				showShortToast("网络连接失败，请稍后重试！");
				break;
			default:
				break;
			}
		}
	};

}
