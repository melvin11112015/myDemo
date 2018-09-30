package com.scancode.ui;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Xml;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.scancode.BaseActivity;
import com.scancode.BaseApplication;
import com.scancode.R;
import com.scancode.adapter.StroageAdapter;
import com.scancode.initview.InitView;
import com.scancode.model.Storage;
import com.scancode.utils.SharedPreferencesHelper;
import com.scancode.webservice.AsyncCaller;
import com.scancode.webservice.BaseWebservice.OnCallbackListener;
import com.scancode.widget.HeaderView;
import com.scancode.widget.swipelistview.SwipeListView;
import com.seuic.scanner.DecodeInfo;
import com.seuic.scanner.DecodeInfoCallBack;
import com.seuic.scanner.Scanner;
import com.seuic.scanner.ScannerFactory;
import com.seuic.scanner.ScannerKey;

/**
 * @ClassName: StorageActivity
 * @Description: TODO 入库activity
 * @author zhaoruquan
 * @date 2015-9-12 下午5:22:34
 * 
 */
public class StorageActivity extends BaseActivity implements
		SwipeRefreshLayout.OnRefreshListener, DecodeInfoCallBack {

	protected SwipeListView mListView;

	private HeaderView headerview;

	private EditText codeEt;

	private Button searchBtn;

	private StroageAdapter adapter;

	private List<String> pORcptStrs = new ArrayList<String>();

	private List<Storage> data = new ArrayList<Storage>();

	private String cgNum;

	private String wlNum;

	private int num;
	
//	Scanner mScanner;
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
		setContentView(R.layout.activity_storage);
		initData();
	}

	@Override
	protected void initData() {
		mListView = (SwipeListView) findViewById(R.id.lv_requiremnt_supply);

		headerview = (HeaderView) findViewById(R.id.headerview);
		
		codeEt = (EditText) findViewById(R.id.code_et);
		codeEt.setHint("扫描采购条码");
		searchBtn = (Button) findViewById(R.id.search_btn);
		InitView.instance().initListView(mListView, this);

		// 设置adapter
		adapter = new StroageAdapter(this, 1);
		mListView.setAdapter(adapter);
		mListView.setHasMore(false);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				List<Storage> data = new ArrayList<Storage>();
				adapter.appendList(data);
			}
		});

		headerview.setOtherTextClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showAlertDialog("入库提示", "是否确定入库", "是",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								inStorage();
							}
						}, "否", null);
			}
		});

		// 手动搜索
		searchBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (TextUtils.isEmpty(codeEt.getText().toString().trim())) {
					showCustomToast("请输入或扫条码");
					return;
				}
				getPOItemRcptQty();
				
			}
		});
	}

	@Override
	protected void onResume() {
		//Set Callback
		BaseApplication.getInstance().getScance().setDecodeInfoCallBack(this);
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		BaseApplication.getInstance().getScance().setDecodeInfoCallBack(null);
		super.onPause();
	}
	
	@Override
	protected void onDestroy() {
		BaseApplication.getInstance().setListener();
		super.onDestroy();
	}

	/*
	 * 获取物料编号和数量
	 */
	private void getPOItemRcptQty() {
		String code = codeEt.getText().toString().trim();
		if (TextUtils.isEmpty(code)) {
			showCustomToast("请输入或扫条码");
			return;
		}
		try {
			if (pORcptStrs.contains(codeEt.getText().toString().trim())) {
				showCustomToast("该包装已经扫描");
				codeEt.setText("");
				return;
			}
			cgNum = code.substring(0, code.indexOf("+"));
			wlNum = code.substring(code.indexOf(cgNum) + cgNum.length() + 1,
					code.lastIndexOf("+"));
			num = Integer.valueOf(code.substring(code.lastIndexOf("+") + 1,
					code.lastIndexOf("#")));
			for (int i = 0; i < data.size(); i++) {
				Storage sto = data.get(i);
				if (sto.getStoNum().equals(cgNum)
						&& sto.getNumber().equals(wlNum)) {
					if (num > data.get(i).getSurplus()
							|| data.get(i).getIn() + num > data.get(i)
									.getSurplus()) {
						showAlertDialog("提示", "物料“" + data.get(i).getName()
								+ "”超出入库数量", "确认",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										codeEt.setText("");
										dialog.dismiss();
									}
								});
						return;
					}
					data.get(i).setIn(data.get(i).getIn() + num);
					adapter.clear();
					adapter.notifyDataSetChanged();
					adapter.addAll(data);
					adapter.notifyDataSetChanged();
					pORcptStrs.add(codeEt.getText().toString().trim());
					codeEt.setText("");
					return;
				}
			}
		} catch (Exception e) {
			codeEt.setText("");
			showCustomToast("请输入扫描正确条码");
			e.printStackTrace();
			return;
		}
		showLoadingDialog();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("Company",
				SharedPreferencesHelper.get().getString("company", ""));
		map.put("POBarcode", code);// TODO 需要测试数据
		requestWebService("GetPOItemRcptQty", map, onBackListener);
	}

	
	
	private OnCallbackListener onBackListener = new OnCallbackListener() {

		@Override
		public void onCallback(Object result, int state) {
			dismissLoadingDialog();
			switch (state) {
			case AsyncCaller.SUCCESS:
				try {
					if (result == null) {
						codeEt.setText("");
						showCustomToast("获取数据失败");
						return;
					}
					String res = "" + result;
					if (res.equals("-100")) {
						codeEt.setText("");
						showCustomToast("没有公司名称");
						return;
					}
					if (res.equals("-99")) {
						codeEt.setText("");
						showCustomToast("服务器异常，请重试！");
						return;
					}
					if (res.equals("0")) {
						showCustomToast("没有该条码采购信息");
					}
					explain(res);
					codeEt.setText("");
					adapter.clear();
					adapter.notifyDataSetChanged();
					adapter.addAll(data);
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

	/*
	 * 提交入库
	 */
	private void inStorage() {
		if (pORcptStrs.size() == 0) {
			showCustomToast("没有添加入库物料");
			return;
		}
		showLoadingDialog();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("Company",
				SharedPreferencesHelper.get().getString("company", ""));
		map.put("userid", SharedPreferencesHelper.getUser().getUserId());
		StringBuffer sb = new StringBuffer("<NewDataSet>");
		for (int i = 0; i < pORcptStrs.size(); i++) {
			sb.append("<Table1><POBarcode>").append(pORcptStrs.get(i)).append("</POBarcode></Table1>");
		}
		sb.append("</NewDataSet>");
		map.put("PORcptStr",sb.toString());
		System.out.println(map);
		requestWebService("AddPORcpt", map, new OnCallbackListener() {
			@Override
			public void onCallback(Object result, int state) {
				dismissLoadingDialog();
				String res = "" + result;
				if (TextUtils.isEmpty(res)) {
					showCustomToast("提交入库失败，请重试！");
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
					showCustomToast("入库成功！");
					finish();
					return;
				}
				showCustomToast("提交入库失败！");
				System.out.println(result);
			}
		});
	}

	private void explain(String result) {
		List<String> itemNos = new ArrayList<String>();
		List<String> itemNames = new ArrayList<String>();
		List<Integer> rcptQtys = new ArrayList<Integer>();
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
					if (name.equalsIgnoreCase("ItemNo")) {
						itemNos.add(parser.nextText());
					}
					if (name.equalsIgnoreCase("ItemName")) {
						itemNames.add(parser.nextText());
					}
					if (name.equalsIgnoreCase("RcptQty")) {
						String n = parser.nextText();
						rcptQtys.add(Integer.valueOf(n.substring(0,
								n.indexOf("."))));
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
		List<Storage> stos = new ArrayList<Storage>();
		boolean handle = false;
		for (int i = 0; i < itemNos.size(); i++) {
			handle = false;
			for (int j = 0; j < stos.size(); j++) {
				//判断是否相同物料
				if (stos.get(j).getNumber().equals(itemNos.get(i))) {
					stos.get(j).setSurplus(stos.get(j).getSurplus() + rcptQtys.get(i));
					handle = true;
					continue;
				}
			}
			if (handle) {
				continue;
			}
			Storage sto = new Storage();
			sto.setNumber(itemNos.get(i));
			sto.setName(itemNames.get(i));
			sto.setSurplus(rcptQtys.get(i));
			sto.setStoNum(cgNum);
			stos.add(sto);
		}
		for (int i = 0; i < stos.size(); i++) {
			if (wlNum.equals(stos.get(i).getNumber())) {
				if (num > stos.get(i).getSurplus()) {
					showAlertDialog("提示", "物料“" + stos.get(i).getName() + "”超出入库数量",
							"确认", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
								}
							});
					return;
				}
				stos.get(i).setIn(num);
			}
		}
		for (int i = 0; i < stos.size(); i++) {
			data.add(stos.get(i));
		}
		pORcptStrs.add(codeEt.getText().toString().trim());
	}
	
	@Override
	public void onDecodeComplete(DecodeInfo info) {
		codeEt.setText("");
		codeEt.setText(info.barcode);
		getPOItemRcptQty();
	}

	@Override
	public void onRefresh() {
		
	}
}
