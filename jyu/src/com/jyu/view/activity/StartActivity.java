package com.jyu.view.activity;

import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;

import com.jyu.R;
import com.jyu.view.adapter.StartViewPagerAdapter;
import com.jyu.view.base.BaseActivity;

public class StartActivity extends BaseActivity {

	private ViewPager mViewPager;
	
	@Override
	protected void initView() {
		setContentView(R.layout.activity_start);
		mViewPager = (ViewPager) findViewById(R.id.MyViewPager);
		StartViewPagerAdapter myAdapter = new StartViewPagerAdapter(
				this.getSupportFragmentManager(), StartActivity.this);
		mViewPager.setAdapter(myAdapter);

	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void processClick(View v) {
		// TODO Auto-generated method stub

	}
	
	@Override
	protected void onStop() {
		finish();
		super.onStop();
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {

		}
		return true;
	}

}
