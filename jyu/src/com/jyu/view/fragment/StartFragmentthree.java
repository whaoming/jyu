package com.jyu.view.fragment;




import com.jyu.R;
import com.jyu.view.activity.FrameActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

@SuppressLint("ValidFragment")
public class StartFragmentthree extends Fragment {

	private Context ctx;

	public StartFragmentthree(Context ctx) {
		super();
		this.ctx = ctx;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = null;
		view = View.inflate(ctx, R.layout.fragment_start03, null);
		ImageView mBtn = (ImageView) view.findViewById(R.id.MyLoginBtn);
		mBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
//				final Dialog dialog = DialogUtils.createProgressDialog(ctx, "");
//				dialog.show();
//				new AsyncTask<Boolean, Void,  List<TopLine>>() {
//					@Override
//					protected List<TopLine> doInBackground(Boolean... params) {
//						TopLineEngineImpl impl = new TopLineEngineImpl(ctx);
//						List<TopLine> result = impl.getTopLineList(false);
//						return result;
//					}
//					@Override
//					protected void onPostExecute(List<TopLine> result) {
//						dialog.dismiss();
//						GlobalParams.topLineList = result;
						Intent intent = new Intent(ctx, FrameActivity.class);
//						Bundle bund = new Bundle();
//						bund.putSerializable("TopList", (Serializable) result);
//						intent.putExtra("value", bund);
						ctx.startActivity(intent);
//						super.onPostExecute(result);
//					}
//				}.execute();
				
			}
		});
		return view;
	}

}
