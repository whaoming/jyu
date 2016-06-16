package com.jyu.view.adapter;



import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.jyu.view.fragment.StartFragmentone;
import com.jyu.view.fragment.StartFragmentthree;
import com.jyu.view.fragment.StartFragmenttwo;

public class StartViewPagerAdapter extends FragmentPagerAdapter{

	private Context ctx;

	//FragmentManager fragment������ ,������
	public StartViewPagerAdapter(FragmentManager fm,Context ctx) {
		super(fm);
		this.ctx = ctx;
	}
	public Fragment getItem(int arg0) {
		Fragment mFragment = null;
		if(arg0 == 0){
			mFragment = new StartFragmentone(ctx);
		}else if(arg0 == 1){
			mFragment = new StartFragmenttwo(ctx);
		}else if(arg0 == 2){
			mFragment = new StartFragmentthree(ctx);
		}
		return mFragment;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 3;
	}

}
