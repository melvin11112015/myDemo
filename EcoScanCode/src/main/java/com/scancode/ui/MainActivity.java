package com.scancode.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.scancode.BaseActivity;
import com.scancode.R;
import com.scancode.utils.SharedPreferencesHelper;
import com.scancode.widget.HeaderView;

/** 
* @ClassName: MainActivity 
* @Description: TODO	功能主界面
* @author zhaoruquan
* @date 2015-9-12 下午4:48:01 
*  
*/
public class MainActivity extends BaseActivity implements OnClickListener{

	private Button storageIbtn;//入库
	private Button theLibraryIbtn;//出库
	private Button splitIbtn;//拆分
	private Button switchIbtn;//切换公司
	
	private HeaderView headerview;//头部
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
		setContentView(R.layout.activity_main);
		initData();
	}

	@Override
	protected void initData() {
		storageIbtn = (Button) findViewById(R.id.storage_ibtn);
		theLibraryIbtn = (Button) findViewById(R.id.the_library_ibtn);
		splitIbtn = (Button) findViewById(R.id.split_ibtn);
		switchIbtn = (Button) findViewById(R.id.switch_ibtn);
		
		headerview = (HeaderView) findViewById(R.id.headerview);
		//设置公司名称
		headerview.setTitle(SharedPreferencesHelper.get().getString("company", ""));
		headerview.showOtherTv(View.VISIBLE);
		
		
		storageIbtn.setOnClickListener(this);
		theLibraryIbtn.setOnClickListener(this);
		splitIbtn.setOnClickListener(this);
		switchIbtn.setOnClickListener(this);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		headerview.setOtherText(SharedPreferencesHelper.getUser().getUserName());
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.storage_ibtn://入库
			startActivity(StorageActivity.class);
			break;
		case R.id.the_library_ibtn://出库
			startActivity(TheLibraryActivity.class);
			break;
		case R.id.split_ibtn://拆分
			startActivity(SplitActivity.class);
			break;
		case R.id.switch_ibtn://切换
			startActivity(SettingActivity.class);
			break;
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		 
        if (keyCode == KeyEvent.KEYCODE_BACK
                 && event.getRepeatCount() == 0) {
        	Intent intent = new Intent(Intent.ACTION_MAIN);      
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);      
            intent.addCategory(Intent.CATEGORY_HOME);      
            startActivity(intent);
             return true;
         }
         return super.onKeyDown(keyCode, event);
     }
}
