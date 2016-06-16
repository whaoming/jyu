package com.jyu.view.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jyu.GlobalParams;
import com.jyu.R;
import com.jyu.bean.R_Lost;
import com.jyu.bean.R_Lost.LostItem;
import com.jyu.commonUtils.BeanFactory;
import com.jyu.commonUtils.CommonUtil;
import com.jyu.domain.LostItemInfo;
import com.jyu.engine.LostItemEngine;
import com.jyu.view.adapter.LostItemAdapter;
import com.jyu.view.base.BaseActivity;
import com.jyu.view.custom.mydialog.AlertDialog;
import com.jyu.view.custom.mydialog.AlertDialog.OnSheetItemClickListeners;
import com.jyu.view.custom.mydialog.AlertDialog.SheetItemColors;
import com.jyu.view.custom.photoutil.FileUtils;
import com.jyu.view.util.DialogUtils;

public class LostActivity extends BaseActivity {

	private PullToRefreshListView mPullRefreshListView;
	List<LostItem> lostItemList = new ArrayList<LostItem>();
	
	private LostItemAdapter adapter;
	private TextView nodata;

	/**
	 * 是否为查看自己发表的页面
	 */
	private boolean isMe;

	@Override
	protected void initView() {
		setContentView(R.layout.activity_lost);
		initTitleBar();
		initRight("发表");
		nodata = (TextView) findViewById(R.id.nodata);
		isMe = getIntent().getBooleanExtra("isMe", false);
		titleTv.setText("失物找回");
		mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
		mPullRefreshListView.setMode(Mode.BOTH);
		// Set a listener to be invoked when the list should be refreshed.
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
						getLostList(lostItemList.size());

					}
				});
		mPullRefreshListView.getRefreshableView().setOnItemClickListener(
				new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						Intent intent = new Intent(ct,
								LostFoundDetailActivity2.class);
						Bundle bund = new Bundle();
						bund.putSerializable("LostItem", lostItemList.get(position - 1));
						intent.putExtra("value", bund);
						startActivity(intent);

					}
				});
	}

	@Override
	protected void initData() {
		LostItemEngine engine = BeanFactory.getInstance().getImpl(
				LostItemEngine.class);
		R_Lost cacheList = null;
		if (isMe) {
			cacheList = engine.getMyItemList(0, true, ct);
		} else {
			cacheList = engine.getItemList(0, true, ct);
		}

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

	private void processDataFromCache(R_Lost cache_R_Lost) {
		lostItemList.addAll(cache_R_Lost.lostItemList);

		if (adapter == null) {
			adapter = new LostItemAdapter(ct, lostItemList, isMe);
			mPullRefreshListView.getRefreshableView().setAdapter(adapter);
		} else {
			adapter.notifyDataSetChanged();
		}
	}

	private void getLostList(final int page) {
		new AsyncTask<String, Void, R_Lost>() {
			@Override
			protected R_Lost doInBackground(String... params) {
				LostItemEngine engine = BeanFactory.getInstance().getImpl(
						LostItemEngine.class);
				R_Lost r_lost =  null;
				if (!isMe) {
					r_lost = engine.getItemList(page, false, ct);
				} else {
					r_lost = engine.getMyItemList(page, false, ct);
				}
				return r_lost;
			}

			@Override
			protected void onPostExecute(R_Lost result) {
				if (result != null && result.success == 1) {
					if (page == 0) {
						lostItemList.clear();
						lostItemList.addAll(result.lostItemList);
					} else {
						lostItemList.addAll(result.lostItemList);
					}

				} else {
					DialogUtils.showToast(ct, "连接不上服务器");
				}
				processData();
				mPullRefreshListView.onRefreshComplete();
				super.onPostExecute(result);
			}
		}.execute();
	}

	protected void processData() {
		if (adapter == null) {
			adapter = new LostItemAdapter(ct, lostItemList, isMe);
			mPullRefreshListView.getRefreshableView().setAdapter(adapter);
		} else {
			adapter.notifyDataSetChanged();
		}
		dismissLoadingView();
		onLoaded();
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
				final Intent intent = new Intent(LostActivity.this,
						LostItemPublishActivity.class);
				new AlertDialog(LostActivity.this)
						.builder()
						.setTitle("要发布的是?")
						.addSheetItem("寻物启事", SheetItemColors.Blue,
								new OnSheetItemClickListeners() {
									@Override
									public void onClick(int which) {
										intent.putExtra("isLost", true);
										startActivityForResult(intent, 1);
									}
								})
						.addSheetItem("失物招领", SheetItemColors.Blue,
								new OnSheetItemClickListeners() {
									@Override
									public void onClick(int which) {
										intent.putExtra("isLost", false);
										startActivityForResult(intent, 1);
									}
								}).show();
				break;
			}
		}
	}

//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		if (data != null) {
//			switch (requestCode) {
//			case 1:
//				boolean isEidt = data.getBooleanExtra("isEdit", false);
//				if (isEidt) {
//					int position = data.getIntExtra("position", -1);
//					Bundle bund = data.getBundleExtra("value");
//					LostItemInfo info = (LostItemInfo) bund
//							.getSerializable("updateitem");
//					item.remove(position);
//					item.add(position, info);
//					adapter.notifyDataSetChanged();
//					updateToWeb(info);
//				} else {
//					Bundle bund = data.getBundleExtra("value");
//					LostItemInfo info = (LostItemInfo) bund
//							.getSerializable("NewItem");
//					item.add(0, info);
//					adapter.notifyDataSetChanged();
//					addLost(info);
//					break;
//				}
//			}
//		}
//
//	}

	private void updateToWeb(final LostItemInfo info) {
		new AsyncTask<String, Void, Boolean>() {
			@Override
			protected Boolean doInBackground(String... params) {
				LostItemEngine engine = BeanFactory.getInstance().getImpl(
						LostItemEngine.class);
				boolean result = engine.updateItem(info);
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

	/**
	 * 把此实体添加到服务器
	 * 
	 * @param lost
	 */
	private void addLost(final LostItemInfo lost) {
		new AsyncTask<String, Void, Boolean>() {
			@Override
			protected Boolean doInBackground(String... params) {
				// LostItemEngineImpl impl = new LostItemEngineImpl(ct);
				LostItemEngine engine = BeanFactory.getInstance().getImpl(
						LostItemEngine.class);
				boolean result = engine.addLost(lost);
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
		if (lostItemList.size() == 0) {
			nodata.setText("当前并无数据");
			nodata.setVisibility(View.VISIBLE);
		} else {
			nodata.setVisibility(View.INVISIBLE);
		}
	}

}
