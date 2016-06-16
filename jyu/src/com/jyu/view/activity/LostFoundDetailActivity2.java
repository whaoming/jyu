package com.jyu.view.activity;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jyu.ConstantValue;
import com.jyu.GlobalParams;
import com.jyu.R;
import com.jyu.bean.R_Lost.LostItem;
import com.jyu.bean.R_Lost_Comment;
import com.jyu.bean.R_Lost_Comment.Lost_CommentItem;
import com.jyu.commonUtils.BeanFactory;
import com.jyu.commonUtils.CommonUtil;
import com.jyu.engine.LostItemEngine;
import com.jyu.view.adapter.LostCommentAdapter;
import com.jyu.view.adapter.MyPhotoGridAdapter;
import com.jyu.view.base.BaseActivity;
import com.jyu.view.custom.mydialog.AlertDialog;
import com.jyu.view.custom.mydialog.AlertDialog.OnSheetItemClickListeners;
import com.jyu.view.custom.mydialog.AlertDialog.SheetItemColors;
import com.jyu.view.custom.photoutil.Bimp;
import com.jyu.view.custom.photoutil.MyGridView;
import com.jyu.view.util.DialogUtils;
import com.lidroid.xutils.BitmapUtils;

public class LostFoundDetailActivity2 extends BaseActivity {

	private PullToRefreshListView mPullRefreshListView;
	private List<Lost_CommentItem> commentList = new ArrayList<Lost_CommentItem>();
	BitmapUtils bitmapUtil;
	// 带过来的关于这条东东的一个信息
	private LostItem info;
	
	LostCommentAdapter adapter;
	RelativeLayout rl_big;

	int reply_comment_id = 0;
	int reply_receiver_id = 0;

	MyGridView noScrollgridview;

	//头部view
	private View header;

	//发送按钮
	public Button btn_send;
	
	//评论框
	public EditText et_comment;

	// 发送评论按钮，用户头像,大图
	private ImageView upic;
	
	//用户名
	private TextView tv_uname;
	
	//用户描述
	private TextView tv_udescription;
	
	//标题
	private TextView tv_title;
	private TextView tv_location;
	private TextView tv_ltime;
	private TextView tv_contact;
	private TextView tv_loadding;
	private TextView tv_replyCount;

	@SuppressLint({ "InflateParams", "ClickableViewAccessibility" })
	@Override
	protected void initView() {
		setContentView(R.layout.activity_lost_found_detail2);
		btn_send = (Button) findViewById(R.id.btn_send);
		rl_big = (RelativeLayout) findViewById(R.id.rl_big);
		rl_big.setOnClickListener(this);
		btn_send.setOnClickListener(this);
		et_comment = (EditText) findViewById(R.id.et_comment);
		LayoutInflater inflater = getLayoutInflater();
		header = inflater.inflate(R.layout.activity_lost_found_detail_header,
				null);
		bitmapUtil = new BitmapUtils(ct);
		initTitleBar();
		titleTv.setText("详情");
		mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
		tv_replyCount = (TextView) header.findViewById(R.id.tv_replyCount);
		tv_loadding = (TextView) header.findViewById(R.id.tv_loadding);
		upic = (ImageView) header.findViewById(R.id.upic);
		tv_uname = (TextView) header.findViewById(R.id.tv_uname);
		tv_udescription = (TextView) header.findViewById(R.id.tv_udescription);
		tv_title = (TextView) header.findViewById(R.id.tv_title);
		tv_location = (TextView) header.findViewById(R.id.tv_location);
		tv_ltime = (TextView) header.findViewById(R.id.tv_ltime);
		tv_contact = (TextView) header.findViewById(R.id.tv_contact);
		noScrollgridview = (MyGridView) header
				.findViewById(R.id.noScrollgridview);
		Intent intent = getIntent();
		Bundle bund = intent.getBundleExtra("value");
		info = (LostItem) bund.getSerializable("LostItem");
		mPullRefreshListView.setMode(Mode.BOTH);
		mPullRefreshListView.getRefreshableView().setOnTouchListener(
				new OnTouchListener() {
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						switch (event.getAction()) {
						case MotionEvent.ACTION_DOWN:
							break;
						case MotionEvent.ACTION_MOVE:
							et_comment.setText("");
							et_comment.setHint("发表你的评论吧");
							reply_comment_id = 0;
							reply_receiver_id = 0;
							break;
						case MotionEvent.ACTION_UP:
							if (reply_comment_id != 0) {
								et_comment.setText("");
								et_comment.setHint("发表你的评论吧");
								reply_comment_id = 0;
								reply_receiver_id = 0;
							}
							break;
						default:
							break;
						}
						return false;
					}
				});
		mPullRefreshListView.getRefreshableView().addHeaderView(header, null,
				true);
		mPullRefreshListView
				.setOnRefreshListener(new OnRefreshListener2<ListView>() {

					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						tv_loadding.setVisibility(View.VISIBLE);
						getCommentList(0);
						refreshView
								.getLoadingLayoutProxy()
								.setLastUpdatedLabel(CommonUtil.getStringDate());
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						getCommentList(commentList.size());
					}
				});
		mPullRefreshListView.getRefreshableView().setOnItemClickListener(
				new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							final int position, long id) {
						/**
						 * 先判断当前用户是否已经登陆
						 * 1.如果已经登陆，则要判断每个item的userid跟当前用户id是否一样
						 */
						if(GlobalParams.userInfo != null){//用户是否已经登陆
							if(commentList.get(position - 2).userInfo.id == GlobalParams.userInfo.id){//用户已经登陆并且item为当前用户发布
								new AlertDialog(ct).builder().setTitle("请选择操作")
								.addSheetItem("删除", SheetItemColors.Blue, new OnSheetItemClickListeners() {
									@Override
									public void onClick(int which) {
										//删除评论
										deleteComment(commentList.get(position - 2));
									}

									
								})
								.addSheetItem("举报", SheetItemColors.Blue, new OnSheetItemClickListeners() {
									@Override
									public void onClick(int which) {
										// TODO Auto-generated method stub
										
									}
								}).show();
							}else{//用户已经登陆并且当前item非当前用户发布
								new AlertDialog(ct).builder().setTitle("请选择操作")
								.addSheetItem("回复", SheetItemColors.Blue, new OnSheetItemClickListeners() {
									@Override
									public void onClick(int which) {
										reply_comment_id = commentList.get(position - 2).id;
										reply_receiver_id = commentList.get(position - 2).userInfo.id;
										et_comment.setHint("回复"
												+ commentList.get(position - 2).userInfo.uname
												+ "(" + commentList.get(position - 2).floor
												+ "楼):");
										et_comment.requestFocus();
									}
								})
								.addSheetItem("举报", SheetItemColors.Blue, new OnSheetItemClickListeners() {
									@Override
									public void onClick(int which) {
										// TODO Auto-generated method stub
										
									}
								}).show();
								
							}
						}else{//用户为登陆
							new AlertDialog(ct).builder().setTitle("请选择操作")
							.addSheetItem("回复", SheetItemColors.Blue, new OnSheetItemClickListeners() {
								@Override
								public void onClick(int which) {
									DialogUtils.showToast(ct, "还未登陆");
								}
							})
							.addSheetItem("举报", SheetItemColors.Blue, new OnSheetItemClickListeners() {
								@Override
								public void onClick(int which) {
									// TODO Auto-generated method stub
									
								}
							}).show();
//							reply_comment_id = commentList.get(position - 2).id;
//							reply_receiver_id = commentList.get(position - 2).userInfo.id;
//							et_comment.setHint("回复"
//									+ commentList.get(position - 2).userInfo.uname
//									+ "(" + commentList.get(position - 2).floor
//									+ "楼):");
//							et_comment.requestFocus();
						}
					}
				});
	}
	
	/**
	 * 向服务器发送删除评论请求
	 * @param lost_CommentItem
	 */
	private void deleteComment(
			final Lost_CommentItem lost_CommentItem) {
		new AsyncTask<String, Void, R_Lost_Comment>() {
			@Override
			protected R_Lost_Comment doInBackground(String... params) {
				LostItemEngine engine = BeanFactory.getInstance().getImpl(
						LostItemEngine.class);
				R_Lost_Comment r_lost = null;
				r_lost = engine.deleteLostItemComment(lost_CommentItem.domainid, lost_CommentItem.id);
				return r_lost;
			}

			@Override
			protected void onPostExecute(R_Lost_Comment result) {
				if (result != null && result.success == 1) {
					//处理成功
					commentList.remove(lost_CommentItem);
					adapter.notifyDataSetChanged();
				} else {
					if(result == null){
						Log.i("wang", "result=nuyll");
					}
					DialogUtils.showToast(ct, "连接不上服务器");
				}
				super.onPostExecute(result);
			}
		}.execute();
		
	}

	@Override
	protected void initData() {
		tv_replyCount.setText("评论"+info.reply+"条");
		tv_uname.setText(info.userInfo.uname);
		tv_udescription.setText(info.userInfo.udescription);
		tv_title.setText(info.title);
		tv_contact.setText(info.contact);
		if (info.lost == 1) {
			tv_location.setText(info.location);
			tv_ltime.setText(info.ltime);
		} else {
			tv_location.setText(info.location);
			tv_ltime.setText(info.ltime);
		}
		bitmapUtil
				.display(upic, ConstantValue.LOTTERY_URI + info.userInfo.upic);
		MyPhotoGridAdapter adapter1 = new MyPhotoGridAdapter(ct,
				info.pic.split(","), false);
		noScrollgridview.setAdapter(adapter1);
		noScrollgridview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(ct, PhotoActivity.class);
				intent.putExtra("ID", position);
				intent.putExtra("isNet", true);
				for (String pic : info.pic.split(",")) {
					Bimp.drr.add(pic);
					Bimp.max = info.pic.split(",").length;
				}
				startActivity(intent);
			}
		});
		if (adapter == null) {
			adapter = new LostCommentAdapter(ct, commentList);
			mPullRefreshListView.getRefreshableView().setAdapter(adapter);
		} else {
			adapter.notifyDataSetChanged();
		}
		getCommentList(0);
	}

	private void getCommentList(final int page) {
		new AsyncTask<String, Void, R_Lost_Comment>() {
			@SuppressWarnings("static-access")
			@Override
			protected R_Lost_Comment doInBackground(String... params) {
				try {
					new Thread().sleep(1500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				LostItemEngine engine = BeanFactory.getInstance().getImpl(
						LostItemEngine.class);
				R_Lost_Comment r_lost = null;
				r_lost = engine.getLostItemCommentList(page, info.id);
				return r_lost;
			}

			@Override
			protected void onPostExecute(R_Lost_Comment result) {
				if (result != null && result.success == 1) {
					if (page == 0) {
						info.reply = result.replyCount;
						tv_replyCount.setText("评论"+info.reply+"条");
						commentList.clear();
						commentList.addAll(result.lostItemCommentList);
					} else {
						commentList.addAll(result.lostItemCommentList);
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
			adapter = new LostCommentAdapter(ct, commentList);
			mPullRefreshListView.getRefreshableView().setAdapter(adapter);
		} else {
			adapter.notifyDataSetChanged();
		}
		mPullRefreshListView.onRefreshComplete();
		onLoaded();
	}

	private void onLoaded() {
		tv_loadding.setVisibility(View.GONE);
	}

	@Override
	protected void processClick(View v) {
		switch (v.getId()) {
		case R.id.btn_send:
			if(GlobalParams.userInfo != null){
				String text = et_comment.getText().toString().trim();
				insertCommentToNet(text);
			}else{
				DialogUtils.showToast(ct, "请先登录");
			}
			
			break;
		default:
			break;
		}
	}

	/**
	 * 连接网络
	 * 
	 * @param text
	 */
	private void insertCommentToNet(final String text) {
		new AsyncTask<String, Void, R_Lost_Comment>() {
			@Override
			protected R_Lost_Comment doInBackground(String... params) {
				LostItemEngine engine = BeanFactory.getInstance().getImpl(
						LostItemEngine.class);
				Lost_CommentItem item = new Lost_CommentItem();
				if (reply_comment_id != 0) {
					item.reply = 1;
					item.replyComment.id = reply_comment_id;
					item.replyComment.userInfo.id = reply_receiver_id;
				} else {
					item.reply = 0;
				}
				item.userInfo.id = GlobalParams.userInfo.id;
				item.content = text;
				item.domainid = info.id;
				item.floor = info.reply + 1;
				R_Lost_Comment result = engine.insertLostItemComment(item);
				return result;
			}

			@Override
			protected void onPostExecute(R_Lost_Comment result) {
				if (result != null && result.success == 1) {
					DialogUtils.showToast(ct, "发表评论成功");
					// 更新到当前adapter当中
					info.reply = result.replyCount;
					tv_replyCount.setText("评论"+info.reply+"条");
					commentList.add(0, result.lostItemCommentList.get(0));
					adapter.notifyDataSetChanged();
				} else {
					DialogUtils.showToast(ct, "连接不上服务器");
				}
				processData();
				mPullRefreshListView.onRefreshComplete();
				super.onPostExecute(result);
			}
		}.execute();
	}

}
