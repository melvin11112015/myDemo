package com.scancode.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.scancode.R;
import com.scancode.model.DO;
import com.scancode.model.DONew;

/** 
* @ClassName: StroageAdapter 
* @Description: TODO	入库adapter
* @author zhaoruquan
* @date 2015-9-12 下午5:56:30 
*  
*/
public class TheLibraryAdapter extends ArrayAdapter<DONew> {

	private Context context;

	public TheLibraryAdapter(Context context, int textViewResourceId) {
		super(context, textViewResourceId);
		this.context = context;
	}

	public TheLibraryAdapter(Context context, int textViewResourceId,
			List<DONew> objects) {
		super(context, textViewResourceId, objects);
		this.context = context;
	}

	public void appendList(List<DONew> objects) {
		this.addAll(objects);
		notifyDataSetChanged();
	}

	@SuppressWarnings("deprecation")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.row_do, parent, false);
			holder.oddNum = (TextView) convertView.findViewById(R.id.odd_num_tv);
			holder.cus_tv = (TextView) convertView.findViewById(R.id.cus_tv);
			holder.do_date_tv = (TextView) convertView.findViewById(R.id.do_date_tv);
			holder.invoice_tv = (TextView) convertView.findViewById(R.id.invoice_tv);
			holder.handler_tv = (TextView) convertView.findViewById(R.id.handler_tv);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		DONew dO = getItem(position);
		holder.oddNum.setText(dO.getShipNo());
//		holder.cus_tv.setText(dO.getCustName());
//		holder.do_date_tv.setText(dO.getShipDate());
		holder.invoice_tv.setText(dO.getOrderDate());
		if (dO.getStatus().equals("1")) {
			holder.handler_tv.setText("可出货");
		}else
			holder.handler_tv.setText("不可出货");
		return convertView;
	}

	private static class ViewHolder {
		TextView oddNum;
		TextView cus_tv;//客户名称
		TextView do_date_tv;//发货日期
		TextView invoice_tv;//发货单日期
		TextView handler_tv;
	}

}
