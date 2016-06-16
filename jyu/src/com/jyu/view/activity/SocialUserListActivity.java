package com.jyu.view.activity;

import java.util.ArrayList;
import java.util.List;

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
import com.jyu.engine.SocialEngine;
import com.jyu.view.adapter.SocialUserListAdapter;
import com.jyu.view.base.BaseActivity;

public class SocialUserListActivity extends BaseActivity {

	
	private PullToRefreshListView mPullRefreshListView;
	private List<UserCommentInfo> item = new ArrayList<UserCommentInfo>();
	//带过来的socialid
	private int socialid;
	private SocialUserListAdapter adapter;
	
	private TextView nodata;
	
	@Override
	protected void initView() {
		setContentView(R.layout.activity_listview);
		initTitleBar();
		titleTv.setText("查看报名用户");
		mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
		nodata = (TextView) findViewById(R.id.nodata);
		mPullRefreshListView.setMode(Mode.BOTH);
		// Set a listener to be invoked when the list should be refreshed.
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
//		ptrLv.setNoLine();
		socialid = getIntent().getIntExtra("socialid", 0);
	}
	

	@Override
	protected void initData() {
		getUserList(0);
	}

	

	@Override
	protected void processClick(View v) {
		
	}	
	
	private void getUserList(final int page) {
		new AsyncTask<String, Void, List<UserCommentInfo>>() {

			@Override
			protected List<UserCommentInfo> doInBackground(String... params) {
				SocialEngine impl = BeanFactory.getInstance().getImpl(SocialEngine.class);
				List<UserCommentInfo> list = impl.getSocialUserList(page, socialid);
				return list;
			}
			@Override
			protected void onPostExecute( List<UserCommentInfo> result) {
				if(result != null){
					if (page == 0) {
						item.clear();
						item.addAll(result);
					} else {
						item.addAll(result);
					}
					processData();
				} else {
					//获取不到数据
				}
				mPullRefreshListView.onRefreshComplete();
			}
		}.execute();
		
	}


	protected void processData() {
		if(adapter == null){
			adapter = new SocialUserListAdapter(ct, item);
			mPullRefreshListView.getRefreshableView().setAdapter(adapter);
		} else {
			adapter.notifyDataSetChanged();
		}
		onLoaded();
	}
	
	

	private void onLoaded() {
		dismissLoadingView();
		if(item.size() == 0){
			nodata.setText("当前并无用户报名");
			nodata.setVisibility(View.VISIBLE);
		}else{
			nodata.setVisibility(View.INVISIBLE);
		}
	}

}
