package com.expertsoft.esmeta.parsers;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.expertsoft.esmeta.data.LS;
import com.expertsoft.esmeta.data.ORMDatabaseHelper;
import com.expertsoft.esmeta.data.OS;
import com.expertsoft.esmeta.data.Projects;
import com.expertsoft.esmeta.data.Works;
import com.expertsoft.esmeta.data.WorksResources;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

public class ZmlParser {
	
	public static final String LOGTAG = "LoadBuild";  
	
	static final String[] CiphersNorm = {"Å",  "Ì",  "Ð",  "ØÄ", "Ï",  "ÏÏ",
									     "Â",  "ÏÓ", "ÏÐ", "ÏÕ", "ÏÌ", "ÏÅ",
									     "ÆÒ", "ÆÐ", "ÒÐ", "ÒÅ", "ÂÌ", "ÏÆ",
									     "Ñ3", "ÄÀ", "ÒÐÓ","ÐÓ", "ÆÑ", "ÆÑ",
									     "ÙÄ", "ÂÅ", "ÒÃ", "ÒÏ"};			
	Dao<Projects,Integer> projectsDao;
	Dao<OS,Integer> osDao;
	Dao<LS,Integer> lsDao;
	Dao<Works,Integer> worksDao;
	Dao<WorksResources,Integer> worksresDao;	
	
	Projects projects;
	OS os;
	LS ls;
	Works works;
	WorksResources worksres;
	
	File zmlFile;
	ORMDatabaseHelper databaseHelper;
	int counter = 0;
	String attrName, attrValue;
	
	int parentNormID = 0;
	int parentID = 0;	
	
	public ZmlParser() {
		// TODO Auto-generated constructor stub
	}

	public ZmlParser(File zmlFile,ORMDatabaseHelper dataHelper) {
				
		try{
			projectsDao = dataHelper.getProjectsDao();
			osDao = dataHelper.getOSDao();
			lsDao = dataHelper.getLSDao();
			worksDao = dataHelper.getWorksDao();
			worksresDao = dataHelper.getWorksResDao();
		}catch(SQLException e){
			Log.i(LOGTAG, e.getMessage().toString());
		}		
		projects = new Projects();
		os = new OS();
		ls = new LS();
		works = new Works();
		worksres = new WorksResources();
		
		this.zmlFile = zmlFile;
		databaseHelper = dataHelper;	
	}
	
	public boolean startParce(){
		try{
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
	        factory.setNamespaceAware(true);
			XmlPullParser parsebuild = factory.newPullParser();
			//parsebuild.setInput(new StringReader(zmlFile.getPath()));
			InputStream in = new FileInputStream(zmlFile.getPath());						
			parsebuild.setInput(in, "UTF-8");
			while(parsebuild.getEventType() != XmlPullParser.END_DOCUMENT){
				switch(parsebuild.getEventType()){
					case XmlPullParser.START_DOCUMENT:
						//TODO Message or dialog, which will be signal parser started
						Log.i(LOGTAG, "Loading start");
						break;						
					case XmlPullParser.START_TAG:
						switch(parsebuild.getName()){
							case "Ñòðîéêà":								
								for(int i = 0; i < parsebuild.getAttributeCount(); i++)
								{									
									attrName = parsebuild.getAttributeName(i);									
									attrValue = parsebuild.getAttributeValue(i);
									switch(attrName){
									case "STROIKANAMEBRIEF":
										projects.setProjectName(attrValue);
										break;
									case "STROIKAPROJECTSHIFR":
										projects.setProjectCipher(attrValue);
										break;
									case "STROIKACREATIONDATE":	
										attrValue = attrValue.replace("/", ".");
										SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
										Date date = sdf.parse(attrValue);										
										projects.setProjectCreatedDate(date);
										break;
									case "STROIKAZAKAZCHIK":
										projects.setProjectCustomer(attrValue);
										break;
									case "STROIKAGENPODR":
										projects.setProjectContractor(attrValue);
										break;
									case "STROIKATOTAL":
										projects.setProjectTotal(Float.parseFloat(attrValue));
										break;
									}																																																					
								}
								addProjects(projects);
								break;
							case "ÎáúåêòíàÿÑìåòà":
								os = new OS();
								for(int i = 0; i < parsebuild.getAttributeCount(); i++)
								{
									attrName = parsebuild.getAttributeName(i);									
									attrValue = parsebuild.getAttributeValue(i);									
									switch(attrName){
									case "OSNAME":
										os.setOsName(attrValue);
										break;
									case "OSNOOS":
										os.setOsCipher(attrValue);
										break;									
									case "OSTOTAL":
										os.setOsTotal(Float.parseFloat(attrValue));
										break;
									}									
								}								
								os.setOsProjects(projects);	
								os.setOsProjectId(projects.getProjectId());
								addOS(os);
								break;
							case "ËîêàëüíàÿÑìåòà":
								ls = new LS();
								for(int i = 0; i < parsebuild.getAttributeCount(); i++)
								{
									attrName = parsebuild.getAttributeName(i);									
									attrValue = parsebuild.getAttributeValue(i);									
									switch(attrName){
									case "LSNAME":
										ls.setLsName(attrValue);
										break;
									case "LSNOLS":
										ls.setLsCipher(attrValue);
										break;									
									case "LSTOTAL":
										ls.setLsTotal(Float.parseFloat(attrValue));
										break;
									}
								}
								ls.setLsHidden(false);
								ls.setLsProjectId(projects.getProjectId());
								ls.setLsOsId(os.getOsId());
								ls.setLsOs(os);								
								ls.setLsProjects(projects);
								addLS(ls);
								break;
							case "ÏîçèöèÿËîêàëüíîéÑìåòû":
																						
								String rec = works.getWRec();
								if(rec != null){
									if(rec.contains("record")){
										parentNormID = works.getWorkId();
									}
									if ((rec.contains("razdel"))|(rec.contains("chast"))){
										parentID = works.getWorkId();
									}
								}
								works = new Works();
								for(int i = 0; i < parsebuild.getAttributeCount(); i++)
								{																												
									attrName = parsebuild.getAttributeName(i);									
									attrValue = parsebuild.getAttributeValue(i);
									switch(attrName){
									case "SLSNAME":
										works.setWName(attrValue);										
										break;
									case "SLSSHIFR":
										works.setWCipher(attrValue);
										break;
									case "SLSOBOSN":
										works.setWCipherObosn(attrValue);
										break;
									case "SLSREC":
										//Fill this field some later
										rec = attrValue;
										break;
									case "SLSRAZDEL":
										works.setWPart(Integer.parseInt(attrValue));
										break;
									case "SLSKOLVO":
										attrValue = attrValue.replace(",", ".");
										works.setWCount(Float.parseFloat(attrValue));
										break;
									case "SLSIZM":
										works.setWMeasured(attrValue);
										break;
									case "SLSKOLVO_PRCNT":
										attrValue = attrValue.replace(",", ".");
										works.setWPercentDone(Float.parseFloat(attrValue));
										break;
									case "SLSKOLVO_DONE":
										attrValue = attrValue.replace(",", ".");
										works.setWCountDone(Float.parseFloat(attrValue));
										break;
									case "SLSTOTAL":
										attrValue = attrValue.replace(",", ".");
										works.setWTotal(Float.parseFloat(attrValue));
										break;
									case "SLSNPP":									
										works.setWNpp(Integer.parseInt(attrValue));
										break;
									case "SLSITOGO":
										attrValue = attrValue.replace(",", ".");
										works.setWItogo(Float.parseFloat(attrValue));
										break;
									case "SLSZP":
										attrValue = attrValue.replace(",", ".");
										works.setWZP(Float.parseFloat(attrValue));
										break;
									case "SLSMACH":
										attrValue = attrValue.replace(",", ".");
										works.setWMach(Float.parseFloat(attrValue));
										break;
									case "SLSZPMACH":
										attrValue = attrValue.replace(",", ".");
										works.setWZPMach(Float.parseFloat(attrValue));
										break;
									case "SLSZPTOTAL":
										attrValue = attrValue.replace(",", ".");
										works.setWZPTotal(Float.parseFloat(attrValue));
										break;
									case "SLSMACHTOTAL":
										attrValue = attrValue.replace(",", ".");
										works.setWMachTotal(Float.parseFloat(attrValue));
										break;
									case "SLSZPMACHTOTAL":
										attrValue = attrValue.replace(",", ".");
										works.setWZPMachTotal(Float.parseFloat(attrValue));
										break;
									case "SLSTZ":
										attrValue = attrValue.replace(",", ".");
										works.setWTz(Float.parseFloat(attrValue));
										break;
									case "SLSTZMACH":
										attrValue = attrValue.replace(",", ".");
										works.setWTZMach(Float.parseFloat(attrValue));
										break;
									case "SLSTZTOTAL":
										attrValue = attrValue.replace(",", ".");
										works.setWTZTotal(Float.parseFloat(attrValue));
										break;									
									case "SLSTZMACHTOTAL":
										attrValue = attrValue.replace(",", ".");
										works.setWTZMachTotal(Float.parseFloat(attrValue));
										break;
									case "SLSNAKLTOTAL":
										attrValue = attrValue.replace(",", ".");
										works.setWNaklTotal(Float.parseFloat(attrValue));
										break;										
									}
								}
								works.setWProjectId(projects.getProjectId());
								works.setWOsId(os.getOsId());
								works.setWLsId(ls.getLsId());
								works.setWLSFK(ls);
								works.setWCurrStateDate(new Date());
								works.setWEndDate(new Date());
								works.setWStartDate(new Date());								
								works.setWOSFK(os);
								String cipher = works.getWCipher();								
								if((rec.equals(""))|(rec.equals("koef"))|((cipher == null)&(rec.contains("record")))){
									works.setWRec("koef");
								}else{
									if((rec.contains("razdel"))|(rec.contains("chast"))){
										works.setWRec(rec);
									}else{
										if (checkForNorm(cipher)){
											works.setWRec("record");
										}else{
											if ((cipher.length() > 2)&
													((cipher.contains("Ñ2"))|
															(cipher.contains("ÑÍ2")))){
												works.setWRec("machine");
											}else{										
												works.setWRec("resource");
											}
										}
									}
								}
								if((works.getWRec().contains("resource"))|(works.getWRec().contains("machine"))){
									works.setWParentNormId(parentNormID);
								}
								works.setWProjectFK(projects);
								works.setWParentId(ls.getLsId());
								addWorks(works);
								counter++;								
								break;
							case "ÑîñòàâÏîçèöèè":	
								worksres = new WorksResources();
								for(int i = 0; i < parsebuild.getAttributeCount(); i++)
								{
									attrName = parsebuild.getAttributeName(i);									
									attrValue = parsebuild.getAttributeValue(i);
									
									switch(attrName){
									case "RSNAME":
									    worksres.setWrName(attrValue);
										break;
									case "RSSHIFR":
										worksres.setWrCipher(attrValue);
										break;
									case "RSIZM":
										worksres.setWrMeasured(attrValue);
										break;
									case "RSKOLVO1":
										attrValue = attrValue.replace(",", ".");
										worksres.setWrCount(Float.parseFloat(attrValue));
										break;
									case "RSSTOIM1":
										attrValue = attrValue.replace(",", ".");
										worksres.setWrCost(Float.parseFloat(attrValue));
										break;
									case "RSVSEGO1":
										worksres.setWrTotalCost(worksres.getWrCount() * worksres.getWrCost());
										break;
									case "RSONOFF":
										
										break;
									case "RSRAZDEL":
										worksres.setWrPart(Integer.parseInt(attrValue));
										break;
									}
								}
								worksres.setWrWorkId(works.getWorkId());
								worksres.setWrWork(works);
								addWorksRes(worksres);
								break;						
						}
						break;								
						
					case XmlPullParser.TEXT:
						break;
						
					case XmlPullParser.END_TAG:
						break;					
				}
				parsebuild.next();
			}				
			//STOPED parse			
			Log.i(LOGTAG, "Loading stoped");
		}catch(Exception e){
			Log.i(LOGTAG,  e.getMessage().toString());
			return false;
		}
		return true;
	}	
	
	private boolean checkForNorm(String Cipher){
		if(Cipher != null){
			for(String CipPart: CiphersNorm){
				if(Cipher.contains(CipPart)){
					return true;
				}
			}
		}
		return false;
	}
	private int getMaxProjectSortID(){
		int counter = 0;
		try{
			final QueryBuilder<Projects, Integer> queryBuilder = projectsDao.queryBuilder();			
			// select 2 aggregate functions as the return
			queryBuilder.selectRaw("MAX("+Projects.TP_FIELD_SORTID+")");			
			// the results will contain 2 string values for the min and max
			GenericRawResults<String[]> rawResult = worksDao.queryRaw(queryBuilder.prepareStatementString());
			String[] results = rawResult.getFirstResult();
			if (results[0] != null){
				counter = Integer.parseInt(results[0]);	
			}
		}catch(SQLException e){
			Log.i(LOGTAG,  e.getMessage().toString());
		}	  
		return counter;
	}
	
	private int getMaxOSSortID(){
		int counter = 0;
		try{
			final QueryBuilder<OS, Integer> queryBuilder = osDao.queryBuilder();			
			// select 2 aggregate functions as the return
			queryBuilder.selectRaw("MAX("+OS.TOS_FIELD_SORT_ID+")");
			queryBuilder.where().eq(OS.TOS_FIELD_PROJECT_ID, projects.getProjectId());
			// the results will contain 2 string values for the min and max
			GenericRawResults<String[]> rawResult = worksDao.queryRaw(queryBuilder.prepareStatementString());
			String[] results = rawResult.getFirstResult();
			if (results[0] != null){
				counter = Integer.parseInt(results[0]);	
			}
		}catch(SQLException e){
			Log.i(LOGTAG,  e.getMessage().toString());
		}		  		  
		return counter;
	}
	
	private int getMaxLSSortID(){
		int counter = 0;
		try{
			final QueryBuilder<LS, Integer> queryBuilder = lsDao.queryBuilder();			
			// select 2 aggregate functions as the return
			queryBuilder.selectRaw("MAX("+LS.TLS_FIELD_SORT_ID+")");
			queryBuilder.where().eq(LS.TLS_FIELD_PROJECT_ID, projects.getProjectId());
			// the results will contain 2 string values for the min and max
			GenericRawResults<String[]> rawResult = worksDao.queryRaw(queryBuilder.prepareStatementString());
			String[] results = rawResult.getFirstResult();
			if (results[0] != null){
				counter = Integer.parseInt(results[0]);	
			}
		}catch(SQLException e){
			Log.i(LOGTAG,  e.getMessage().toString());
		}		  		  
		return counter;
	}
	
	private int getMaxWorksSortid(){
		int counter = 0;
		try{							
			final QueryBuilder<Works, Integer> queryBuilder = worksDao.queryBuilder();			
			// select 2 aggregate functions as the return
			queryBuilder.selectRaw("MAX("+Works.TW_FIELD_SORT_ORDER+")");
			queryBuilder.where().eq(Works.TW_FIELD_PROJECT_ID, projects.getProjectId());
			// the results will contain 2 string values for the min and max
			GenericRawResults<String[]> rawResult = worksDao.queryRaw(queryBuilder.prepareStatementString());
			String[] results = rawResult.getFirstResult();
			if (results[0] != null){
				counter = Integer.parseInt(results[0]);					
			}
		}catch(SQLException e){
			Log.i(LOGTAG,  e.getMessage().toString());
		}		  		  
		return counter;
	}
	
	private void addProjects(Projects proj){
		proj.setProjectSortId(getMaxProjectSortID() + 1);
		try{
			projectsDao.create(proj);
		}catch(SQLException e){
			Log.i(LOGTAG, e.getMessage().toString());
		}	  
	}
	
	private void addOS(OS os){		
		os.setOsSortId(getMaxOSSortID() + 1);
		try{
			osDao.create(os);
		}catch(SQLException e){
			Log.i(LOGTAG, e.getMessage().toString());
		}		  
	}
	
	private void addLS(LS ls){	
		ls.setLsSortId(getMaxLSSortID() + 1);
		try{
			lsDao.create(ls);
		}catch(SQLException e){
			Log.i(LOGTAG, e.getMessage().toString());
		}		  
	}

	private void addWorks(Works work){
		work.setWSortOrder(getMaxWorksSortid() + 1);
		try{
			worksDao.create(work);
		}catch(SQLException e){
			Log.i(LOGTAG, e.getMessage().toString());
		}		  
	}
	
	private void addWorksRes(WorksResources wr){			
		try{
			worksresDao.create(wr);
		}catch(SQLException e){
			Log.i(LOGTAG, e.getMessage().toString());
		}	  
	}
	
}
