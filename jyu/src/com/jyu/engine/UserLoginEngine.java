package com.jyu.engine;

import android.content.Context;

import com.jyu.bean.R_User;
import com.jyu.domain.User;


public interface UserLoginEngine {
	R_User getUserLoginInfoByList(boolean isCache,String username,String password,Context ct);
	
	boolean updateUserInfo(String column,String value,Context ct);

	User registerUser(User user);

	void updateUserInfoToCache(User user, Context ct);
}
