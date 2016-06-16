package com.jyu.view.fragment;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jyu.ConstantValue;
import com.jyu.R;
import com.jyu.commonUtils.BeanFactory;
import com.jyu.commonUtils.CommonUtil;
import com.jyu.domain.TopLine;
import com.jyu.engine.TopLineEngine;
import com.jyu.view.activity.NewsDetailActivity;
import com.jyu.view.base.BaseFragment;
import com.jyu.view.custom.RollViewPager;
import com.jyu.view.custom.RollViewPager.OnPagerClickCallback;

@SuppressLint("InflateParams")
public class IndexFragment extends BaseFragment {

	// private List<TopLine> item;

	private View view;
	// 置顶新闻的标题
	// @ViewInject(R.id.top_news_title)
	private TextView topNewsTitle;
	// pager里面的点
	private LinearLayout dotLl;
	// 放置置顶新闻viewpager的一个容器
	private LinearLayout mViewPagerLay;
	private View topViewPage;

	private ArrayList<String> titleList, urlList;

	private List<TopLine> list = new ArrayList<TopLine>();

	private ImageView iv_guanwang;
	private ImageView iv_zhengfang;
	/**
	 * 放置公告
	 */
	private List<TopLine> normalList = new ArrayList<TopLine>();

	// 放置点的集合
	private ArrayList<View> dotList;

	private RollViewPager mViewPager;

	private TextView tv_1;
	private TextView tv_2;
	private TextView tv_3;

	@Override
	public View initView(LayoutInflater inflater) {
		view = inflater.inflate(R.layout.fragment_index, null);
		iv_guanwang = (ImageView) view.findViewById(R.id.iv_guanwang);
		iv_zhengfang = (ImageView) view.findViewById(R.id.iv_zhengfang);
		tv_1 = (TextView) view.findViewById(R.id.tv_1);
		tv_2 = (TextView) view.findViewById(R.id.tv_2);
		tv_3 = (TextView) view.findViewById(R.id.tv_3);
		topViewPage = view.findViewById(R.id.myviewpage);

		mViewPagerLay = (LinearLayout) topViewPage
				.findViewById(R.id.top_news_viewpager);

		dotLl = (LinearLayout) topViewPage.findViewById(R.id.dots_ll);

		topNewsTitle = (TextView) topViewPage.findViewById(R.id.top_news_title);
		iv_guanwang.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Uri uri = Uri.parse("http://www.jyu.edu.cn/");
				Intent it = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(it);
			}
		});
		iv_zhengfang.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Uri uri = Uri.parse("http://210.38.162.123/");
				Intent it = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(it);

			}
		});
		return view;
	}

	@Override
	public void initData(Bundle savedInstanceState) {
		// getTopLineList(0);
		/**
		 * 先初始化参数，然后从G里面取得数据 这中间有一个问题，如果不是第一次打开，那肯定要从缓存取数据，然后从服务器取数据的
		 * 步骤要在哪里执行呢？因为取完要更新ui，如果是在启动界面，要是遇到服务器卡的情况怎么办
		 * 如果是在indexfragment取的话，那更新ui要怎么办，而且这里取的话设涉及到第一次启动ui会难搞
		 * 
		 * 第一个思路：在启动页面取，然后存到G中，一般这里不会出现网络卡顿的情况，因为还有三个fragment等着你滑动
		 * 然后以后也都在这里取，但是如果以后启动过程的时间不足以从服务器接收数据的时间呢？
		 * 
		 * 第二个思路：第一次启动时，从fragment取，然后存到G中(当然也要存到缓存中)/或者直接存到缓存中，
		 * 此时如果出现服务器卡顿，就显示processdialog，等接收完了再跳转页面，如果长时间
		 * 接收不到也要跳转，然后index页面就显示原始的东西 index页面设置lodding，然后还是按原来的道理，从缓存中取然后再从服务器取
		 * 此时又想到一个问题，如果此时fragment的线程还在等待服务器的回复，然后你这里又开启
		 * 一个线程去做同样的操作，这样不好不好。或者g中可以记录一个是否是第一次启动的字段，
		 * 
		 * 
		 * 第三个思路：直接抛弃第二个思路，在启动界面接收数据然后存到缓存中就行了,然后这个又有问题，因为缓存取的只能是
		 * String，所以要考虑数据处理从哪里搞
		 * 
		 * 适配:从取得的list<TopLine>里面拿到类型为1的，然后添加()
		 */

		TopLineEngine engine = BeanFactory.getInstance().getImpl(
				TopLineEngine.class);
		if (engine.getTopLineListFromCache(ct) != null) {
			if (engine.getTopLineListFromCache(ct).size() != 0) {
				list.addAll(engine.getTopLineListFromCache(ct));
			}
		}
		if (list.size() > 0) {
			processData();
		}
		getTopLineList(0);

	}

	private void getTopLineList(int page) {
		new AsyncTask<Boolean, Void, List<TopLine>>() {
			@Override
			protected List<TopLine> doInBackground(Boolean... params) {
				TopLineEngine engine = BeanFactory.getInstance().getImpl(
						TopLineEngine.class);
				List<TopLine> result = engine.getTopLineList(false, ct);
//				Log.i("wang", "到这里了1");
				return result;
			}

			@Override
			protected void onPostExecute(List<TopLine> result) {
//				Log.i("wang", "到这里了2");
//				Log.i("wang", "size="+result.size());
				if (result != null) {
					list = result;
				}
				processData();
				super.onPostExecute(result);
			}
		}.execute();

	}

	protected void processData() {
		if (list.size() > 0) {
			titleList = new ArrayList<String>();
			urlList = new ArrayList<String>();
			init();
			mViewPager = new RollViewPager(ct, dotList, R.drawable.dot_focus,
					R.drawable.dot_normal, new OnPagerClickCallback() {
						@Override
						public void onPagerClick(int position) {
							System.out.println("新闻被点击了");
						}
					});
			mViewPager.setLayoutParams(new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			mViewPager.setUriList(urlList);
			mViewPager.setTitle(topNewsTitle, titleList);
			mViewPager.startRoll();
			mViewPagerLay.removeAllViews();
			mViewPagerLay.addView(mViewPager);
		}

	}

	/**
	 * 让缓存中的topline在这个方法中分类
	 */
	private void init() {
		int num = 0;
		for (TopLine item : list) {
			if (item.getType() == 1) {
				titleList.add(item.getTitle());
				urlList.add(ConstantValue.LOTTERY_URI + item.getPic());
				num++;
			} else {
				normalList.add(item);
			}
		}
		initDot(num);
		tv_1.setText(normalList.get(0).getTitle());
		tv_1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ct, NewsDetailActivity.class);
				String url = ConstantValue.LOTTERY_URI
						+ ConstantValue.GETTOPLINE + "&id="
						+ normalList.get(0).getId();
				// String url = "http://m.5read.com/jyu";
				intent.putExtra("url", url);
				ct.startActivity(intent);
			}
		});
		tv_2.setText(normalList.get(1).getTitle());
		tv_2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ct, NewsDetailActivity.class);
				String url = ConstantValue.LOTTERY_URI
						+ ConstantValue.GETTOPLINE + "&id="
						+ normalList.get(1).getId();
				intent.putExtra("url", url);
				ct.startActivity(intent);

			}
		});
		tv_3.setText(normalList.get(2).getTitle());
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

	public void onReStart() {
		if (list.size() == 0) {
			getTopLineList(0);
		}
		super.onResume();
	}
	

}
