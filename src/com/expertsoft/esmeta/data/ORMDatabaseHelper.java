package com.expertsoft.esmeta.data;

import java.io.File;
import java.io.Serializable;
import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import com.expertsoft.esmeta.R;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class ORMDatabaseHelper extends OrmLiteSqliteOpenHelper implements Serializable{

	public static final String DATABASE_NAME = "DATA.db";
	public static final int DATABASE_VER = 1;
	private static final long serialVersionUID = -222864131214757024L;
	
	//Our database tables 
	private Dao<Projects, Integer> projectsDao;
	private Dao<OS, Integer> objectestDao;
	private Dao<LS, Integer> localestDao;
	private Dao<Works, Integer> worksDao;
	private Dao<WorksResources, Integer> worksrestDao;
	
	public ORMDatabaseHelper(Context context) {
		// TODO Auto-generated constructor stub
		super(context, Environment.getExternalStorageDirectory()+"/Android/data/com.expertsoft.esmeta/database"
			    + File.separator + DATABASE_NAME, null, DATABASE_VER, R.raw.ormlite_config);
	}

	@Override
	public void onCreate(SQLiteDatabase sqlitedatabase, ConnectionSource connectionSource) {
		// TODO Auto-generated method stub
		try{
			TableUtils.createTable(connectionSource, Projects.class);
			TableUtils.createTable(connectionSource, OS.class);
			TableUtils.createTable(connectionSource, LS.class);
			TableUtils.createTable(connectionSource, Works.class);
			TableUtils.createTable(connectionSource, WorksResources.class);
			
		}catch(SQLException e)
		{
			Log.d("muLogs", e.getMessage().toString());
		}	
	}

	@Override
	public void onUpgrade(SQLiteDatabase sqlitedatabase, ConnectionSource connectionSource, int oldVer,
			int newVer) {
		// Database update....
		
		try{
			TableUtils.dropTable(connectionSource, Projects.class, true);
			TableUtils.dropTable(connectionSource, OS.class,true);
			TableUtils.dropTable(connectionSource, LS.class,true);
			TableUtils.dropTable(connectionSource, Works.class, true);
			TableUtils.dropTable(connectionSource, WorksResources.class,true);
			
			onCreate(sqlitedatabase, connectionSource);
			
		}catch(SQLException e)
		{
			e.printStackTrace();
		}	
		
	}
	
	public Dao<Projects, Integer> getProjectsDao() throws SQLException {
		if (projectsDao == null) {
			projectsDao = getDao(Projects.class);
		}
		return projectsDao;
	}
	
	public Dao<OS, Integer> getOSDao() throws SQLException {
		if (objectestDao == null) {
			objectestDao = getDao(OS.class);
		}
		return objectestDao;
	}
	
	public Dao<LS, Integer> getLSDao() throws SQLException {
		if (localestDao == null) {
			localestDao = getDao(LS.class);
		}
		return localestDao;
	}
	
	public Dao<Works, Integer> getWorksDao() throws SQLException {
		if (worksDao == null) {
			worksDao = getDao(Works.class);
		}
		return worksDao;
	}
	
	public Dao<WorksResources, Integer> getWorksResDao() throws SQLException {
		if (worksrestDao == null) {
			worksrestDao = getDao(WorksResources.class);
		}
		return worksrestDao;
	}

}
