package com.myscene.data;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DAO {
	private SQLiteDatabase db;
	private DBHelper dbHelper;
	private static final String TAG = "DAO";
	private String[] allColunms = { DBHelper.COLUMN_ID,
	// DBHelper.PRO_WELL_DEPTH,
	// DBHelper.PRO_WELL_DIRT,
	// DBHelper.PRO_WELL_DIRTDETAIL,
	// DBHelper.PRO_WELL_EXTRA,
	// DBHelper.PRO_PID_DEPTH,
	// DBHelper.PRO_PID_READER,
	// DBHelper.PRO_PID_BAK,
	// DBHelper.PRO_PID_EXTRA,
	// DBHelper.PRO_WASHWELL_ID,
	// DBHelper.PRO_WASHWELL_DATE,
	// DBHelper.PRO_WASHWELL_TIME,
	// DBHelper.PRO_WASHWELL_WEATHER,
	// DBHelper.PRO_WASHWELL_WAY,
	// DBHelper.PRO_WASHWELL_TEMP,
	// DBHelper.PRO_WASHWELL_DIANDAO,
	// DBHelper.PRO_WASHWELL_PH,
	// DBHelper.PRO_WASHWELL_WATER,
	// DBHelper.PRO_WASHWELL_GAOCHENG
	};

	/** 项目表 */
	private String[] project = { DBHelper.COLUMN_ID, DBHelper.PROJECT_ID,
			DBHelper.PROJECT_NAME, DBHelper.CREATE_TIME };
	private String[] baseInfoShort = { DBHelper.COLUMN_ID, DBHelper.PROJECT_ID,
			DBHelper.CREATE_TIME, DBHelper.WELL_NUM };
	/**
	 * 基本信息表
	 */
	private String[] baseInfo = { DBHelper.COLUMN_ID, DBHelper.PROJECT_ID,
			DBHelper.CREATE_TIME, DBHelper.WELL_NUM, DBHelper.BASE_WEATHER,
			DBHelper.BASE_DATE, DBHelper.BASE_WORK, DBHelper.BASE_DEPTH,
			DBHelper.BASE_MRADIS, DBHelper.BASE_RADIS, DBHelper.BASE_WAY,
			DBHelper.BASE_WRITER, DBHelper.BASE_GPS, DBHelper.BASE_FIRSTDEPTH };

	/** 土层表 */
	private String[] dirt = { DBHelper.COLUMN_ID, DBHelper.WELL_NUM,
			DBHelper.PROJECT_ID,  DBHelper.DIRT_DEPTH,
			DBHelper.DIRT_NATURE, DBHelper.DIRT_DESCRIP, DBHelper.DIRT_EXTRA,DBHelper.CREATE_TIME

	};
    /** pid表*/
	private String[] pid = { DBHelper.COLUMN_ID, DBHelper.PROJECT_ID,DBHelper.WELL_NUM,
			DBHelper.PID_DEPTH, DBHelper.PID_VALUE, DBHelper.PID_MEMO,
			DBHelper.PID_EXTRA, DBHelper.CREATE_TIME };
	/** 洗井记录表 */
	private String[] washwell = { DBHelper.COLUMN_ID, DBHelper.PROJECT_ID,
			DBHelper.WELL_NUM, DBHelper.WASHWELL_DATE, DBHelper.WASHWELL_TIME,
			DBHelper.WASHWELL_WEATHER, DBHelper.WASHWELL_WAY,
			DBHelper.WASHWELL_TEMP, DBHelper.WASHWELL_COND,
			DBHelper.WASHWELL_PH, DBHelper.WASHWELL_WATER,
			DBHelper.WASHWELL_GAOCHENG, DBHelper.CREATE_TIME };

	private String[] sugs = {
	        DBHelper.COLUMN_ID,
	        DBHelper.SUGGESTION_NAME,
	        DBHelper.SUG_TYPE,
	        DBHelper.SUG_TIME
	};
	
	public void close() {
		if (db != null)
			db.close();
	}

	public DAO(Context context) {
		dbHelper = new DBHelper(context);
	}

	public void open() {
		try {
		    if( null == db)
		        db = dbHelper.getWritableDatabase();
		} catch (SQLException err) {
			err.printStackTrace();
		} catch (Exception err) {
			err.printStackTrace();
		}
	}

	/**
	 * 检测是否在sug中
	 * @param sugg_name
	 * @return
	 */
	public boolean checkInSugg(String sugg_name){
	    Cursor cursor = null;
	    try{
	        if(null == db){
	            open();
	        }
	        db.beginTransaction();
	        String sql = "select count(*) from "+DBHelper.SUG_TABLE + " where "+DBHelper.SUGGESTION_NAME+ " =?";
	        cursor = db.rawQuery(sql,new String[] {sugg_name});
	        if(cursor !=null && cursor.getCount() > 0 )
	            return true;
	    } catch (Exception err) {
	        err.printStackTrace();
	    }finally{
	        if (null != cursor){
	            cursor.close();
	        }
	    }
	    db.setTransactionSuccessful();
        db.endTransaction();
	    return false;
	}
	
	/**
	 * 获取 sug 的建议列表
	 * @param sugg
	 * @return
	 */
	public List<Suggestion> getAllSugs(String sugg) {
	    List<Suggestion> result = null;
	    Cursor cursor = null;
	    if ( null == db ) {
	        open();
	    }
	    db.beginTransaction();
	    cursor = db.query(DBHelper.SUG_TABLE, sugs, DBHelper.SUGGESTION_NAME+" like '%"+sugg+"%' order by "+DBHelper.SUG_TIME +" desc",
                null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        else
            return result;
        result = new ArrayList<Suggestion>();
        while (!cursor.isAfterLast()) {
            Suggestion sug = new Suggestion();
            sug.set_id(cursor.getInt(0));
            sug.setSug_name(cursor.getString(1));
            sug.setSug_type(cursor.getString(2));
            sug.setSug_time(cursor.getLong(3));
            cursor.moveToNext();
            result.add(sug);
        }
        db.setTransactionSuccessful();
        db.endTransaction();
	    return result;        
	}
	
	/**
	 * 内容重复的话，那么就更新 时间
	 * @param sug_name
	 * @param sug_time
	 */
	public void updateSugTime(String sug_name,long sug_time) {
	    try {
            if (db == null)
                open();
            db.beginTransaction();
            String sql = "update "+DBHelper.SUG_TABLE + " set "+DBHelper.SUG_TIME +"='"+sug_time+"' where "+DBHelper.SUGGESTION_NAME +" ='"+sug_name+"'";
            db.rawQuery(sql, null);
        } catch (Exception err) {
            err.printStackTrace();
        }finally{
            db.setTransactionSuccessful();
            db.endTransaction();
        }
	}
	
	/**
	 * 清空输入历史
	 */
	public void clearSug(){
	    try {
            if (db == null)
                open();
            db.beginTransaction();
            String sql = "delete from "+DBHelper.SUG_TABLE;
            db.rawQuery(sql, null);
        } catch (Exception err) {
            err.printStackTrace();
        }finally{
            db.setTransactionSuccessful();
            db.endTransaction();
        }
	}
	/**
	 * 插入到 sug 表
	 * @param sug
	 * @return
	 */
	public boolean insertSug(Suggestion sug){
	    Util.Print("insert sug", "sug");
	    try {
            if (db == null)
                open();
            ContentValues values = new ContentValues();
            values.put(DBHelper.SUGGESTION_NAME, sug.getSug_name());
            values.put(DBHelper.SUG_TYPE, sug.getSug_type());
            values.put(DBHelper.SUG_TIME, sug.getSug_time());
            long insertId = db.insert(DBHelper.SUG_TABLE, null, values);
            if (-1 == insertId)
                return false;
        } catch (Exception err) {
            err.printStackTrace();
            return false;
        }
        return true;
	}
	/**
	 * 检测数据库中是否已存在该项目
	 * 
	 * @param projectId
	 * @param name
	 * @return
	 */
	public boolean checkInProject(String projectId, String name) {
		try {
			if (db == null)
				open();
			String selection = "select project_id from project where project_id=? or project_name=?";
			Cursor cursor = db.rawQuery(selection, new String[] { projectId,
					name });
			if (cursor.getCount() > 0) {
				cursor.close();
				return true;
			}
			cursor.close();
		} catch (Exception err) {
			err.printStackTrace();
			return false;
		}
		return false;
	}

	
	
	/**
	 * update by colunm id
	 * 
	 * @param _id
	 * @param project
	 *            using this method need update all other data with project_id
	 */
	public void updateProject(int _id, Project project) {
		System.out.println("_id:" + _id);
		ContentValues values = new ContentValues();
		values.put(DBHelper.PROJECT_ID, project.getProject_id());
		values.put(DBHelper.PROJECT_NAME, project.getProject_name());
		values.put(DBHelper.CREATE_TIME, project.getProject_name());
		try {
			if (db == null)
				open();
			Project temp = getProjectByColunmId(_id);
			int rows = db.update(DBHelper.TABLE_NAME, values, "_id=?",
					new String[] { String.valueOf(_id).trim() });
			System.out.println("update rows:" + rows);
			String updateBaseInfo="update baseinfo set project_id='"+project.getProject_id()+"' where project_id='"+temp.getProject_id()+"'";
			String updateWashWell="update washwell set project_id='"+project.getProject_id()+"' where project_id='"+temp.getProject_id()+"'";
			String updatePid="update pid set project_id='"+project.getProject_id()+"' where project_id='"+temp.getProject_id()+"'";
			String updateDirt="update dirt set project_id='"+project.getProject_id()+"' where project_id='"+temp.getProject_id()+"'";
			db.execSQL(updateBaseInfo);
			db.execSQL(updateWashWell);
			db.execSQL(updatePid);
			db.execSQL(updateDirt);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	public void updateProject(String  projectId, Project project) {
		ContentValues values = new ContentValues();
		System.out.println(project.toString());
		System.out.println(projectId);
		values.put(DBHelper.PROJECT_ID, project.getProject_id());
		values.put(DBHelper.PROJECT_NAME, project.getProject_name());
		values.put(DBHelper.CREATE_TIME, project.getProject_name());
		try {
			if (db == null)
				open();
			int rows = db.update(DBHelper.TABLE_NAME, values, "project_id=?",
					new String[] { projectId.trim() });
			System.out.println("update rows:" + rows);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * UPDATE PROJECT FEATURE
	 * 
	 * @param projectId
	 * @param name
	 */
	public void updateProject(String colunmId, String projectId, String name,String createtime) {
		if(null == db)
			open();
		Util.Print("db = null", ""+(null==db));
		Util.Print("colunmid", colunmId);
		Util.Print("project id", projectId);
		Util.Print("name", name);
		Util.Print("createtime", createtime);
		ContentValues values = new ContentValues();
		values.put(DBHelper.PROJECT_ID, projectId);
		values.put(DBHelper.PROJECT_NAME, name);
		values.put(DBHelper.CREATE_TIME, createtime);
		try {
			db.update(DBHelper.TABLE_NAME, values, "_id=?",
					new String[] { colunmId.trim() });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	
	/**
	 * PROEJCT DELETE BY ID
	 * 
	 * @param colunmId
	 */
	public void deleteProject(String colunmId) {
		try {
			System.out.println("删除id :" + colunmId);
			db.execSQL("delete from project where _id='" + colunmId.trim()
					+ "'");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 删除 项目中的 井信息
	 * @param project_id
	 * @param well_num
	 * @return
	 */
	public boolean deleteWellData(final String project_id,final String well_num) {
	    db.beginTransaction();
        try {
            db.execSQL("delete from baseinfo where project_id='" + project_id.trim()
                    + "' and "+DBHelper.WELL_NUM +"='"+well_num+"'");
            db.execSQL("delete from washwell where project_id='" + project_id.trim()
                    + "' and "+DBHelper.WELL_NUM +"='"+well_num+"'");
            db.execSQL("delete from dirt where project_id='" + project_id.trim()
                    + "' and "+DBHelper.WELL_NUM +"='"+well_num+"'");
            db.execSQL("delete from pid where project_id='" + project_id
                    + "' and "+DBHelper.WELL_NUM +"='"+well_num+"'");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }finally{
        db.setTransactionSuccessful();
        db.endTransaction();
        }
        return true;
	}
	
	@SuppressWarnings("static-access")
	public boolean deleteProjectData(final Context context,final String project_id) {
		if(null == project_id || "".equals(project_id)) {
			Util.Print("DAO", "PROJECT ID IS NULL");
			return false;
		}
		else
			Util.Print("DAO", "PROJECT ID:"+project_id);
//		db.openDatabase(context.getFilesDir().get, null, 0);
//		db.openOrCreateDatabase(context.getFilesDir().getParent()+"/database/myscene.db", null);
		db.beginTransaction();
		try {
			db.execSQL("delete from project where project_id='" + project_id.trim()
					+ "'");
			db.execSQL("delete from baseinfo where project_id='" + project_id.trim()
					+ "'");
			db.execSQL("delete from washwell where project_id='" + project_id.trim()
					+ "'");
			db.execSQL("delete from dirt where project_id='" + project_id.trim()
					+ "'");
			db.execSQL("delete from pid where project_id='" + project_id
					+ "'");
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}finally{
		db.setTransactionSuccessful();
		db.endTransaction();
		}
		return true;
	}
	/**
	 * 删除此项目的所有数据，上传时，或者长按 删除
	 * @param project_id
	 */
	public boolean deleteProjectData(final String project_id) {
		if(null == project_id || "".equals(project_id)) {
			Util.Print("DAO", "PROJECT ID IS NULL");
			return false;
		}
		else
			Util.Print("DAO", "PROJECT ID:"+project_id);
//		db.openDatabase(, null, 0);
		if (db == null)
			open();
		db.beginTransaction();
		try {
			db.execSQL("delete from project where project_id='" + project_id.trim()
					+ "'");
			db.execSQL("delete from baseinfo where project_id='" + project_id.trim()
					+ "'");
			db.execSQL("delete from washwell where project_id='" + project_id.trim()
					+ "'");
			db.execSQL("delete from dirt where project_id='" + project_id.trim()
					+ "'");
			db.execSQL("delete from pid where project_id='" + project_id
					+ "'");
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		db.endTransaction();
		return true;
	}
	
	/**
	 * 删除洗井记录
	 * 
	 * @param _id
	 */
	public void deleteWashWell(int _id) {
		try {
			System.out.println("删除id :" + _id);
			db.execSQL("delete from washwell where _id='" + _id + "'");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void deletePid(int _id) {
		try {
			System.out.println("删除id :" + _id);
			db.execSQL("delete from pid where _id='" + _id + "'");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void deleteDirt(int _id) {
		try {
			System.out.println("删除id :" + _id);
			db.execSQL("delete from dirt where _id='" + _id + "'");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void deleteBaseInfo(int _id) {
		try {
			System.out.println("删除id :" + _id);
			db.execSQL("delete from baseinfo where _id='" + _id + "'");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param dirt
	 * @return
	 */
	public boolean insertDirt(Dirt dirt) {
		Util.Print("DAO", "INSERT DIRT");
		try {
			if (db == null)
				open();
			ContentValues values = new ContentValues();
			values.put(DBHelper.PROJECT_ID, dirt.getProject_id());
			values.put(DBHelper.WELL_NUM, dirt.getWell_num());
			values.put(DBHelper.DIRT_DEPTH, dirt.getDirt_depth());
			values.put(DBHelper.DIRT_DESCRIP, dirt.getDirt_descrip());
			values.put(DBHelper.DIRT_EXTRA, dirt.getDirt_extra());
			values.put(DBHelper.DIRT_NATURE, dirt.getDirt_nature());
			values.put(DBHelper.CREATE_TIME, dirt.getCreate_time());
			long insertId = db.insert(DBHelper.TABLE_NAME_DIRT, null, values);
			if (-1 == insertId)
				return false;
		} catch (Exception err) {
			err.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 
	 * @param pid
	 * @return
	 */
	public boolean insertPid(Pid pid) {
		try {
			if (db == null)
				open();
			ContentValues values = new ContentValues();
			values.put(DBHelper.PROJECT_ID, pid.getProject_id());
			values.put(DBHelper.WELL_NUM, pid.getWell_num());
			values.put(DBHelper.PID_DEPTH, pid.getPid_depth());
			values.put(DBHelper.PID_EXTRA, pid.getPid_extra());
			values.put(DBHelper.PID_MEMO, pid.getPid_memo());
			values.put(DBHelper.PID_VALUE, pid.getPid_value());
			values.put(DBHelper.CREATE_TIME, pid.getCreate_time());
			long insertId = db.insert(DBHelper.TABLE_NAME_PID, null, values);
			if (-1 == insertId)
				return false;
		} catch (Exception err) {
			err.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 创建项目
	 * 
	 * @param project
	 * @return
	 */
	public boolean insertPorject(Project project) {
		try {
			if (db == null)
				open();
			ContentValues values = new ContentValues();
			values.put(DBHelper.PROJECT_ID, project.getProject_id());
			values.put(DBHelper.PROJECT_NAME, project.getProject_name());
			values.put(DBHelper.CREATE_TIME, project.getCreate_time());
			long insertId = db.insert(DBHelper.TABLE_NAME, null, values);
			if (-1 == insertId)
				return false;

		} catch (Exception err) {
			err.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * get project by PROECT id
	 * 
	 * @param colounmId
	 * @return
	 */
	public Project getProjectByProjectId(String projectId) {
		Project p = null;
		try {
			Cursor cursor = null;
			if (db == null)
				open();
			try {
				cursor = db.query(DBHelper.TABLE_NAME, project, DBHelper.PROJECT_ID+"=?",
						new String[] { projectId }, null, null, null);
				if (cursor != null)
					cursor.moveToFirst();
				else
					return null;
				while (!cursor.isAfterLast()) {
					Project project = new Project();
					project.set_id(cursor.getInt(0));
					project.setProject_id(cursor.getString(1));
					project.setProject_name(cursor.getString(2));
					project.setCreate_time(cursor.getString(3));
					p = project;
					cursor.moveToNext();
				}
			} catch (Exception err) {
				err.printStackTrace();
			} finally {
				if (null != cursor)
					cursor.close();
			}
		} catch (Exception err) {
			err.printStackTrace();
		}
		return p;
	}

	/**
	 * get project by colounm id
	 * 
	 * @param colounmId
	 * @return
	 */
	public Project getProjectByColunmId(int colounmId) {
		Project p = null;
		try {
			Cursor cursor = null;
			if (db == null)
				open();
			try {
				cursor = db.query(DBHelper.TABLE_NAME, project, "_id=?",
						new String[] { "" + colounmId }, null, null, null);
				if (cursor != null)
					cursor.moveToFirst();
				else
					return null;
				while (!cursor.isAfterLast()) {
					Project project = new Project();
					project.set_id(cursor.getInt(0));
					project.setProject_id(cursor.getString(1));
					project.setProject_name(cursor.getString(2));
					project.setCreate_time(cursor.getString(3));
					p = project;
					cursor.moveToNext();
				}
			} catch (Exception err) {
				err.printStackTrace();
			} finally {
				if (null != cursor)
					cursor.close();
			}
		} catch (Exception err) {
			err.printStackTrace();
		}
		return p;
	}
	/**
	 * 获取 所有土层记录
	 * 
	 * @param project_id
	 * @param well_num
	 * @return
	 */
	public List<Dirt> getAllDirts(String project_id, String well_num) {
		if (well_num == null || "".equals(well_num))
			return null;
		List<Dirt> dirts = new ArrayList<Dirt>();
		try {
			Cursor cursor = null;
			if (db == null)
				open();
			try {
				cursor = db.query(DBHelper.TABLE_NAME_DIRT, dirt,
						DBHelper.PROJECT_ID + "=? and " + DBHelper.WELL_NUM
								+ "=?", new String[] { project_id, well_num },
						null, null, null);
				if (cursor != null)
					cursor.moveToFirst();
				else
					return null;
				while (!cursor.isAfterLast()) {
					Dirt dirt = new Dirt();
					dirt.set_id(cursor.getInt(0));
					dirt.setWell_num(cursor.getString(1));
					dirt.setProject_id(cursor.getString(2));
					dirt.setDirt_depth(cursor.getString(3));
					dirt.setDirt_nature(cursor.getString(4));
					dirt.setDirt_descrip(cursor.getString(5));
					dirt.setDirt_extra(cursor.getString(6));
					dirt.setCreate_time(cursor.getString(7));
					dirts.add(dirt);
					cursor.moveToNext();
				}
			} catch (Exception err) {
				err.printStackTrace();
			} finally {
				if (null != cursor)
					cursor.close();
			}
		} catch (Exception err) {
			err.printStackTrace();
		}
		return dirts;
	}

	/**
	 * 读取 井号 和project id 唯一的 pid读数
	 * 
	 * @param project_id
	 * @param well_num
	 * @return
	 */
	public List<Pid> getAllPids(String project_id, String well_num) {
		if (well_num == null || "".equals(well_num))
			return null;
		List<Pid> pids = new ArrayList<Pid>();
		try {
			Cursor cursor = null;
			if (db == null)
				open();
			try {
				cursor = db.query(DBHelper.TABLE_NAME_PID, pid,
						DBHelper.PROJECT_ID + "=? and " + DBHelper.WELL_NUM
								+ "=?", new String[] { project_id, well_num },
						null, null, null);
				if (cursor != null)
					cursor.moveToFirst();
				else
					return null;
				while (!cursor.isAfterLast()) {
					Pid pid = new Pid();
					pid.set_id(cursor.getInt(0));
					pid.setProject_id(cursor.getString(1));
					pid.setWell_num(cursor.getString(2));
					pid.setPid_depth(cursor.getString(3));
					pid.setPid_value(cursor.getString(4));
					pid.setPid_memo(cursor.getString(5));
					pid.setPid_extra(cursor.getString(6));
					pid.setCreate_time(cursor.getString(7));
					pids.add(pid);
					Util.Print("pid", pid.toString());
					cursor.moveToNext();
				}
			} catch (Exception err) {
				err.printStackTrace();
			} finally {
				if (null != cursor)
					cursor.close();
			}
		} catch (Exception err) {
			err.printStackTrace();
		}
		return pids;
	}

	/**
	 * 获取所有项目表
	 * 
	 * @return
	 */
	public List<Project> getAllProject() {
		List<Project> pros = new ArrayList<Project>();
		try {
			Cursor cursor = null;
			if (db == null)
				open();
			try {
				cursor = db.query(DBHelper.TABLE_NAME, project, null, null,
						null, null, null);
				if (cursor != null)
					cursor.moveToFirst();
				else
					return null;
				while (!cursor.isAfterLast()) {
					Project project = new Project();
					project.set_id(cursor.getInt(0));
					project.setProject_id(cursor.getString(1));
					project.setProject_name(cursor.getString(2));
					project.setCreate_time(cursor.getString(3));
					pros.add(project);
					cursor.moveToNext();
				}
			} catch (Exception err) {
				err.printStackTrace();
			} finally {
				if (null != cursor)
					cursor.close();
			}
		} catch (Exception err) {
			err.printStackTrace();
		}

		return pros;
	}

	/**
	 * 获取所以项目简略表
	 * 
	 * @return
	 */
	public List<BaseInfo> getAllBaseInfosShort(String projectId) {
		List<BaseInfo> pros = new ArrayList<BaseInfo>();
		System.out.println("get all base info short");
		System.out.println("project id:"+projectId);
		try {
			Cursor cursor = null;
			if (db == null)
				open();
			try {
				cursor = db.query(DBHelper.TABLE_NAME_BASEINFO, baseInfoShort,
						DBHelper.PROJECT_ID + "=?", new String[] { projectId },
						null, null, null);
				if (cursor != null)
					cursor.moveToFirst();
				else
					return null;
				while (!cursor.isAfterLast()) {
					BaseInfo baseinfo = new BaseInfo();
					baseinfo.set_id(cursor.getInt(0));
					baseinfo.setProject_id(cursor.getString(1));
					baseinfo.setCreate_time(cursor.getString(2));
					baseinfo.setWell_num(cursor.getString(3));
					pros.add(baseinfo);
					cursor.moveToNext();
				}
			} catch (Exception err) {
				err.printStackTrace();
			} finally {
				if (null != cursor)
					cursor.close();
			}
		} catch (Exception err) {
			err.printStackTrace();
		}
		System.out.println("local base num:"+pros.size());
		return pros;
	}

	/**
	 * 读取洗井信息
	 * 
	 * @param projectId
	 * @param well_num
	 * @return
	 */
	public Washwell getWashWell(String projectId, String well_num) {
		if (well_num == null)
			return null;
		System.out.println("well num:" + well_num);
		Washwell wash = null;
		try {
			Cursor cursor = null;
			if (db == null)
				open();
			try {
				cursor = db.query(DBHelper.TABLE_NAME_WASHWELL, washwell,
						DBHelper.PROJECT_ID + "=? and " + DBHelper.WELL_NUM
								+ "=?", new String[] { projectId, well_num },
						null, null, null);
				if (cursor != null)
					cursor.moveToFirst();
				else
					return null;
				System.out.println("count:" + cursor.getCount());
				while (!cursor.isAfterLast()) {
					wash = new Washwell();
					wash.set_id(cursor.getInt(0));
					wash.setProject_id(cursor.getString(1));
					wash.setWell_num(cursor.getString(2));
					wash.setWw_date(cursor.getString(3));
					wash.setWw_time(cursor.getString(4));
					wash.setWw_weather(cursor.getString(5));
					wash.setWw_method(cursor.getString(6));
					wash.setWw_temp(cursor.getString(7));
					wash.setWw_cond(cursor.getString(8));
					wash.setWw_ph(cursor.getString(9));
					wash.setWw_water(cursor.getString(10));
					wash.setWw_gaocheng(cursor.getString(11));
					wash.setCreate_time(cursor.getString(12));
					return wash;
				}
			} catch (Exception err) {
				err.printStackTrace();
			} finally {
				if (null != cursor)
					cursor.close();
			}
		} catch (Exception err) {
			err.printStackTrace();
		}
		return wash;
	}

	/**
	 * 读取所有洗井信息
	 * @param projectId
	 * @return
	 */
	public List<Washwell> getWashWellByProjectId(String projectId) {
		System.out.println("get wash well lists");
		List<Washwell> washList = new ArrayList<Washwell>();
		try {
			Cursor cursor = null;
			if (db == null)
				open();
			try {
				cursor = db.query(DBHelper.TABLE_NAME_WASHWELL, washwell,
						DBHelper.PROJECT_ID + "=?", new String[] { projectId},
						null, null, null);
				if (cursor != null)
					cursor.moveToFirst();
				else
					return null;
				System.out.println("count:" + cursor.getCount());
				while (!cursor.isAfterLast()) {
					Washwell wash = new Washwell();
					wash.set_id(cursor.getInt(0));
					wash.setProject_id(cursor.getString(1));
					wash.setWell_num(cursor.getString(2));
					wash.setWw_date(cursor.getString(3));
					wash.setWw_time(cursor.getString(4));
					wash.setWw_weather(cursor.getString(5));
					wash.setWw_method(cursor.getString(6));
					wash.setWw_temp(cursor.getString(7));
					wash.setWw_cond(cursor.getString(8));
					wash.setWw_ph(cursor.getString(9));
					wash.setWw_water(cursor.getString(10));
					wash.setWw_gaocheng(cursor.getString(11));
					wash.setCreate_time(cursor.getString(12));
					washList.add(wash);
					cursor.moveToNext();
				}
			} catch (Exception err) {
				err.printStackTrace();
			} finally {
				if (null != cursor)
					cursor.close();
			}
		} catch (Exception err) {
			err.printStackTrace();
		}
		return washList;
	}
	/**
	 * 获取基本信息
	 * 
	 * @param projectId
	 * @param _id
	 * @return
	 */
	public List<BaseInfo> getBaseInfoByProjectId(String projectId) {
		System.out.println("get all base info list");
		List<BaseInfo> baseList = new ArrayList<BaseInfo>();
		try {
			Cursor cursor = null;
			if (db == null)
				open();
			try {
				cursor = db.query(DBHelper.TABLE_NAME_BASEINFO, baseInfo,
						DBHelper.PROJECT_ID + "=?", new String[] { projectId}, null,
					null, null);
				if (cursor != null)
					cursor.moveToFirst();
				else
					return null;
				while (!cursor.isAfterLast()) {
					BaseInfo baseinfo = new BaseInfo();
					baseinfo.set_id(cursor.getInt(0));
					baseinfo.setProject_id(cursor.getString(1));
					baseinfo.setCreate_time(cursor.getString(2));
					baseinfo.setWell_num(cursor.getString(3));
					baseinfo.setBase_weather(cursor.getString(4));
					baseinfo.setBase_date(cursor.getString(5));
					baseinfo.setBase_work(cursor.getString(6));
					baseinfo.setBase_depth(cursor.getString(7));
					baseinfo.setBase_mradis(cursor.getString(8));
					baseinfo.setBase_radis(cursor.getString(9));
					baseinfo.setBase_way(cursor.getString(10));
					baseinfo.setBase_writer(cursor.getString(11));
					baseinfo.setBase_gps(cursor.getString(12));
					baseinfo.setBase_first_depth(cursor.getString(13));
					baseList.add(baseinfo);
					cursor.moveToNext();
				}
			} catch (Exception err) {
				err.printStackTrace();
			} finally {
				if (null != cursor)
					cursor.close();
			}
		} catch (Exception err) {
			err.printStackTrace();
		}
		return baseList;
	}
	
	/**
	 * 获取基本信息
	 * 
	 * @param projectId
	 * @param _id
	 * @return
	 */
	public BaseInfo getBaseInfo(String projectId, String _id) {
		if (null == _id)
			return null;
		BaseInfo baseinfo = null;
		try {
			Cursor cursor = null;
			if (db == null)
				open();
			try {
				cursor = db.query(DBHelper.TABLE_NAME_BASEINFO, baseInfo,
						DBHelper.PROJECT_ID + "=? and " + DBHelper.COLUMN_ID
								+ "=?", new String[] { projectId, _id }, null,
						null, null);
				if (cursor != null)
					cursor.moveToFirst();
				else
					return null;
				while (!cursor.isAfterLast()) {
					baseinfo = new BaseInfo();
					baseinfo.set_id(cursor.getInt(0));
					baseinfo.setProject_id(cursor.getString(1));
					baseinfo.setCreate_time(cursor.getString(2));
					baseinfo.setWell_num(cursor.getString(3));
					baseinfo.setBase_weather(cursor.getString(4));
					baseinfo.setBase_date(cursor.getString(5));
					baseinfo.setBase_work(cursor.getString(6));
					baseinfo.setBase_depth(cursor.getString(7));
					baseinfo.setBase_mradis(cursor.getString(8));
					baseinfo.setBase_radis(cursor.getString(9));
					baseinfo.setBase_way(cursor.getString(10));
					baseinfo.setBase_writer(cursor.getString(11));
					baseinfo.setBase_gps(cursor.getString(12));
					baseinfo.setBase_first_depth(cursor.getString(13));
					return baseinfo;
				}
			} catch (Exception err) {
				err.printStackTrace();
			} finally {
				if (null != cursor)
					cursor.close();
			}
		} catch (Exception err) {
			err.printStackTrace();
		}

		return baseinfo;
	}

	/**
	 * 獲取土层信息
	 * @param projectId
	 * @return
	 */
	public List<Dirt> getDirtByProjectId(String projectId) {
		if (null == projectId)
			return null;
		List<Dirt> dirts = new ArrayList<Dirt>();
		try {
			Cursor cursor = null;
			if (db == null)
				open();
			try {
				cursor = db.query(DBHelper.TABLE_NAME_DIRT, dirt, DBHelper.PROJECT_ID+ "=?", new String[] { projectId }, null, null, null);
				if (cursor != null)
					cursor.moveToFirst();
				else
					return null;
				while (!cursor.isAfterLast()) {
					Dirt d = new Dirt();
					d.set_id(cursor.getInt(0));
					d.setWell_num(cursor.getString(1));
					d.setProject_id(cursor.getString(2));
					d.setDirt_depth(cursor.getString(3));
					d.setDirt_nature(cursor.getString(4));
					d.setDirt_descrip(cursor.getString(5));
					d.setDirt_extra(cursor.getString(6));
					d.setCreate_time(cursor.getString(7));
					dirts.add(d);
					cursor.moveToNext();
				}
			} catch (Exception err) {
				err.printStackTrace();
			} finally {
				if (null != cursor)
					cursor.close();
			}
		} catch (Exception err) {
			err.printStackTrace();
		}

		return dirts;
	}

	/**
	 * 土层信息
	 * @param _id
	 * @return
	 */
	public Dirt getDirt(String _id) {
		if (null == _id)
			return null;
		Dirt d = null;
		try {
			Cursor cursor = null;
			if (db == null)
				open();
			try {
				cursor = db.query(DBHelper.TABLE_NAME_DIRT, dirt, DBHelper.COLUMN_ID+ "=?", new String[] { _id }, null, null, null);
				if (cursor != null)
					cursor.moveToFirst();
				else
					return null;
				while (!cursor.isAfterLast()) {
					d = new Dirt();
					d.set_id(cursor.getInt(0));
					d.setWell_num(cursor.getString(1));
					d.setProject_id(cursor.getString(2));
					d.setDirt_depth(cursor.getString(3));
					d.setDirt_nature(cursor.getString(4));
					d.setDirt_descrip(cursor.getString(5));
					d.setDirt_extra(cursor.getString(6));
					d.setCreate_time(cursor.getString(7));
					return d;
				}
			} catch (Exception err) {
				err.printStackTrace();
			} finally {
				if (null != cursor)
					cursor.close();
			}
		} catch (Exception err) {
			err.printStackTrace();
		}

		return d;
	}

	/**
	 * @param _id
	 * @return
	 */
	public List<Pid> getPidByProjectId(String projectId) {
		if (null == projectId)
			return null;
		List<Pid> pids = new ArrayList<Pid>();
		try {
			Cursor cursor = null;
			if (db == null)
				open();
			try {
				cursor = db.query(DBHelper.TABLE_NAME_PID, pid, DBHelper.PROJECT_ID+ "=?", new String[] { projectId }, null, null, null);
				if (cursor != null)
					cursor.moveToFirst();
				else
					return null;
				while (!cursor.isAfterLast()) {
					Pid d = new Pid();
					d.set_id(cursor.getInt(0));
					d.setProject_id(cursor.getString(1));
					d.setWell_num(cursor.getString(2));
					d.setPid_depth(cursor.getString(3));
					d.setPid_value(cursor.getString(4));
					d.setPid_memo(cursor.getString(5));
					d.setPid_extra(cursor.getString(6));
					d.setCreate_time(cursor.getString(7));
					pids.add(d);
					cursor.moveToNext();
				}
			} catch (Exception err) {
				err.printStackTrace();
			} finally {
				if (null != cursor)
					cursor.close();
			}
		} catch (Exception err) {
			err.printStackTrace();
		}
		return pids;
	}

	/**
	 * @param _id
	 * @return
	 */
	public Pid getPid(String _id) {
		if (null == _id)
			return null;
		Pid d = null;
		try {
			Cursor cursor = null;
			if (db == null)
				open();
			try {
				cursor = db.query(DBHelper.TABLE_NAME_PID, pid, DBHelper.COLUMN_ID+ "=?", new String[] { _id }, null, null, null);
				if (cursor != null)
					cursor.moveToFirst();
				else
					return null;
				while (!cursor.isAfterLast()) {
					d = new Pid();
					d.set_id(cursor.getInt(0));
					d.setProject_id(cursor.getString(1));
					d.setWell_num(cursor.getString(2));
					d.setPid_depth(cursor.getString(3));
					d.setPid_value(cursor.getString(4));
					d.setPid_memo(cursor.getString(5));
					d.setPid_extra(cursor.getString(6));
					d.setCreate_time(cursor.getString(7));
					return d;
				}
			} catch (Exception err) {
				err.printStackTrace();
			} finally {
				if (null != cursor)
					cursor.close();
			}
		} catch (Exception err) {
			err.printStackTrace();
		}

		return d;
	}
	
	/**
	 * 更新洗井记录
	 * 
	 * @param _id
	 * @param base
	 * @return
	 */
	public boolean updateWashWell(int _id, Washwell washwell) {
		System.out.println("update washwell");
		System.out.println("update id:" + _id);
		System.out.println("well num:" + washwell.getWell_num());
		System.out.println(washwell.toString());
		int count;
		ContentValues values = new ContentValues();
		values.put(DBHelper.COLUMN_ID, washwell.get_id());
		values.put(DBHelper.PROJECT_ID, washwell.getProject_id());
		values.put(DBHelper.WASHWELL_COND, washwell.getWw_cond());
		values.put(DBHelper.WASHWELL_DATE, washwell.getWw_date());
		values.put(DBHelper.WASHWELL_GAOCHENG, washwell.getWw_gaocheng());
		values.put(DBHelper.WELL_NUM, washwell.getWell_num());
		values.put(DBHelper.WASHWELL_PH, washwell.getWw_ph());
		values.put(DBHelper.WASHWELL_TEMP, washwell.getWw_temp());
		values.put(DBHelper.WASHWELL_WATER, washwell.getWw_water());
		values.put(DBHelper.WASHWELL_WAY, washwell.getWw_method());
		values.put(DBHelper.WASHWELL_WEATHER, washwell.getWw_weather());
		values.put(DBHelper.CREATE_TIME, Util.getCurrentTime());
		values.put(DBHelper.WASHWELL_TIME, washwell.getWw_time());
		try {
			count = db.update(DBHelper.TABLE_NAME_WASHWELL, values, "_id=?",
					new String[] { String.valueOf(_id).trim() });
			System.out.println("update rows :" + count);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		if (count > 0)
			return true;
		return false;
	}

	/**
	 * 更新基本信息
	 * 
	 * @param _id
	 * @param base
	 * @param changed identify well_num is changed or not
	 * @return
	 */
	public boolean updateBaseInfo(int _id, BaseInfo base,boolean changed,String old_well_num) {
		System.out.println("update base info");
		System.out.println("update id:" + _id);
		System.out.println("well num:" + base.getWell_num());
		int count;
		ContentValues values = new ContentValues();
		values.put(DBHelper.CREATE_TIME, base.getBase_date());
		values.put(DBHelper.PROJECT_ID, base.getProject_id());
		values.put(DBHelper.WELL_NUM, base.getWell_num());
		values.put(DBHelper.BASE_WEATHER, base.getBase_weather());
		values.put(DBHelper.BASE_DATE, base.getBase_date());
		values.put(DBHelper.BASE_WORK, base.getBase_work());
		values.put(DBHelper.BASE_DEPTH, base.getBase_depth());
		values.put(DBHelper.BASE_MRADIS, base.getBase_mradis());
		values.put(DBHelper.BASE_RADIS, base.getBase_radis());
		values.put(DBHelper.BASE_WAY, base.getBase_way());
		values.put(DBHelper.BASE_WRITER, base.getBase_writer());
		values.put(DBHelper.BASE_GPS, base.getBase_gps());
		values.put(DBHelper.BASE_FIRSTDEPTH, base.getBase_first_depth());
		try {
			if(!base.getBase_work().equals("")){
			    if(checkInSugg(base.getBase_work())){
			        Suggestion sug = new Suggestion();
			        sug.setSug_name(base.getBase_work());
			        sug.setSug_type("0");
			        sug.setSug_time(System.currentTimeMillis());
			        insertSug(sug);
			    }
			}
			count = db.update(DBHelper.TABLE_NAME_BASEINFO, values, "_id=?",
					new String[] { String.valueOf(_id).trim() });
			System.out.println("update rows :" + count);
			if(changed){
				String updateDirt="update dirt set well_num='"+base.getWell_num()+"' where well_num='"+old_well_num+"'";
				String updatePid="update pid set well_num='"+base.getWell_num()+"' where well_num='"+old_well_num+"'";
				String updateWashWell="update washwell set well_num='"+base.getWell_num()+"' where well_num='"+old_well_num+"'";
				db.execSQL(updateDirt);
				db.execSQL(updatePid);
				db.execSQL(updateWashWell);
			}
//			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		if (count > 0)
			return true;
		return false;
	}

	/**
	 * 更新土层信息
	 * @param _id
	 * @param dirt
	 * @return
	 */
	public boolean updateDirt(int _id, Dirt dirt) {
		System.out.println("updateDirt");
		System.out.println("update id:" + _id);
		System.out.println(dirt.toString());
		int count;
		ContentValues values = new ContentValues();
		values.put(DBHelper.DIRT_DEPTH, dirt.getDirt_depth());
		values.put(DBHelper.DIRT_DESCRIP, dirt.getDirt_descrip());
		values.put(DBHelper.DIRT_EXTRA, dirt.getDirt_extra());
		values.put(DBHelper.DIRT_NATURE, dirt.getDirt_nature());
		values.put(DBHelper.CREATE_TIME, dirt.getCreate_time());
		try {
			count = db.update(DBHelper.TABLE_NAME_DIRT, values, "_id=?",
					new String[] { String.valueOf(_id).trim() });
			System.out.println("update rows :" + count);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		if (count > 0)
			return true;
		return false;
	}
	/**
	 * PID信息
	 * @param _id
	 * @param dirt
	 * @return
	 */
	public boolean updatePid(int _id, Pid pid) {
		Util.Print("DAO", "UPDATE PID");
		Util.Print("pid id", _id+"");
		System.out.println(pid.toString());
		int count;
		ContentValues values = new ContentValues();
		values.put(DBHelper.PROJECT_ID, pid.getProject_id());
		values.put(DBHelper.WELL_NUM, pid.getWell_num());
		values.put(DBHelper.PID_DEPTH, pid.getPid_depth());
		values.put(DBHelper.PID_EXTRA, pid.getPid_extra());
		values.put(DBHelper.PID_MEMO, pid.getPid_memo());
		values.put(DBHelper.PID_VALUE, pid.getPid_value());
		values.put(DBHelper.CREATE_TIME, pid.getCreate_time());
		try {
			count = db.update(DBHelper.TABLE_NAME_PID, values, "_id=?",
					new String[] { String.valueOf(_id).trim() });
			System.out.println("update rows :" + count);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		if (count > 0)
			return true;
		return false;
	}
	/**
	 * @param projectId
	 * @param well_num
	 * @return
	 */
	public boolean checkInBaseinfo(String _id, String projectId, String well_num) {
		try {
			if (db == null)
				open();
			System.out.println("_id:" + _id);
			System.out.println("project id:" + projectId);
			System.out.println("well num:" + well_num);
			String selection = "select project_id from baseinfo where project_id=? and well_num=? and _id !=?";
			Cursor cursor = db.rawQuery(selection, new String[] { projectId,
					well_num, _id });
			if (cursor.getCount() > 0) {
				cursor.close();
				return true;
			}
			cursor.close();
		} catch (Exception err) {
			err.printStackTrace();
			return false;
		}
		System.out.println("not exist");
		return false;
	}

	/**
	 * 插入基本信息
	 * 
	 * @param base
	 * @return
	 */
	public boolean insertBaseInfo(BaseInfo base) {
		Util.Print("DAO", "base info");
		ContentValues values = new ContentValues();
		values.put(DBHelper.CREATE_TIME, base.getBase_date());
		values.put(DBHelper.PROJECT_ID, base.getProject_id());
		values.put(DBHelper.WELL_NUM, base.getWell_num());
		values.put(DBHelper.BASE_WEATHER, base.getBase_weather());
		values.put(DBHelper.BASE_DATE, base.getBase_date());
		values.put(DBHelper.BASE_WORK, base.getBase_work());
		values.put(DBHelper.BASE_DEPTH, base.getBase_depth());
		values.put(DBHelper.BASE_MRADIS, base.getBase_mradis());
		values.put(DBHelper.BASE_RADIS, base.getBase_radis());
		values.put(DBHelper.BASE_WAY, base.getBase_way());
		values.put(DBHelper.BASE_WRITER, base.getBase_writer());
		values.put(DBHelper.BASE_GPS, base.getBase_gps());
		values.put(DBHelper.BASE_FIRSTDEPTH, base.getBase_first_depth());
		db.beginTransaction();
		long insertId = db.insert(DBHelper.TABLE_NAME_BASEINFO, null, values);
		db.setTransactionSuccessful();
		db.endTransaction();
		
		if(!base.getBase_work().equals("")){
		    if(checkInSugg(base.getBase_work())){
		        Suggestion sug = new Suggestion();
		        sug.setSug_name(base.getBase_work());
		        sug.setSug_type("0");
		        sug.setSug_time(System.currentTimeMillis());
		        insertSug(sug);
		    }
		}
		if (insertId == -1)
			return false;
		return true;
	}

	/**
	 * 检测是否已有该项目的洗井记录
	 * 
	 * @param projectId
	 * @param well_num
	 * @return
	 */
	public boolean checkInWashWell(String projectId, String well_num) {
		try {
			if (db == null)
				open();
			String selection = "select project_id from washwell where project_id=? and well_num=?";
			Cursor cursor = db.rawQuery(selection, new String[] { projectId,
					well_num });
			if (cursor.getCount() > 0) {
				cursor.close();
				return true;
			}
			cursor.close();
		} catch (Exception err) {
			err.printStackTrace();
			return false;
		}
		return false;
	}

	/**
	 * 
	 * @param washwell
	 */
	public boolean insertWashWell(Washwell washwell) {
		Util.Print("DAO", "wash well info");
		Util.Print("DAO", washwell.getWw_time());
		System.out.println(washwell.toString());
		ContentValues values = new ContentValues();
		values.put(DBHelper.PROJECT_ID, washwell.getProject_id());
		values.put(DBHelper.WASHWELL_COND, washwell.getWw_cond());
		values.put(DBHelper.WASHWELL_DATE, washwell.getWw_date());
		values.put(DBHelper.WASHWELL_GAOCHENG, washwell.getWw_gaocheng());
		values.put(DBHelper.WELL_NUM, washwell.getWell_num());
		values.put(DBHelper.WASHWELL_PH, washwell.getWw_ph());
		values.put(DBHelper.WASHWELL_TEMP, washwell.getWw_temp());
		values.put(DBHelper.WASHWELL_TIME, washwell.getWw_time());
		values.put(DBHelper.WASHWELL_WATER, washwell.getWw_water());
		values.put(DBHelper.WASHWELL_WAY, washwell.getWw_method());
		values.put(DBHelper.WASHWELL_WEATHER, washwell.getWw_weather());
		values.put(DBHelper.CREATE_TIME, Util.getCurrentTime());
		db.beginTransaction();
		long insertId = db.insert(DBHelper.TABLE_NAME_WASHWELL, null, values);
		db.setTransactionSuccessful();
		db.endTransaction();
		if (insertId == -1)
			return false;
		return true;
	}

	// public boolean updateWashWell(int _id, Washwell washwell) {
	// ContentValues values = new ContentValues();
	// values.put(DBHelper.WASHWELL_COND, washwell.getWw_cond());
	// values.put(DBHelper.WASHWELL_DATE, washwell.getWw_date());
	// values.put(DBHelper.WASHWELL_GAOCHENG, washwell.getWw_gaocheng());
	// values.put(DBHelper.WELL_NUM, washwell.getWell_num());
	// values.put(DBHelper.WASHWELL_PH, washwell.getWw_ph());
	// values.put(DBHelper.WASHWELL_TEMP, washwell.getWw_temp());
	// values.put(DBHelper.WASHWELL_TIME, washwell.getWw_time());
	// values.put(DBHelper.WASHWELL_WATER, washwell.getWw_water());
	// values.put(DBHelper.WASHWELL_WAY, washwell.getWw_method());
	// values.put(DBHelper.WASHWELL_WEATHER, washwell.getWw_weather());
	// try {
	// db.update(DBHelper.TABLE_NAME_WASHWELL, values, "_id=?",
	// new String[] { String.valueOf(_id).trim() });
	// //
	// ////System.out.println("update bookmark set title='"+title.trim()+"' and url='"+url.trim()+"' where title='"+old_title.trim()+"'");
	// //
	// db.execSQL("update bookmark set bm_title='"+title.trim()+"' and bm_url='"+url.trim()+"' where bm_title='"+old_title.trim()+"'");
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }

	/**
	 * create a scene
	 * 
	 * @param Scene
	 * @return
	 */
	public Scene CreateScene(Scene scene) {
		ContentValues values = new ContentValues();
		// values.put(DBHelper.TABLE_NAME, DBHelper.TABLE_NAME);
		// values.put(DBHelper.PRO_CREATETIME, getDate());
		// values.put(DBHelper.CREATE_ID, scene.getCreate_id());
		// values.put(DBHelper.PRO_NAME, scene.getPro_name());
		// values.put(DBHelper.PRO_NOW_WEATHER, scene.getPRO_NOW_WEATHER());
		// values.put(DBHelper.PRO_NOW_DATE, scene.getPRO_NOW_DATE());
		// values.put(DBHelper.PRO_NOW_UNIT, scene.getPRO_NOW_UNIT());
		// values.put(DBHelper.PRO_NOW_WELLDEPTH, scene.getPRO_NOW_WELLDEPTH());
		// values.put(DBHelper.PRO_NOW_WELLMONITAOR_RADIS,
		// scene.getPRO_NOW_WELLMONITAOR_RADIS());
		// values.put(DBHelper.PRO_NOW_WELL_RADIS,
		// scene.getPRO_NOW_WELL_RADIS());
		// values.put(DBHelper.PRO_NOW_WELL_WAY, scene.getPRO_NOW_WELL_WAY());
		// values.put(DBHelper.PRO_NOW_WELL_WRITER,
		// scene.getPRO_NOW_WELL_WRITER());
		// values.put(DBHelper.PRO_NOW_WELL_GPS, scene.getPRO_NOW_WELL_GPS());
		// values.put(DBHelper.PRO_NOW_WELL_ID, scene.getPRO_NOW_WELL_ID());
		// values.put(DBHelper.PRO_NOW_WELL_FIRSTDEPTH,
		// scene.getPRO_NOW_WELL_FIRSTDEPTH());
		// values.put(DBHelper.PRO_WELL_DEPTH, scene.getPRO_WELL_DEPTH());
		// values.put(DBHelper.PRO_WELL_DIRT, scene.getPRO_WELL_DIRT());
		// values.put(DBHelper.PRO_WELL_DIRTDETAIL,
		// scene.getPRO_WELL_DIRTDETAIL());
		// values.put(DBHelper.PRO_WELL_EXTRA, scene.getPRO_WELL_EXTRA());
		// values.put(DBHelper.PRO_PID_DEPTH, scene.getPRO_PID_DEPTH());
		// values.put(DBHelper.PRO_PID_READER, scene.getPRO_PID_READER());
		// values.put(DBHelper.PRO_PID_BAK, scene.getPRO_PID_BAK());
		// values.put(DBHelper.PRO_PID_EXTRA, scene.getPRO_PID_EXTRA());
		// values.put(DBHelper.PRO_WASHWELL_ID, scene.getPRO_WASHWELL_ID());
		// values.put(DBHelper.PRO_WASHWELL_DATE, scene.getPRO_WASHWELL_DATE());
		// values.put(DBHelper.PRO_WASHWELL_TIME, scene.getPRO_WASHWELL_TIME());
		// values.put(DBHelper.PRO_WASHWELL_WEATHER,
		// scene.getPRO_WASHWELL_WEATHER());
		// values.put(DBHelper.PRO_WASHWELL_WAY, scene.getPRO_WASHWELL_WAY());
		// values.put(DBHelper.PRO_WASHWELL_TEMP, scene.getPRO_WASHWELL_TEMP());
		// values.put(DBHelper.PRO_WASHWELL_DIANDAO,
		// scene.getPRO_WASHWELL_DIANDAO());
		// values.put(DBHelper.PRO_WASHWELL_PH, scene.getPRO_WASHWELL_PH());
		// values.put(DBHelper.PRO_WASHWELL_WATER,
		// scene.getPRO_WASHWELL_WATER());
		// values.put(DBHelper.PRO_WASHWELL_GAOCHENG,
		// scene.getPRO_WASHWELL_GAOCHENG());
		long insertId = db.insert(DBHelper.TABLE_NAME, null, values);
		Cursor cursor = db.query(DBHelper.TABLE_NAME, allColunms,
				DBHelper.COLUMN_ID + " = " + insertId, null, null, null, null);
		cursor.moveToFirst();
		Scene newScene = cusorScene(cursor);
		cursor.close();
		return newScene;
	}

	/**
	 * 修改Scene
	 * 
	 * @param scene
	 */
	public void updateScene(Scene scene) {
		ContentValues values = new ContentValues();
		// values.put(DBHelper.TABLE_NAME, DBHelper.TABLE_NAME);
		// values.put(DBHelper.PRO_CREATETIME, getDate());
		// values.put(DBHelper.CREATE_ID, scene.getCreate_id());
		// values.put(DBHelper.PRO_NAME, scene.getPro_name());
		// values.put(DBHelper.PRO_NOW_WEATHER, scene.getPRO_NOW_WEATHER());
		// values.put(DBHelper.PRO_NOW_DATE, scene.getPRO_NOW_DATE());
		// values.put(DBHelper.PRO_NOW_UNIT, scene.getPRO_NOW_UNIT());
		// values.put(DBHelper.PRO_NOW_WELLDEPTH, scene.getPRO_NOW_WELLDEPTH());
		// values.put(DBHelper.PRO_NOW_WELLMONITAOR_RADIS,
		// scene.getPRO_NOW_WELLMONITAOR_RADIS());
		// values.put(DBHelper.PRO_NOW_WELL_RADIS,
		// scene.getPRO_NOW_WELL_RADIS());
		// values.put(DBHelper.PRO_NOW_WELL_WAY, scene.getPRO_NOW_WELL_WAY());
		// values.put(DBHelper.PRO_NOW_WELL_WRITER,
		// scene.getPRO_NOW_WELL_WRITER());
		// values.put(DBHelper.PRO_NOW_WELL_GPS, scene.getPRO_NOW_WELL_GPS());
		// values.put(DBHelper.PRO_NOW_WELL_ID, scene.getPRO_NOW_WELL_ID());
		// values.put(DBHelper.PRO_NOW_WELL_FIRSTDEPTH,
		// scene.getPRO_NOW_WELL_FIRSTDEPTH());
		// values.put(DBHelper.PRO_WELL_DEPTH, scene.getPRO_WELL_DEPTH());
		// values.put(DBHelper.PRO_WELL_DIRT, scene.getPRO_WELL_DIRT());
		// values.put(DBHelper.PRO_WELL_DIRTDETAIL,
		// scene.getPRO_WELL_DIRTDETAIL());
		// values.put(DBHelper.PRO_WELL_EXTRA, scene.getPRO_WELL_EXTRA());
		// values.put(DBHelper.PRO_PID_DEPTH, scene.getPRO_PID_DEPTH());
		// values.put(DBHelper.PRO_PID_READER, scene.getPRO_PID_READER());
		// values.put(DBHelper.PRO_PID_BAK, scene.getPRO_PID_BAK());
		// values.put(DBHelper.PRO_PID_EXTRA, scene.getPRO_PID_EXTRA());
		// values.put(DBHelper.PRO_WASHWELL_ID, scene.getPRO_WASHWELL_ID());
		// values.put(DBHelper.PRO_WASHWELL_DATE, scene.getPRO_WASHWELL_DATE());
		// values.put(DBHelper.PRO_WASHWELL_TIME, scene.getPRO_WASHWELL_TIME());
		// values.put(DBHelper.PRO_WASHWELL_WEATHER,
		// scene.getPRO_WASHWELL_WEATHER());
		// values.put(DBHelper.PRO_WASHWELL_WAY, scene.getPRO_WASHWELL_WAY());
		// values.put(DBHelper.PRO_WASHWELL_TEMP, scene.getPRO_WASHWELL_TEMP());
		// values.put(DBHelper.PRO_WASHWELL_DIANDAO,
		// scene.getPRO_WASHWELL_DIANDAO());
		// values.put(DBHelper.PRO_WASHWELL_PH, scene.getPRO_WASHWELL_PH());
		// values.put(DBHelper.PRO_WASHWELL_WATER,
		// scene.getPRO_WASHWELL_WATER());
		// values.put(DBHelper.PRO_WASHWELL_GAOCHENG,
		// scene.getPRO_WASHWELL_GAOCHENG());
		db.update(DBHelper.TABLE_NAME, values, DBHelper.COLUMN_ID + " = "
				+ scene.getId(), null);
		// long result =
	}

	/**
	 * get all the stored data of scenes
	 * 
	 * @return
	 */
	public List<Scene> getAllScene() {
		List<Scene> scenes = new ArrayList<Scene>();
		Cursor cursor = null;
		try {
			cursor = db.query(DBHelper.TABLE_NAME, allColunms, null, null,
					null, null, null);
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				Scene scene = cusorScene(cursor);
				scenes.add(scene);
				cursor.moveToNext();
			}
		} catch (Exception err) {
			err.printStackTrace();
		} finally {
			if (null != cursor)
				cursor.close();
		}
		return scenes;
	}

	/**
	 * delete a scene by create_id
	 * 
	 * @param create_id
	 * @return
	 */
	public int deleteScene(final int create_id) {
		return db.delete(DBHelper.TABLE_NAME, DBHelper.PROJECT_ID + " = "
				+ create_id, null);
	}

	/**
	 * delete a scene by create_name
	 * 
	 * @param pro_name
	 * @return
	 */
	public int deleteScene(final String pro_name) {
		return db.delete(DBHelper.TABLE_NAME, DBHelper.PROJECT_NAME + " = "
				+ pro_name, null);
	}

	private Scene cusorScene(Cursor cursor) {
		Scene scene = new Scene();
		scene.setId(cursor.getInt(0));
		scene.setCreate_id(cursor.getInt(1));
		scene.setPro_name(cursor.getString(2));
		scene.setPro_createtime(cursor.getString(3));
		return scene;
	}

//	private BaseInfo cusorBaseInfo(Cursor cursor) {
//		BaseInfo base = new BaseInfo();
//		base.setProject_id(cursor.getString(1));
//		return base;
//	}

	/**s
	 * 获取手机当前时间
	 * 
	 * @return
	 */
	public String getDate() {
		Calendar ca = Calendar.getInstance();
		int year = ca.get(Calendar.YEAR);// 获取年份
		int month = ca.get(Calendar.MONTH);// 获取月份
		int day = ca.get(Calendar.DATE);// 获取日
		int minute = ca.get(Calendar.MINUTE);// 分
		int hour = ca.get(Calendar.HOUR);// 小时
		int second = ca.get(Calendar.SECOND);// 秒
		return "" + year + month + day + hour + minute + second;
	}
}
