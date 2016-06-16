package com.jyu.view.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;

public abstract class BaseFragment extends Fragment implements
		FragmentInterface, OnTouchListener {
	public View view;
	public Context ct;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		ct = getActivity();
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = initView(inflater);
		return view;
	}

	public View getRootView() {
		return view;
	}

	/**
	 * 模拟后退键
	 */
	protected void back() {
		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.popBackStackImmediate();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onViewCreated(android.view.View,
	 * android.os.Bundle)
	 */
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		view.setOnTouchListener(this);
		super.onViewCreated(view, savedInstanceState);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnTouchListener#onTouch(android.view.View,
	 * android.view.MotionEvent)
	 */
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// 拦截触摸事件，防止传递到下一层的View
		return true;
	}

	public void dispatchCommand(int id, Bundle args) {
		if (getActivity() instanceof FragmentCallback) {
			FragmentCallback callback = (FragmentCallback) getActivity();
			callback.onFragmentCallback(this, id, args);
		} else {
			throw new IllegalStateException(
					"The host activity does not implement FragmentCallback.");
		}
	}

	public void refreshViews() {

	}

	public boolean commitData() {
		return false;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {

		super.onActivityCreated(savedInstanceState);
		initData(savedInstanceState);
	}

	/**
	 * 初始化view
	 */
	public abstract View initView(LayoutInflater inflater);

	/**
	 * 初始化数据
	 */
	public abstract void initData(Bundle savedInstanceState);

//	@Override
//	public void onActivityResult(int requestCode, int resultCode, Intent data) {
//		super.onActivityResult(requestCode, resultCode, data);
//	}
	public abstract void updateItem(Object obj);
	
}
