package com.scancode.ui;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scancode.BaseActivity;
import com.scancode.BaseApplication;
import com.scancode.R;
import com.scancode.adapter.SplitAdapter;
import com.scancode.adapter.SplitAdapter.Delete;
import com.scancode.initview.InitView;
import com.scancode.model.Split;
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
* @ClassName: SplitActivity 
* @Description: TODO	拆分
* @author zhaoruquan
* @date 2015-9-12 下午6:06:31 
*  
*/
public class SplitActivity extends BaseActivity implements DecodeInfoCallBack{

	protected SwipeListView mListView;
	
	private HeaderView headerview;
	
	private SplitAdapter adapter;
	
	private EditText codeEt;
	private CheckBox txtisdel;
	
	
	private Button serachBtn;
	
	private TextView newCodeTv;//新托盘条码
	private String pallet;//存储托盘信息
	
	private List<String> tuopanCode = new ArrayList<String>();//存放托盘条码
	
//	Scanner mScanner;
	
	private List<Split> splits = new ArrayList<Split>();
	private List<Split> splitstotal = new ArrayList<Split>();
	private List<String> code = new ArrayList<String>();
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
		setContentView(R.layout.activity_split);
		initData();
	}

	@Override
	protected void initData() {
		newCodeTv = (TextView) findViewById(R.id.new_code_tv);
		
		mListView = (SwipeListView) findViewById(R.id.lv_requiremnt_supply);
		
		headerview = (HeaderView) findViewById(R.id.headerview);
		
		serachBtn = (Button) findViewById(R.id.search_btn);
		
		codeEt = (EditText) findViewById(R.id.code_et);
		txtisdel = (CheckBox) findViewById(R.id.isdel);
		
		codeEt.setHint("请扫描拆分托盘或添加的包装");
		
		InitView.instance().initListView(mListView, this);
		
//		getPalletsPackage();

		// 设置adapter
		adapter = new SplitAdapter(this, 1);
		adapter.setDeleteListener(new Delete() {
			@Override
			public void delete(final int position) {
				showAlertDialog("删除提示", "是否要删除“"+adapter.getItem(position).getMatterNum()+"”包装", "是", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						splits.remove(position);
						code.remove(position);
						DataBind();
						/*adapter.clear();
						adapter.notifyDataSetChanged();
						adapter.addAll(splits);
						adapter.notifyDataSetChanged();
						*/
					}
				}, "否", null);
			}
		});
		mListView.setAdapter(adapter);
		mListView.setHasMore(false);
		headerview.setOtherTextClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showAlertDialog("分拆提示", "是否要对该托盘进行分拆", "是", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						showDialog();
					}
				}, "否", null);
			}
		});
		
		//搜索
		serachBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				getPalletsPackage();
			}
		});
	}

	//获取托盘信息
	private void getPalletsPackage(){
		if (TextUtils.isEmpty(codeEt.getText().toString().trim())) {
			showCustomToast("请输入或扫描条码");
			return;
		}
		if (tuopanCode.contains(codeEt.getText().toString().trim())) {
			showCustomToast("该托盘已扫描");
			return;
		}
		if (codeEt.getText().toString().trim().contains("#")) {
			if(txtisdel.isChecked())
			{
				//删除
				delMatter(codeEt.getText().toString().trim());
				codeEt.setText("");
			}
			else
				addMatter();
			return;
		}
		
		
		showLoadingDialog();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("Company", SharedPreferencesHelper.get().getString("company", ""));
		map.put("PalletsBarcode", codeEt.getText().toString().trim());
		requestWebService("GetPalletsPackage", map, onBackListener
		);
	}
	
	private EditText newCode;
	private boolean isShow = false;// 标记是否显示了dialog
	private void showDialog(){
		Builder dialog = new AlertDialog.Builder(this);
		   LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		   LinearLayout layout = (LinearLayout)inflater.inflate(R.layout.editdialog, null);
		   dialog.setView(layout);
		   newCode = (EditText)layout.findViewById(R.id.new_code_et);
		   newCode.setHint("请扫描新托盘条码");
		   newCode.setText(newCodeTv.getText().toString().trim());
		   dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int which) {
		    	addPalletsPackages();
		    }
		   });
		   dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			    public void onClick(DialogInterface dialog, int which) {
			    	isShow = false;
			    }
			   });
		   dialog.show();
		   isShow = true;
	}
	
	//添加新托盘信息
	private void addPalletsPackages(){//还需要显示一个dialog来输入
		if (splits.size() == 0) {
			return;
		}
		//还需要声称上传xml格式的托盘信息
		showLoadingDialog();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("Company", SharedPreferencesHelper.get().getString("company", ""));
		map.put("palletsBarcode", newCode.getText().toString().trim());
		map.put("userid", SharedPreferencesHelper.getUser().getUserId());
		StringBuffer sb = new StringBuffer("<NewDataSet>");
		for (int i = 0; i < splits.size(); i++) {
			sb.append("<Table1><PkBarcode>").append(splits.get(i).getNumber()).append("+").append(splits.get(i).getMatterNum()).append("+").append(splits.get(i).getAmount()).append(splits.get(i).getTag()).append("</PkBarcode></Table1>");
		}
		sb.append("</NewDataSet>");
		map.put("PackageBarcodeStr", sb.toString());
		requestWebService("AddPalletsPackages", map, new OnCallbackListener() {
			@Override
			public void onCallback(Object result, int state) {
				dismissLoadingDialog();
				String res = "" + result;
				if (TextUtils.isEmpty(res)) {
					showCustomToast("分拆失败，请重试！");
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
					showCustomToast("分拆成功！");
					finish();
					return;
				}
				showCustomToast("提交分拆失败！");
			}
		});
	}
	//del新物料
	private void delMatter(String delBarCode){
		try{
			int site=-1;
			for (int i = 0; i < code.size(); i++) {
				if (delBarCode.equals(code.get(i))) {
					site = i;
				}
			}
			if(site>=0)
			{
				code.remove(site);
			}
			for (int i = 0; i < splits.size(); i++) {
				if (delBarCode.equals(splits.get(i).getNumber())) {
					site = i;
				}
			}
			if(site>=0)
			{
				splits.remove(site);
			}
			DataBind();
			/*
			adapter.clear();
			adapter.notifyDataSetChanged();
			adapter.addAll(splits);
			adapter.notifyDataSetChanged();
			*/
			codeEt.setHint("请扫描新增包装条码");
		}catch(Exception e){
			showCustomToast("请扫描或输入正确的包装条码");
			e.printStackTrace();
		}
	}
	//添加新物料
	private void addMatter(){
		showLoadingDialog();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("Company", SharedPreferencesHelper.get().getString("company", ""));
		map.put("PackageBarcode", codeEt.getText().toString().trim());
		requestWebService("GetPackageUse", map, new OnCallbackListener() {
			@Override
			public void onCallback(Object result, int state) {
				dismissLoadingDialog();
				String res = "" + result;
				if (TextUtils.isEmpty(res)) {
					showCustomToast("查询包装错误，请重试");
					codeEt.setText("");
					return;
				}
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
				if (res.equals("-98")) {
					codeEt.setText("");
					showCustomToast("该包装已出货请确认");
					return;
				}
				if (res.equals("0")) {
					showCustomToast("没有找到包装");
					return;
				}
				if (!res.equals("1")) {
					codeEt.setText("");
					showCustomToast("该包装不可用请确认");
					return;
				}
				add();
			}
		});
	}
	
	private void add(){
		//状态改为0后可以继续使用
				//TODO 需要获取Edittext的值进行搜索，查询完成清空EditText，如果解析编码出错就增加错误提示
				try{
					String pkBarCode = codeEt.getText().toString().trim();
					for (int i = 0; i < code.size(); i++) {
						if (pkBarCode.equals(code.get(i))) {
							showCustomToast("该包装已经扫描");
							codeEt.setText("");
							return;
						}
					}
					Split split = new Split();
					split.setNumber(pkBarCode.substring(0, pkBarCode.indexOf("+")));
					split.setMatterNum(pkBarCode.substring(pkBarCode.indexOf(split.getNumber())+split.getNumber().length()+1, pkBarCode.lastIndexOf("+")));
//					split.setAmount(Integer.valueOf(pkBarCode.substring(pkBarCode.lastIndexOf("+")+1, pkBarCode.length())));
					split.setAmount(Integer.valueOf(pkBarCode.substring(pkBarCode.lastIndexOf("+")+1, pkBarCode.lastIndexOf("#"))));
					split.setTag((pkBarCode.substring(pkBarCode.lastIndexOf("#"), pkBarCode.length())));
					code.add(pkBarCode);
					splits.add(split);
					DataBind();
					codeEt.setText("");
					codeEt.setHint("请扫描新增包装条码");
				}catch(Exception e){
					showCustomToast("请扫描或输入正确的包装条码");
					e.printStackTrace();
				}
	}
	private void DataBind()
	{
		splitstotal.clear();
		String strMatterNum="";
		for (int i = 0; i < splits.size(); i++) {
			strMatterNum = "";
			for (int j = 0; j < splitstotal.size(); j++) {
				if (splitstotal.get(j).getMatterNum().equals(splits.get(i).getMatterNum())) {
					strMatterNum=splitstotal.get(j).getMatterNum();
					int qty=splitstotal.get(j).getAmount();
					qty+=splits.get(i).getAmount();
					splitstotal.get(j).setAmount(qty);
				}
			}
			if("".equals(strMatterNum))
			{
				Split splitall = new Split();
				splitall.setNumber(splits.get(i).getNumber());
				splitall.setMatterNum(splits.get(i).getMatterNum());
				splitall.setAmount(splits.get(i).getAmount());
				splitall.setTag(splits.get(i).getTag());
				splitstotal.add(splitall);
			}
		}
		adapter.clear();
		adapter.notifyDataSetChanged();
		adapter.addAll(splitstotal);
		adapter.notifyDataSetChanged();
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
						codeEt.setText("");
						showCustomToast("托盘没有包装数据或已取消");
						return;
					}
					explain(res);
					newCodeTv.setText(codeEt.getText().toString().trim());
					codeEt.setText("");
					
					DataBind();
					/*adapter.clear();
					adapter.notifyDataSetChanged();
					adapter.addAll(splits);
					adapter.notifyDataSetChanged();*/
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
		for (int i = 0; i < contents.size(); i++) {
			Split split = new Split();
			code.add(contents.get(i));
			String pkBarCode = contents.get(i);
			split.setNumber(pkBarCode.substring(0, pkBarCode.indexOf("+")));
			split.setMatterNum(pkBarCode.substring(pkBarCode.indexOf(split.getNumber())+split.getNumber().length()+1, pkBarCode.lastIndexOf("+")));
			split.setAmount(Integer.valueOf(pkBarCode.substring(pkBarCode.lastIndexOf("+")+1, pkBarCode.lastIndexOf("#"))));
			split.setTag((pkBarCode.substring(pkBarCode.lastIndexOf("#"), pkBarCode.length())));
			splits.add(split);
		}
		tuopanCode.add(codeEt.getText().toString().trim());
	}

	@Override
	public void onDecodeComplete(DecodeInfo info) {
		if (isShow) {
			newCode.setText("");
			newCode.setText(info.barcode);
			addPalletsPackages();
			return;
		}
		for (int i = 0; i < code.size(); i++) {
			if (code.equals(info.barcode)) {
				showDeleDialog(i);
				return;
			}
		}
		codeEt.setText("");
		codeEt.setText(info.barcode);
		getPalletsPackage();
	}
	
	private void showDeleDialog(final int position){
		showAlertDialog("删除提示", "是否要删除“"+adapter.getItem(position).getMatterNum()+"”包装", "是", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				splits.remove(position);
				DataBind();
				/*
				adapter.clear();
				adapter.notifyDataSetChanged();
				adapter.addAll(splits);
				adapter.notifyDataSetChanged();*/
			}
		}, "否", null);
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
}
