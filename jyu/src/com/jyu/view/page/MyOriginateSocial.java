package com.jyu.view.page;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jyu.R;
import com.jyu.commonUtils.BeanFactory;
import com.jyu.commonUtils.CommonUtil;
import com.jyu.domain.Social;
import com.jyu.engine.SocialEngine;
import com.jyu.view.activity.SocialDetailActivity;
import com.jyu.view.adapter.SocialAboutMeAdapter;
import com.jyu.view.base.BasePage;
import com.jyu.view.util.DialogUtils;

public class MyOriginateSocial extends BasePage {

	private PullToRefreshListView mPullRefreshListView;
	List<Social> item;
	private FrameLayout loading_view;
	private SocialAboutMeAdapter adapter;
	private TextView nodata;

	public MyOriginateSocial(Context context) {
		super(context);
	}

	@Override
	public void onClick(View v) {
		
	}

	@SuppressLint("InflateParams")
	@Override
	public View initView(LayoutInflater inflater) {
		View view = inflater.inflate(R.layout.pager_social, null);
		nodata = (TextView) view.findViewById(R.id.nodata);
		item = new ArrayList<Social>();
		loading_view = (FrameLayout) view.findViewById(R.id.loading_view);
		// 上拉加载不可用
//		ptrLv.setNoLine();
		mPullRefreshListView = (PullToRefreshListView) view.findViewById(R.id.pull_refresh_list);
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
						SocialDetailActivity.class);
				Bundle bund = new Bundle();
				bund.putSerializable("socialinfo", item.get(position));
				// intent.putIntegerArrayListExtra("hasDot",
				// (ArrayList<Integer>) hasDot);
				// int id =
				// if(hasDot.contains(item.get(position).getId())){
				 intent.putExtra("isJoin", true);
				// }else{
				// intent.putExtra("isJoin", false);
				// }
//				intent.putExtra("location", position);
				intent.putExtra("value", bund);
//				Activity activity = (Activity) ct;
//				activity.startActivityForResult(intent, 1);
				 ct.startActivity(intent);
				
			}
		});
		return view;
	}

	@Override
	public void initData() {
		if (0 == CommonUtil.isNetworkAvailable(ct)) {// 无网络
			DialogUtils.showToast(ct, "检测不到网络连接");
		} else {
			getLostList(0);
		}
	}
	public void getLostList(final int page) {
		new AsyncTask<String, Void, List<Social>>() {
			@Override
			protected List<Social> doInBackground(String... params) {
//				SocialEngineImpl engine = new SocialEngineImpl();
				SocialEngine engine = BeanFactory.getInstance().getImpl(SocialEngine.class);
				List<Social> list = engine.getMyOriginateSocialList(page);
				return list;
			}

			@Override
			protected void onPostExecute(List<Social> result) {
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

	public void processData() {
		if (adapter == null) {
			 adapter = new SocialAboutMeAdapter(ct, item,true);
			 mPullRefreshListView.getRefreshableView().setAdapter(adapter);
		} else {
			adapter.notifyDataSetChanged();
		}
		onLoaded();
	}
	
	protected void onLoaded() {
		if (loading_view != null)
			loading_view.setVisibility(View.INVISIBLE);
		if(item.size() == 0){
			nodata.setText("当前并无参加过的活动");
			nodata.setVisibility(View.VISIBLE);
		}else{
			nodata.setVisibility(View.INVISIBLE);
		}

	}


	public void updateItem(Intent intent) {
		int position = intent.getIntExtra("position", 0);
		Bundle bund = intent.getBundleExtra("value");
		Social info = (Social) bund.getSerializable("info");
		item.remove(position);
		item.add(position, info);
		adapter.notifyDataSetChanged();
	}
	


}
