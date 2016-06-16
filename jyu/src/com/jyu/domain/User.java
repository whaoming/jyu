package com.jyu.domain;


public class User {

	private int id;
	private String username;
	private String password;
	private String pic;
	private String name;
	private String picture;
	private String snum;
	private String tname;
	private String hobit;
	private int sex;
	private int love;
	private String description;
	
	
	
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public User(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}
	public User(String username, String password, String pic, String name,
			String picture, String tname, String hobit,  String snum,int sex,
			int love,String description) {
		super();
		this.username = username;
		this.password = password;
		this.pic = pic;
		this.name = name;
		this.picture = picture;
		this.snum = snum;
		this.tname = tname;
		this.hobit = hobit;
		this.sex = sex;
		this.love = love;
		this.description = description;
	}
	
	
	public User(int id,String username,String name,  String description, String tname, String snum,
			 int sex, int love, String hobit ,String pic) {
		super();
		this.username = username;
		this.id = id;
		this.username = username;
		this.pic = pic;
		this.name = name;
		this.snum = snum;
		this.tname = tname;
		this.hobit = hobit;
		this.sex = sex;
		this.love = love;
		this.description = description;
	}
	public String getPicture() {
		return picture;
	}
	public void setPicture(String picture) {
		this.picture = picture;
	}
	public String getSnum() {
		return snum;
	}
	public void setSnum(String snum) {
		this.snum = snum;
	}
	public String getTname() {
		return tname;
	}
	public void setTname(String tname) {
		this.tname = tname;
	}
	public String getHobit() {
		return hobit;
	}
	public void setHobit(String hobit) {
		this.hobit = hobit;
	}
	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
		this.sex = sex;
	}
	public int getLove() {
		return love;
	}
	public void setLove(int love) {
		this.love = love;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public User() {
		super();
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", password="
				+ password + ", pic=" + pic + ", name=" + name + ", picture="
				+ picture + ", snum=" + snum + ", tname=" + tname + ", hobit="
				+ hobit + ", sex=" + sex + ", love=" + love + ", description="
				+ description + "]";
	}
	public User(String username, String password, String pic, String name) {
		super();
		this.username = username;
		this.password = password;
		this.pic = pic;
		this.name = name;
	}
	
	
	
}
