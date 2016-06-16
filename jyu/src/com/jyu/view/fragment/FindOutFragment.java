package com.jyu.view.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

import com.jyu.GlobalParams;
import com.jyu.R;
import com.jyu.view.activity.ExpressActivity;
import com.jyu.view.activity.LoginActivity;
import com.jyu.view.activity.LostActivity;
import com.jyu.view.activity.NewsDetailActivity;
import com.jyu.view.activity.PhoneActivity;
import com.jyu.view.activity.RepairActivity;
import com.jyu.view.activity.SecondHandActivity;
import com.jyu.view.activity.SocialActivity;
import com.jyu.view.base.BaseFragment;
import com.jyu.view.util.DialogUtils;

@SuppressLint("InflateParams")
public class FindOutFragment extends BaseFragment {

	private View view ;
	private RelativeLayout rl_lost;
	private RelativeLayout rl_social;
	private RelativeLayout rl_repair;
	private RelativeLayout rl_secondhand;
	private RelativeLayout rl_phone;
	private RelativeLayout rl_express;
	private RelativeLayout rl_library;
	
	@Override
	public View initView(LayoutInflater inflater) {
		view = inflater.inflate(R.layout.fragment_findout, null);
		rl_lost = (RelativeLayout) view.findViewById(R.id.rl_lost);
		rl_social = (RelativeLayout) view.findViewById(R.id.rl_social);
		rl_repair = (RelativeLayout) view.findViewById(R.id.rl_repair);
		rl_secondhand = (RelativeLayout) view.findViewById(R.id.rl_secondhand);
		rl_phone = (RelativeLayout) view.findViewById(R.id.rl_phones);
		rl_express = (RelativeLayout) view.findViewById(R.id.rl_express);
		rl_library = (RelativeLayout) view.findViewById(R.id.rl_library);
		rl_library.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ct, NewsDetailActivity.class);
				String url = "http://m.5read.com/jyu";
				intent.putExtra("url", url);
				ct.startActivity(intent);
			}
		});
		rl_lost.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ct, LostActivity.class);
				ct.startActivity(intent);
			}
		});
		rl_social.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ct,SocialActivity.class);
				ct.startActivity(intent);
				
			}
		});
		rl_secondhand.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ct, SecondHandActivity.class);
				ct.startActivity(intent);
				
			}
		});
		rl_phone.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ct, PhoneActivity.class);
				ct.startActivity(intent);
				
			}
		});
		rl_express.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ct, ExpressActivity.class);
				ct.startActivity(intent);
				
			}
		});
		rl_repair.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (GlobalParams.userInfo == null) {
					DialogUtils.showToast(ct, "亲,请先登录");
					Intent intent = new Intent(ct, LoginActivity.class);
					ct.startActivity(intent);
				} else {
					Intent intent = new Intent(ct, RepairActivity.class);
					ct.startActivity(intent);
				}
				
			}
		});
		return view;
	}

	@Override
	public void initData(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateItem(Object obj) {
		// TODO Auto-generated method stub

	}

}
