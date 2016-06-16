package com.jyu.view.fragment;




import com.jyu.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

@SuppressLint("ValidFragment")
public class StartFragmenttwo extends Fragment {

	private Context ctx;

	public StartFragmenttwo(Context ctx) {
		super();
		this.ctx = ctx;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = null;
		view = View.inflate(ctx, R.layout.fragment_start01, null);
		LinearLayout mLinear = (LinearLayout) view
				.findViewById(R.id.Fragment01Linear);
		mLinear.setBackgroundResource(R.drawable.guidance_new2);
		return view;
	}

}
