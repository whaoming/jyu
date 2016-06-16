package com.jyu.view.adapter;


import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jyu.ConstantValue;
import com.jyu.R;
import com.jyu.bean.R_Lost_Comment.Lost_CommentItem;
import com.lidroid.xutils.BitmapUtils;

public class LostCommentAdapter extends BaseAdapter {

	private Context ctx;
	private List<Lost_CommentItem> list;
	BitmapUtils bitmapUtil;

	public LostCommentAdapter(Context ctx,List<Lost_CommentItem> list) {
		this.ctx = ctx;
		this.list = list;
		bitmapUtil = new BitmapUtils(ctx);
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		Holder hold;
		 Lost_CommentItem comment = list.get(position);
		if (convertView == null) {
			hold = new Holder();
			convertView = View.inflate(ctx, R.layout.item_lost_comment, null);
			hold.upic = (ImageView) convertView.findViewById(R.id.upic);
			hold.uname = (TextView) convertView.findViewById(R.id.uname);
			hold.udescription = (TextView) convertView
					.findViewById(R.id.udescription);
			hold.tv_reply = (TextView) convertView.findViewById(R.id.tv_reply);
			hold.content = (TextView) convertView.findViewById(R.id.tv_content);
			hold.tv_chat = (TextView) convertView.findViewById(R.id.tv_chat);
//			hold.rl_big = (RelativeLayout) convertView.findViewById(R.id.rl_big);
			convertView.setTag(hold);
		} else {
			hold = (Holder) convertView.getTag();
		}
		
//		if(comment.delete == 0){//没被删除
//			hold.rl_big.setVisibility(View.VISIBLE);
			bitmapUtil.display(hold.upic, ConstantValue.LOTTERY_URI+comment.userInfo.upic);
			hold.uname.setText(comment.userInfo.uname+"("+comment.floor+"楼)");
			hold.udescription.setText(comment.userInfo.udescription);
			hold.content.setText(comment.content);
			if(comment.reply == 1){//是回复型评论
				hold.tv_chat.setVisibility(View.VISIBLE);
				hold.tv_reply.setVisibility(View.VISIBLE);
				if(comment.replyComment.delete == 0){//所恢复的评论还未被删除
					hold.tv_reply.setText("回复@"+comment.replyComment.userInfo.uname+"的评论("+comment.replyComment.floor+"楼):"+comment.replyComment.content);
					hold.tv_chat.setVisibility(View.VISIBLE);
				}else{
					hold.tv_reply.setText("该评论已经删除");
				}
			}else{
				hold.tv_chat.setVisibility(View.GONE);
				hold.tv_reply.setVisibility(View.GONE);
			}
			
//		}else{//评论已经被删除
//			Log.i("wang", "convertView.setVisibility(View.GONE);");
//			hold.rl_big.setVisibility(View.GONE);
//			return null;
//		}
//		if(comment.reply == 1 && comment.delete == 0){
//			hold.tv_reply.setVisibility(View.VISIBLE);
//			if(comment.replyComment.delete == 0){
//				hold.tv_reply.setText("回复@"+comment.replyComment.userInfo.uname+"的评论("+comment.replyComment.floor+"楼):"+comment.replyComment.content);
//				hold.tv_chat.setVisibility(View.VISIBLE);
//			}else{
//				hold.tv_reply.setText("该评论已经删除");
//			}
//		}else{
//			hold.tv_reply.setVisibility(View.GONE);
//			hold.tv_chat.setVisibility(View.GONE);
//		}
		
		
		return convertView;
	}

	static class Holder {
		TextView content;
		ImageView upic;
		TextView uname;
		TextView udescription;
		TextView tv_reply;
		TextView tv_chat;
//		RelativeLayout rl_big;
	}

}
