package com.jyu.engine;

import java.util.List;

import com.jyu.domain.CommentInfoItem;


public interface CommentItemEngine {

	/**
	 * 获取某个domain的评论信息，例如例如LostItem的评论信息
	 * 
	 * @param type
	 *            domain对应的type 1-失物找回
	 * @param size
	 *            list大小，用于下拉刷新 或者可以说是分页功能
	 * @return
	 */
	public List<CommentInfoItem> getComment(int type, int id, int size);
}
