package com.jyu.engine;

import java.util.List;

import android.content.Context;

import com.jyu.bean.R_Base;
import com.jyu.bean.R_Lost;
import com.jyu.bean.R_Lost_Comment;
import com.jyu.bean.R_Lost_Comment.Lost_CommentItem;
import com.jyu.domain.LostItemInfo;

public interface LostItemEngine {

	R_Lost_Comment getLostItemCommentList(int page, int domainid);

	R_Lost getMyItemList(int page, boolean isCache, Context ct);

	R_Lost getItemList(int size, boolean isCache, Context ct);
	
	R_Lost_Comment insertLostItemComment(Lost_CommentItem item);
	
	R_Lost_Comment deleteLostItemComment(int domainid,int commentId);

	List<LostItemInfo> getLostList(int size, boolean isCache, Context ct);

	boolean addLost(LostItemInfo item);

	List<LostItemInfo> getFoundItemList(int page, boolean isCache, Context ct);

	boolean updateItem(LostItemInfo info);

}
