package com.jyu.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

	private static final String NAME = "jdzs.db";
//	private static final int START_VERSION = 1;
//	private static final int HISTORY_VERSION = 2;
	private static final int CURRENT_VERSION = 3;

	public static final String TABLE_ID = "id";


	public static final String TABLE_NUM_NAME = "number";
	public static final String TABLE_NUM_ONE = "one";
	public static final String TABLE_NUM_SECOND = "second";
	public static final String TABLE_NUM_THIRD = "third";
	public static final String TABLE_NUM_NUM = "num";


	public DBHelper(Context context) {
		super(context, NAME, null, CURRENT_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
//		db.execSQL("CREATE TABLE " + TABLE_SEARCH_NAME + " (" + //
//				TABLE_ID + " integer primary key autoincrement, " + //
//				TABLE_SEARCH_WORD + " VARCHAR(200))"//
//		);
//
//		db.execSQL("CREATE TABLE "
//				+ TABLE_CATEGORY
//				+ " ("
//				+ //
//				TABLE_CATEGORY_ID + " integer primary key,"
//				+ TABLE_CATEGORY_NAME + " VARCHAR(200),"
//				+ TABLE_CATEGORY_PARENT_ID + " integer ," + TABLE_CATEGORY_PIC
//				+ " VARCHAR(200)," + TABLE_CATEGORY_TAG + " VARCHAR(200),"
//				+ TABLE_CATEGORY_ISLEAFNODE + " VARCHAR(200) )"
//
//		);
//		onUpgrade(db, START_VERSION, CURRENT_VERSION);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//		Log.i(TAG, "oldVersion:" + oldVersion + "&newVersion:" + newVersion);
		// 数据库更行

		switch (oldVersion) {
		/*
		 * case START_VERSION:// 版本一升级的用户 // 版本二添加的表 db.execSQL("CREATE TABLE "
		 * + TABLE_BOOK_NAME + " (" + // TABLE_ID +
		 * " integer primary key autoincrement, " + //
		 * 
		 * TABLE_BOOK_TITLE + " VARCHAR(200))"// ); case HISTORY_VERSION://
		 * 版本二升级的用户 // 版本三添加表 db.execSQL("CREATE TABLE " + TABLE_NAME3 + " (" +
		 * // TABLE_ID + " integer primary key autoincrement, " + //
		 * 
		 * TABLE_BOOK_TITLE + " VARCHAR(200))"// ); // case 增加或修改表 break;
		 */
		}
	}
}
