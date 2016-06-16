package com.jyu.view.fragment;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.jyu.ConstantValue;
import com.jyu.R;
import com.jyu.bean.R_News;
import com.jyu.bean.R_News.NormalNewsItem;
import com.jyu.bean.R_News.TopicNewsItem;
import com.jyu.commonUtils.BeanFactory;
import com.jyu.commonUtils.CommonUtil;
import com.jyu.engine.NewsGetEngine;
import com.jyu.view.activity.NewsDetailActivity;
import com.jyu.view.adapter.NewsAdapter;
import com.jyu.view.base.BaseFragment;
import com.jyu.view.custom.RollViewPager;
import com.jyu.view.custom.RollViewPager.OnPagerClickCallback;
import com.jyu.view.util.DialogUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class NewsFragment extends BaseFragment {

	// 放置从服务器获取到的新闻
//	private List<News> item1 = new ArrayList<News>();
	private List<NormalNewsItem> normalNewsList = new ArrayList<NormalNewsItem>();
	private List<TopicNewsItem> topicNewsList = new ArrayList<TopicNewsItem>();
	// 适配器 下面listview
	private NewsAdapter adapter;

	private PullToRefreshListView mPullRefreshListView;

	// 置顶新闻的标题
	@ViewInject(R.id.top_news_title)
	private TextView topNewsTitle;

	// 放置置顶新闻viewpager的一个容器
	@ViewInject(R.id.top_news_viewpager)
	private LinearLayout mViewPagerLay;

	// pager里面的点
	@ViewInject(R.id.dots_ll)
	private LinearLayout dotLl;

	// 表示这个viewpager
	private View topNewsView;

	// 加载的那个空间
	@ViewInject(R.id.loading_view)
	private FrameLayout loading_view;

	// 放置点的集合
	private ArrayList<View> dotList;
	// 存放置顶新闻的一个集合
	private ArrayList<String> titleList, urlList;
	private RollViewPager mViewPager;
	
	private TextView nodata;

	@SuppressLint("InflateParams")
	@Override
	public View initView(LayoutInflater inflater) {
		view = inflater.inflate(R.layout.fragment_news, null);
		topNewsView = inflater.inflate(R.layout.layout_roll_view, null);
		ViewUtils.inject(this, view); // 注入view和事件
		ViewUtils.inject(this, topNewsView);
		nodata = (TextView) view.findViewById(R.id.nodata);
		mPullRefreshListView = (PullToRefreshListView) view
				.findViewById(R.id.pull_refresh_list);
		mPullRefreshListView.setMode(Mode.BOTH);
		mPullRefreshListView
				.setOnRefreshListener(new OnRefreshListener2<ListView>() {

					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						getNewsList(0);
						refreshView
								.getLoadingLayoutProxy()
								.setLastUpdatedLabel(CommonUtil.getStringDate());
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						getNewsList(normalNewsList.size());

					}
				});
		mPullRefreshListView.getRefreshableView().setOnItemClickListener(
				new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						Intent intent = new Intent(ct, NewsDetailActivity.class);
						String url = "";
						NormalNewsItem newsItem;
						if (mPullRefreshListView.getRefreshableView()
								.getHeaderViewsCount() > 0) {
							newsItem = normalNewsList.get(position - 2);
						} else {
							newsItem = normalNewsList.get(position-1);
						}
						url = ConstantValue.LOTTERY_URI + ConstantValue.NEWS
								+ "&newsid=" + newsItem.id;
						intent.putExtra("url", url);
						ct.startActivity(intent);
					}
				});
		mPullRefreshListView.getRefreshableView().setHeaderDividersEnabled(
				false);
		return view;
	}

	@Override
	public void initData(Bundle savedInstanceState) {
//		NewsGetEngineImpl engine = new NewsGetEngineImpl(ct);
		NewsGetEngine engine = BeanFactory.getInstance().getImpl(NewsGetEngine.class);
		R_News cache_r_news = engine.getNewsList(0, true,ct);
		if (cache_r_news != null) {
			processDataFromCache(cache_r_news);
			dismissLoadingView();
		}
		if (0 == CommonUtil.isNetworkAvailable(ct)) {// 无网络
			DialogUtils.showToast(ct, "检测不到网络连接");
		} else {
			getNewsList(0);
		}

	}

	private void getNewsList(final int page) {
		new AsyncTask<String, Void, R_News>() {
			@Override
			protected R_News doInBackground(String... params) {
				NewsGetEngine engine = BeanFactory.getInstance().getImpl(NewsGetEngine.class);
				R_News list = engine.getNewsList(page, false,ct);
				return list;
			}

			@Override
			protected void onPostExecute(R_News result) {
				if (result != null && "newsgetall".equals(result.response)) {
					if (page == 0) {
						normalNewsList.clear();
						normalNewsList.addAll(result.normalNewsList);
						topicNewsList.clear();
						topicNewsList.addAll(result.topicNewsList);
					} else {
						normalNewsList.addAll(result.normalNewsList);
					}
					processData(page);
				} else {
					/**
					 * 这里要判断是连接不上服务器还是没有服务器没有多余数据了
					 */
					DialogUtils.showToast(ct, "连接不上服务器");
					onLoaded();
				}
				dismissLoadingView();
				mPullRefreshListView.onRefreshComplete();
				super.onPostExecute(result);
			}
		}.execute();

	}

	protected void processData(int page) {
		if (page == 0) {
//			topNews = new ArrayList<News>();
//			News newss = null;
//			for (int i = 0; i < item1.size(); i++) {
//				newss = item1.get(i);
//				if (newss.getType() == 1) {
//					topNews.add(newss);
//				}
//			}
//			item1.removeAll(topNews);
			initDot(topicNewsList.size());
			titleList = new ArrayList<String>();
			urlList = new ArrayList<String>();
			for (TopicNewsItem news : topicNewsList) {
				titleList.add(news.title);
				urlList.add(ConstantValue.LOTTERY_URI + news.picurl);
			}
			mViewPager = new RollViewPager(ct, dotList, R.drawable.dot_focus,
					R.drawable.dot_normal, new OnPagerClickCallback() {
						@Override
						public void onPagerClick(int position) {
						}
					});
			mViewPager.setLayoutParams(new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			mViewPager.setUriList(urlList);
			mViewPager.setTitle(topNewsTitle, titleList);
			mViewPager.startRoll();
			mViewPagerLay.removeAllViews();
			mViewPagerLay.addView(mViewPager);
			if (mPullRefreshListView.getRefreshableView().getHeaderViewsCount() < 2) {
				mPullRefreshListView.getRefreshableView().addHeaderView(
						topNewsView, null, true);
				mPullRefreshListView.getRefreshableView()
						.setHeaderDividersEnabled(false);
			}
		}
		if (adapter == null) {
			adapter = new NewsAdapter(ct, normalNewsList);
			mPullRefreshListView.getRefreshableView().setAdapter(adapter);
		} else {
			adapter.notifyDataSetChanged();
		}
		onLoaded();

	}

	private void processDataFromCache(R_News cacheList) {
//		topNews = new ArrayList<News>();
//		News newss = null;
//		for (int i = 0; i < cacheList.size(); i++) {
//			newss = cacheList.get(i);
//			if (newss.getType() == 1) {
//				topNews.add(newss);
//			}
//		}
//		cacheList.removeAll(topNews);
//		item1.addAll(cacheList);
		initDot(cacheList.topicNewsList.size());
		titleList = new ArrayList<String>();
		urlList = new ArrayList<String>();
		for (TopicNewsItem news : cacheList.topicNewsList) {
			titleList.add(news.title);
			urlList.add(ConstantValue.LOTTERY_URI + news.picurl);
		}
		mViewPager = new RollViewPager(ct, dotList, R.drawable.dot_focus,
				R.drawable.dot_normal, new OnPagerClickCallback() {
					@Override
					public void onPagerClick(int position) {

					}
				});
		mViewPager.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		mViewPager.setUriList(urlList);
		mViewPager.setTitle(topNewsTitle, titleList);
		mViewPager.startRoll();
		mViewPagerLay.removeAllViews();
		mViewPagerLay.addView(mViewPager);

		if (mPullRefreshListView.getRefreshableView().getHeaderViewsCount() < 2) {
			mPullRefreshListView.getRefreshableView().addHeaderView(
					topNewsView, null, true);
			mPullRefreshListView.getRefreshableView().setHeaderDividersEnabled(
					false);
		}
		if (adapter == null) {
			normalNewsList = cacheList.normalNewsList;
			adapter = new NewsAdapter(ct, normalNewsList);
			mPullRefreshListView.getRefreshableView().setAdapter(adapter);
		} else {
			adapter.notifyDataSetChanged();
		}
		onLoaded();
	}

	private void onLoaded() {
		if (normalNewsList.size() == 0) {
			nodata.setText("当前并无数据");
			nodata.setVisibility(View.VISIBLE);
		} else {
			nodata.setVisibility(View.INVISIBLE);
		}
	}

	@ViewInject(R.id.loading_view)
	protected View loadingView;

	public void dismissLoadingView() {
		if (loadingView != null)
			loadingView.setVisibility(View.INVISIBLE);
	}

	private void initDot(int size) {
		dotList = new ArrayList<View>();
		dotLl.removeAllViews();
		for (int i = 0; i < size; i++) {
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					CommonUtil.dip2px(ct, 6), CommonUtil.dip2px(ct, 6));
			params.setMargins(5, 0, 5, 0);
			View m = new View(ct);
			if (i == 0) {
				m.setBackgroundResource(R.drawable.dot_focus);
			} else {
				m.setBackgroundResource(R.drawable.dot_normal);
			}
			m.setLayoutParams(params);
			dotLl.addView(m);
			dotList.add(m);
		}
	}

	@Override
	public void updateItem(Object obj) {
		// TODO Auto-generated method stub

	}
}
