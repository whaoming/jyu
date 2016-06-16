package com.jyu.engine;

import java.util.List;

import android.content.Context;

import com.jyu.bean.R_Social;
import com.jyu.domain.Social;
import com.jyu.domain.UserCommentInfo;


public interface SocialEngine {

	public R_Social getSocialList(int type,int page,boolean isCache,Context ct);

	boolean joinSocial(int socialid);

	boolean publishSocial(Social social);

	List<Social> getUserSocialList(int page);

	List<Social> getMyOriginateSocialList(int page);

	List<UserCommentInfo> getSocialUserList(int page, int socialid);

	boolean updateSocial(Social social);
}
