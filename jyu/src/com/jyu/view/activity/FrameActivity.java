package com.jyu.view.activity;


import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.jyu.R;
import com.jyu.view.fragment.FindOutFragment;
import com.jyu.view.fragment.IndexFragment;
import com.jyu.view.fragment.NewsFragment;
import com.jyu.view.fragment.SettingFragment;
import com.jyu.view.util.DialogUtils;

public class FrameActivity extends FragmentActivity implements OnClickListener {

	private FindOutFragment findOutFragment;
	private IndexFragment indexFragment;
	private NewsFragment newsFragment;
	private SettingFragment settingFragment;

	private Button button_state1;
	private Button button_state2;
	private Button button_state3;
	private Button button_state4;
	
	private TextView txt_title;

	/**
	 * 用于对Fragment进行管理
	 */
	private FragmentManager fragmentManager;
	private int mState = 5;
	
	/** 再按一次退出程序 */
	private long exitTime = 0;

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
		button_state1.setSelected(true);
		setTabSelection(0);
		
//		Intent intent = getIntent();
//		Bundle bund = intent.getBundleExtra("value");
//		list = (ArrayList<TopLine>) bund.getSerializable("TopList");
	}

	private void initView() {
		txt_title = (TextView) findViewById(R.id.txt_title);
		button_state1 = (Button) findViewById(R.id.button_state1);
		button_state2 = (Button) findViewById(R.id.button_state2);
		button_state3 = (Button) findViewById(R.id.button_state3);
		button_state4 = (Button) findViewById(R.id.button_state4);
		button_state1.setOnClickListener(this);
		button_state2.setOnClickListener(this);
		button_state3.setOnClickListener(this);
		button_state4.setOnClickListener(this);
		fragmentManager = getSupportFragmentManager();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_state1:
			changeState(0);
			break;
		case R.id.button_state2:
			changeState(1);
			break;
		case R.id.button_state3:
			changeState(2);
			break;
		case R.id.button_state4:
			changeState(3);
			break;
		}
	}

	private void changeState(int id) {
		if (mState  == id) {
			if(id == 0){
				indexFragment.onReStart();
			}else{
				return;
			}
			
		} else{
			mState = id;
			button_state1.setSelected(false);
			button_state2.setSelected(false);
			button_state3.setSelected(false);
			button_state4.setSelected(false);
		}
		switch (id) {
		case 0:
			button_state1.setSelected(true);
			setTabSelection(0);
			break;
		case 1:
			button_state2.setSelected(true);
			setTabSelection(1);
			break;
		case 2:
			button_state3.setSelected(true);
			setTabSelection(2);
			break;
		case 3:
			button_state4.setSelected(true);
			setTabSelection(3);
			break;
		}

	}

	private void setTabSelection(int index) {
		// 每次选中之前先清楚掉上次的选中状态
		clearSelection();
		// 开启一个Fragment事务
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		// 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
		hideFragments(transaction);

		switch (index) {
		case 0:
			txt_title.setText("我的嘉大");
			// 当点击了消息tab时，改变控件的图片和文字颜色
			// messageImage.setImageResource(R.drawable.message_selected);
			// messageText.setTextColor(Color.WHITE);
			if (indexFragment == null) {
				// 如果MessageFragment为空，则创建一个并添加到界面上
				indexFragment = new IndexFragment();
				
				transaction.add(R.id.content, indexFragment);
			} else {
				// 如果MessageFragment不为空，则直接将它显示出来
				transaction.show(indexFragment);
			}
			break;
		case 1:
			txt_title.setText("新闻中心");
			// 当点击了联系人tab时，改变控件的图片和文字颜色
			// contactsImage.setImageResource(R.drawable.contacts_selected);
			// contactsText.setTextColor(Color.WHITE);
			if (newsFragment == null) {
				// 如果ContactsFragment为空，则创建一个并添加到界面上
				newsFragment = new NewsFragment();
				
				transaction.add(R.id.content, newsFragment);
			} else {
				// 如果ContactsFragment不为空，则直接将它显示出来
				transaction.show(newsFragment);
			}
			break;
		case 2:
			txt_title.setText("发现");
			// 当点击了动态tab时，改变控件的图片和文字颜色
			// find.setImageResource(R.drawable.news_selected);
			// newsText.setTextColor(Color.WHITE);
			if (findOutFragment == null) {
				// 如果NewsFragment为空，则创建一个并添加到界面上
				findOutFragment = new FindOutFragment();
				transaction.add(R.id.content, findOutFragment);
			} else {
				// 如果NewsFragment不为空，则直接将它显示出来
				transaction.show(findOutFragment);
			}
			break;
		case 3:
			txt_title.setText("个人");
			// 当点击了设置tab时，改变控件的图片和文字颜色
			// settingImage.setImageResource(R.drawable.setting_selected);
			// settingText.setTextColor(Color.WHITE);
			if (settingFragment == null) {
				// 如果SettingFragment为空，则创建一个并添加到界面上
				settingFragment = new SettingFragment();
				transaction.add(R.id.content, settingFragment);
			} else {
				// 如果SettingFragment不为空，则直接将它显示出来
				transaction.show(settingFragment);
			}
			break;
		}
		transaction.commit();
	}

	/**
	 * 清除掉所有的选中状态。
	 */
	private void clearSelection() {
		// messageImage.setImageResource(R.drawable.message_unselected);
		// messageText.setTextColor(Color.parseColor("#82858b"));
		// contactsImage.setImageResource(R.drawable.contacts_unselected);
		// contactsText.setTextColor(Color.parseColor("#82858b"));
		// newsImage.setImageResource(R.drawable.news_unselected);
		// newsText.setTextColor(Color.parseColor("#82858b"));
		// settingImage.setImageResource(R.drawable.setting_unselected);
		// settingText.setTextColor(Color.parseColor("#82858b"));
	}

	/**
	 * 将所有的Fragment都置为隐藏状态。
	 * 
	 * @param transaction
	 *            用于对Fragment执行操作的事务
	 */
	private void hideFragments(FragmentTransaction transaction) {
		if (indexFragment != null) {
			transaction.hide(indexFragment);
		}
		if (findOutFragment != null) {
			transaction.hide(findOutFragment);
		}
		if (newsFragment != null) {
			transaction.hide(newsFragment);
		}
		if (settingFragment != null) {
			transaction.hide(settingFragment);
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				DialogUtils.showToast(this, "再按一次退出程序");
				exitTime = System.currentTimeMillis();
			} else {
				finish();
				System.exit(0);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
