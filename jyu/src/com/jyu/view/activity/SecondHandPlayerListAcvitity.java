package com.jyu.view.activity;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jyu.R;
import com.jyu.commonUtils.BeanFactory;
import com.jyu.commonUtils.CommonUtil;
import com.jyu.domain.UserCommentInfo;
import com.jyu.engine.SecondHandEngine;
import com.jyu.view.adapter.SecondHandPlayerListAdapter;
import com.jyu.view.base.BaseActivity;

public class SecondHandPlayerListAcvitity extends BaseActivity {
	private PullToRefreshListView mPullRefreshListView;
	private int shid;
	private List<UserCommentInfo> item = new ArrayList<UserCommentInfo>();
	private SecondHandPlayerListAdapter adapter;
	private TextView nodata;

	@Override
	protected void initView() {
		setContentView(R.layout.activity_listview);
		initTitleBar();
		titleTv.setText("关注用户");
		nodata = (TextView) findViewById(R.id.nodata);
		mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
		mPullRefreshListView.setMode(Mode.BOTH);
		mPullRefreshListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				getUserList(0);
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(CommonUtil.getStringDate());
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				getUserList(item.size());
				
			}
		});
		mPullRefreshListView.getRefreshableView().setSelector(new ColorDrawable(Color.TRANSPARENT));
		shid = getIntent().getIntExtra("shid", 0);
	}

	@Override
	protected void initData() {
		getUserList(0);
	}

	@Override
	protected void processClick(View v) {
		// TODO Auto-generated method stub

	}

	private void getUserList(final int page) {
		new AsyncTask<String, Void, List<UserCommentInfo>>() {
			@Override
			protected List<UserCommentInfo> doInBackground(String... params) {
//				SecondHandEngineImpl impl = new SecondHandEngineImpl(ct);
				SecondHandEngine engine = BeanFactory.getInstance().getImpl(SecondHandEngine.class);
				List<UserCommentInfo> list = engine.getSecondHandPlayer(page,
						shid, false);
				return list;
			}

			@Override
			protected void onPostExecute(List<UserCommentInfo> result) {
				if (result != null) {
					if (page == 0) {
						item.clear();
						item.addAll(result);
					} else {
						item.addAll(result);
					}
					processData();
				} else {
					// 获取不到数据
				}
				mPullRefreshListView.onRefreshComplete();
			}
		}.execute();
	}

	protected void processData() {
		if(adapter == null){
			adapter = new SecondHandPlayerListAdapter(ct, item);
			mPullRefreshListView.getRefreshableView().setAdapter(adapter);
		}else {
			adapter.notifyDataSetChanged();
		}
		onLoaded();
	}

	private void onLoaded() {
		dismissLoadingView();
		if(item.size() == 0){
			nodata.setText("当前并无用户关注");
			nodata.setVisibility(View.VISIBLE);
		}else{
			nodata.setVisibility(View.INVISIBLE);
		}
	}

}
