package com.jyu;

public class ConstantValue {

	public static String ENCODING = "UTF-8";
	//记录服务器ip地址
	public static String LOTTERY_URI = "http://192.168.211.1:8080/jdzs1";

	public static String NEWSGET = "/NewsServlet?action=getall";

	public static String NEWS = "/NewsServlet?action=getnews";
	
	public static String GETALLLOST = "/LostServlet?action=getalllost";

	public static String GETCOMMENT = "/CommentServlet?action=getcomment";

	public static String GETSOCIALLIST = "/SocialServlet?action=getsoiallist";

	public static String LOGIN = "/UserServlet?action=login";

	public static String SAVAUSERHEAD = "/UserServlet?action=updateuserhead";

	public static String UPDATEUSERINFO = "/UserServlet?action=updatecolumn";

	public static String ADDREPAIR = "/RepairServlet?action=addrepair";

	public static String GETSECONDHANDLIST = "/SecondHandServlet?action=getsecond";

	public static String INSERTLOST = "/LostItemServlet?action=insertitem";

	public static String JOINSOCIAL = "/SocialServlet?action=joinsocial";

	public static String PUBLISHSOCIAL = "/SocialServlet?action=publishsocial";

	public static String INSERTSECONDHAND = "/SecondHandServlet?action=insertsecondhand";

	public static String GETMYORIGINATESOCIALLIST = "/SocialServlet?action=getmyoriginatesociallist";

	public static String GETUSERSOCIAL = "/SocialServlet?action=getusersocial";

	public static String GETSOCIALUSERLIST = "/SocialServlet?action=getsocialuserlist";

	public static String GETSECONDHANDPUBLISHER = "/SecondHandServlet?action=getsecondhandpublisher";

	public static String GETSECONDHANDPLAYER = "/SecondHandServlet?action=getsecondhandplayer";

	public static String SENDSECONDHANDREPLY = "/SecondHandServlet?action=sendsecondhandreply";

	public static String UPDATESOCIAL = "/SocialServlet?action=updatesocial";

	public static String GETLOSTITEMLIST = "/LostItemServlet?action=getlostitemlist";

	public static String GETFOUNDITEMLIST = "/LostItemServlet?action=getfounditemlist";

	public static String GETLOSTFOUNDITEMLIST = "/LostItemServlet?action=getlostitemlist";

	public static String INSERTITEM = "/LostItemServlet?action=insertitem";

	public static String GETMYITEMLIST = "/LostItemServlet?action=getmyitemlist";

	public static String UPDATEMYITEM = "/LostItemServlet?action=updatemyitem";

	public static String REGISTERUSER = "/UserServlet?action=register";

	public static String GETTOPLINELIST = "/TopLineServlet?action=gettopline";

	public static String GETTOPLINE =  "/NewsServlet?action=gettopline";

	public static String TESTCONNECT = "/CommonServlet?action=testconnect";
	
	public static String GETLOSTITEMCOMMENTLIST = "/LostItemServlet?action=getItemComment";
	
	public static String INSERTLOSTITEMCOMMENT = "/LostItemServlet?action=insertLostItemComment";
	
	public static String DELETELOSTITEMCOMMENT = "/LostItemServlet?action=deleteLostItemComment";
}
