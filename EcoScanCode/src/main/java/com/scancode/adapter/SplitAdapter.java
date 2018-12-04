package com.scancode.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.scancode.R;
import com.scancode.model.Split;

import java.util.List;

/** 
* @ClassName: StroageAdapter 
* @Description: TODO	拆分adapter
* @author zhaoruquan
* @date 2015-9-12 下午5:56:30 
*  
*/
public class SplitAdapter extends ArrayAdapter<Split> {

	private Context context;
	
	private Delete delete;

	public SplitAdapter(Context context, int textViewResourceId) {
		super(context, textViewResourceId);
		this.context = context;
	}

	public SplitAdapter(Context context, int textViewResourceId,
			List<Split> objects) {
		super(context, textViewResourceId, objects);
		this.context = context;
	}
	
	public void setDeleteListener(Delete delete){
		this.delete = delete;
	}

	public void appendList(List<Split> objects) {
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
					R.layout.row_split, parent, false);
			holder.name = (TextView) convertView.findViewById(R.id.name_tv);
			//holder.number = (TextView) convertView.findViewById(R.id.number_tv);
			//holder.number.setVisibility(0);
			holder.sum = (TextView) convertView.findViewById(R.id.sum_tv);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Split spl = getItem(position);
		//holder.name.setText(spl.getNumber());
		//holder.number.setText(spl.getMatterNum());//+spl.getTag()
		holder.name.setText(spl.getMatterNum());//+spl.getTag()
		holder.sum.setText(""+spl.getAmount());
		/*holder.delete_iv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (delete == null) {
					return;
				}
				delete.delete(position);
			}
		});*/
		return convertView;
	}

	private static class ViewHolder {
		TextView name;
		TextView sum;
		//ImageView delete_iv;
	}

	public interface Delete{
		public void delete(int position);
	}
}
