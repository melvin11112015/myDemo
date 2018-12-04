package com.scancode.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.scancode.R;
import com.scancode.model.Ship;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/** 
* @ClassName: StroageAdapter 
* @Description: TODO	入库adapter
* @author zhaoruquan
* @date 2015-9-12 下午5:56:30 
*  
*/
public class ShipAdapter extends ArrayAdapter<Ship> {

	private Context context;
	
	private Map<String, Boolean> check = new HashMap<String, Boolean>();//保存选择状态
	
	private Map<String, Ship> shipSelect = new HashMap<String, Ship>();//保存选择的物料

	public ShipAdapter(Context context, int textViewResourceId) {
		super(context, textViewResourceId);
		this.context = context;
	}

	public ShipAdapter(Context context, int textViewResourceId,
			List<Ship> objects) {
		super(context, textViewResourceId, objects);
		this.context = context;
	}

	public void appendList(List<Ship> objects) {
		this.addAll(objects);
		notifyDataSetChanged();
	}

	@SuppressWarnings("deprecation")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.row_split2, parent, false);
			holder.name = (TextView) convertView.findViewById(R.id.name_tv);
			holder.number = (TextView) convertView.findViewById(R.id.number_tv);
			holder.sum = (TextView) convertView.findViewById(R.id.sum_tv);
			holder.surplus = (TextView) convertView.findViewById(R.id.surplus_tv);
			holder.in = (TextView) convertView.findViewById(R.id.in_tv);
			holder.tray_tv = (TextView) convertView.findViewById(R.id.tray_tv);
			//holder.check = (CheckBox) convertView.findViewById(R.id.check);
			
			holder.out_tv = (TextView) convertView.findViewById(R.id.out_tv);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		if (!check.containsKey(Integer.toString(position))) {
			check.put(Integer.toString(position), false);
		}
		final Ship str = getItem(position);
		if (str.getShipQty().equals(str.getOutNum())) {
			check.put(Integer.toString(position), false);
			//holder.check.setClickable(false);
		}
		/*final Boolean isCheck = check.get(Integer.toString(position));
		holder.check.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					shipSelect.put(Integer.toString(position), str);
				}else{
					shipSelect.remove(Integer.toString(position));
				}
				check.put(Integer.toString(position), isChecked);
				notifyDataSetChanged();
			}
		});
		*/
		//holder.check.setChecked(isCheck);
		holder.name.setText(str.getName1());
		holder.number.setText(str.getLineNo());
		holder.sum.setText(""+str.getOrderQty());
		holder.surplus.setText(""+str.getOutstandingQty());
		holder.in.setText(""+str.getShipQty());
		if (!TextUtils.isEmpty(str.getOutNum())) {
			holder.out_tv.setText(str.getOutNum());
		}
		if (str.getTuopan().size() == 0) {
			holder.tray_tv.setText("");
		}else{
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < str.getTuopan().size(); i++) {
				sb.append(str.getTuopan().get(i)).append("\n");
			}
			holder.tray_tv.setText(sb.toString());
		}
		return convertView;
	}

	private static class ViewHolder {
		TextView name;
		TextView number;
		TextView sum;
		TextView surplus;
		TextView in;
		TextView tray_tv;
		TextView out_tv;
		//CheckBox check;
	}
	
	public List<Ship> getShips(){
		List<Ship> list = new ArrayList<Ship>();
		Iterator iter = shipSelect.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next(); Object key = entry.getKey();
			Ship val = (Ship) entry.getValue();
			list.add(val);
		}
		return list;
	}
	
	public void cleanSelect(){
		if (shipSelect != null) {
			shipSelect.clear();
		}
	}
	
	public Map<String, Ship> getCheck(){
		return shipSelect;
	}

}
