package com.jyu.bean;

import java.util.List;

import com.jyu.bean.R_User.UserCommonInfo;

public class R_Lost_Comment extends R_Base{

	public List<Lost_CommentItem> lostItemCommentList;
	public int replyCount;
	
	public static class Lost_CommentItem{
		public int id;
		public int domainid;
		public int delete;
		public String date;
		public String content;
		public int reply;
		public int floor;
		public UserCommonInfo userInfo = new UserCommonInfo();
		public Lost_Reply_CommentItem replyComment = new Lost_Reply_CommentItem();
		
	}
	public static class Lost_Reply_CommentItem{
		public int id;
		public int floor;
		public int delete;
		public String content;
		public UserCommonInfo userInfo = new UserCommonInfo();
	}
	
}
