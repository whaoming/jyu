package com.jyu.view.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jyu.ConstantValue;
import com.jyu.GlobalParams;
import com.jyu.R;
import com.jyu.commonUtils.BeanFactory;
import com.jyu.commonUtils.SharePrefUtil;
import com.jyu.engine.ServerEngine;
import com.jyu.view.activity.LoginActivity;
import com.jyu.view.activity.LostActivity;
import com.jyu.view.activity.MySecondHandAboutActivity;
import com.jyu.view.activity.MySocialAboutActivity;
import com.jyu.view.activity.UserInfoActivity;
import com.jyu.view.base.BaseFragment;
import com.jyu.view.custom.mydialog.AlertDialog;
import com.jyu.view.custom.mydialog.AlertDialog.OnSheetItemClickListeners;
import com.jyu.view.custom.mydialog.AlertDialog.SheetItemColors;
import com.jyu.view.custom.mydialog.AlertDialog.setEditPositiveClickListener;
import com.jyu.view.util.DialogUtils;
import com.lidroid.xutils.BitmapUtils;

public class SettingFragment extends BaseFragment implements OnClickListener {

	private View view;

	private Button bt_login;
	private RelativeLayout rl_haslogin;
	private ImageView iv_upic;
	private TextView tv_name;
	private TextView tv_description;
	private BitmapUtils bitmapUtil;

	private TextView editinfo;
	private TextView logout;
	private RelativeLayout social;
	private RelativeLayout secondhand;
	private RelativeLayout bt_lost;
	private RelativeLayout rl_setip;

	@SuppressLint("InflateParams")
	@Override
	public View initView(LayoutInflater inflater) {
		view = inflater.inflate(R.layout.fragment_personal, null);
		logout = (TextView) view.findViewById(R.id.logout);
		logout.setOnClickListener(this);
		rl_haslogin = (RelativeLayout) view.findViewById(R.id.rl_haslogin);
		rl_setip = (RelativeLayout) view.findViewById(R.id.rl_setip);
		rl_setip.setOnClickListener(this);
		social = (RelativeLayout) view.findViewById(R.id.socialabout);
		social.setOnClickListener(this);
		bt_lost = (RelativeLayout) view.findViewById(R.id.bt_lost);
		bt_lost.setOnClickListener(this);
		secondhand = (RelativeLayout) view.findViewById(R.id.secondhand);
		secondhand.setOnClickListener(this);
		bt_login = (Button) view.findViewById(R.id.bt_login);
		bt_login.setOnClickListener(this);
		iv_upic = (ImageView) view.findViewById(R.id.iv_upic);
		tv_name = (TextView) view.findViewById(R.id.tv_name);
		tv_description = (TextView) view.findViewById(R.id.tv_description);
		editinfo = (TextView) view.findViewById(R.id.editinfo);
		editinfo.setOnClickListener(this);
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

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.bt_login:
			intent = new Intent(ct, LoginActivity.class);
			startActivity(intent);
			break;
		case R.id.editinfo:
			if (GlobalParams.userInfo == null) {
				DialogUtils.showToast(ct, "别闹，您还未登录");
			} else {
				intent = new Intent(ct, UserInfoActivity.class);
				startActivity(intent);
			}
			break;
		case R.id.logout:
			if(GlobalParams.userInfo == null){
				DialogUtils.showToast(ct, "别闹，您还未登录");
			}else{
				new AlertDialog(ct).builder().setTitle("确定退出吗？")
				.addSheetItem("是", SheetItemColors.Blue, new OnSheetItemClickListeners() {
					
					@Override
					public void onClick(int which) {
						GlobalParams.userInfo = null;
						SharePrefUtil.saveString(ct, "userinfo", "");
						onResume();
					}
				}).addSheetItem("否",SheetItemColors.Blue, new OnSheetItemClickListeners() {
					
					@Override
					public void onClick(int which) {
						
					}
				}).show();
			}
			break;
		case R.id.socialabout:
			if (GlobalParams.userInfo == null) {
				DialogUtils.showToast(ct, "别闹，您还未登录");
			} else {
				intent = new Intent(ct, MySocialAboutActivity.class);
				startActivity(intent);
			}
			break;
		case R.id.secondhand:
			if (GlobalParams.userInfo == null) {
				DialogUtils.showToast(ct, "别闹，您还未登录");
			} else {
				intent = new Intent(ct, MySecondHandAboutActivity.class);
				startActivity(intent);
			}
			break;
		case R.id.bt_lost:
			if (GlobalParams.userInfo == null) {
				DialogUtils.showToast(ct, "别闹，您还未登录");
			} else {
				intent = new Intent(ct, LostActivity.class);
				intent.putExtra("isMe", true);
				startActivity(intent);
			}
			break;
		case R.id.rl_setip:
			
			new AlertDialog(ct).builder().setTitle("设置服务器ip地址").setCancelable(false)
			.setPositiveButton("确认", new setEditPositiveClickListener() {
				@Override
				public void onClick(View v, String msg) {
					ConstantValue.LOTTERY_URI = msg;
					//这里可以顺便测试是否能连接上服务器
					/**
					 * 测试是否能连接上服务器
					 */
					final Dialog dialog = DialogUtils.createProgressDialog(ct, "正在连接服务器");
					dialog.show();
					new AsyncTask<String,Void,Boolean>() {
						@Override
						protected Boolean doInBackground(String... params) {
//							ServerEngineImpl impl = new ServerEngineImpl();
							ServerEngine impl = BeanFactory.getInstance().getImpl(ServerEngine.class);
							boolean result = impl.testIsConnect();
							return result;
						}
						
						@Override
						protected void onPostExecute(Boolean result){
							GlobalParams.isConnect = result;
							if(!result){
								DialogUtils.showToast(ct, "不能连接到服务器");
							}else{
								DialogUtils.showToast(ct, "成功连接上服务器");
								SharePrefUtil.saveString(ct, "LOTTERY_URI", ConstantValue.LOTTERY_URI);
							}
							dialog.dismiss();
						}
					}.execute();
				}
			}).setNegativeButton("取消", new OnClickListener() {
				@Override
				public void onClick(View v) {
					
				}
			}).addEdit("",ConstantValue.LOTTERY_URI).show();
			
		}

	}

	@Override
	public void onResume() {
	
		if (GlobalParams.userInfo != null) {
			bt_login.setVisibility(View.GONE);
			rl_haslogin.setVisibility(View.VISIBLE);
			bitmapUtil = new BitmapUtils(ct);
			bitmapUtil.display(iv_upic, ConstantValue.LOTTERY_URI
					+ GlobalParams.userInfo.pic);
			tv_name.setText(GlobalParams.userInfo.name);
			tv_description.setText(GlobalParams.userInfo.description);
		} else {
			bt_login.setVisibility(View.VISIBLE);
			rl_haslogin.setVisibility(View.GONE);
		}
		super.onResume();
	}

}
