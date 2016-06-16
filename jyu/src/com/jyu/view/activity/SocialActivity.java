package com.jyu.view.activity;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jyu.GlobalParams;
import com.jyu.R;
import com.jyu.bean.R_Social;
import com.jyu.bean.R_Social.SocialItem;
import com.jyu.commonUtils.BeanFactory;
import com.jyu.commonUtils.CommonUtil;
import com.jyu.engine.SocialEngine;
import com.jyu.view.adapter.SocialAdapter;
import com.jyu.view.base.BaseActivity;
import com.jyu.view.util.DialogUtils;

public class SocialActivity extends BaseActivity {

	private PullToRefreshListView mPullRefreshListView;
	List<SocialItem> socialList;
	List<Integer> hasDot;
	private FrameLayout loading_view;
	private SocialAdapter adapter;

	@SuppressLint("InflateParams")
	@Override
	protected void initView() {
		setContentView(R.layout.activity_social);
		initTitleBar();
		titleTv.setText("校园活动");
		initRight("发起");
		socialList = new ArrayList<SocialItem>();
		hasDot = new ArrayList<Integer>();
		loading_view = (FrameLayout) findViewById(R.id.loading_view);
		mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
		mPullRefreshListView.setMode(Mode.BOTH);
		mPullRefreshListView
				.setOnRefreshListener(new OnRefreshListener2<ListView>() {

					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						getLostList(0);
						refreshView
								.getLoadingLayoutProxy()
								.setLastUpdatedLabel(CommonUtil.getStringDate());
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						getLostList(socialList.size());

					}
				});
		mPullRefreshListView.getRefreshableView().setOnItemClickListener(
				new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						Intent intent = new Intent(ct,
								SocialDetailActivity.class);
						Bundle bund = new Bundle();
						bund.putSerializable("socialinfo",
								socialList.get(position - 1));
						if (hasDot != null
								&& hasDot.contains(socialList
										.get(position - 1).id)) {
							intent.putExtra("isJoin", true);
						} else {
							intent.putExtra("isJoin", false);
						}
						intent.putExtra("location", position);
						intent.putExtra("value", bund);
						// 因为你修改了 所以回来也要修改界面ui
						startActivityForResult(intent, 2);
					}
				});
	}

	@Override
	protected void initData() {
		SocialEngine engine = BeanFactory.getInstance().getImpl(
				SocialEngine.class);
		R_Social cache_r_social = engine.getSocialList(1, 0, true, ct);
		if (cache_r_social != null) {
			processDataFromCache(cache_r_social);
		}
		if (0 == CommonUtil.isNetworkAvailable(ct)) {// 无网络
			DialogUtils.showToast(ct, "检测不到网络连接");
		} else {
			getLostList(0);
		}
	}

	public void getLostList(final int page) {
		new AsyncTask<String, Void, R_Social>() {
			@Override
			protected R_Social doInBackground(String... params) {
				SocialEngine engine = BeanFactory.getInstance().getImpl(
						SocialEngine.class);
				R_Social list = engine.getSocialList(1, page, false, ct);
				return list;
			}

			@Override
			protected void onPostExecute(R_Social result) {
				if (result != null && "getsocial".equals(result.response)) {
					if (page == 0) {
						socialList.clear();
						socialList.addAll(result.socialList);
						hasDot.addAll(result.hasJoinIdList);
					} else {
						socialList.addAll(result.socialList);
					}
					processData();
				} else {
					DialogUtils.showToast(ct, "不能成功连接到服务器");
				}
				mPullRefreshListView.onRefreshComplete();
				super.onPostExecute(result);
			}
		}.execute();
	}

	public void processData() {
		if (adapter == null) {
			adapter = new SocialAdapter(ct, socialList,hasDot);
			mPullRefreshListView.getRefreshableView().setAdapter(adapter);
		} else {
			adapter.notifyDataSetChanged();
		}
		onLoaded();
	}

	public void processDataFromCache(R_Social cache_r_social) {
		hasDot.addAll(cache_r_social.hasJoinIdList);
		socialList.addAll(cache_r_social.socialList);
		if (adapter == null) {
			adapter = new SocialAdapter(ct, socialList,hasDot);
			mPullRefreshListView.getRefreshableView().setAdapter(adapter);
		} else {
			adapter.notifyDataSetChanged();
		}
		onLoaded();

	}

	protected void onLoaded() {
		if (loading_view != null)
			loading_view.setVisibility(View.INVISIBLE);

	}

	@Override
	protected void processClick(View v) {
		switch (v.getId()) {
		/**
		 * 点击发起按钮 1.弹出一个对话框，显示是要社团还是个人，先点击个人 2.然后就跳到那个新建页面
		 */
		case R.id.rightbutton:
			if (GlobalParams.userInfo == null) {
				DialogUtils.showToast(ct, "亲，还未登陆，请先登录");
				Intent intent = new Intent(ct, LoginActivity.class);
				ct.startActivity(intent);
			} else {
				Intent intent = new Intent(SocialActivity.this,
						SocialPublishActivity.class);
				startActivityForResult(intent, 1);
			}
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		if (intent != null) {
			if (requestCode == 1) {
				getLostList(0);
			}
			// ppage.update(intent);
			// }else if(requestCode == 2){
			// tpage.update(intent);
			// }
		}
		super.onActivityResult(requestCode, resultCode, intent);
	}

}
