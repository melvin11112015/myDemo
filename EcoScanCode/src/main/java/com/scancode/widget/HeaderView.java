package com.scancode.widget;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.scancode.R;


public class HeaderView extends RelativeLayout {

	private TextView common_title;// 标题
	private TextView other_tv;//其他作用的tx

	private LinearLayout backLly;// 返回的linearLayout

	private Context context;

	public HeaderView(Context context) {
		super(context);
		this.context = context;
		// 初始化视图
		init();
	}

	@SuppressLint("Recycle")
	public HeaderView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		// 初始化视图
		init();
		if (attrs == null) {
			return;
		}
		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.HeaderView);
		String title = a.getString(R.styleable.HeaderView_title);
		// 设置标题
		if (!TextUtils.isEmpty(title)) {
			int i = title.indexOf("/");
			if (i >= 0) {
				String name = title.substring(i + 1);
				String value = null;
				if (title.startsWith("@string")) {
					value = getResources().getString(
							getResources().getIdentifier(name, "string",
									getContext().getPackageName()));
				} else if (title.startsWith("@android:string")) {
					value = getResources().getString(
							getResources().getIdentifier(name, "string",
									"android"));
				}
				if (!TextUtils.isEmpty(value) && common_title != null) {
					common_title.setText(value);
				}
			} else {
				if (common_title != null) {
					common_title.setText(title);
				}
			}
		}
		// 设置右边内容
		String content = a.getString(R.styleable.HeaderView_otherContent);
		if (!TextUtils.isEmpty(content)) {
			int i = content.indexOf("/");
			if (i >= 0) {
				String name = content.substring(i + 1);
				String value = null;
				if (content.startsWith("@string")) {
					value = getResources().getString(
							getResources().getIdentifier(name, "string",
									getContext().getPackageName()));
				} else if (content.startsWith("@android:string")) {
					value = getResources().getString(
							getResources().getIdentifier(name, "string",
									"android"));
				}
				if (!TextUtils.isEmpty(value) && other_tv != null) {
					other_tv.setText(value);
				}
			} else {
				if (other_tv != null) {
					other_tv.setText(content);
				}
			}
		}
		// 设置是否显示返回图标，默认为true
		boolean showBack = a.getBoolean(R.styleable.HeaderView_showBack, true);
		// 设置显示返回如果为true则显示，false则不显示
		backLly.setVisibility(showBack ? View.VISIBLE : View.GONE);
		
		//设置是否显示右边的textView
		boolean showOther = a.getBoolean(R.styleable.HeaderView_showOther, false);
		other_tv.setVisibility(showOther ? View.VISIBLE : View.GONE);
	}


	/**
	 * 设置标题
	 * 
	 * @param title
	 *            标题内容
	 */
	public void setTitle(String title) {
		if (!TextUtils.isEmpty(title) && common_title != null) {
			common_title.setText(title);
		}
	}

	/**
	 * 设置标题
	 * 
	 * @param resId
	 *            根据本地资源id
	 */
	public void setTitle(int resId) {
		if (resId <= 0) {
			return;
		}

		String title = getResources().getString(resId);
		if (!TextUtils.isEmpty(title) && common_title != null) {
			common_title.setText(title);
		}
	}
	
	/**
	 * 
	* @Title: showOtherTv 
	* @Description: TODO	设置显示或者隐藏右边的TextView 
	* @param @param showOther    View.VISIBLE或者View.GONE
	* @return void    返回类型 
	* @throws
	 */
	public void showOtherTv(int showOther){
		if ((showOther == View.VISIBLE || showOther == View.GONE) && other_tv != null) {
			other_tv.setVisibility(showOther);
		}
	}
	
	public void showBack(int showBack){
		if ((showBack == View.VISIBLE || showBack == View.GONE) && backLly != null) {
			backLly.setVisibility(showBack);
		}
	}
	
	/**
	 * 
	* @Title: setOtherText 
	* @Description: TODO	设置右边TextView的文字内容
	* @param @param content    设置的内容
	* @return void    返回类型 
	* @throws
	 */
	public void setOtherText(String content){
		if (!TextUtils.isEmpty(content) && other_tv != null) {
			other_tv.setText(content);
		}
	}
	
	/**
	 * 
	* @Title: setOtherText 
	* @Description: TODO	设置右边TextView的文字内容
	* @param @param resId	设置的资源id
	* @return void    返回类型 
	* @throws
	 */
	public void setOtherText(int resId){
		if (resId <= 0) {
			return;
		}
		String title = getResources().getString(resId);
		if (!TextUtils.isEmpty(title) && other_tv != null) {
			other_tv.setText(title);
		}
	}
	
	public void setOtherTextClickListener(OnClickListener lister){
		if (lister == null || other_tv == null) {
			return;
		}
		other_tv.setOnClickListener(lister);
	}

	/**
	 * 初始化界面
	 */
	private void init() {
		LayoutParams lp = (LayoutParams) getLayoutParams();
		if (lp == null) {
			lp = new LayoutParams(
					android.view.ViewGroup.LayoutParams.MATCH_PARENT,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			setLayoutParams(lp);
		}

		LayoutInflater inflater = (LayoutInflater) getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.header_view, this);

		backLly = (LinearLayout) findViewById(R.id.newsfeedpublish_lly_back);
		common_title = (TextView) findViewById(R.id.tv_header_title);
		other_tv = (TextView) findViewById(R.id.header_other_tv);
		backLly.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (getContext() instanceof Activity) {
					((Activity) getContext()).finish();
				}
			}
		});
	}
}
