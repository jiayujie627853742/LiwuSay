package com.jiayujie.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBManager {

	private DBHelper dbHelper;
	
	public DBManager(Context  context) {
		// TODO 自动生成的构造函数存根
		dbHelper = new DBHelper(context);
	}

	//增删改
	public boolean executeSQL(String sql,Object[] bindArgs){
		
		if (sql==null) {
			return false;
		}
		
		SQLiteDatabase database = null;
		try {
			database = dbHelper.getWritableDatabase();
			database.execSQL(sql, bindArgs);
			return true;
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if (database!=null) {
				database.close();
			}
		}
		return false;
	}
	
	
	
	//查
	public  Cursor  queryCursor(String sql,String[] selectionArgs){
		
		if (sql==null) {
			return null;
		}
		
		SQLiteDatabase database = null;
		Cursor cursor = null;
		try {
			database = dbHelper.getReadableDatabase();
			cursor = database.rawQuery(sql, selectionArgs);
			//cursor.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return cursor;
	}
}
