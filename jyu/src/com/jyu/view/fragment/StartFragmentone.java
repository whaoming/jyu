package com.jyu.view.fragment;




import com.jyu.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

@SuppressLint("ValidFragment")
public class StartFragmentone extends Fragment {

	private Context ctx;

	@SuppressLint("ValidFragment")
	public StartFragmentone(Context ctx) {
		super();
		this.ctx = ctx;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = null;
		view = View.inflate(ctx, R.layout.fragment_start01, null);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	@Override
	public void onDestroyView() {
		// fragment���viewʱ����
		super.onDestroyView();
	}

	@Override
	public void onDestroy() {
		// activity���
		super.onDestroy();
	}

	@Override
	public void onDetach() {
		// fragment ��ɾ��ʱ����
		super.onDetach();
	}
}
