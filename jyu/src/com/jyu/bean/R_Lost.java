package com.jyu.bean;

import java.io.Serializable;
import java.util.List;

import com.jyu.bean.R_User.UserCommonInfo;

public class R_Lost extends R_Base{

	public List<LostItem> lostItemList;
	
	public static class  LostItem implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public String contact;
		public String description;
		public int found;
		public int id;
		public String ilabel;
		public String llabel;
		public String location;
		public int lost;
		public String ltime;
		public String pic;
		public String ptime;
		public int reply;
		public String title;
		public String type;
		public int userid;
		public UserCommonInfo userInfo = new UserCommonInfo();
	}
}
