package com.myscene.data;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 1;

	public static final String TABLE_NAME = "project";
	public static final String TABLE_NAME_BASEINFO = "baseinfo";
	public static final String TABLE_NAME_DIRT = "dirt";
	public static final String TABLE_NAME_PID = "pid";
	public static final String TABLE_NAME_WASHWELL = "washwell";

	public static final String COLUMN_ID = "_id"; // 默认id
	public static final String PROJECT_ID = "project_id"; // 项目创建id
	public static final String PROJECT_NAME = "project_name"; // 项目名称
	public static final String CREATE_TIME = "create_time"; // 创建时间
	// 现场记录
	public static final String WELL_NUM = "well_num";
	public static final String BASE_WEATHER = "base_weather"; // 天气
	public static final String BASE_DATE = "base_date"; // 钻孔日期
	public static final String BASE_WORK = "base_work"; // 勘察单位
	public static final String BASE_DEPTH = "base_depth";// 钻孔深度
	public static final String BASE_MRADIS = "base_mradis"; // 检测井内径
	public static final String BASE_RADIS = "base_radis";// 钻孔内径
	public static final String BASE_WAY = "base_way"; // 钻孔方式
	public static final String BASE_WRITER = "base_writer"; // 记录人
	public static final String BASE_GPS = "base_gps"; // 井位 GPS
	// public static final String BASE_WELL_ID = "BASE_well_id";//井号
	public static final String BASE_FIRSTDEPTH = "base_first_depth";// 初见水位深度

	// /////////////////////////////////////////////////////////////////////////////////
	// 钻井记录 土层
	public static final String DIRT_DEPTH = "dirt_depth"; // 深度
	public static final String DIRT_NATURE = "dirt_nature"; // 主土层性质
	public static final String DIRT_DESCRIP = "dirt_descrip"; // 土层细节描述
	public static final String DIRT_EXTRA = "dirt_extra"; // 补充

	// ////////////////////////////////////////////////////////////////////
	// 钻井记录 PID
	public static final String PID_DEPTH = "pid_depth"; // 样品选取深度，选择
	public static final String PID_VALUE = "pid_value";// PID读数
	public static final String PID_MEMO = "pid_memo";// PID备注
	public static final String PID_EXTRA = "pid_extra";// PID补充
	// //////////////////////////////////////////////////////////////
	// 洗井记录
	public static final String WASHWELL_ID = "washwell_id"; // 井号
	public static final String WASHWELL_DATE = "washwell_date";// 洗井日期
	public static final String WASHWELL_TIME = "washwell_time";// 洗井时间
	public static final String WASHWELL_WEATHER = "washwell_weather";// 天气
	public static final String WASHWELL_WAY = "washwell_way";// 洗井方法
	public static final String WASHWELL_TEMP = "washwell_temp"; // 样品温度
	public static final String WASHWELL_COND = "washwell_cond";// 样品电导
	public static final String WASHWELL_PH = "washwell_ph";// 样品ph值
	public static final String WASHWELL_WATER = "washwell_water";// 样品水位
	public static final String WASHWELL_GAOCHENG = "washwell_gaocheng"; // 高程
	// ///////////////////////////////////////////////////////////////////////////////////////
	// add sug since 2012-12-21
	public static final String SUGGESTION_NAME = "sug_name";
	public static final String SUG_TABLE = "suggestion";
	public static final String SUG_TYPE = "sugg_type";
	public static final String SUG_TIME = "sugg_time";
	private static final String DATABASE_CREATE_SUG = "create table if not exists "
			+ SUG_TABLE
			+ "("
			+ COLUMN_ID
			+ " integer primary key autoincrement,"
			+ SUGGESTION_NAME
			+ " text," + SUG_TYPE + " text," + SUG_TIME + " long)";
	// end of sug
	private static final String DB_STORE_NAME = "myscene.db";
	private static final String DATABASE_CREATE = "create table if not exists "
			+ TABLE_NAME + "(" + COLUMN_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT," + PROJECT_ID
			+ " text not null," + PROJECT_NAME + " text not null,"
			+ CREATE_TIME + " text not null" + ")";
	private static final String DATABASE_CREATE_BASEINFO = "create table if not exists "
			+ TABLE_NAME_BASEINFO
			+ "("
			+ COLUMN_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ PROJECT_ID
			+ " text,"
			+ WELL_NUM
			+ " text,"
			+ BASE_WEATHER
			+ " text ,"
			+ BASE_DATE
			+ " text,"
			+ BASE_WORK
			+ " text,"
			+ BASE_DEPTH
			+ " text,"
			+ BASE_MRADIS
			+ " text,"
			+ BASE_RADIS
			+ " text,"
			+ BASE_WAY
			+ " text,"
			+ BASE_WRITER
			+ " text,"
			+ BASE_GPS
			+ " text,"
			+ BASE_FIRSTDEPTH + " text," + CREATE_TIME + " text" + ")";
	private static final String DATABASE_CREATE_DIRT = "create table if not exists "
			+ TABLE_NAME_DIRT
			+ "("
			+ COLUMN_ID
			+ " integer primary key autoincrement,"
			+ PROJECT_ID
			+ " text,"
			+ WELL_NUM
			+ " text,"
			+ DIRT_DEPTH
			+ " text ,"
			+ DIRT_NATURE
			+ " text,"
			+ DIRT_DESCRIP
			+ " text,"
			+ DIRT_EXTRA
			+ " text,"
			+ CREATE_TIME + " text not null" + ")";
	private static final String DATABASE_CREATE_PID = "create table if not exists "
			+ TABLE_NAME_PID
			+ "("
			+ COLUMN_ID
			+ " integer primary key autoincrement,"
			+ PROJECT_ID
			+ " text,"
			+ WELL_NUM
			+ " text,"
			+ PID_DEPTH
			+ " text,"
			+ PID_VALUE
			+ " text,"
			+ PID_MEMO
			+ " text,"
			+ PID_EXTRA
			+ " text,"
			+ CREATE_TIME
			+ " text not null" + ")";
	private static final String DATABASE_CREATE_WASHWELL = " create table if not exists "
			+ TABLE_NAME_WASHWELL
			+ "("
			+ COLUMN_ID
			+ " integer primary key autoincrement,"
			+ PROJECT_ID
			+ " text,"
			+ WELL_NUM
			+ " text not null,"
			+ WASHWELL_DATE
			+ " text,"
			+ WASHWELL_TIME
			+ " text,"
			+ WASHWELL_WEATHER
			+ " text,"
			+ WASHWELL_WAY
			+ " text,"
			+ WASHWELL_TEMP
			+ " text,"
			+ WASHWELL_COND
			+ " text,"
			+ WASHWELL_PH
			+ " text,"
			+ WASHWELL_WATER
			+ " text,"
			+ WASHWELL_GAOCHENG
			+ " text,"
			+ CREATE_TIME + " text" + ")";

	public DBHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	/**
	 * construct fan
	 * 
	 * @param context
	 */
	public DBHelper(Context context) {
		super(context, DB_STORE_NAME, null, DATABASE_VERSION);
	}

	public DBHelper(Context context, String name, CursorFactory factory,
			int version, DatabaseErrorHandler errorHandler) {
		super(context, name, factory, version, errorHandler);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(DATABASE_CREATE);
		db.execSQL(DATABASE_CREATE_BASEINFO);
		db.execSQL(DATABASE_CREATE_DIRT);
		db.execSQL(DATABASE_CREATE_PID);
		db.execSQL(DATABASE_CREATE_WASHWELL);
		db.execSQL(DATABASE_CREATE_SUG);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		// db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(db);
	}

	/**
	 * 根据输入内容模糊查询
	 * 
	 * @param name
	 * @return
	 */
	public Cursor query(String name) {
		SQLiteDatabase db = this.getReadableDatabase();
		return db.rawQuery("select * from " + SUG_TABLE + " where "+SUGGESTION_NAME+" like '%"
				+ name + "%'", null);
	}
}
