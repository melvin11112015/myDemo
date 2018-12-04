package com.scancode.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Xml;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.scancode.BaseActivity;
import com.scancode.R;
import com.scancode.adapter.CompanyAdapter;
import com.scancode.initview.InitView;
import com.scancode.utils.SharedPreferencesHelper;
import com.scancode.webservice.AsyncCaller;
import com.scancode.webservice.BaseWebservice.OnCallbackListener;
import com.scancode.widget.HeaderView;
import com.scancode.widget.swipelistview.SwipeListView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: CompanyActivity
 * @Description: TODO 选择公司
 * @author zhaoruquan
 * @date 2015-9-12 下午3:28:54
 * 
 */
public class CompanyActivity extends BaseActivity {

	protected SwipeListView mListView;

	private HeaderView headerview;

	private CompanyAdapter adapter;
	@Override
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
		setContentView(R.layout.activity_company);
		
		initData();
	}

	@Override
	protected void initData() {

		mListView = (SwipeListView) findViewById(R.id.lv_requiremnt_supply);

		headerview = (HeaderView) findViewById(R.id.headerview);

		InitView.instance().initListView(mListView, this);
		if (getIntent() != null && getIntent().getExtras() != null && getIntent().getExtras().containsKey("firt")) {
		}else{
			headerview.showBack(View.VISIBLE);
		}
		getCompany();
		// InviteMessgeDao dao = new InviteMessgeDao(getActivity());
		// List<InviteMessage> msgs = dao.getMessagesList();
		// //设置adapter
		// NewFriendsMsgAdapter adapter = new
		// NewFriendsMsgAdapter(getActivity(), 1, msgs);
		// lv_new_friends_msg.setAdapter(adapter);

		// 设置adapter
		adapter = new CompanyAdapter(this, 1);
		mListView.setAdapter(adapter);
		mListView.setHasMore(false);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					final int position, long id) {
				showAlertDialog("提示", "是否确定设置公司名称", "是",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								SharedPreferencesHelper
										.get()
										.edit()
										.putString("company",
												adapter.getItem(position))
										.commit();
								if (getIntent() == null || getIntent().getExtras() == null || !getIntent().getExtras().containsKey("firt")) {
									
								}else{
									startActivity(MainActivity.class);
								}
								finish();
							}

						}, "否", null);
			}
		});
		
//		String result = "<NewDataSet><Table1><Name>ECO DS China</Name></Table1><Table1><Name>ECO DTChina</Name></Table1><Table1><Name>ECO DT HK</Name></Table1></NewDataSet>";
//		explain(result);
	}


	private void getCompany() {
		showLoadingDialog();
		Map<String, Object> map = new HashMap<String, Object>();
		requestWebService("GetCompany", map, onBackListener
		// new OnCallbackListener() {
		// @Override
		// public void onCallback(Object result, int state) {
		// // <NewDataSet><Table1><Name>ECO DS
		// China</Name></Table1><Table1><Name>ECO DT
		// China</Name></Table1><Table1><Name>ECO DT
		// HK</Name></Table1></NewDataSet>
		// System.out.println(result);
		// List<String> data = new ArrayList<String>();
		// adapter.appendList(data);
		// }
		// }
		);
	}

	private OnCallbackListener onBackListener = new OnCallbackListener() {

		@Override
		public void onCallback(Object result, int state) {
			dismissLoadingDialog();
			switch (state) {
			case AsyncCaller.SUCCESS:
				try {
					// <NewDataSet><Table1><Name>ECO DS
					// China</Name></Table1><Table1><Name>ECO DT
					// China</Name></Table1><Table1><Name>ECO DT
					// HK</Name></Table1></NewDataSet>
					System.out.println(result);
					
					adapter.appendList(explain(""+result));
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

	private List<String> explain(String result) {
		List<String> data = new ArrayList<String>();
		ByteArrayInputStream tInputStringStream = null;
		try {
			if (result != null && !result.trim().equals("")) {
				tInputStringStream = new ByteArrayInputStream(result.getBytes());
			}
		} catch (Exception e) {
			e.printStackTrace();
			return data;
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
					if (name.equalsIgnoreCase("Name")) {
						data.add(parser.nextText());
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
		
		return data;
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		 
        if (keyCode == KeyEvent.KEYCODE_BACK
                 && event.getRepeatCount() == 0) {
             return true;
         }
         return super.onKeyDown(keyCode, event);
     }

}
