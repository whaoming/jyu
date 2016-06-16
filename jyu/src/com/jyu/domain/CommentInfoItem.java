package com.jyu.domain;

public class CommentInfoItem {

	private int id;
	private String value;
	private String time;
	private int domainid;
	private int uid;
	private String upic;
	private String uname;
	private int state;
	private int reply;
	private String udesprition;
	private int position;
	
	

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getReply() {
		return reply;
	}

	public void setReply(int reply) {
		this.reply = reply;
	}

	public String getUdesprition() {
		return udesprition;
	}

	public void setUdesprition(String udesprition) {
		this.udesprition = udesprition;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public int getDomainid() {
		return domainid;
	}

	public void setDomainid(int domainid) {
		this.domainid = domainid;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public String getUpic() {
		return upic;
	}

	public void setUpic(String upic) {
		this.upic = upic;
	}

	public String getUname() {
		return uname;
	}

	public void setUname(String uname) {
		this.uname = uname;
	}

}
