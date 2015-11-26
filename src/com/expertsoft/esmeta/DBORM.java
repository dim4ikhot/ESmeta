package com.expertsoft.esmeta;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.expertsoft.esmeta.data.LS;
import com.expertsoft.esmeta.data.ORMDatabaseHelper;
import com.expertsoft.esmeta.data.OS;
import com.expertsoft.esmeta.data.Projects;
import com.expertsoft.esmeta.data.Works;
import com.expertsoft.esmeta.data.WorksResources;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

public class DBORM implements Serializable {

	ORMDatabaseHelper databaseHelper = null;
	Context context;
	
	private static final long serialVersionUID = -222864131214757024L;
	
	public DBORM(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
		 getHelper();
	}
	
	public ORMDatabaseHelper getHelper(){
		if(databaseHelper == null)
		{
			databaseHelper = OpenHelperManager.getHelper(context, ORMDatabaseHelper.class);
		}		
		return databaseHelper;			
	}
	
	public void destroyHelper(){
		if (databaseHelper != null) {
			OpenHelperManager.releaseHelper();
			databaseHelper = null;
		}
	}
	
	public List<Projects> getAllProjectsData(){
		try{
			return getHelper().getProjectsDao().queryForAll();
		}catch(SQLException e)
		{
			Log.d("muLogs", e.getMessage().toString());
			return new ArrayList<Projects>();
		}
		
	}
	
	public List<OS> getAllOEstimateData(){
		try{
			return getHelper().getOSDao().queryForAll();
		}catch(SQLException e)
		{
			Log.d("muLogs", e.getMessage().toString());
			return new ArrayList<OS>();
		}
		
	}
	
	public List<OS> getObjectEstimate(Projects proj){
		List<OS> osList = new ArrayList<OS>();
		try {
			// This is how, a reference of DAO object can be done
			Dao<OS,Integer> osDao =  getHelper().getOSDao();
			
			// Get our query builder from the DAO
			final QueryBuilder<OS, Integer> queryBuilder = osDao.queryBuilder();
			
			// We need only Students who are associated with the selected Teacher, so build the query by "Where" clause
			queryBuilder.where().eq(OS.TOS_FIELD_PROJECT_ID,  proj.getProjectId());
			
			// Prepare our SQL statement
			final PreparedQuery<OS> preparedQuery = queryBuilder.prepare();
			
			// Fetch the list from Database by queryingit 
			final Iterator<OS>  studentsIt = osDao.query(preparedQuery).iterator();
			
			// Iterate through the StudentDetails object iterator and populate the comma separated String
			while (studentsIt.hasNext()) {
				final OS oss = studentsIt.next();
				osList.add(oss);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return osList;
	}
	
	public List<LS> getLocalEstimate(Projects proj, OS os){
		List<LS> lsList = new ArrayList<LS>();
		try {
			// This is how, a reference of DAO object can be done
			Dao<LS,Integer> lsDao =  getHelper().getLSDao();
			
			// Get our query builder from the DAO
			final QueryBuilder<LS, Integer> queryBuilder = lsDao.queryBuilder();
			
			// We need only Students who are associated with the selected Teacher, so build the query by "Where" clause
			queryBuilder.where().eq(LS.TLS_FIELD_PROJECT_ID,  proj.getProjectId());
			queryBuilder.where().eq(LS.TLS_FIELD_OS_ID,  os.getOsId());
			
			// Prepare our SQL statement
			final PreparedQuery<LS> preparedQuery = queryBuilder.prepare();
			
			// Fetch the list from Database by queryingit 
			final Iterator<LS>  studentsIt = lsDao.query(preparedQuery).iterator();
			
			// Iterate through the StudentDetails object iterator and populate the comma separated String
			while (studentsIt.hasNext()) {
				final LS oss = studentsIt.next();
				lsList.add(oss);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return lsList;
	}
	
	public List<Works> getWorks(Projects proj, OS os, LS ls){
		List<Works> worksList = new ArrayList<Works>();
		try {
			// This is how, a reference of DAO object can be done
			Dao<Works,Integer> worksGroupDao =  getHelper().getWorksDao();
			
			// Get our query builder from the DAO
			final QueryBuilder<Works, Integer> queryBuilder = worksGroupDao.queryBuilder();
			
			// We need only Students who are associated with the selected Teacher, so build the query by "Where" clause
			Where<Works,Integer> where = queryBuilder.where();
			/*where.eq(Works.TW_FIELD_PROJECT_ID,  proj.getProjectId())
				 .and()			
 				 .eq(Works.TW_FIELD_OS_ID,  os.getOsId())
				 .and()			
				 .eq(Works.TW_FIELD_LS_ID,  ls.getLsId())
				 .and()
				 .ne(Works.TW_FIELD_REC, "machine")
				 .and()
				 .ne(Works.TW_FIELD_REC, "resource");*/
			where.and(
					where.eq(Works.TW_FIELD_PROJECT_ID,  proj.getProjectId()),
					where.eq(Works.TW_FIELD_OS_ID,  os.getOsId()),
					where.eq(Works.TW_FIELD_LS_ID,  ls.getLsId()),
					where.or(							 
						where.and(
								where.ne(Works.TW_FIELD_REC, "machine"),
								where.ne(Works.TW_FIELD_REC, "resource")),							 
						where.eq(Works.TW_FIELD_PARENT_NORM_ID, 0)));							
				 
			
			// Prepare our SQL statement
			final PreparedQuery<Works> preparedQuery = queryBuilder.prepare();
			
			// Fetch the list from Database by queryingit 
			final Iterator<Works>  studentsIt = worksGroupDao.query(preparedQuery).iterator();
			
			// Iterate through the StudentDetails object iterator and populate the comma separated String
			while (studentsIt.hasNext()) {
				final Works oss = studentsIt.next();
				worksList.add(oss);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return worksList;
	}
	
	public Works getDataForCurrentWorks(Projects proj, OS os, LS ls, Works w){
		Works worksList = new Works();
		try {
			// This is how, a reference of DAO object can be done
			Dao<Works,Integer> worksGroupDao =  getHelper().getWorksDao();
			
			// Get our query builder from the DAO
			final QueryBuilder<Works, Integer> queryBuilder = worksGroupDao.queryBuilder();
			
			// We need only Students who are associated with the selected Teacher, so build the query by "Where" clause
			Where<Works,Integer> where = queryBuilder.where();
			where.eq(Works.TW_FIELD_PROJECT_ID,  proj.getProjectId())
				 .and()			
 				 .eq(Works.TW_FIELD_OS_ID,  os.getOsId())
				 .and()			
				 .eq(Works.TW_FIELD_LS_ID,  ls.getLsId())
				 .and()			
				 .eq(Works.TW_FIELD_ID,  w.getWorkId())
				 .and()
				 .ne(Works.TW_FIELD_REC, "machine")
				 .and()
				 .ne(Works.TW_FIELD_REC, "resource");
			
			// Prepare our SQL statement
			final PreparedQuery<Works> preparedQuery = queryBuilder.prepare();
			
			// Fetch the list from Database by queryingit 
			final Iterator<Works>  studentsIt = worksGroupDao.query(preparedQuery).iterator();
			
			// Iterate through the StudentDetails object iterator and populate the comma separated String
			while (studentsIt.hasNext()) {
				final Works oss = studentsIt.next();
				worksList = oss;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return worksList;
	}
	
	public List<Works> getWorksChild(Projects proj, OS os, LS ls, Works work){
		List<Works> worksList = new ArrayList<Works>();
		try {
			// This is how, a reference of DAO object can be done
			Dao<Works,Integer> workChildDao =  getHelper().getWorksDao();
			
			// Get our query builder from the DAO
			final QueryBuilder<Works, Integer> queryBuilder = workChildDao.queryBuilder();
			
			// We need only Students who are associated with the selected Teacher, so build the query by "Where" clause
			Where<Works,Integer> where = queryBuilder.where();
			where.and(
					where.eq(Works.TW_FIELD_PROJECT_ID,  proj.getProjectId()),
					where.eq(Works.TW_FIELD_OS_ID,  os.getOsId()),
					where.eq(Works.TW_FIELD_LS_ID,  ls.getLsId()),
					where.eq(Works.TW_FIELD_PARENT_NORM_ID,  work.getWorkId()),
					where.or(where.eq(Works.TW_FIELD_REC, "machine"),						 
							 where.eq(Works.TW_FIELD_REC, "resource"))
					);		
			// Prepare our SQL statement
			final PreparedQuery<Works> preparedQuery = queryBuilder.prepare();
			
			// Fetch the list from Database by queryingit 
			final Iterator<Works>  studentsIt = workChildDao.query(preparedQuery).iterator();
			
			// Iterate through the StudentDetails object iterator and populate the comma separated String
			while (studentsIt.hasNext()) {
				final Works oss = studentsIt.next();
				worksList.add(oss);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return worksList;
	}
	
	public List<WorksResources> getWorksResource(Works work){
		List<WorksResources> worksList = new ArrayList<WorksResources>();
		try {
			// This is how, a reference of DAO object can be done
			Dao<WorksResources,Integer> workResDao =  getHelper().getWorksResDao();			
			// Get our query builder from the DAO
			final QueryBuilder<WorksResources, Integer> queryBuilder = workResDao.queryBuilder();			
			// We need only Students who are associated with the selected Teacher, so build the query by "Where" clause
			Where<WorksResources,Integer> where = queryBuilder.where();
			where.eq(WorksResources.TWS_FIELD_WORK_ID,  work.getWorkId());				 				
			// Prepare our SQL statement
			final PreparedQuery<WorksResources> preparedQuery = queryBuilder.prepare();			
			// Fetch the list from Database by queryingit 
			final Iterator<WorksResources>  studentsIt = workResDao.query(preparedQuery).iterator();			
			// Iterate through the StudentDetails object iterator and populate the comma separated String
			while (studentsIt.hasNext()) {
				final WorksResources oss = studentsIt.next();
				worksList.add(oss);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return worksList;
	}
	
	

}
