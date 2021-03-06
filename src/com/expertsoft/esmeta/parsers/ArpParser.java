package com.expertsoft.esmeta.parsers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.util.Log;

import com.expertsoft.esmeta.data.LS;
import com.expertsoft.esmeta.data.ORMDatabaseHelper;
import com.expertsoft.esmeta.data.OS;
import com.expertsoft.esmeta.data.Projects;
import com.expertsoft.esmeta.data.Works;
import com.expertsoft.esmeta.data.WorksResources;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.stmt.QueryBuilder;

public class ArpParser {

	static final String LOGTAG = "LoadBuild";     
	
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
	
	List<List<String>> lineNumberList;
	List<String> dataList;
	
	File arpFile;
	ORMDatabaseHelper databaseHelper;
	int counter = 0;
	String projectName;
	int parentNormID = 0;
	int globalNPP = 0;
	
	KoefValue PosZP = new KoefValue();
	KoefValue PosMM  = new KoefValue();
	KoefValue PosMAT  = new KoefValue();
	KoefValue PosDV  = new KoefValue();
	
	public ArpParser() {
		// TODO Auto-generated constructor stub
	}
	
	public ArpParser(File arpFile,ORMDatabaseHelper dataHelper, String prjName) {
				
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
		
		String[] projPathParts = prjName.split("/");		
		projectName = projPathParts[projPathParts.length - 1];
		projPathParts = projectName.split("\\.");
		if (projPathParts.length > 0){
			projectName = projPathParts[0];
		}else{
			projectName = "����������� ��� �������";
		}
		
		this.arpFile = arpFile;
		databaseHelper = dataHelper;		
		lineNumberList = new ArrayList<List<String>>();
		dataList = new ArrayList<String>();
	}
	
	public static float abs(float a) {
		return (a <= 0.0F) ? 0.0F - a : a;
	}
	
	public boolean startParce(){
		String priorLine = "";
		int priorID = 0;
		double smetaTotal;
		float partTotal;
		try{
			FileInputStream is = new FileInputStream(arpFile);
			InputStreamReader inputreader = new InputStreamReader(is); 
            BufferedReader buffreader = new BufferedReader(inputreader); 					
			String currLine = "";
			while((currLine = buffreader.readLine())!= null){
				currLine += "#1";
				ExplodeString(currLine, "#");								
			}
			buffreader.close();
			inputreader.close();
			is.close();
			for(int i = 0; i < lineNumberList.size(); i++){
				dataList = lineNumberList.get(i);
				switch(dataList.get(0)){
				case "0":
					if(dataList.get(1).contains("�����")){
						String[] total = dataList.get(1).split("\\s+");
						if(!total[total.length-1].replace(",", ".").equals("")){
							DecimalFormat df = new DecimalFormat("0.###");
							Number num =  df.parse(total[total.length-1].replace(",", "."));
							smetaTotal = num.doubleValue();
							//smetaTotal = Double.parseDouble(total[total.length-1].replace(",", "."));
						}else{
							smetaTotal = 0;
						}
						
						projects.setProjectTotal(smetaTotal);
						addProjects(projects);
					}
					if(dataList.get(1).contains("����� �� �������")){
						String[] total = dataList.get(1).split("\\s+");
						if(!total[total.length-1].replace(",", ".").equals("")){
							DecimalFormat df = new DecimalFormat("0.###");
							Number num =  df.parse(total[total.length-1].replace(",", "."));
							partTotal = num.floatValue();
							//smetaTotal = Double.parseDouble(total[total.length-1].replace(",", "."));
						}else{
							partTotal = 0;
						}
						
						works.setWTotal(partTotal);
						updateWorks(works);
					}
					break;
				case "3":// Project
					projects.setProjectName(projectName);
					if(dataList.size() > 17){
						projects.setProjectCipher(dataList.get(17));
					}else{
						projects.setProjectCipher("");
					}
					projects.setProjectTotal(0);
					projects.setProjectContractor(dataList.get(8));
					projects.setProjectCreatedDate(new Date());
					projects.setProjectCustomer(dataList.get(6));	
					projects.setProjectSortId(getMaxProjectSortID() + 1);
					addProjects(projects);
					
					os.setOsName("��������� �����");
					os.setOsCipher("");
					os.setOsTotal(0);
					os.setOsProjectId(projects.getProjectId());
					os.setOsProjects(projects);
					addOS(os);
					
					ls.setLsName("��������� �����");
					ls.setLsCipher("");
					ls.setLsTotal(0);
					ls.setLsProjectId(projects.getProjectId());
					ls.setLsOsId(os.getOsId());
					ls.setLsProjects(projects);
					ls.setLsHidden(true);
					ls.setLsOs(os);
					addLS(ls);
					break;
				case "10"://Part (������)				
					works.setWName(dataList.get(3));					
					works.setWCipher("");					
					works.setWCipherObosn("");					
					works.setWRec("razdel");
					works.setWPart(0);				
					works.setWCount(0);						
					works.setWMeasured("");						
					works.setWPercentDone(0);	
					works.setWParentNormId(0);
					works.setWCountDone(0);						
					works.setWTotal(0);															
					works.setWNpp(0);						
					works.setWItogo(0);						
					works.setWZP(0);						
					works.setWMach(0);						
					works.setWZPMach(0);						
					works.setWZPTotal(0);						
					works.setWMachTotal(0);						
					works.setWZPMachTotal(0);						
					works.setWTz(0);						
					works.setWTZMach(0);						
					works.setWTZTotal(0);						
					works.setWTZMachTotal(0);											
					works.setWNaklTotal(0);						
					works.setWLSFK(ls);
					works.setWCurrStateDate(new Date());
					works.setWEndDate(new Date());
					works.setWStartDate(new Date());								
					works.setWOSFK(os);
					works.setWProjectFK(projects);
					works.setWParentId(ls.getLsId());
					works.setWProjectId(projects.getProjectId());
					works.setWOsId(os.getOsId());
					works.setWLsId(ls.getLsId());
					addWorks(works);
					priorID = works.getWorkId();
					break;
				case "20"://Norms(�����)
					if(globalNPP == 11)
					{
						globalNPP = 11;
					}
					boolean isWorkRes = isNextNormResource(i+1);					
					String strForParse = "";
					switch(PosZP.operation){
	              	case 0:             
	              	  strForParse =dataList.get(16).replace(",", ".");
	              	  if(! strForParse.equals("")){
	              		  PosZP.price = PosZP.value * Float.parseFloat(strForParse);
	              	  }else{
	              		  PosZP.price = 1;
	              	  }
	              	  break;
	              	case 1:                    		
	              		strForParse =dataList.get(16).replace(",", ".");
		              	  if(! strForParse.equals("")){
		              		  PosZP.price = Float.parseFloat(strForParse) / PosZP.value;
		              	  }else{
		              		  PosZP.price = 1;
		              	  }
	              	  break;
	              	} 
					
					switch(PosMM.operation){                    	  
	              	case 0: 
	              		strForParse =dataList.get(17).replace(",", ".");
	              		if(! strForParse.equals("")){
	              			PosMM.price = PosMM.value * Float.parseFloat(strForParse);
	              		}else{
	              			PosMM.price = 1;
	              		}
	              	  break;
	              	case 1:                    		 
	              		strForParse =dataList.get(17).replace(",", ".");
	              		if(! strForParse.equals("")){
	              			PosMM.price = Float.parseFloat(strForParse) / PosMM.value;
	              		}else{
	              			PosMM.price = 1;
	              		}
	              	  break;
	              	}
					
					switch(PosMAT.operation){
              	  	case 0:                    		 
              	  	strForParse =dataList.get(19).replace(",", ".");
	              	  	if(! strForParse.equals("")){
	              	  		PosMAT.price = PosMAT.value * Float.parseFloat(strForParse);
	              	  	}else{
	              	  		PosMAT.price = 1;
	              	  	}
              		  break;
              	  	case 1:                    	
              	  	strForParse =dataList.get(19).replace(",", ".");
	              	  	if(! strForParse.equals("")){
	              	  		PosMAT.price = Float.parseFloat(strForParse) / PosMAT.value;
	              	  	}else{
	              	  		PosMAT.price = 1;
	              	  	}
              		  break;
              	  	}
					String rec = works.getWRec();
					if(rec != null){
						if(rec.contains("record")){
							parentNormID = works.getWorkId();
						}else{
							parentNormID = 0;
						}							
					}	
					works = new Works();  
                    if (((PosZP.value == PosMM.value) & (PosZP.value == PosMAT.value) & (PosZP.value == PosDV.value)&
                        (PosMM.value == PosMAT.value) & (PosMM.value == PosDV.value) & (PosMAT.value == PosDV.value))&
                        ((PosZP.operation == PosMM.operation) & (PosZP.operation == PosMAT.operation) & (PosZP.operation == PosDV.operation)&
                        (PosMM.operation == PosMAT.operation) & (PosMM.operation == PosDV.operation) & (PosMAT.operation == PosDV.operation)))
                    {                             
                       switch(PosZP.operation){
                       case 0:
                    	   strForParse =dataList.get(15).replace(",", ".");
                    	   if(! strForParse.equals("")){
                    		   works.setWItogo(Float.parseFloat(strForParse) * PosZP.value);
                    	   }else{
                    		   works.setWItogo(0f);
                    	   }
                      	 break;
                       case 1: 
                    	   strForParse =dataList.get(15).replace(",", ".");
                    	   if(! strForParse.equals("")){
                    		   works.setWItogo(Float.parseFloat(strForParse) / PosZP.value);
                    	   }else{
                    		   works.setWItogo(0f);
                    	   }
                       	break;
                       }
                    }else{                             
                    	works.setWItogo(PosZP.price + PosMM.price + PosMAT.price);
                    }								
					works.setWName(dataList.get(4));					
					works.setWCipher(dataList.get(2));					
					works.setWCipherObosn("");					
					works.setWRec("record");					
					works.setWPart(0);
					strForParse = dataList.get(26).replace(",", ".");
					if (! strForParse.equals("")){
						works.setWCount(Float.parseFloat(strForParse));
					}else{
						works.setWCount(0f);
					}
					works.setWMeasured(dataList.get(3));						
					works.setWPercentDone(0);					
					works.setWCountDone(0);						
					works.setWTotal(works.getWItogo() * works.getWCount());															
					works.setWNpp(++globalNPP);						
					//works.setWItogo(Float.parseFloat(dataList.get(15).replace(",", ".")));						
					
					works.setWZP(PosZP.price);
	                works.setWZPTotal(works.getWZP() * works.getWCount());				
	                works.setWMach(PosMM.price);
                    works.setWMachTotal(works.getWMach() * works.getWCount());	
                    works.setWZPMach(PosDV.price);
                    works.setWZPMachTotal(works.getWZPMach() * works.getWCount());
                    
                    strForParse = dataList.get(23).replace(",", ".");
					if (! strForParse.equals("")){
						works.setWTz(Float.parseFloat(dataList.get(23).replace(",", ".")));
					}else{
						works.setWTz(0f);
					}
					strForParse = dataList.get(24).replace(",", ".");
					if (! strForParse.equals("")){
						works.setWTZMach(Float.parseFloat(dataList.get(24).replace(",", ".")));
					}else{
						works.setWTZMach(0f);
					}
					works.setWTZTotal(works.getWTz() * works.getWCount());						
					works.setWTZMachTotal(works.getWTZMach() * works.getWCount());								
					works.setWNaklTotal(0);	
					
					works.setWLSFK(ls);
					works.setWCurrStateDate(new Date());
					works.setWEndDate(new Date());
					works.setWStartDate(new Date());								
					works.setWOSFK(os);
					works.setWProjectFK(projects);
					works.setWProjectId(projects.getProjectId());
					works.setWOsId(os.getOsId());
					works.setWLsId(ls.getLsId());
					works.setWParentId(ls.getLsId());										
					works.setWSortOrder(getMaxWorksSortid() + 1);
					try{																			                   				
						if( (abs(PosMAT.price - works.getWItogo()) <= 0.2f) & (! isWorkRes)){
							works.setWRec("resource");
						}
						if ((abs(PosMM.price - works.getWItogo()) <= 0.2)&(! isWorkRes)){
							works.setWRec("machine");
						}
						if((works.getWRec().contains("resource"))|(works.getWRec().contains("machine"))){
							works.setWParentNormId(parentNormID);
						}
					}catch(Exception e){
						e.printStackTrace();
					}
					addWorks(works);
					break;				
				case "30"://Resources (������)
					worksres = new WorksResources();
					worksres.setWrName(dataList.get(3));					
					worksres.setWrCipher(dataList.get(1));						
					worksres.setWrMeasured(dataList.get(2));
					if(! dataList.get(5).equals("")){
						worksres.setWrCount(Float.parseFloat(dataList.get(5).replace(",", ".")));
					}else{
						worksres.setWrCount(0);
					}
					//Need do like in KLP
					if(! dataList.get(6).equals("")){	
						worksres.setWrCost(Float.parseFloat(dataList.get(6).replace(",", ".")));
					}else{
						worksres.setWrCost(0);
					}					
					worksres.setWrTotalCost(worksres.getWrCount() * worksres.getWrCost());
					if(! dataList.get(4).equals("")){
						worksres.setWrPart(Integer.parseInt(dataList.get(4)));
					}else{
						worksres.setWrPart(2);
					}
					worksres.setWrOnOff(-1);
					worksres.setWrWork(works);
					worksres.setWrWorkId(works.getWorkId());
					addWorksRes(worksres);
					break;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	private void ExplodeString(String explodedstr, String border){
		List<String> datalist = new ArrayList<String>();
		String exploded = explodedstr + border;
		String[] temp = exploded.split(border);
		for(int i = 0; i < temp.length; i++){
			datalist.add(temp[i]);
		}
		lineNumberList.add(datalist);
	}
	
	private boolean isNextNormResource(int next){
		List<String> tempDataList = new ArrayList<String>();
		for(int i = next; i < lineNumberList.size(); i++){
			tempDataList = lineNumberList.get(i);
			if(tempDataList.get(0).equals("25")){
				if (tempDataList.get(1).equals("0"))
				{
                  switch(tempDataList.get(2)){
                  case "0":
                	  PosZP.value = Float.parseFloat(tempDataList.get(4).replace(",", "."));
                	  PosZP.operation = Byte.parseByte(tempDataList.get(3));	                    	                    	  
                      break;
                  case "1":
                	  PosMM.value = Float.parseFloat(tempDataList.get(4).replace(",", "."));
                	  PosMM.operation = Byte.parseByte(tempDataList.get(3));	                    	                     	  
                      break;
                  case "2":
                	  PosMAT.value = Float.parseFloat(tempDataList.get(4).replace(",", "."));
                	  PosMAT.operation = Byte.parseByte(tempDataList.get(3));	                    	          
                      break;
                  case "3":
                	  PosDV.value = Float.parseFloat(tempDataList.get(4).replace(",", "."));  
                	  PosDV.operation = Byte.parseByte(tempDataList.get(3));
                      break;
                  }
				}else{
					PosZP.value = 1;
                    PosZP.operation = 0;
                    PosMM.value = 1;
                    PosMM.operation = 0;
                    PosMAT.value = 1;
                    PosMAT.operation = 0;
                    PosDV.value = 1;
                    PosDV.operation = 0;
				}				
			}else if (tempDataList.get(0).equals("30")){
					return true;
			}else if ((tempDataList.get(0).equals("20"))|(tempDataList.get(0).equals("10"))){
					return false;
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
		try{
			projectsDao.createOrUpdate(proj);
		}catch(SQLException e){
			Log.i(LOGTAG, e.getMessage().toString());
		}
	}
	
	private void addOS(OS os){		
		os.setOsSortId(getMaxOSSortID() + 1);
		try{
			osDao.createOrUpdate(os);
		}catch(SQLException e){
			Log.i(LOGTAG, e.getMessage().toString());
		}
	}
	
	private void addLS(LS ls){	
		ls.setLsSortId(getMaxLSSortID() + 1);
		try{
			lsDao.createOrUpdate(ls);
		}catch(SQLException e){
			Log.i(LOGTAG, e.getMessage().toString());
		}
	}

	private void addWorks(Works work){		
		try{
			worksDao.create(work);
		}catch(SQLException e){
			Log.i(LOGTAG, e.getMessage().toString());
		}
	}
	
	private void updateWorks(Works work){		
		try{
			worksDao.createOrUpdate(work);
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
	
	private void updateWorksRes(WorksResources wr){			
		try{
			worksresDao.createOrUpdate(wr);
		}catch(SQLException e){
			Log.i(LOGTAG, e.getMessage().toString());
		}
	}
	
	class KoefValue{
		float value;
		float price;
		byte operation;
	}

}
