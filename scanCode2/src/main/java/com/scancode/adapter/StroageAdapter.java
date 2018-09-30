package com.scancode.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.scancode.R;
import com.scancode.model.Storage;

/** 
* @ClassName: StroageAdapter 
* @Description: TODO	入库adapter
* @author zhaoruquan
* @date 2015-9-12 下午5:56:30 
*  
*/
public class StroageAdapter extends ArrayAdapter<Storage> {

	private Context context;

	public StroageAdapter(Context context, int textViewResourceId) {
		super(context, textViewResourceId);
		this.context = context;
	}

	public StroageAdapter(Context context, int textViewResourceId,
			List<Storage> objects) {
		super(context, textViewResourceId, objects);
		this.context = context;
	}

	public void appendList(List<Storage> objects) {
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
					R.layout.row_storage, parent, false);
			holder.name = (TextView) convertView.findViewById(R.id.name_tv);
			holder.number = (TextView) convertView.findViewById(R.id.number_tv);
			holder.num = (TextView) convertView.findViewById(R.id.num_tv);
			holder.surplus = (TextView) convertView.findViewById(R.id.surplus_tv);
			holder.in = (TextView) convertView.findViewById(R.id.in_tv);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Storage str = getItem(position);
		holder.name.setText(str.getName());
		holder.number.setText(str.getNumber());
		holder.num.setText(str.getStoNum());
		holder.surplus.setText(""+str.getSurplus());
		holder.in.setText(""+str.getIn());

		return convertView;
	}

	private static class ViewHolder {
		TextView name;
		TextView number;
		TextView num;
		TextView surplus;
		TextView in;
	}

}
