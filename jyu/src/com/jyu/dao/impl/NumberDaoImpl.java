package com.jyu.dao.impl;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jyu.domain.MyNumber;

public class NumberDaoImpl {

	private static String path = "data/data/com.jyc/files/phone.db";

	public static List<MyNumber> query(String one, String second) {

		List<MyNumber> list = new ArrayList<MyNumber>();
		SQLiteDatabase database = SQLiteDatabase.openDatabase(path, null,
				SQLiteDatabase.OPEN_READONLY);
		Cursor cursor = database.rawQuery(
				"select * from number where one =? and second =?",
				new String[] { one, second });
		while (cursor.moveToNext()) {
			MyNumber num = new MyNumber();
			num.setOne(cursor.getString(cursor.getColumnIndex("one")));
			num.setSecond(cursor.getString(cursor.getColumnIndex("second")));
			num.setThird(cursor.getString(cursor.getColumnIndex("third")));
			num.setNum(cursor.getString(cursor.getColumnIndex("num")));
			list.add(num);
		}
		cursor.close();
		return list;
	}

}
