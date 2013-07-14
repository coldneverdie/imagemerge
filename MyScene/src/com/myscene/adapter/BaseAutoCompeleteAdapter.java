package com.myscene.adapter;

import com.myscene.data.DBHelper;

import android.content.Context;
import android.database.Cursor;
import android.widget.SimpleCursorAdapter;

public class BaseAutoCompeleteAdapter extends SimpleCursorAdapter {
	DBHelper dbHelper;
	Context context;
	private String from;

	@SuppressWarnings("deprecation")
	public BaseAutoCompeleteAdapter(Context context, int layout, Cursor c,
			String from, int[] to) {
		super(context, layout, c, new String[] { from }, to);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.from = from;
	}

	
	@SuppressWarnings("deprecation")
	public BaseAutoCompeleteAdapter(Context context, int layout, Cursor c,String from, int to) {
	       super(context, layout, c, new String[] { from },new int[] { to });
	       this.context = context;
	}
	@Override
	public Cursor runQueryOnBackgroundThread(CharSequence constraint) {
		// TODO Auto-generated method stub
		if (null != constraint) {
			return getDBHelper().query(constraint.toString());
		}
		else 
			return null;
	}

	public DBHelper getDBHelper() {
		if (null == dbHelper) {
			dbHelper = new DBHelper(this.context);
		}
		return dbHelper;
	}
}
