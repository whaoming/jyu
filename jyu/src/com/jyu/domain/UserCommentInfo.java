package com.jyu.domain;

public class UserCommentInfo {

	private int id;
	private String uname;
	private String udescription;
	private String upic;
	private String utname;
	private String usnum;
	private String parameter = "";
	
	public String getParameter() {
		return parameter;
	}
	public void setParameter(String parameter) {
		this.parameter = parameter;
	}
	public String getUtname() {
		return utname;
	}
	public void setUtname(String utname) {
		this.utname = utname;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUname() {
		return uname;
	}
	public void setUname(String uname) {
		this.uname = uname;
	}
	public String getUdescription() {
		return udescription;
	}
	public void setUdescription(String udescription) {
		this.udescription = udescription;
	}
	public String getUpic() {
		return upic;
	}
	public void setUpic(String upic) {
		this.upic = upic;
	}
	public String getUsnum() {
		return usnum;
	}
	public void setUsnum(String usnum) {
		this.usnum = usnum;
	}
	
}
