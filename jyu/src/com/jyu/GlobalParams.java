package com.jyu;

//import com.shopping_ec.redbaby.bean.SendInfo;


import java.util.List;

import android.content.Context;

import com.jyu.bean.R_User.UserInfo;

public class GlobalParams {

	public static String PROXY_IP = "";
	public static int PROXY_PORT;

	public static int WIN_WIDTH;


	public static Context CONTEXT;

	
//	public static User user = null;
	public static UserInfo userInfo = null;
	
	
	/**
	 * 记录用户参加过的活动
	 */
	public static List<Integer> socials;
	
	public static boolean isConnect;
	

}
