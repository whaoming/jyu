package com.jyu.engine;

import java.util.List;

import android.content.Context;

import com.jyu.domain.SecondHandInfo;
import com.jyu.domain.UserCommentInfo;


public interface SecondHandEngine {


	List<SecondHandInfo> getSecondHandList(int page, boolean isCache, Context ct);

	boolean addSecondHand(SecondHandInfo info);

	List<SecondHandInfo> getSecondHandPublisher(int page, boolean isCache,
			Context ct);

	List<UserCommentInfo> getSecondHandPlayer(int page, int shid,
			boolean isCache);

	boolean sendReply(int shid);
}
