package com.jyu.dao.base;

import java.io.Serializable;
import java.util.List;

/**
 * 数据操作的接口的公共部分
 * 
 * @author Administrator
 * 
 * @param <M>
 */
public interface DAO<M> {
	/**
	 * 添加
	 * 
	 * @param m
	 * @return
	 */
	long insert(M m);

	/**
	 * 删除
	 * 
	 * @param id
	 * @return
	 */
	int delete(Serializable id);// int long String:JPA id Serializable

	/**
	 * 更新
	 * 
	 * @param m
	 * @return
	 */
	int updata(M m);

	/**
	 * 查询
	 * 
	 * @return
	 */
	List<M> findAll();

	/**
	 * 按照条件查询
	 * 
	 * @param selection
	 * @param selectionArgs
	 * @param orderBy
	 * @return
	 */
	List<M> findByCondition(String selection, String[] selectionArgs,
			String orderBy);
}
