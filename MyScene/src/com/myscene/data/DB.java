package com.myscene.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.Browser;
//import android.util.Log;

/**
 * 
 * @author liudongqi
 * 
 */
public class DB {

	public SQLiteDatabase db;
	public String filepath;

	public void CreateDB(String dbName, Context context) {
		filepath = context.getFilesDir().toString() + "/" + dbName;
		db = SQLiteDatabase.openOrCreateDatabase(filepath, null);
	}

	private String[] HistoryProjection = new String[] {
			Browser.BookmarkColumns._ID, Browser.BookmarkColumns.TITLE,
			Browser.BookmarkColumns.URL, Browser.BookmarkColumns.VISITS,
			Browser.BookmarkColumns.DATE, Browser.BookmarkColumns.CREATED,
			Browser.BookmarkColumns.BOOKMARK, Browser.BookmarkColumns.FAVICON };

	public boolean Insert(String name, int type) {
		boolean result = false;
		if (!checkInHistory(name)) {
			// 检查历史中不存在
			try {
//				////System.out.println("Insert db state:" + db.isOpen());
				if (db == null)
					db = SQLiteDatabase.openOrCreateDatabase(filepath, null);
//				////System.out.println("begin get hint");
				if (!db.isOpen())
					SQLiteDatabase.openOrCreateDatabase(filepath, null);
				ContentValues value = new ContentValues();
				value.put("name", name);
				value.put("type", type);
				db.insertOrThrow("input", null, value);
			} catch (SQLException e) {
				e.printStackTrace();
				try{
				db.execSQL("create table if not exists input(id integer primary key autoincrement,name varchar(500),type integer)");
				ContentValues value = new ContentValues();
				value.put("name", name);
				value.put("type", type);
				db.insertOrThrow("input", null, value);
				}catch(Exception err){
					err.printStackTrace();
				}
				// db.execSQL("insert into input values(null,?)",new
				// String[]{name});
			} finally {
//				//Log.i("insert into db", name);
			}
		}
		return result;
	}

	public Cursor getLocalHints() {
		try {
//			////System.out.println("begin get local hint");
			// if(db == null)
			// db = SQLiteDatabase.openOrCreateDatabase(filepath, null);
//			////System.out.println("begin get hint");
			if (db == null || !db.isOpen())
				db = SQLiteDatabase.openOrCreateDatabase(filepath, null);
			Cursor cursor = db.rawQuery(
					"select name,type,local_id,url from inputlocal", null);
			return cursor;
		} catch (SQLException e) {
			db.execSQL("create table if not exists inputlocal(id integer primary key autoincrement,name varchar(500),type integer,local_id integer,url varchar(500))");
			Cursor cursor = db.rawQuery(
					"select name,type,local_id,url from inputlocal", null);
			return cursor;
		}
	}

	
	public  Cursor getAllStockRecords(ContentResolver contentResolver) {
		return contentResolver.query(Browser.BOOKMARKS_URI, HistoryProjection, null, null, null);
	}
	
	public Cursor getHints(String query) {
		if (query != null) {
			String sql = "select name,type from input where type!=6 and name like ?  ESCAPE '/'";
			try {
				// if(db == null)
				// db = SQLiteDatabase.openOrCreateDatabase(filepath, null);
				if (db == null || !db.isOpen())
					db = SQLiteDatabase.openOrCreateDatabase(filepath, null);
				// ////System.out.println("query db state:" + db.isOpen());
				// String selection = "name like \'" + query + "%\'";
				// Cursor cursor = db.rawQuery( sql, new String[]{ "%" +
				// query.replace("'", "/'").replace("%", "/%").replace("_",
				// "/_").trim() });
				if (query != null)
					query = query.replace("'", "/'").replace("%", "/%")
							.replace("_", "/_").trim();
				Cursor cursor = db.rawQuery(sql, new String[] { "%" + query
						+ "%" });
				return cursor;
				// return db.query("input", new String[] { "name" }, selection,
				// null, null, null, null);
			} catch (SQLException e) {
				db.execSQL("create table if not exists input(id integer primary key autoincrement,name varchar(500),type integer)");
				// String selection = "name like \'" + query + "%\'";
				// return db.query("input", new String[] { "name","type" },
				// selection,
				// null, null, null, null);
				Cursor cursor = null;
				try {
					cursor = db.rawQuery(sql, new String[] { query });
				} catch (Exception ex) {
					e.printStackTrace();
				}
				return cursor;
			}
		} else
			return null;
	}

	/**
	 * get history and bookamrk hint sugg
	 * @param contentResolver
	 * @param pattern
	 * @return
	 */
	public Cursor getHistoryAndBookmarkHints(ContentResolver contentResolver,String pattern){
		if(null == pattern || pattern.equals("")){
			return null;
		}
		String sqlPattern = "%" + pattern + "%";
		
		Cursor stockCursor = contentResolver.query(Browser.BOOKMARKS_URI,
				HistoryProjection,
				Browser.BookmarkColumns.TITLE + " LIKE '" + sqlPattern + "' OR " + Browser.BookmarkColumns.URL  + " LIKE '" + sqlPattern + "'",
				null,
				null);
		return stockCursor;
	}
	public Cursor getAllHints() {
		try {
			// if(db == null)
			// db = SQLiteDatabase.openOrCreateDatabase(filepath, null);
//			////System.out.println("begin get hint");
			if (db == null || !db.isOpen())
				db = SQLiteDatabase.openOrCreateDatabase(filepath, null);
//			////System.out.println("query db state:" + db.isOpen());
			// String selection = "name like \'" + query + "%\'";
			Cursor cursor = db.rawQuery(
					"select name,type from input where type!=6", null);
			return cursor;
			// return db.query("input", new String[] { "name" }, selection,
			// null, null, null, null);
		} catch (Exception e) {
			Cursor cursor = null;
			try {
				db.execSQL("create table if not exists input(id integer primary key autoincrement,name varchar(500),type integer)");
				// String selection = "name like \'" + query + "%\'";
				// return db.query("input", new String[] { "name","type" },
				// selection,
				// null, null, null, null);
				cursor = db.rawQuery(
						"select name,type from input where type!=6", null);
			} catch (Exception ee) {
				ee.printStackTrace();
			}
			return cursor;
		}
	}

	public boolean Insert(String name) {
		boolean result = false;
		try {
//			////System.out.println("Insert db state:" + db.isOpen());
			if (!db.isOpen())
				SQLiteDatabase.openOrCreateDatabase(filepath, null);
			ContentValues value = new ContentValues();
			value.put("name", name);
			db.insertOrThrow("input", null, value);
			// db.execSQL("insert into input values(null,?)",new
			// String[]{name});
		} catch (SQLException e) {
			e.printStackTrace();
			db.execSQL("create table if not exists input(id integer primary key autoincrement,name varchar(100))");
			ContentValues value = new ContentValues();
			value.put("name", name);
			db.insertOrThrow("input", null, value);
			// db.execSQL("insert into input values(null,?)",new
			// String[]{name});
		} finally {
			//Log.i("insert into db", name);
		}
		return result;
	}

	public boolean InsertLocalHistory(String name, int type, int local_id,
			String url) {
		// url only for web shuqian
		boolean result = false;
		try {
			// 应 先检查是否 已经存在
			if (!checkInLocalHistory(name, type, local_id, url)) {
//				////System.out.println("不存在");
				////System.out.println(name);
				////System.out.println(type);
				////System.out.println(local_id);
				////System.out.println(url);
				if (!db.isOpen())
					SQLiteDatabase.openOrCreateDatabase(filepath, null);
				ContentValues value = new ContentValues();
				value.put("name", name);
				value.put("type", type);
				value.put("local_id", local_id);
				value.put("url", url);
				db.insertOrThrow("inputlocal", null, value);
			} else {
				////System.out.println("已经存在");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			db.execSQL("create table if not exists inputlocal(id integer primary key autoincrement,name varchar(500),type integer,local_id integer,url varchar(500))");
			ContentValues value = new ContentValues();
			value.put("name", name);
			value.put("type", type);
			value.put("local_id", local_id);
			value.put("url", url);
			db.insertOrThrow("inputlocal", null, value);
			// db.insertWithOnConflict("inputlocal", null, value, -1);
			// db.execSQL("insert into input values(null,?)",new
			// String[]{name});
		}
		return result;
	}

	/**
	 * 检查网页搜索历史
	 * 
	 * @param name
	 * @return
	 */
	public boolean checkInHistory(String name) {
		String selection = "select name from input where name=?";
		Cursor cursor = null;
		try{
			cursor = db.rawQuery(selection, new String[] { name });
			////System.out.println("check count:" + cursor.getCount());
			if (cursor.getCount() > 0) {
				cursor.close();
				return true;
			}
		}catch(Exception err){
			err.printStackTrace();
		}finally{
			if(null!=cursor)
				cursor.close();
		}
		return false;
	}

	/**
	 * 检测是否已经存在历史记录中
	 * 
	 * @param name
	 * @param type
	 * @param local_id
	 * @param url
	 * @return
	 */
	public boolean checkInLocalHistory(String name, int type, int local_id,
			String url) {
		String selection = "select type from inputlocal where name=? and type=? and local_id=? and url=?";
		Cursor cursor = db.rawQuery(
				selection,
				new String[] { name, String.valueOf(type),
						String.valueOf(local_id), url });
		// Cursor cursor =
		// db.rawQuery("select * from inputlocal where name='"+name+"' and type='"+type+"' and local_id='"+local_id+"' and url='"+url+"'",null);
		////System.out.println("check count:" + cursor.getCount());
		if (cursor.getCount() > 0) {
			cursor.close();
			return true;
		}
		cursor.close();
		return false;
	}

	/**
	 * 清楚历史搜索记录
	 */
	public void clearLocalSearch() {
		db.execSQL("delete from inputlocal");
		////System.out.println("清楚历史搜索记录成功");
	}

	public Cursor Query(String query) {
		if (query != null) {
			try {
				////System.out.println("query db state:" + db.isOpen());
				////System.out.println("databse:" + filepath);
				if (!db.isOpen())
					SQLiteDatabase.openOrCreateDatabase(filepath, null);
				////System.out.println("query db state:" + db.isOpen());
				String selection = "name like \'" + query + "%\'";
				return db.query("input", new String[] { "name" }, selection,
						null, null, null, null);
			} catch (SQLException e) {
				db.execSQL("create table if not exists input(id integer primary key autoincrement,name varchar(100))");
				String selection = "name like \'" + query + "%\'";
				return db.query("input", new String[] { "name" }, selection,
						null, null, null, null);
			}
		} else
			return null;
	}

	// ////////////////////////////书签数据库操作
	//
	// public static boolean InsertBookmark(String bm_title,String bm_url) {
	// boolean result = false;
	// try {
	// ////System.out.println("Insert db state:" + db.isOpen());
	// if (!db.isOpen())
	// SQLiteDatabase.openOrCreateDatabase(filepath, null);
	// ContentValues value = new ContentValues();
	// value.put("bm_title", bm_title);
	// value.put("bm_url", bm_url);
	// db.insertOrThrow("bookmark", null, value);
	// result = true;
	// } catch (SQLException e) {
	// e.printStackTrace();
	// db.execSQL("create table if not exists bookmark(id integer primary key autoincrement,bm_title varchar(200),bm_url varchar(500))");
	// //Log.i("创建数据库成功","bookmark");
	// ContentValues value = new ContentValues();
	// value.put("bm_title", bm_title);
	// value.put("bm_url", bm_url);
	// db.insertOrThrow("bookmark", null, value);
	// result = true;
	// } catch(Exception e){
	// e.printStackTrace();
	// return result;
	// } finally {
	// //Log.i("insert into bookmark db", bm_title+"&"+bm_url);
	// }
	// return result;
	// }
	/**
	 * 查询书签
	 * 
	 * @return
	 */
//	public List<BookmarkBeen> getBookmark() {
//		////System.out.println("db state:" + db.isOpen());
//		if (!db.isOpen())
//			SQLiteDatabase.openDatabase(filepath, null,
//					SQLiteDatabase.OPEN_READONLY);
//		// SQLiteDatabase.openOrCreateDatabase(filepath, null);
//		////System.out.println("db state:" + db.isOpen());
//		List<BookmarkBeen> result = new ArrayList<BookmarkBeen>();
//		try {
//			Cursor cursor = db.rawQuery("select * from bookmark", null);
//			cursor.moveToFirst();
//			while (cursor.isAfterLast() == false) {
//				int index = cursor.getColumnIndex("bm_title");
//				////System.out.println("index" + index);
//				////System.out.println("bm_title" + cursor.getString(index));
//				//Log.i("bookmark result",
////						cursor.getString(0) + cursor.getString(1)
////								+ cursor.getString(2));
//				BookmarkBeen book = new BookmarkBeen();
//				book.setBm_title(cursor.getString(1));
//				book.setBm_url(cursor.getString(2));
//				result.add(book);
//				cursor.moveToNext();
//			}
//			cursor.close();// 关闭游标
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return result;
//	}

	/**
	 * 删除书签
	 * 
	 * @param title
	 */
	public void DeleteBookmark(String title) {
		try {
			db.delete("bookmark", "bm_title=?", new String[] { title.trim() });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 根据id删除书签，并不删除历史
	 * 
	 * @param contentResolver
	 * @param id
	 */
	public void deleteBookmark(ContentResolver contentResolver, long id) {
		String selection = Browser.BookmarkColumns._ID + " = " + id;
		
//		/*
		Cursor c = contentResolver.query(Browser.BOOKMARKS_URI,
				HistoryProjection, selection, null, null);
		if (c != null) {
			if (c.moveToFirst()) {
				if (c.getInt(c.getColumnIndex(Browser.BookmarkColumns.BOOKMARK)) == 1) {
					if (c.getInt(c
							.getColumnIndex(Browser.BookmarkColumns.VISITS)) > 0) {
						// If this record has been visited, keep it in history,
						// but remove its bookmark flag.
						/*
						ContentValues values = new ContentValues();
						values.put(Browser.BookmarkColumns.BOOKMARK, 0);
						values.putNull(Browser.BookmarkColumns.CREATED);
						contentResolver.update(Browser.BOOKMARKS_URI, values,
								selection, null);
						*/
						contentResolver.delete(Browser.BOOKMARKS_URI,
								selection, null);
						////System.out.println("in history,delete bookmark to history");
						////System.out.println("in history,delete bookmark to history");
						String title = c.getString(1);
						String url = c.getString(2);
						String created = c.getString(5);
						String date = c.getString(4);
						ContentValues values2 = new ContentValues();
						values2.put(Browser.BookmarkColumns.BOOKMARK, 0);
						values2.put(Browser.BookmarkColumns.VISITS, 1);
						values2.put(Browser.BookmarkColumns.TITLE, title);
						values2.put(Browser.BookmarkColumns.URL, url);
						values2.put(Browser.BookmarkColumns.DATE, date);
						values2.put(Browser.BookmarkColumns.CREATED, created);
						contentResolver.insert(Browser.BOOKMARKS_URI, values2);
						// Log.d("database", String.valueOf(check));
					} else {
						// never visited, it can be deleted.
						contentResolver.delete(Browser.BOOKMARKS_URI,
								selection, null);
						// Log.d("database", String.valueOf(check));
					}
				}
			}
			c.close();
		}
//		deleteHistoryRecord(contentResolver, id);
//		*/
//		try{
//		String[] colums = new String[] {
//				android.provider.Browser.BookmarkColumns._ID,
//				Browser.BookmarkColumns.BOOKMARK,
//				Browser.BookmarkColumns.TITLE, Browser.BookmarkColumns.URL,
//				Browser.BookmarkColumns.CREATED, Browser.BookmarkColumns.DATE };
//		Cursor cursor = contentResolver.query(
//		android.provider.Browser.BOOKMARKS_URI, colums,
//		selection, null, null);
//		if(null != cursor && cursor.getCount()>0){
//		cursor.moveToFirst();
//		String title = cursor.getString(2);
//		String url = cursor.getString(3);
//		String created = cursor.getString(4);
//		String date = cursor.getString(5);
//		ContentValues values = new ContentValues();
//		ContentValues values2 = new ContentValues();
//	
//		values.put(Browser.BookmarkColumns.BOOKMARK, 1);
//		values.put(Browser.BookmarkColumns.VISITS, 1);
//		values.put(Browser.BookmarkColumns.TITLE, title);
//		values.put(Browser.BookmarkColumns.URL, url);
//		values.put(Browser.BookmarkColumns.CREATED, created);
//		values.put(Browser.BookmarkColumns.DATE, date);
//		values2.put(Browser.BookmarkColumns.BOOKMARK, 0);
//		values2.put(Browser.BookmarkColumns.VISITS, 1);
//		values2.put(Browser.BookmarkColumns.TITLE, title);
//		values2.put(Browser.BookmarkColumns.URL, url);
//		values2.put(Browser.BookmarkColumns.DATE, date);
//		values2.put(Browser.BookmarkColumns.CREATED, created);
//		
//		 contentResolver.delete(Browser.BOOKMARKS_URI,
//					selection, null);
//		 
//		contentResolver.insert(Browser.BOOKMARKS_URI, values);
//		contentResolver.insert(Browser.BOOKMARKS_URI, values2);
//		}
//		}catch(Exception err){
//			err.printStackTrace();
//		}
	}

	/**
	 * 删除历史，如果是书签不删除
	 * 
	 * @param contentResolver
	 *            The content resolver.
	 * @param id
	 *            The history id.
	 */
	public void deleteHistoryRecord(ContentResolver contentResolver, long id) {
		String selection = Browser.BookmarkColumns._ID + " = " + id;
		Cursor cursor = contentResolver.query(Browser.BOOKMARKS_URI,
				HistoryProjection, selection, null, null);
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				if (cursor.getInt(cursor
						.getColumnIndex(Browser.BookmarkColumns.BOOKMARK)) == 1) {
					// The record is a bookmark, so we cannot delete it.
					// Instead, reset its visited count and last visited date.
					/*
					ContentValues values = new ContentValues();
					values.put(Browser.BookmarkColumns.VISITS, 0);
					values.putNull(Browser.BookmarkColumns.DATE);
					contentResolver.update(Browser.BOOKMARKS_URI, values,
							selection, null);
							*/
					contentResolver.delete(Browser.BOOKMARKS_URI,
							selection, null);
					String title = cursor.getString(1);
					String url = cursor.getString(2);
					String created = cursor.getString(5);
					String date = cursor.getString(4);
					ContentValues values2 = new ContentValues();
					values2.put(Browser.BookmarkColumns.BOOKMARK, 1);
					values2.put(Browser.BookmarkColumns.VISITS, 0);
					values2.put(Browser.BookmarkColumns.TITLE, title);
					values2.put(Browser.BookmarkColumns.URL, url);
					values2.put(Browser.BookmarkColumns.DATE, date);
					values2.put(Browser.BookmarkColumns.CREATED, created);
					contentResolver.insert(Browser.BOOKMARKS_URI, values2);
				} else {
					// The record is not a bookmark, we can delete it.
					contentResolver.delete(Browser.BOOKMARKS_URI, selection,
							null);
				}
			}
			cursor.close();
		}
	}

	
	public int getBookmarkId(ContentResolver contentResolver,String url){
		String[] colums = new String[] {
				android.provider.Browser.BookmarkColumns._ID};
		String whereClause = android.provider.Browser.BookmarkColumns.URL
		+ " = " + url;
		Cursor cursor = contentResolver.query(
				android.provider.Browser.BOOKMARKS_URI, colums,
				whereClause, null, null);
		if(cursor != null && cursor.getCount()>0){
			return cursor.getInt(0);
		}
		return -1;
	}
	
	
	public  void insertBookmark(ContentResolver contentResolver, long id, String title, String url, boolean isBookmark) {
		System.out.println("insert into bm");
		boolean bookmarkExist = false;
			//增加书签
		String[] colums = new String[] { android.provider.Browser.BookmarkColumns._ID, android.provider.Browser.BookmarkColumns.VISITS,android.provider.Browser.BookmarkColumns.CREATED};
		String whereClause = android.provider.Browser.BookmarkColumns.URL + " = \"" + url.replace("'", "''") + "\"";
		Cursor cursor = contentResolver.query(android.provider.Browser.BOOKMARKS_URI, colums, whereClause, null, null);
		bookmarkExist = (cursor != null) && (cursor.moveToFirst());
		int v = 0;
		if (bookmarkExist) {
			id = cursor.getLong(cursor.getColumnIndex(android.provider.Browser.BookmarkColumns._ID));
			v = cursor.getInt(cursor
					.getColumnIndex(Browser.BookmarkColumns.VISITS)) + 1;
		}

		ContentValues values = new ContentValues();
		ContentValues values2 = new ContentValues();
		if (title != null) {
			values.put(android.provider.Browser.BookmarkColumns.TITLE, title);
		}

		if (url != null) {
			values.put(android.provider.Browser.BookmarkColumns.URL, url);
			values2.put(android.provider.Browser.BookmarkColumns.URL, url);
		}

		if (isBookmark) {
			values.put(android.provider.Browser.BookmarkColumns.DATE, new Date().getTime());
			values2.put(android.provider.Browser.BookmarkColumns.DATE, new Date().getTime());
			values.put(android.provider.Browser.BookmarkColumns.CREATED, cursor.getString(2));
			values2.put(android.provider.Browser.BookmarkColumns.CREATED, cursor.getString(2));
			values.put(android.provider.Browser.BookmarkColumns.BOOKMARK, 1);
			values2.put(android.provider.Browser.BookmarkColumns.BOOKMARK, 0);
		} else{
			values.put(android.provider.Browser.BookmarkColumns.DATE, new Date().getTime());
			values2.put(android.provider.Browser.BookmarkColumns.DATE, new Date().getTime());
			values.put(android.provider.Browser.BookmarkColumns.CREATED, cursor.getString(2));
			values2.put(android.provider.Browser.BookmarkColumns.CREATED, cursor.getString(2));
		}
		values.put(android.provider.Browser.BookmarkColumns.VISITS, v);
		values2.put(android.provider.Browser.BookmarkColumns.VISITS, v);
		////System.out.println("v:"+v);
		if (bookmarkExist) {  
			////System.out.println("书签已经存在");
			String selection = android.provider.Browser.BookmarkColumns._ID + " = "+id;
			contentResolver.delete(Browser.BOOKMARKS_URI,
					selection, null);	
			contentResolver.insert(android.provider.Browser.BOOKMARKS_URI, values);
			contentResolver.insert(android.provider.Browser.BOOKMARKS_URI, values2);
		} else {          
			////System.out.println("书签不存在");
			contentResolver.insert(android.provider.Browser.BOOKMARKS_URI,
	                values);
			contentResolver.insert(android.provider.Browser.BOOKMARKS_URI,
	                values2);
//			contentResolver.insert(Browser.BOOKMARKS_URI, values);
		}
		if(null != cursor)
			cursor.close();
	}
	
	/**
	 * 把历史加为书签|增加书签 -1
	 * 
	 * @param contentResolver
	 * @param id
	 * @param title
	 * @param url
	 * @param isBookmark
	 */
	public  void setAsBookmark(ContentResolver contentResolver, long id, String title, String url, boolean isBookmark) {
		System.out.println("setAsBookmark");
		boolean bookmarkExist = false;
		if (id != -1) {
			//把历史改为书签
			String[] colums = new String[] { android.provider.Browser.BookmarkColumns._ID };
			String whereClause = android.provider.Browser.BookmarkColumns._ID + " = " + id;
			Cursor cursor = contentResolver.query(android.provider.Browser.BOOKMARKS_URI, colums, whereClause, null, null);
			bookmarkExist = (cursor != null) && (cursor.moveToFirst());
		} else {
			//增加书签
			String[] colums = new String[] { android.provider.Browser.BookmarkColumns._ID };
			String whereClause = android.provider.Browser.BookmarkColumns.URL + " = \"" + url.replace("'", "''") + "\"";
			Cursor cursor = contentResolver.query(android.provider.Browser.BOOKMARKS_URI, colums, whereClause, null, null);
			bookmarkExist = (cursor != null) && (cursor.moveToFirst());
			if (bookmarkExist) {
				id = cursor.getLong(cursor.getColumnIndex(android.provider.Browser.BookmarkColumns._ID));
			}
			if(null != cursor)
				cursor.close();
		}

		ContentValues values = new ContentValues();
		if (title != null) {
			values.put(android.provider.Browser.BookmarkColumns.TITLE, title);
		}

		if (url != null) {
			values.put(android.provider.Browser.BookmarkColumns.URL, url);
		}

		if (isBookmark) {
			values.put(android.provider.Browser.BookmarkColumns.BOOKMARK, 1);
			values.put(android.provider.Browser.BookmarkColumns.CREATED, new Date().getTime());
		} else {
			values.put(android.provider.Browser.BookmarkColumns.BOOKMARK, 0);
		}

		if (bookmarkExist) {                                    
			contentResolver.update(android.provider.Browser.BOOKMARKS_URI, values, android.provider.Browser.BookmarkColumns._ID + " = " + id, null);
		} else {                        
			contentResolver.insert(android.provider.Browser.BOOKMARKS_URI,
	                values);
//			contentResolver.insert(Browser.BOOKMARKS_URI, values);
		}
	}

	/**
	 * 修改书签
	 * 
	 * @param old_title
	 * @param title
	 * @param url
	 */
	public void UpdateBookmark(String old_title, String title, String url) {
		////System.out.println("title:" + title.trim());
		////System.out.println("url" + url.trim());
		ContentValues values = new ContentValues();
		values.put("bm_title", title.trim());
		values.put("bm_url", url.trim());
		try {
			db.update("bookmark", values, "bm_title=?",
					new String[] { old_title.trim() });
			// ////System.out.println("update bookmark set title='"+title.trim()+"' and url='"+url.trim()+"' where title='"+old_title.trim()+"'");
			// db.execSQL("update bookmark set bm_title='"+title.trim()+"' and bm_url='"+url.trim()+"' where bm_title='"+old_title.trim()+"'");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void ClearBookmark() {
		// 清空搜索数据库
		try {
			if (!db.isOpen())
				SQLiteDatabase.openOrCreateDatabase(filepath, null);
			//Log.i("clear book mark ", "true");
			db.execSQL("delete from bookmark");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void Close() {
		if (db != null && db.isOpen())
			db.close();
	}

	/**
	 * 更新历史
	 * @param contentResolver
	 * @param title
	 * @param url
	 * @param originalUrl
	 */
	public  void updateHistory(ContentResolver contentResolver, String title, String url, String originalUrl) {
		String[] colums = new String[] { Browser.BookmarkColumns._ID, Browser.BookmarkColumns.URL, Browser.BookmarkColumns.BOOKMARK, Browser.BookmarkColumns.VISITS };
		String whereClause = Browser.BookmarkColumns.URL + " = \"" + url.replace("'", "''") + "\" OR " + Browser.BookmarkColumns.URL + " = \"" + originalUrl.replace("'", "''") + "\"";
		Cursor cursor = contentResolver.query(Browser.BOOKMARKS_URI, colums, whereClause, null, null);
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				System.out.println("update histroy");
				long id = cursor.getLong(cursor.getColumnIndex(Browser.BookmarkColumns._ID));
				int visits = cursor.getInt(cursor.getColumnIndex(Browser.BookmarkColumns.VISITS)) + 1;
				ContentValues values = new ContentValues();

				// If its not a bookmark, we can update the title. If we were doing it on bookmarks, we would override the title choosen by the user.
				if (cursor.getInt(cursor.getColumnIndex(Browser.BookmarkColumns.BOOKMARK)) != 1) {
					values.put(Browser.BookmarkColumns.TITLE, title);
				}
				values.put(Browser.BookmarkColumns.DATE, new Date().getTime());
				values.put(Browser.BookmarkColumns.VISITS, visits);
				contentResolver.update(Browser.BOOKMARKS_URI, values, Browser.BookmarkColumns._ID + " = " + id, null);
			} else {
				System.out.println("new histroy");
				ContentValues values = new ContentValues();
				values.put(Browser.BookmarkColumns.TITLE, title);
				values.put(Browser.BookmarkColumns.URL, url);
				values.put(Browser.BookmarkColumns.DATE, new Date().getTime());
				values.put(Browser.BookmarkColumns.VISITS, 1);
				values.put(Browser.BookmarkColumns.BOOKMARK, 0);
				contentResolver.insert(Browser.BOOKMARKS_URI, values);
			}               
		}
		if(null != cursor)
			cursor.close();
	}
	
	/**
	 * 更新历史
	 * 
	 * @param contentResolver
	 * @param title
	 * @param url
	 * @param originalUrl
	 */
	public void updateHistoryHTC(ContentResolver contentResolver, String title,
			String url, String originalUrl) {
		////System.out.println("update history");
		String whereClause = Browser.BookmarkColumns.URL + " = \""
				+ url.replace("'", "''") + "\" OR "
				+ Browser.BookmarkColumns.URL + " = \""
				+ originalUrl.replace("'", "''") + "\"";
//		Cursor cursor = contentResolver.query(Browser.BOOKMARKS_URI, colums,
//				whereClause, null, null);
		Cursor cursor = contentResolver.query(Browser.BOOKMARKS_URI, HistoryProjection,
				whereClause, null, null);
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				////System.out.println("存在历史");
				long id = cursor.getLong(cursor
						.getColumnIndex(Browser.BookmarkColumns._ID));
				int visits = cursor.getInt(cursor
						.getColumnIndex(Browser.BookmarkColumns.VISITS)) + 1;
				ContentValues values = new ContentValues();
				// If its not a bookmark, we can update the title. If we were
				// doing it on bookmarks, we would override the title choosen by
				// the user.
				String created = cursor.getString(5);
				if (cursor.getInt(cursor
						.getColumnIndex(Browser.BookmarkColumns.BOOKMARK)) != 1) {
					values.put(Browser.BookmarkColumns.TITLE, title);
				}
				int type = cursor.getInt(6);
				String selection = Browser.BookmarkColumns._ID + " = " + id;
				if(type == 0){
					//history
//					values.put(Browser.BookmarkColumns.DATE, new Date().getTime());
					values.put(Browser.BookmarkColumns.VISITS, visits);
					values.put(Browser.BookmarkColumns.TITLE, title);
					values.put(Browser.BookmarkColumns.URL, url);
					values.put(Browser.BookmarkColumns.CREATED, created);
					values.put(Browser.BookmarkColumns.DATE, new Date().getTime());
					contentResolver.delete(Browser.BOOKMARKS_URI, selection,null);
					contentResolver.insert(Browser.BOOKMARKS_URI, values);
				}
				else{
					//bookmark 
					////System.out.println("update 书签");
					contentResolver.delete(Browser.BOOKMARKS_URI,
							selection, null);
					values.put(Browser.BookmarkColumns.BOOKMARK, 1);
					values.put(Browser.BookmarkColumns.VISITS, 1);
					values.put(Browser.BookmarkColumns.TITLE, title);
					values.put(Browser.BookmarkColumns.VISITS, visits);
					values.put(Browser.BookmarkColumns.URL, url);
					values.put(Browser.BookmarkColumns.CREATED, created);
					values.put(Browser.BookmarkColumns.DATE, new Date().getTime());
					ContentValues values2 = new ContentValues();
					values2.put(Browser.BookmarkColumns.BOOKMARK, 0);
					values2.put(Browser.BookmarkColumns.VISITS, 1);
					values2.put(Browser.BookmarkColumns.TITLE, title);
					values2.put(Browser.BookmarkColumns.URL, url);
					values2.put(Browser.BookmarkColumns.VISITS, visits);
					values2.put(Browser.BookmarkColumns.CREATED, created);
					values2.put(Browser.BookmarkColumns.DATE, new Date().getTime());
					contentResolver.insert(Browser.BOOKMARKS_URI, values);
					contentResolver.insert(Browser.BOOKMARKS_URI, values2);
				}
				
			} else {
				////System.out.println("不存在历史");
				ContentValues values = new ContentValues();
				values.put(Browser.BookmarkColumns.TITLE, title);
				values.put(Browser.BookmarkColumns.URL, url);
				values.put(Browser.BookmarkColumns.CREATED, new Date().getTime());
				values.put(Browser.BookmarkColumns.DATE, new Date().getTime());
				values.put(Browser.BookmarkColumns.VISITS, 1);
				values.put(Browser.BookmarkColumns.BOOKMARK, 0);
				contentResolver.insert(Browser.BOOKMARKS_URI, values);
			}
			cursor.close();
		}
	}

	/**
	 * 书签与历史转换
	 * @param contentResolver
	 * @param id
	 * @param bookmark
	 */
	public  void toggleBookmark(ContentResolver contentResolver, long id, boolean bookmark) {
		String[] colums = new String[] { Browser.BookmarkColumns._ID };
		String selection = Browser.BookmarkColumns._ID + " = " + id;
		Cursor cursor = contentResolver.query(Browser.BOOKMARKS_URI, colums, selection, null, null);
		boolean recordExists = (cursor != null) && (cursor.moveToFirst());
		
		if (recordExists) {
			ContentValues values = new ContentValues();
			values.put(Browser.BookmarkColumns.BOOKMARK, bookmark);
			if (bookmark) {
				values.put(Browser.BookmarkColumns.CREATED, new Date().getTime());
			} else {
				values.putNull(Browser.BookmarkColumns.CREATED);
			}
			contentResolver.update(Browser.BOOKMARKS_URI, values, selection, null);
		}
		if(cursor != null){
			cursor.close();
		}
	}
	
	/**
	 * 书签与历史转换
	 * 
	 * @param contentResolver
	 * @param id
	 * @param bookmark
	 */
	public void toggleBookmarkHTC(ContentResolver contentResolver, long id,
			boolean bookmark) {
		////System.out.println("toggle history");
		// /*
		try {
			////System.out.println("id:" + id);
			String[] colums = new String[] {
					android.provider.Browser.BookmarkColumns._ID,
					Browser.BookmarkColumns.BOOKMARK,
					Browser.BookmarkColumns.TITLE, Browser.BookmarkColumns.URL,
					Browser.BookmarkColumns.CREATED,
					Browser.BookmarkColumns.DATE,
					Browser.BookmarkColumns.VISITS};
			String selection = android.provider.Browser.BookmarkColumns._ID
					+ " = " + id;
			Cursor cursor = contentResolver.query(Browser.BOOKMARKS_URI,
					colums, selection, null, null);
			boolean recordExists = (cursor != null) && (cursor.moveToFirst());

			if (recordExists) {
				////System.out.println("recod exists");
				ContentValues values = new ContentValues();
				int type = cursor.getInt(1);
				String title = cursor.getString(2);
				String url = cursor.getString(3);
				String created = cursor.getString(4);
//				String date = cursor.getString(5);
				int v = cursor.getInt(6) + 1;
				if (bookmark) {
					if (type == 0) {
						////System.out.println("not bookmark");
						contentResolver.delete(Browser.BOOKMARKS_URI,
								selection, null);
						values.put(Browser.BookmarkColumns.BOOKMARK, 1);
						values.put(Browser.BookmarkColumns.VISITS, v);
						values.put(Browser.BookmarkColumns.TITLE, title);
						values.put(Browser.BookmarkColumns.URL, url);
						values.put(Browser.BookmarkColumns.CREATED, created);
						values.put(Browser.BookmarkColumns.DATE, new Date().getTime());
						ContentValues values2 = new ContentValues();
						values2.put(Browser.BookmarkColumns.BOOKMARK, 0);
						values2.put(Browser.BookmarkColumns.VISITS, v);
						values2.put(Browser.BookmarkColumns.TITLE, title);
						values2.put(Browser.BookmarkColumns.URL, url);
						values2.put(Browser.BookmarkColumns.DATE, new Date().getTime());
						values2.put(Browser.BookmarkColumns.CREATED, created);
						contentResolver.insert(Browser.BOOKMARKS_URI, values);
						contentResolver.insert(Browser.BOOKMARKS_URI, values2);
					} 
					else {
						////System.out.println("is bookmark");
//						contentResolver.delete(Browser.BOOKMARKS_URI,
//								selection, null);
//						values.put(Browser.BookmarkColumns.DATE, date);
//						values.put(Browser.BookmarkColumns.CREATED, created);
//						values.put(Browser.BookmarkColumns.VISITS, 1);
//						values.put(Browser.BookmarkColumns.BOOKMARK, 0);
//						values.put(Browser.BookmarkColumns.TITLE, title);
//						values.put(Browser.BookmarkColumns.URL, url);
//						contentResolver.insert(Browser.BOOKMARKS_URI, values);
					}
				} else {
					contentResolver.delete(Browser.BOOKMARKS_URI, selection,
							null);
					values.put(Browser.BookmarkColumns.DATE, new Date().getTime());
					values.put(Browser.BookmarkColumns.CREATED, created);
					values.put(Browser.BookmarkColumns.VISITS, v);
					values.put(Browser.BookmarkColumns.BOOKMARK, 0);
					values.put(Browser.BookmarkColumns.TITLE, title);
					values.put(Browser.BookmarkColumns.URL, url);
					contentResolver.insert(Browser.BOOKMARKS_URI, values);
				}
			}
		} catch (Exception err) {
			err.printStackTrace();
		}
		// */
		////System.out.println("toggle history end");
	}

//	public BookmarkItem getStockBookmarkById(ContentResolver contentResolver,
//			long id) {
//		BookmarkItem result = null;
//		String whereClause = Browser.BookmarkColumns._ID + " = " + id;
//
//		Cursor c = contentResolver.query(Browser.BOOKMARKS_URI,
//				HistoryProjection, whereClause, null, null);
//		if (c != null) {
//			if (c.moveToFirst()) {
//				String title = c.getString(c
//						.getColumnIndex(Browser.BookmarkColumns.TITLE));
//				String url = c.getString(c
//						.getColumnIndex(Browser.BookmarkColumns.URL));
//				result = new BookmarkItem(title, url);
//			}
//
//			c.close();
//		}
//
//		return result;
//	}

	public Cursor getStockBookmarks(ContentResolver contentResolver,
			int sortMode) {
		String whereClause = Browser.BookmarkColumns.BOOKMARK + " = 1";

		String orderClause;
		switch (sortMode) {
		case 0:
			orderClause = Browser.BookmarkColumns.VISITS + " DESC, "
					+ Browser.BookmarkColumns.TITLE + " COLLATE NOCASE";
			break;
		case 1:
			orderClause = Browser.BookmarkColumns.TITLE + " COLLATE NOCASE";
			break;
		case 2:
			orderClause = Browser.BookmarkColumns.CREATED + " DESC";
			break;
		default:
			orderClause = Browser.BookmarkColumns.TITLE + " COLLATE NOCASE";
			break;
		}

		return contentResolver.query(Browser.BOOKMARKS_URI, HistoryProjection,
				whereClause, null, orderClause);
	}

	public Cursor getStockHistory(ContentResolver contentResolver) {
		// String whereClause = Browser.BookmarkColumns.VISITS + " > 0 AND " +
		// Browser.BookmarkColumns.BOOKMARK + " = 0" ;
		// String selection = "(" + Browser.BookmarkColumns.BOOKMARK +
		// " = 0) OR (" + Browser.BookmarkColumns.BOOKMARK + " IS NULL)";
		String whereClause = Browser.BookmarkColumns.VISITS + " > 0";
		String orderClause = Browser.BookmarkColumns.DATE + " DESC";
		return contentResolver.query(Browser.BOOKMARKS_URI, HistoryProjection,
				whereClause, null, orderClause);
	}

	/**
	 * 清空书签
	 * 
	 * @param contentResolver
	 */
	public void clearBookmark(final ContentResolver contentResolver) {
		new Thread() {
			public void run() {
				String selection = Browser.BookmarkColumns.BOOKMARK + " = 1";
				contentResolver.delete(Browser.BOOKMARKS_URI, selection, null);
			}
		}.start();
	}

	/**
	 * 清空历史 把书签跟历史的visits都设为0,然后删除非书签的历史
	 * 
	 * @param contentResolver
	 * @param clearHistory
	 * @param clearBookmarks
	 */
	public void clearHistory(final ContentResolver contentResolver) {
		
		String whereClause = "(" + Browser.BookmarkColumns.BOOKMARK + " = 0) OR (" + Browser.BookmarkColumns.BOOKMARK + " IS NULL)";
		contentResolver.delete(Browser.BOOKMARKS_URI, whereClause, null);		
//		/*
		String selection = Browser.BookmarkColumns.VISITS + " > 0";
		Cursor cursor = contentResolver.query(Browser.BOOKMARKS_URI,
				HistoryProjection, selection, null, null);
		if (cursor != null) {
			if(cursor.getCount()>0)
			while (cursor.moveToNext()) {
				if (cursor.getInt(cursor
						.getColumnIndex(Browser.BookmarkColumns.BOOKMARK)) == 1) {
					// The record is a bookmark, so we cannot delete it.
					// Instead, reset its visited count and last visited date.
					////System.out.println("delete history not bookmark");
					String selection2 = Browser.BookmarkColumns._ID +" = "+ cursor.getString(0);
					contentResolver.delete(Browser.BOOKMARKS_URI,
							selection2, null);
					String title = cursor.getString(1);
					String url = cursor.getString(2);
					String created = cursor.getString(5);
					String date = cursor.getString(4);
					ContentValues values2 = new ContentValues();
					values2.put(Browser.BookmarkColumns.BOOKMARK, 1);
					values2.put(Browser.BookmarkColumns.VISITS, 0);
					values2.put(Browser.BookmarkColumns.TITLE, title);
					values2.put(Browser.BookmarkColumns.URL, url);
					values2.put(Browser.BookmarkColumns.DATE, date);
					values2.put(Browser.BookmarkColumns.CREATED, created);
					contentResolver.insert(Browser.BOOKMARKS_URI, values2);
				} 
				}
				else {
//					// The record is not a bookmark, we can delete it.
					contentResolver.delete(Browser.BOOKMARKS_URI, selection,
							null);
				}
			}
			cursor.close();
	}

	public void Clear() {
		// 清空搜索数据库
		try {
			if (!db.isOpen())
				SQLiteDatabase.openOrCreateDatabase(filepath, null);
			db.execSQL("delete from input");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
