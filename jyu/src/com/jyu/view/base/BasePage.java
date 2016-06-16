package com.jyu.view.base;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;

public abstract class BasePage implements OnClickListener {
	public Context ct;
	private View view;
	public BasePage(Context context) {
		ct = context;
		LayoutInflater inflater = (LayoutInflater)ct.getSystemService(
			      Context.LAYOUT_INFLATER_SERVICE);
		view = initView(inflater);
		initData();
	}
	public View getContentView() {
		return view;
	}

	public abstract View initView(LayoutInflater inflater);

	public abstract void initData();


}
