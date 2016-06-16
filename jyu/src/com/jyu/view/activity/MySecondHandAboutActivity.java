package com.jyu.view.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jyu.R;
import com.jyu.commonUtils.BeanFactory;
import com.jyu.commonUtils.CommonUtil;
import com.jyu.domain.SecondHandInfo;
import com.jyu.engine.SecondHandEngine;
import com.jyu.view.adapter.SecondHandAdapter;
import com.jyu.view.base.BaseActivity;

/**
 * 点击我的二手货相关的页面，要列举出我所发送的所有二手货
 * @author Administrator
 *
 */
public class MySecondHandAboutActivity extends BaseActivity {

	private PullToRefreshListView mPullRefreshListView;
	private List<SecondHandInfo> item = new ArrayList<SecondHandInfo>();
	private SecondHandAdapter adapter;
	private LinearLayout title_bar;
	private TextView nodata;
	
	@Override
	protected void initView() {
		setContentView(R.layout.activity_secondhand);
		nodata = (TextView) findViewById(R.id.nodata);
		initTitleBar();
		titleTv.setText("我的二手货");
		title_bar = (LinearLayout) findViewById(R.id.title_bar);
		title_bar.setBackgroundColor(Color.WHITE);
		
		mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
		mPullRefreshListView.setMode(Mode.BOTH);
		// Set a listener to be invoked when the list should be refreshed.
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
					Intent intent = new Intent(ct,
							SecondHandPlayerListAcvitity.class);
					intent.putExtra("shid", item.get(position-1).getId());
					Bundle bund = new Bundle();
					bund.putSerializable("SecondHandItem",
							item.get(position-1));
					intent.putExtra("value", bund);
					startActivity(intent);
				
			}
		});
	}

	@Override
	protected void initData() {
//		SecondHandEngineImpl impl = new SecondHandEngineImpl();
		List<SecondHandInfo> cacheList = new ArrayList<SecondHandInfo>();
		if (cacheList != null) {
			processDataFromCache(cacheList);
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
				List<SecondHandInfo> list = engine.getSecondHandPublisher(page, false,ct);
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
					processData();
				} else {
//					Log.i("LostActivity", "获取数据失败,result为空");
					onLoaded();
				}
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
		
		onLoaded();

	}

	@Override
	protected void processClick(View v) {
		switch (v.getId()) {
		}
	}

//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		if (data != null) {
//			switch (requestCode) {
//			case 1:
//				Bundle bund = data.getBundleExtra("value");
//				SecondHandInfo info = (SecondHandInfo) bund
//						.getSerializable("NewItem");
//				item.add(0, info);
//				adapter.notifyDataSetChanged();
//				updateSecondHandInfo(info);
//				break;
//			}
//		}
//	}

//	private void updateSecondHandInfo(final SecondHandInfo info) {
//		new AsyncTask<String, Void, Boolean>() {
//			@Override
//			protected Boolean doInBackground(String... params) {
//				SecondHandEngineImpl impl = new SecondHandEngineImpl(ct);
//				boolean result = impl.addSecondHand(info);
//				return result;
//			}
//
//			@Override
//			protected void onPostExecute(Boolean result) {
//				if (result) {
//					DialogUtils.showToast(ct, "发布成功!!");
//					FileUtils.deleteDir();
//				} else {
//					DialogUtils.showToast(ct, "发布失败!!");
//				}
//				super.onPostExecute(result);
//			}
//		}.execute();
//	}


	protected void onLoaded() {
		dismissLoadingView();
		if(item.size() == 0){
			nodata.setText("当前并无您发布的信息");
			nodata.setVisibility(View.VISIBLE);
		}else{
			nodata.setVisibility(View.INVISIBLE);
		}

	}
	
}
