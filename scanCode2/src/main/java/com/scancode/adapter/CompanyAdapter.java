package com.scancode.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.scancode.R;

public class CompanyAdapter extends ArrayAdapter<String> {

	private Context context;

	public CompanyAdapter(Context context, int textViewResourceId) {
		super(context, textViewResourceId);
		this.context = context;
	}

	public CompanyAdapter(Context context, int textViewResourceId,
			List<String> objects) {
		super(context, textViewResourceId, objects);
		this.context = context;
	}

	public void appendList(List<String> objects) {
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
					R.layout.row_company, parent, false);
			holder.name = (TextView) convertView.findViewById(R.id.name_tv);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		String name = getItem(position);
		holder.name.setText(name);

		return convertView;
	}

	private static class ViewHolder {
		TextView name;
	}

}
