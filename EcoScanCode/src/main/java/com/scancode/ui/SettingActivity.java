package com.scancode.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Xml;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.scancode.BaseActivity;
import com.scancode.BaseApplication;
import com.scancode.R;
import com.scancode.utils.VresionUpdate;
import com.scancode.webservice.AsyncCaller;
import com.scancode.webservice.BaseWebservice.OnCallbackListener;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SettingActivity extends BaseActivity implements OnClickListener{
	
	private LinearLayout modifyPsw;
	
	private LinearLayout company;
	
	private LinearLayout versionLly;
	
	private Button outLogin;
	
	private String version;
	
	private String url;
	
	private final int HAVE_VERSION_UPDATE = 03;
	private final int HAVE_NOT_UPDATE = 04;
	private final int CHECK_VERSION_FAIL = 05;
	private final int DOWNLOAD_FAIL = -1;
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
		setContentView(R.layout.activity_setting);
		initData();
	}

	@Override
	protected void initData() {
		company = (LinearLayout) findViewById(R.id.company_lly);
		modifyPsw = (LinearLayout) findViewById(R.id.modify_psw_lly);
		outLogin = (Button) findViewById(R.id.outl_login_btn);
		versionLly = (LinearLayout) findViewById(R.id.version_lly);
		
		company.setOnClickListener(this);
		modifyPsw.setOnClickListener(this);
		outLogin.setOnClickListener(this);
		versionLly.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.company_lly:
			startActivity(CompanyActivity.class);
			break;
		case R.id.modify_psw_lly:
			startActivity(ModifyPasswordActivity.class);
			break;
		case R.id.version_lly:
			getVersion();
			break;
		case R.id.outl_login_btn:
showAlertDialog("提示", "是否退出登陆？", "取消", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					
				}
			}, "确定", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					startActivity(LoginActivity.class);
					BaseApplication.getInstance().exitToLoginActivity();
				}});
			break;
		}
	}
	
	private void getVersion(){
		showLoadingDialog();
    	Map<String, Object> property = new HashMap<String, Object>();
    	requestWebService("GetVersion", property, onBackListener);
	}

	private OnCallbackListener onBackListener = new OnCallbackListener() {

		@Override
		public void onCallback(Object result, int state) {
			dismissLoadingDialog();
			switch (state) {
			case AsyncCaller.SUCCESS:
				try {
					if (result == null) {
						showCustomToast("获取数据失败");
						return;
					}
					String res = "" + result;
					if (res.equals("-100")) {
						showCustomToast("没有公司名称");
						return;
					}
					if (res.equals("-99")) {
						showCustomToast("服务器异常，请重试！");
						return;
					}
					if (res.equals("0")) {
						showCustomToast("获取版本号失败");
					}
					explain(""+result);
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
	
	private void explain(String result) {
		ByteArrayInputStream tInputStringStream = null;
		try {
			if (result != null && !result.trim().equals("")) {
				tInputStringStream = new ByteArrayInputStream(result.getBytes());
			}
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		XmlPullParser parser = Xml.newPullParser();
		try {
			parser.setInput(tInputStringStream, "UTF-8");
			int eventType = parser.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				switch (eventType) {
				case XmlPullParser.START_DOCUMENT:// 文档开始事件,可以进行数据初始化处理
					break;
				case XmlPullParser.START_TAG:// 开始元素事件
					String name = parser.getName();
					if (name.equalsIgnoreCase("LastVersion")) {
						version = parser.nextText();
					}
					if (name.equalsIgnoreCase("ItemName")) {
						url = parser.nextText();
					}
					break;
				case XmlPullParser.END_TAG:// 结束元素事件
					// }
					break;
				}
				eventType = parser.next();
			}
			tInputStringStream.close();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (TextUtils.isEmpty(version)) {
			showCustomToast("获取版本信息失败，请重试");
			return;
		}
		if (Integer.valueOf(version) <= getResources().getInteger(R.integer.version_coe)) {
			showCustomToast("当前版本为最新版");
			return;
		}
		mHandler.sendEmptyMessage(HAVE_VERSION_UPDATE);
	}
	
	Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			int state = msg.what;
			switch (state) {

			case HAVE_VERSION_UPDATE:// 有新版本更新
//				String downloadUrl1 = msg.getData().getString("downloadUrl");
				String updateContent = "最新版本已更新请下载!";
				VresionUpdate vu = new VresionUpdate(SettingActivity.this,
						url, this);
				try {
					vu.showCommonUpdateDialog(updateContent,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
								}
							});
				} catch (Exception e) {
					e.printStackTrace();
				}

				break;
			case HAVE_NOT_UPDATE:// 无新版本更新
				// stop();
				break;
			case DOWNLOAD_FAIL:// 下载APK包出错
				// stop();
				break;
			case CHECK_VERSION_FAIL:// 检测版本出错
				// stop();
				break;

			}

		};
	};
}
