package com.scancode.ui;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Xml;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.scancode.BaseActivity;
import com.scancode.R;
import com.scancode.adapter.TheLibraryAdapter;
import com.scancode.initview.InitView;
import com.scancode.model.DO;
import com.scancode.model.DONew;
import com.scancode.utils.SharedPreferencesHelper;
import com.scancode.webservice.AsyncCaller;
import com.scancode.webservice.BaseWebservice.OnCallbackListener;
import com.scancode.widget.HeaderView;
import com.scancode.widget.swipelistview.SwipeListView;

/** 
* @ClassName: SplitActivity 
* @Description: TODO	出货单
* @author zhaoruquan
* @date 2015-9-12 下午6:06:31 
*  
*/
public class TheLibraryActivity extends BaseActivity {

	protected SwipeListView mListView;
	
	private HeaderView headerview;
	
	private TheLibraryAdapter adapter;
	
	private List<DONew> dos = new ArrayList<DONew>();
	
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
		setContentView(R.layout.activity_the_library);
		initData();
	}

	@Override
	protected void initData() {
		
		mListView = (SwipeListView) findViewById(R.id.lv_requiremnt_supply);
		
		headerview = (HeaderView) findViewById(R.id.headerview);
		
		InitView.instance().initListView(mListView, this);
		
		getCompany();

		// 设置adapter
		adapter = new TheLibraryAdapter(this, 1);
		mListView.setAdapter(adapter);
		mListView.setHasMore(false);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (!adapter.getItem(position).getStatus().equals("1")) {
					return;
				}
				Intent intent = new Intent(TheLibraryActivity.this, ShipActivity.class);
				intent.putExtra("do", adapter.getItem(position));
				startActivityForResult(intent, 0x1);
			}
		});
		
	}

	private void getCompany(){
		showLoadingDialog();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("Company", SharedPreferencesHelper.get().getString("company", ""));
		map.put("userid", SharedPreferencesHelper.getUser().getUserId());
		requestWebService("GetPlanShipLst", map, onBackListener
		);
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
					if (res.equals("-99") || res.equals("0")) {
						showCustomToast("服务器异常，请重试！");
						return;
					}
					explain(res);
					adapter.clear();
					adapter.notifyDataSetChanged();
					adapter.addAll(dos);
					adapter.notifyDataSetChanged();
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
		List<String> shipNo = new ArrayList<String>();
//		List<String> shipDate = new ArrayList<String>();
//		List<String> custNo = new ArrayList<String>();
//		List<String> custName = new ArrayList<String>();
		List<String> orderDate = new ArrayList<String>();
//		List<String> palletCount = new ArrayList<String>();
		List<String> status = new ArrayList<String>();
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
//					ShipNo-发货单号
//					ShipDate-发货日期
//					CustNo-客户编号
//					CustName-客户名称
//					OrderDate-发货单日期
//					PalletCount-发货单卡板数
					
//					ShipNo-出货计划单号
//					OrderDate-单据日期
//					Status-出货计划单状态，1-表示可用于出货，其他值现在都不允许出货


					String name = parser.getName();
					if (name.equalsIgnoreCase("ShipNo")) {
						shipNo.add(parser.nextText());
					}
//					if (name.equalsIgnoreCase("ShipDate")) {
//						shipDate.add(parser.nextText());
//					}
//					if (name.equalsIgnoreCase("CustNo")) {
//						custNo.add(parser.nextText());
//					}
//					if (name.equalsIgnoreCase("CustName")) {
//						custName.add(parser.nextText());
//					}
					if (name.equalsIgnoreCase("OrderDate")) {
						orderDate.add(parser.nextText());
					}
					if (name.equalsIgnoreCase("Status")) {
						status.add(parser.nextText());
					}
//					if (name.equalsIgnoreCase("PalletCount")) {
//						palletCount.add(parser.nextText());
//					}
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
		if (dos.size()>0) {
			dos.clear();
		}
//		for (int i = 0; i < shipNo.size(); i++) {
//			DONew doItem = new DONew();
//			doItem.setShipNo(shipNo.get(i));
//			doItem.setShipDate(shipDate.get(i));
//			doItem.setCustNo(custNo.get(i));
//			doItem.setCustName(custName.get(i));
//			doItem.setOrderDate(orderDate.get(i));
//			doItem.setPalletCount(palletCount.get(i));
//			dos.add(doItem);
//		}
		for (int i = 0; i < shipNo.size(); i++) {
			DONew doItem = new DONew();
			doItem.setShipNo(shipNo.get(i));
			doItem.setOrderDate(orderDate.get(i));
			doItem.setStatus(status.get(i));
			dos.add(doItem);
		}
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
		getCompany();
	}
}
