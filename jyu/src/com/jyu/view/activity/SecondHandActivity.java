package com.jyu.view.activity;


import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jyu.GlobalParams;
import com.jyu.R;
import com.jyu.commonUtils.BeanFactory;
import com.jyu.commonUtils.CommonUtil;
import com.jyu.domain.SecondHandInfo;
import com.jyu.engine.SecondHandEngine;
import com.jyu.view.adapter.SecondHandAdapter;
import com.jyu.view.base.BaseActivity;
import com.jyu.view.custom.photoutil.FileUtils;
import com.jyu.view.util.DialogUtils;

public class SecondHandActivity extends BaseActivity {

	private PullToRefreshListView mPullRefreshListView;
	private List<SecondHandInfo> item = new ArrayList<SecondHandInfo>();
	private SecondHandAdapter adapter;
	private View header;
	private LinearLayout title_bar;
	private TextView nodata;

	@SuppressLint("InflateParams")
	@Override
	protected void initView() {
		setContentView(R.layout.activity_secondhand);
		LayoutInflater inflater = getLayoutInflater();
		header = inflater.inflate(R.layout.header_secondhand, null);
		nodata = (TextView) findViewById(R.id.nodata);
		initTitleBar();
		title_bar = (LinearLayout) findViewById(R.id.title_bar);
		title_bar.setBackgroundColor(Color.WHITE);
		initRight("发布");
		titleTv.setText("跳蚤市场");
		mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
		mPullRefreshListView.getRefreshableView().addHeaderView(header);
		mPullRefreshListView.setMode(Mode.BOTH);
		mPullRefreshListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				getLostList(0);
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(CommonUtil.getStringDate());
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				getLostList(item.size());
				
			}
		});
		mPullRefreshListView.getRefreshableView().setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if(position<2){
					return;
				}else{
				Intent intent = new Intent(ct,
						SecondHandDetailActivity.class);
				Bundle bund = new Bundle();
				bund.putSerializable("SecondHandItem",
						item.get(position-2));
				intent.putExtra("value", bund);
				startActivity(intent);
				}
			}
		});
	}

	@Override
	protected void initData() {
		SecondHandEngine engine = BeanFactory.getInstance().getImpl(SecondHandEngine.class);
		List<SecondHandInfo> cacheList = engine.getSecondHandList(0, true,ct);
		if (cacheList != null) {
			processDataFromCache(cacheList);
			dismissLoadingView();
		}
		if (0 == CommonUtil.isNetworkAvailable(ct)) {// 无网络
			showToast("检测不到网络连接");
		} else {
			getLostList(0);
		}
	}

	private void getLostList(final int page) {
		new AsyncTask<String, Void, List<SecondHandInfo>>() {
			@Override
			protected List<SecondHandInfo> doInBackground(String... params) {
//				SecondHandEngineImpl engine = new SecondHandEngineImpl(ct);
				SecondHandEngine engine = BeanFactory.getInstance().getImpl(SecondHandEngine.class);
				List<SecondHandInfo> list = engine.getSecondHandList(page,
						false,ct);
				return list;
			}

			@Override
			protected void onPostExecute(List<SecondHandInfo> result) {
				if (result != null) {
					if (page == 0) {
						item.clear();
						item.addAll(result);
					} else {
						item.addAll(result);
					}
					
				} else {
					DialogUtils.showToast(ct, "连接不到服务器");
				}
				processData();
				mPullRefreshListView.onRefreshComplete();
				super.onPostExecute(result);
			}
		}.execute();

	}

	protected void processData() {
		if (adapter == null) {
			adapter = new SecondHandAdapter(ct, item);
			mPullRefreshListView.getRefreshableView().setAdapter(adapter);
		} else {
			adapter.notifyDataSetChanged();
		}
		dismissLoadingView();
		onLoaded();

	}

	private void processDataFromCache(List<SecondHandInfo> cacheList) {
		item.addAll(cacheList);
		if (adapter == null) {
			adapter = new SecondHandAdapter(ct, item);
			mPullRefreshListView.getRefreshableView().setAdapter(adapter);
		} else {
			adapter.notifyDataSetChanged();
		}
		dismissLoadingView();

	}

	@Override
	protected void processClick(View v) {
		switch (v.getId()) {
		case R.id.rightbutton:
			if (GlobalParams.userInfo == null) {
				DialogUtils.showToast(ct, "亲，还未登陆，请先登录");
				Intent intent = new Intent(ct, LoginActivity.class);
				ct.startActivity(intent);
			} else {
				Intent intent = new Intent(SecondHandActivity.this,
						SecondHandPublishActivity.class);
				startActivityForResult(intent, 1);
			}
			break;
		
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data != null) {
			switch (requestCode) {
			case 1:
				Bundle bund = data.getBundleExtra("value");
				SecondHandInfo info = (SecondHandInfo) bund
						.getSerializable("NewItem");
				item.add(0, info);
				adapter.notifyDataSetChanged();
				updateSecondHandInfo(info);
				break;
			}
		}
	}

	private void updateSecondHandInfo(final SecondHandInfo info) {
		new AsyncTask<String, Void, Boolean>() {
			@Override
			protected Boolean doInBackground(String... params) {
//				SecondHandEngineImpl impl = new SecondHandEngineImpl(ct);
				SecondHandEngine engine = BeanFactory.getInstance().getImpl(SecondHandEngine.class);
				boolean result = engine.addSecondHand(info);
				return result;
			}

			@Override
			protected void onPostExecute(Boolean result) {
				if (result) {
					DialogUtils.showToast(ct, "发布成功!!");
					FileUtils.deleteDir();
				} else {
					DialogUtils.showToast(ct, "发布失败!!");
				}
				super.onPostExecute(result);
			}
		}.execute();
	}


	private void onLoaded() {
		
		if (item.size() == 0) {
			nodata.setText("当前暂无数据");
			nodata.setVisibility(View.VISIBLE);
		} else {
			nodata.setVisibility(View.INVISIBLE);
		}
	}

}
