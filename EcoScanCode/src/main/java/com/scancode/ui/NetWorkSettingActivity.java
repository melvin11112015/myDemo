package com.scancode.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.scancode.BaseActivity;
import com.scancode.R;
import com.scancode.utils.SharedPreferencesHelper;
import com.scancode.widget.HeaderView;

/** 
* @ClassName: NetWorkSettingActivity 
* @Description: TODO	设置网络地址
* @author zhaoruquan
* @date 2015-9-14 下午10:36:21 
*  
*/
public class NetWorkSettingActivity extends BaseActivity{
	
	private EditText ipEt;//ip
	private EditText portEt;//端口
	
	private HeaderView headerView;//头部
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
		setContentView(R.layout.activity_network_setting);
		initData();
	}

	@Override
	protected void initData() {
		ipEt = (EditText) findViewById(R.id.ip_et);
		portEt = (EditText) findViewById(R.id.port_et);
		
		headerView = (HeaderView) findViewById(R.id.headerview);
		
		ipEt.setText(SharedPreferencesHelper.getIp());
		portEt.setText(SharedPreferencesHelper.getPort());
		
		headerView.setOtherTextClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (TextUtils.isEmpty(ipEt.getText().toString().trim())) {
					showCustomToast("请输入ip");
					return;
				}
				if (TextUtils.isEmpty(portEt.getText().toString().trim())) {
					showCustomToast("请输入端口");
					return;
				}
				showAlertDialog("提示", "是否要修改该访问网络地址", "是", new DialogInterface.OnClickListener(){
					@Override
					public void onClick(DialogInterface dialog, int which) {
						setting();
					}
				}, "否", null);
			}
		});
	}

	private void setting(){
		SharedPreferencesHelper.get().edit().putString("ip", ipEt.getText().toString().trim()).commit();
		SharedPreferencesHelper.get().edit().putString("port", portEt.getText().toString().trim()).commit();
		showCustomToast("修改成功!");
		finish();
	}
}
