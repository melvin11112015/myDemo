package com.scancode.ui;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Xml;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.scancode.BaseActivity;
import com.scancode.R;
import com.scancode.initview.User;
import com.scancode.utils.SharedPreferencesHelper;
import com.scancode.webservice.AsyncCaller;
import com.scancode.webservice.BaseWebservice.OnCallbackListener;

/** 
* @ClassName: LoginActivity 
* @Description: TODO	登录
* @author zhaoruquan
* @date 2015-9-14 下午10:31:39 
*  
*/
public class LoginActivity extends BaseActivity {

	private EditText nameEt;//用户名
	private EditText passwordEt;//密码
	private LinearLayout nameLayout;
	private LinearLayout passwordLayout;
	
	private Button loginBtn;//登陆
	
	private ImageView settingIv;//设置
	
	private CheckBox check_id;
	
	private ScrollView sView;
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
        setContentView(R.layout.activity_login);
        nameEt = (EditText) findViewById(R.id.login_name_et);
        passwordEt = (EditText) findViewById(R.id.login_password_et);
        nameLayout = (LinearLayout) findViewById(R.id.nameLayout);
        passwordLayout = (LinearLayout) findViewById(R.id.passwordLayout);
        settingIv = (ImageView) findViewById(R.id.setting_iv);
        check_id = (CheckBox) findViewById(R.id.check_id);
        settingIv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(NetWorkSettingActivity.class);
			}
		});
        //用户名失去焦点事件
        nameEt.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View arg0, boolean hasFocus) {
				if(hasFocus){
					nameLayout.setBackgroundResource(R.drawable.user2);
				}else{
					nameLayout.setBackgroundResource(R.drawable.user1);
				}
			}
		});

        //密码控件失去焦点事件
        passwordEt.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View arg0, boolean hasFocus) {
				if(hasFocus){
					passwordLayout.setBackgroundResource(R.drawable.password2);
				}else{
					passwordLayout.setBackgroundResource(R.drawable.password1);
				}
			}
		});
        loginBtn = (Button) findViewById(R.id.login_login_et);
        
        //点击登录事件
        loginBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
//				nameEt.setText("test1");
//				passwordEt.setText("test123");
				login();
			}
		});
        sView = (ScrollView) findViewById(R.id.sc_view);
        nameEt.setText(SharedPreferencesHelper.getUser().getAccount());
        if (SharedPreferencesHelper.get().getBoolean("save", false)) {
        	check_id.setChecked(true);
        	passwordEt.setText(SharedPreferencesHelper.getUser().getPassword());
        }
//        login();
    }
    
    private void getVi(){
//    	<NewDataSet><Table1><LastVersion>1.02</LastVersion><AppUrl>http://192.168.0.100/scancode.apk</AppUrl></Table1></NewDataSet>
    	Map<String, Object> property = new HashMap<String, Object>();
    	requestWebService("GetVersion", property, onLoginListener);
    }
    
    //登录
    private void login(){
    	String name = nameEt.getText().toString().trim();
		String password = passwordEt.getText().toString().trim();
		//判断用户名是否为空
		if(TextUtils.isEmpty(name))
		{
			showShortToast("请输入用户名");
			nameEt.requestFocus();
			return;
		}
		//判断密码是否为空
		if(TextUtils.isEmpty(password))
		{
			showShortToast("请输入密码");
			passwordEt.requestFocus();
			return;
		}
		//初始化调用登录接口参数
		Map<String, Object> property = new HashMap<String, Object>();
		property.put("userid", name); //这个是传递参数的
		property.put("password", password); //这个是传递参数的
		showLoadingDialog();
//		if(!isNetworkConnected())
//		{
//			dismissLoadingDialog();
//			showShortToast("网络连接失败，请稍后重试！");
//			return;
//		}
		//调用登录接口
		requestWebService("UserLogin", property, onLoginListener);
    }
    
    //登录回调
    private OnCallbackListener onLoginListener = new OnCallbackListener() {

		@Override
		public void onCallback(Object result, int state) {
			dismissLoadingDialog();
			switch (state) {
			case AsyncCaller.SUCCESS:
				try {
//					//处理登录返回的数据
//					JSONObject jo = new JSONObject("" + result);
//					if (jo.getInt("status") != 1) {
//						showLongToast(jo.getString("data"));
//						return;
//					}
//					//设置用户名
//					jo = new JSONObject(jo.getString("data"));
					System.out.println(result);
					String res = ""+result;
					if (TextUtils.isEmpty(res)) {
						showCustomToast("登录失败，请重试！");
						return;
					}
					if (res.equals("-99")) {
						showCustomToast("服务器异常，请重试！");
						return;
					}
					if (res.equals("0")) {
						showCustomToast("用户名或密码错误！");
						return;
					}
					explain(""+result);
					//showCustomToast(SharedPreferencesHelper.getUser().getUserId());
					if (TextUtils.isEmpty(SharedPreferencesHelper.get().getString("company", ""))) {
						Intent intent = new Intent(LoginActivity.this, CompanyActivity.class);
						intent.putExtra("firt", true);
						startActivity(intent);
						finish();
						return;
					}
					startActivity(MainActivity.class);
					finish();
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
	
    
	@Override
	protected void initData() {
		
	}	
	
	private void explain(String result) {
		User userInfo = new User();
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
					if (name.equalsIgnoreCase("UserId")) {
						userInfo.setUserId(parser.nextText());
					}
					if (name.equalsIgnoreCase("UserName")) {
						userInfo.setUserName(parser.nextText());
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
			showCustomToast("登录失败，请重试");
			e.printStackTrace();
			return;
		} catch (IOException e) {
			showCustomToast("登录失败，请重试");
			e.printStackTrace();
			return;
		}
		
		if (TextUtils.isEmpty(userInfo.getUserId())) {
			showCustomToast("用户名或密码错误");
			return;
		}
		userInfo.setAccount(nameEt.getText().toString().trim());
		userInfo.setPassword(passwordEt.getText().toString().trim());
		//TODO 	还需要初始化用户id和用户名称
		SharedPreferencesHelper.addUser(userInfo);
		SharedPreferencesHelper.get().edit().putBoolean("save", check_id.isChecked()).commit();
	}
}
