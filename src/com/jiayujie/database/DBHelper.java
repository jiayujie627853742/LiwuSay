package com.jiayujie.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

	private final static String DB_NAME="liwu.db";
	private final static int  VERSION= 1;
	
	public DBHelper(Context context) {
		super(context, DB_NAME, null, VERSION);
	}

	
	//   编号   (id,type,title,content,imageurl)
	@Override
	public void onCreate(SQLiteDatabase db) {
		String  createSQL = "create  table liwu (id  integer primary key,type varchar(64),title varchar(64),content varchar(64),imageurl  varchar(64))";
		db.execSQL(createSQL);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

}
