package com.jyu.view.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jyu.ConstantValue;
import com.jyu.GlobalParams;
import com.jyu.R;
import com.jyu.bean.R_Social.SocialItem;
import com.jyu.commonUtils.BeanFactory;
import com.jyu.engine.SocialEngine;
import com.jyu.view.activity.LoginActivity;
import com.jyu.view.custom.mydialog.JoinSocialDialog;
import com.jyu.view.custom.mydialog.JoinSocialDialog.JoinSocialDialogListener;
import com.jyu.view.util.CustomProgressDialog;
import com.jyu.view.util.DialogUtils;
import com.lidroid.xutils.BitmapUtils;

public class SocialAdapter extends BaseAdapter {

	private List<SocialItem> list;
	private Context ct;
	BitmapUtils bitmapUtil;
	List<Integer> hasDot;

	public SocialAdapter(Context ct, List<SocialItem> list,List<Integer> hasDot) {
		this.ct = ct;
		this.list = list;
		bitmapUtil = new BitmapUtils(ct);
		this.hasDot = hasDot;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final Holder hold;
		final SocialItem social = list.get(position);
		if (convertView == null) {
			hold = new Holder();
			convertView = View.inflate(ct, R.layout.item_social, null);
			hold.bt_join = (RelativeLayout) convertView
					.findViewById(R.id.bt_join);
			hold.title = (TextView) convertView.findViewById(R.id.tv_title);
			hold.pic = (ImageView) convertView.findViewById(R.id.iv_pic);
			hold.like = (ImageView) convertView.findViewById(R.id.iv_like);
			hold.des = (ImageView) convertView.findViewById(R.id.iv_des);
			hold.tv_join = (TextView) convertView.findViewById(R.id.tv_join);
			hold.tv_num = (TextView) convertView.findViewById(R.id.tv_num);
			convertView.setTag(hold);
		} else {
			hold = (Holder) convertView.getTag();
		}

		if (social.issignup == 1) {
			hold.tv_join.setText("Join");
			hold.tv_num.setVisibility(View.VISIBLE);
			hold.tv_num.setText(social.issignup + "/" + social.need);
			hold.tv_join.setClickable(false);
		} else {
			hold.tv_join.setText("无需报名");
			hold.tv_num.setVisibility(View.INVISIBLE);
			hold.tv_join.setClickable(true);
		}
		hold.title.setText(social.title);
		hold.like.setImageBitmap(BitmapFactory.decodeResource(
				ct.getResources(), R.drawable.blue_heart));
		hold.like.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				hold.like.setImageBitmap(BitmapFactory.decodeResource(
						ct.getResources(), R.drawable.click_heart));

			}
		});
		bitmapUtil.display(hold.pic,
				ConstantValue.LOTTERY_URI + social.pic);
		hold.bt_join.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (GlobalParams.userInfo == null) {
					DialogUtils.showToast(ct, "亲,请先登录");
					Intent intent = new Intent(ct, LoginActivity.class);
					ct.startActivity(intent);
				} else {
					if (!(social.issignup == 1)) {
						DialogUtils.showToast(ct, "这是一个无须报名的活动");
					} else {
						if (hasDot.contains(social.id)) {
							DialogUtils.showToast(ct, "亲,您已经包过名了");
						} else {
							final CustomProgressDialog d = (CustomProgressDialog) DialogUtils
									.createProgressDialog(ct, "");
							new JoinSocialDialog(ct,
									GlobalParams.userInfo.pic,
									GlobalParams.userInfo.name,
									new JoinSocialDialogListener() {
										@Override
										public void seng(String tname,
												String snum) {
											d.show();
											new AsyncTask<String, Void, Boolean>() {
												@Override
												protected Boolean doInBackground(
														String... params) {
//													SocialEngineImpl impl = new SocialEngineImpl();
													SocialEngine impl = BeanFactory.getInstance().getImpl(SocialEngine.class);
													boolean result = impl
															.joinSocial(social
																	.id);
													return result;
												}

												@Override
												protected void onPostExecute(
														Boolean result) {
													if (result) {
														DialogUtils.showToast(
																ct, "报名成功");
														hold.tv_num.setText(social.issignup +1+ "/" + social.need);
													} else {
														DialogUtils.showToast(
																ct, "报名失败");
													}
													d.dismiss();

													super.onPostExecute(result);
												}
											}.execute();
										}
									}).show();
						}
					}

				}
			}
		});
		return convertView;
	}

	static class Holder {
		ImageView pic;
		TextView title;
		ImageView des;
		ImageView like;
		RelativeLayout bt_join;
		TextView tv_join;
		TextView tv_num;
	}

}
