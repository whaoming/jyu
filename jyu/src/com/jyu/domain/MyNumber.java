package com.jyu.domain;

import com.jyu.dao.DBHelper;
import com.jyu.dao.annotation.Column;
import com.jyu.dao.annotation.ID;


//@TableName(DBHelper.TABLE_NUM_NAME)
public class MyNumber {

//	@Column(DBHelper.TABLE_ID)
	@ID(autoincrement = true)
	private String id;
//	@Column(DBHelper.TABLE_NUM_ONE)
	private String one;//一级分类
//	@Column(DBHelper.TABLE_NUM_SECOND)
	private String second;//二级分类
//	@Column(DBHelper.TABLE_NUM_THIRD)
	private String third;
	public String getThird() {
		return third;
	}
	public void setThird(String third) {
		this.third = third;
	}
	@Column(DBHelper.TABLE_NUM_NUM)
	private String num;//具体号码
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getOne() {
		return one;
	}
	public void setOne(String one) {
		this.one = one;
	}
	public String getSecond() {
		return second;
	}
	public void setSecond(String second) {
		this.second = second;
	}
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	
	
}
