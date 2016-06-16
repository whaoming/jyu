package com.jyu.bean;

import java.io.Serializable;

public class R_User extends R_Base{

	public UserInfo userInfo;
	public static class UserInfo{
		public int id;
		public String description;
		public String hobit;
		public int love;
		public String name;
		public String password;
		public String pic;
		public String picture;
		public int sex;
		public String snum;
		public String username;
		public String tname;
	}
	
	public static class UserCommonInfo implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 5539822805647213675L;
		public int id;
		public String udescription;
		public String uname;
		public String upic;
		public String usnum;
		public String utname;
	}
}
