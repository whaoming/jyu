package com.jyu.bean;

import java.io.Serializable;
import java.util.List;

import com.jyu.bean.R_User.UserCommonInfo;

public class R_Social extends R_Base{

	public List<Integer> hasJoinIdList;
	public List<SocialItem> socialList;
	public static class SocialItem implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 2508177319395116369L;
		/**
		 * 
		 */
		public String contact;
		public String description;
		public int id;
		public int ischecked;
		public int issignup;
		public String location;
		public String name;
		public int need;
		public String pic;
		public int signup;
		public String time;
		public String title;
		public int type;
		public int userid;
		public UserCommonInfo userInfo = new UserCommonInfo();;
	}
}
