package com.scancode.ui;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.scancode.BaseActivity;
import com.scancode.BaseApplication;
import com.scancode.R;
import com.scancode.adapter.ShipAdapter;
import com.scancode.initview.InitView;
import com.scancode.model.DONew;
import com.scancode.model.Pack;
import com.scancode.model.Ship;
import com.scancode.model.Split;
import com.scancode.model.Tuopan;
import com.scancode.utils.SharedPreferencesHelper;
import com.scancode.webservice.AsyncCaller;
import com.scancode.webservice.BaseWebservice.OnCallbackListener;
import com.scancode.widget.HeaderView;
import com.scancode.widget.swipelistview.SwipeListView;
import com.seuic.scanner.DecodeInfo;
import com.seuic.scanner.DecodeInfoCallBack;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: StorageActivity
 * @Description: TODO 出库activity
 * @author zhaoruquan
 * @date 2015-9-12 下午5:22:34
 * 
 */
public class ShipActivity extends BaseActivity implements DecodeInfoCallBack {

	protected SwipeListView mListView;

	private HeaderView headerview;

	private EditText codeEt;
	private TextView txtpacklst;
	private CheckBox txtisok;
	private CheckBox txtisnotok;
	
	private Button serachBtn;

	private ShipAdapter adapter;

	// private CompanyAdapter adapter2;

	private TextView ship_id;

	private List<Ship> ships = new ArrayList<Ship>();
	
	String OrderNO="";
    int isok=-1;
	// private List<String> palletsBarcodes = new ArrayList<String>();

	// private List<String> tuopanCode = new ArrayList<String>();//托盘条码

	private List<Tuopan> tuopan = new ArrayList<Tuopan>();//

	private List<Pack> packs = new ArrayList<Pack>();//托盘是否已经完成扫描
	// Scanner mScanner;

	private int posi;// 选中的位置
	protected Handler getHandler() {
		// TODO Auto-generated method stub
		return handler;
	}
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				showTypeDialog();
				break;
			case -100:
				Toast.makeText(ShipActivity.this, "获取装箱单失败"+OrderNO, Toast.LENGTH_SHORT).show();
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
		setContentView(R.layout.activity_ship);
		initData();
	}
	
	@Override
	protected void initData() {

		mListView = (SwipeListView) findViewById(R.id.lv_requiremnt_supply);

		headerview = (HeaderView) findViewById(R.id.headerview);

		serachBtn = (Button) findViewById(R.id.search_btn);

		ship_id = (TextView) findViewById(R.id.ship_id);

		codeEt = (EditText) findViewById(R.id.code_et);
		txtpacklst = (TextView) findViewById(R.id.txtPackLst);
		txtisok= (CheckBox) findViewById(R.id.isok);
		txtisnotok= (CheckBox) findViewById(R.id.isnotok);
		codeEt.setHint("请扫描出库托盘条码");

		InitView.instance().initListView(mListView, this);

		DONew doItem = (DONew) getIntent().getSerializableExtra("do");
		OrderNO = doItem.getShipNo();
		getShipPack();
		getShip();

		// 设置adapter
		adapter = new ShipAdapter(this, 1);
		mListView.setAdapter(adapter);
		mListView.setHasMore(false);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				posi = arg2;
				showDialog();
			}
		});

		headerview.setOtherTextClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				v.setFocusable(false);
				isok =0;
				//showCustomToast(isok);
				showAlertDialog("出货提示", "是否要确定出货", "是",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								/*showAlertDialog("是否已完成", "是否已完成", "是",
										new DialogInterface.OnClickListener() {
											@Override
											public void onClick(DialogInterface dialog,
													int which) {
												isok = 1;
											}
										}, "否",null);*/
								addShipPallets();
							}
						}, "否", null);
			}
		});

		// 搜索
		serachBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getPalletsPackage();
			}
		});
	}
	public void selectPackLst(View v){
		//new selectPackLstThread().start();
		showTypeDialog();
	}
	//装箱单
	private OnCallbackListener onBackPackLstListener = new OnCallbackListener() {

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
						showCustomToast("没有装箱单");
						return;
					}
					explainPackLst(res);
					//System.out.println(ships);
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case AsyncCaller.FAIL:
				showCustomToast("网络连接失败，请稍后重试！");
				break;
			case AsyncCaller.NETERROR:
				showCustomToast("网络连接失败，请稍后重试！");
				break;
			default:
				break;
			}
		}
	};
	//装箱单
	private void explainPackLst(String result) {
		List<String> contents = new ArrayList<String>();
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
					if (name.equalsIgnoreCase("PackLstNo")) {
						//showCustomToast(parser.nextText());
						contents.add(parser.nextText());
					}
					break;
				case XmlPullParser.END_TAG:// 结束元素事件
					// }
					break;
				}
				eventType = parser.next();
			}
			groupList = contents;
			tInputStringStream.close();
		} catch (XmlPullParserException e) {
			showCustomToast(e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			showCustomToast(e.getMessage());
			e.printStackTrace();
		}
	}
	List<String> groupList = null;
	//TextView txtPackLstNo;
	//装箱单
	protected void showTypeDialog() {
		// TODO Auto-generated method stub
		if(groupList==null){
			showCustomToast("null");
			return;
		}
		if(groupList.size()==0){
			showCustomToast("0");
			return;
		}
		if(groupList!=null&&groupList.size()>0){
			String[] cust = new String[groupList.size()];
			for(int i=0;i<groupList.size();i++){
				cust[i] = groupList.get(i).toString();
			}
			new AlertDialog.Builder(this).setTitle("选择装箱单").setItems(cust, new android.content.DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					//txtPackLstNo.setText(groupList.get(which).toString());
					txtpacklst.setText(groupList.get(which).toString());
					dialog.cancel();
				}
			}).show();
		}
	}
	// 获取单号信息
	private void getShip() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("Company",
				SharedPreferencesHelper.get().getString("company", ""));
		DONew doItem = (DONew) getIntent().getSerializableExtra("do");

		ship_id.setText("单号：" + doItem.getShipNo());
		map.put("ShipNo", doItem.getShipNo());
		requestWebService("GetPalnShipLines", map, onBackShipListener);
	}
	private void getShipPack() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("Company",SharedPreferencesHelper.get().getString("company", ""));
		map.put("ShipNo",OrderNO);
		requestWebService("GetPalnShipPackLst", map, onBackPackLstListener);
	}
	

	// 获取装箱单列表接口,或者托盘
	private void getPalnShipPackLs() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("Company",
				SharedPreferencesHelper.get().getString("company", ""));
		DONew doItem = (DONew) getIntent().getSerializableExtra("do");
		map.put("ShipNo", doItem.getShipNo());
		requestWebService("GetPalnShipPackLst", map, onBackPalnShipPackLsttener);
	}

	// 获取托盘信息
	private void getPalletsPackage() {
		if (TextUtils.isEmpty(codeEt.getText().toString().trim())) {
			showCustomToast("请扫描或输入条码");
			return;
		}
		for (int i = 0; i < tuopan.size(); i++) {
			if (tuopan.get(i).getCode()
					.equals(codeEt.getText().toString().trim())) {
				showCustomToast("该托盘已经扫描");
				codeEt.setText("");
				return;
			}
		}
		//装箱单号
		for (int i = 0; i < packs.size(); i++) {
			if (packs.get(i).getPackLstNo().equals(codeEt.getText().toString().trim())) {
				if (packs.get(i).getPackingComplete().equals("0")) {
					break;
				}
				showCustomToast("托盘已经扫描");
				return;
			}
		}
		showLoadingDialog();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("Company",
				SharedPreferencesHelper.get().getString("company", ""));
		map.put("PalletsBarcode", codeEt.getText().toString().trim());
		requestWebService("GetPalletsPackage", map, onBackListener);
	}

	// 上传出库的托盘信息
	private void addShipPallets() {
		if (tuopan.size() == 0) {
			showCustomToast("请扫描出货托盘");
			return;
		}
		if ("".equals(txtpacklst.getText().toString())) {
			showCustomToast("请选择装箱单");
			return;
		}
		if(txtisok.isChecked()==false && txtisnotok.isChecked()==false)
		{
			showCustomToast("请选择是否已完成");
			return;
		}
		if(txtisok.isChecked() && txtisnotok.isChecked())
		{
			showCustomToast("是否已完成只能选一个。");
			return;
		}
		// 判断是否达到了出货计划
		for (int i = 0; i < ships.size(); i++) {
			//if (!ships.get(i).getOutNum().equals(ships.get(i).getShipQty())) {
			//	showCustomToast("“" + ships.get(i).getLineNo() + "”没有达到计划出货数量");
			//	return;
			//}
		}
		showLoadingDialog();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("Company",
				SharedPreferencesHelper.get().getString("company", ""));
		map.put("userid", SharedPreferencesHelper.getUser().getUserId());
		DONew doItem = (DONew) getIntent().getSerializableExtra("do");
		StringBuffer sb = new StringBuffer("<NewDataSet>");
		for (int i = 0; i < ships.size(); i++) {
			for (int j = 0; j < ships.get(i).getTpNum().size(); j++) {
				sb.append("<Table1><ShipNo>")
						.append(doItem.getShipNo())
						.append("</ShipNo><ShipLine>")
						.append(ships.get(i).getLineNo())
						.append("</ShipLine><PalletsBarcode>")
						.append(ships.get(i).getTuopan().get(j))
						.append("</PalletsBarcode><PlanQty>")
						.append(ships.get(i).getShipQty())
						.append("</PlanQty><ShipQty>")
						.append(ships.get(i).getOutNum())
						.append("</ShipQty><PalletShipQty>")
						.append(ships.get(i).getTpNum(
								ships.get(i).getTuopan().get(j)))
						.append("</PalletShipQty></Table1>");
			}
		}
		sb.append("</NewDataSet>");
		map.put("ShipBufferStr", sb.toString());
		map.put("PackLstNo", txtpacklst.getText().toString());
		if(txtisok.isChecked())
		{
			isok=1;
		}
		if(txtisnotok.isChecked())
		{
			isok=0;
		}
		map.put("PackComplete", isok);
		
		requestWebService("AddShipPallets", map, new OnCallbackListener() {
			@Override
			public void onCallback(Object result, int state) {
				dismissLoadingDialog();
				String res = "" + result;
				if (TextUtils.isEmpty(res)) {
					showCustomToast("出库失败，请重试！");
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
					showCustomToast("出库成功！");
					finish();
					return;
				}
				showCustomToast("提交出库失败！");
			}
		});
	}

	private OnCallbackListener onBackShipListener = new OnCallbackListener() {

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
						codeEt.setText("");
						showCustomToast("没有出库信息");
						return;
					}
					explainShip(res);
					System.out.println(ships);
					adapter.clear();
					adapter.notifyDataSetChanged();
					adapter.addAll(ships);
					adapter.notifyDataSetChanged();
					getPalnShipPackLs(); 
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

	private void explainShip(String result) {
		List<String> LineNo = new ArrayList<String>();
		List<String> OrderNo = new ArrayList<String>();
		List<String> OrderLine = new ArrayList<String>();
		List<String> ItemNo = new ArrayList<String>();
		List<String> Name1 = new ArrayList<String>();
		List<String> Name2 = new ArrayList<String>();
		List<String> OrderQty = new ArrayList<String>();
		List<String> ShipQty = new ArrayList<String>();
		List<String> OutstandingQty = new ArrayList<String>();
		//List<String> OutQty = new ArrayList<String>();
		List<String> PackQty = new ArrayList<String>();
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
					if (name.equalsIgnoreCase("LineNo")) {
						LineNo.add(parser.nextText());
					}
					if (name.equalsIgnoreCase("OrderNo")) {
						OrderNo.add(parser.nextText());
					}
					if (name.equalsIgnoreCase("OrderLine")) {
						OrderLine.add(parser.nextText());
					}
					if (name.equalsIgnoreCase("ItemNo")) {
						ItemNo.add(parser.nextText());
					}
					if (name.equalsIgnoreCase("Name1")) {
						Name1.add(parser.nextText());
					}
					if (name.equalsIgnoreCase("Name2")) {
						Name2.add(parser.nextText());
					}
					if (name.equalsIgnoreCase("OrderQty")) {
						OrderQty.add(parser.nextText());
					}
					if (name.equalsIgnoreCase("ShipQty")) {
						ShipQty.add(parser.nextText());
					}
					if (name.equalsIgnoreCase("OutstandingQty")) {
						OutstandingQty.add(parser.nextText());
					}
					if (name.equalsIgnoreCase("PackQty")) {
						PackQty.add(parser.nextText());						
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
		for (int i = 0; i < LineNo.size(); i++) {
			Ship split = new Ship();
			split.setLineNo(LineNo.get(i));
			split.setOrderNo(OrderLine.get(i));
			split.setOrderLine(OrderLine.get(i));
			split.setItemNo(ItemNo.get(i));
			split.setName1(Name1.get(i));
			split.setName2(Name2.get(i));
			split.setOrderQty(OrderQty.get(i).substring(0,
					OrderQty.get(i).indexOf(".")));
			split.setShipQty(ShipQty.get(i).substring(0,
					ShipQty.get(i).indexOf(".")));
			split.setOutstandingQty(OutstandingQty.get(i).substring(0,
					OutstandingQty.get(i).indexOf(".")));
			//split.setOutNum(OutQty.get(i).substring(0,OutQty.get(i).indexOf(".")));
			split.setPackQty(Integer.valueOf(PackQty.get(i).substring(0,
					PackQty.get(i).indexOf("."))));
			ships.add(split);
		}
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
					if (res.equals("-98")) {
						codeEt.setText("");
						showCustomToast("该托盘已出货，请确认");
						return;
					}
					if (res.equals("-99")) {
						codeEt.setText("");
						showCustomToast("服务器异常，请重试！");
						return;
					}
					if (res.equals("0")) {
						showCustomToast("没有托盘信息");
						return;
					}
					explain(res);
					codeEt.setText("");
					adapter.clear();
					adapter.notifyDataSetChanged();
					adapter.addAll(ships);
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
		List<String> contents = new ArrayList<String>();
		List<Split> splits = new ArrayList<Split>();
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
					if (name.equalsIgnoreCase("PkBarcode")) {
						contents.add(parser.nextText());
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
		// 统计卡板物料数量
		for (int i = 0; i < contents.size(); i++) {
			Split split = new Split();
			String pkBarCode = contents.get(i);
			split.setNumber(pkBarCode.substring(0, pkBarCode.indexOf("+")));
			split.setMatterNum(pkBarCode.substring(
					pkBarCode.indexOf(split.getNumber())
							+ split.getNumber().length() + 1,
					pkBarCode.lastIndexOf("+")));
			split.setAmount(Integer.valueOf(pkBarCode.substring(
					pkBarCode.lastIndexOf("+") + 1, pkBarCode.indexOf("#"))));
			split.setSur(split.getAmount());
			boolean handler = false;
			for (int j = 0; j < splits.size(); j++) {
				if (splits.get(j).getMatterNum().equals(split.getMatterNum())) {
					splits.get(j).setAmount(
							splits.get(j).getAmount() + split.getAmount());
					splits.get(j).setSur(splits.get(j).getAmount());
					handler = true;
					break;
				}
			}
			if (!handler) {
				splits.add(split);
			}
		}
		
		// 统计勾选的物料
		List<Split> select = new ArrayList<Split>();
		//List<Ship> ships = adapter.getShips();
		for (int i = 0; i < ships.size(); i++) {
			boolean handler = false;
			Split split = new Split();
			split.setMatterNum(ships.get(i).getItemNo());
			split.setAmount(Integer.valueOf(ships.get(i).getShipQty()));
			split.setSur(split.getAmount()
					- Integer.valueOf(ships.get(i).getOutNum()));
			for (int j = 0; j < select.size(); j++) {
				if (select.get(j).getMatterNum().equals(split.getMatterNum())) {
					select.get(j).setAmount(
							select.get(j).getAmount() + split.getAmount());
					select.get(j).setSur(
							select.get(j).getSur() + split.getSur());
					handler = true;
					break;
				}
			}
			if (!handler) {
				select.add(split);
			}
		}
		//showCustomToast("11");
		//for (int a = 0; a < select.size(); a++) {
		//	showCustomToast("7");
		//}
		
		//showCustomToast("88");
		// 检查是否含有不是本次勾选的物料
		for (int i = 0; i < splits.size(); i++) {
			for (int j = 0; j < select.size(); j++) {
				if (select.get(j).getMatterNum()
						.equals(splits.get(i).getMatterNum())) {
					if (select.get(j).getSur() < splits.get(i).getAmount()) {
						// 弹出确认分拆界面
						showCustomToast("超出计划出货数量");
						return;
					}
					continue;
				}
				if (i == splits.size() - 1 && j == select.size() - 1) {
					showCustomToast("含有非法的物料");
					return;
				}
			}
		}
		// 添加数量
		for (int i = 0; i < splits.size(); i++) {
			for (int j = 0; j < select.size(); j++) {
				if (!select.get(j).getMatterNum()
						.equals(splits.get(i).getMatterNum())) {
					continue;
				}
				// 添加出货数量
				//Iterator iter = adapter.getCheck().entrySet().iterator();
				//while (iter.hasNext()) {
				for (int k = 0; k < ships.size(); k++) {
					//Map.Entry entry = (Map.Entry) iter.next();
					//Object key = entry.getKey();
					//Ship val = (Ship) entry.getValue();
					// 判断是否相同物料
					//int position = this.ships.indexOf(val);
					if (!this.ships.get(k).getItemNo()
							.equals(select.get(j).getMatterNum())) {
						continue;
					}
					// this.ships.get(position).getTuopan().add(codeEt.getText().toString().trim());
					if (TextUtils.isEmpty(this.ships.get(k).getOutNum())) {
						this.ships.get(k).setOutNum("0");
					}
					// 判断是否到达了计划出货数量
					if (!TextUtils
							.isEmpty(Integer.toString(Integer.valueOf(this.ships.get(k).getOutNum()) + this.ships.get(k).getPackQty()))
							&& this.ships
									.get(k)
									.getOutNum()
									.equals(this.ships.get(k)
											.getShipQty())) {
						continue;
					}
					if (splits.get(i).getSur() == 0) {
						continue;
					}
					// 如果大于总数则全部添加
					if ((Integer.valueOf(this.ships.get(k).getShipQty()) - this.ships.get(k).getPackQty()) >= splits
							.get(i).getSur()) {
						this.ships.get(k).setOutNum(
								Integer.toString(Integer.valueOf(this.ships
										.get(k).getOutNum())
										+ splits.get(i).getSur()));
						this.ships.get(k).addTuopan(
								codeEt.getText().toString().trim());
						this.ships.get(k).addTpNum(
								splits.get(i).getSur());
						splits.get(i).setSur(0);

						continue;
					}
					this.ships.get(k).setOutNum(
							Integer.toString(Integer.valueOf(this.ships.get(k).getShipQty()) - this.ships.get(k).getPackQty()));
					this.ships.get(k).addTuopan(
							codeEt.getText().toString().trim());
					this.ships.get(k).addTpNum(Integer.valueOf(this.ships.get(k).getShipQty()) - this.ships.get(k).getPackQty());
					splits.get(i).setSur(
							splits.get(i).getSur()
									- Integer.valueOf(this.ships.get(k).getShipQty()) - this.ships.get(k).getPackQty());
				}
			}
		}
		Tuopan tp = new Tuopan();
		tp.setCode(codeEt.getText().toString().trim());
		tp.setSplits(splits);
		tuopan.add(tp);
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onDecodeComplete(DecodeInfo info) {
		if (isShow) {
			return;
		}
		//if (adapter.getShips().size() == 0) {
		//	showCustomToast("请勾选订单");
		//	return;
		//}
		codeEt.setText("");
		codeEt.setText(info.barcode);
		getPalletsPackage();
	}

	@Override
	protected void onResume() {
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

	private EditText newCode;
	private boolean isShow = false;// 标记是否显示了dialog

	private void showDialog() {
		Builder dialog = new AlertDialog.Builder(this);
		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout layout = (LinearLayout) inflater.inflate(
				R.layout.editdialog, null);
		dialog.setView(layout);
		newCode = (EditText) layout.findViewById(R.id.new_code_et);
		newCode.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
		newCode.setHint("请输入计划出货数量");
		newCode.setText(ships.get(posi).getShipQty());
		dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				if (Integer.valueOf(ships.get(posi).getOutstandingQty()) < Integer
						.valueOf(newCode.getText().toString().trim())) {
					showCustomToast("不能大于出货计划");
					return;
				}
				if (!TextUtils.isEmpty(ships.get(posi).getOutNum())
						&& Integer.valueOf(ships.get(posi).getOutNum()) > Integer
								.valueOf(newCode.getText().toString().trim())) {
					showCustomToast("不能小于出货数量");
					return;
				}
				ships.get(posi).setShipQty(
						Integer.toString(Integer.valueOf(newCode.getText()
								.toString().trim())));
				adapter.notifyDataSetChanged();
				isShow = false;
				dialog.dismiss();
			}
		});
		dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				isShow = false;
			}
		});
		dialog.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				isShow = false;
			}
		});
		dialog.show();
		isShow = true;
	}

	private OnCallbackListener onBackPalnShipPackLsttener = new OnCallbackListener() {

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
						codeEt.setText("");
						showCustomToast("没有装箱单信息");
						return;
					}
					PalnShipPackLst(res);
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
	
	private void PalnShipPackLst(String result) {
		List<String> PackLstNo = new ArrayList<String>();
		List<String> PackingComplete = new ArrayList<String>();
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
					if (name.equalsIgnoreCase("PackLstNo")) {
						PackLstNo.add(parser.nextText());
					}
					if (name.equalsIgnoreCase("PackingComplete")) {
						PackingComplete.add(parser.nextText());
					}
					break;
				case XmlPullParser.END_TAG:// 结束元素事件
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
		for (int i = 0; i < PackLstNo.size(); i++) {
			Pack pack = new Pack();
			pack.setPackingComplete(PackingComplete.get(i));
			pack.setPackLstNo(PackLstNo.get(i));
			packs.add(pack);
		}
	}
}
