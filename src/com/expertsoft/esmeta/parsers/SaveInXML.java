package com.expertsoft.esmeta.parsers;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import android.util.Log;
import android.util.Xml;

import com.expertsoft.esmeta.DBORM;
import com.expertsoft.esmeta.data.LS;
import com.expertsoft.esmeta.data.ORMDatabaseHelper;
import com.expertsoft.esmeta.data.OS;
import com.expertsoft.esmeta.data.Projects;
import com.expertsoft.esmeta.data.Works;
import com.expertsoft.esmeta.data.WorksResources;

public class SaveInXML {

	public static final String LOGTAG = "SaveBuild";  
	
	Projects projects;
	OS os;
	LS ls;
	Works works;
	WorksResources worksres;
		
	List<OS> osList;
	List<LS> lsList;
	List<Works> worksList;
	List<Works> worksListChild;
	List<WorksResources> worksresList;
	List<WorksResources> worksresListChild;
	
	String attrName,attrValue;
	DBORM database;
	ORMDatabaseHelper helper;
	String fileSave;
	String rec;
	String exec;
	
	public SaveInXML(DBORM database, Projects pr, String fileSave) {
		// TODO Auto-generated constructor stub
		this.database = database;
		helper = database.getHelper();
		projects = pr;
		this.fileSave = fileSave;						
	}
	
	private String getExecString(Date execDate, float countDone){
		SimpleDateFormat sdf = new SimpleDateFormat("MM.yyyy");		
		String str = sdf.format(execDate) + "-" + String.valueOf(countDone).replace(".", ",") + ";";
		return str;
 	}
	
	public boolean startSave(){
		try{
			XmlSerializer serializer = Xml.newSerializer();	        	
			OutputStream out = new FileOutputStream(fileSave);						
			serializer.setOutput(out, "UTF-8");
			serializer.startDocument("windows-1251", Boolean.valueOf(true));
			//Save project			
			serializer.startTag(null, "—тройка");
			serializer.attribute(null, "STROIKANAMEBRIEF", projects.getProjectName());
			if (projects.getProjectCipher() != null){
			  serializer.attribute(null, "STROIKAPROJECTSHIFR", projects.getProjectCipher());
			}else{
				serializer.attribute(null, "STROIKAPROJECTSHIFR", "");
			}
			SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
			serializer.attribute(null, "STROIKACREATIONDATE", sdf.format(projects.getProjectCreatedDate()));
			serializer.attribute(null, "STROIKAZAKAZCHIK", projects.getProjectCustomer());
			serializer.attribute(null, "STROIKAGENPODR", projects.getProjectContractor());
			serializer.attribute(null, "STROIKATOTAL", String.valueOf(projects.getProjectTotal()).replace(".", ","));			
			
			osList = database.getObjectEstimate(projects);
			for(int osi = 0; osi< osList.size(); osi++){
				os = osList.get(osi);
				//save OS to XML 
				serializer.startTag(null, "ќбъектна€—мета");
				serializer.attribute(null, "OSNAME", os.getOsName());
				if (os.getOsCipher() != null){
					serializer.attribute(null, "OSNOOS", os.getOsCipher());
				}else{
					serializer.attribute(null, "OSNOOS", "");
				}
				serializer.attribute(null, "OSTOTAL", String.valueOf(os.getOsTotal()).replace(".", ","));				
				
				lsList = database.getLocalEstimate(projects, os);				
				for(int lsi = 0; lsi< lsList.size(); lsi++){
					ls = lsList.get(lsi);
					//save LS to XML 
					serializer.startTag(null, "Ћокальна€—мета");
					serializer.attribute(null, "LSNAME", ls.getLsName());
					if(ls.getLsCipher() != null){
						serializer.attribute(null, "LSNOLS", ls.getLsCipher());
					}else{
						serializer.attribute(null, "LSNOLS", "");
					}
					serializer.attribute(null, "LSTOTAL", String.valueOf(ls.getLsTotal()).replace(".", ","));					
					
					worksList = database.getWorks(projects, os, ls);					
					for(int wi = 0; wi< worksList.size(); wi++){
						works = worksList.get(wi);
						//save Works to XML 
						serializer.startTag(null, "ѕозици€Ћокальной—меты");
						
						serializer.attribute(null, "SLSNAME", works.getWName());
						serializer.attribute(null, "SLSSHIFR", works.getWCipher());
						serializer.attribute(null, "SLSOBOSN", works.getWCipherObosn());
						// с руком нужно погойдатьс€
						rec = works.getWRec();
						if((rec.contains("resources"))|(rec.contains("machines"))){
							serializer.attribute(null, "SLSREC", "record");
						}else{
							serializer.attribute(null, "SLSREC", rec);
						}						
						serializer.attribute(null, "SLSRAZDEL", String.valueOf(works.getWPart()));
						serializer.attribute(null, "SLSKOLVO", String.valueOf(works.getWCount()).replace(".", ","));
						serializer.attribute(null, "SLSIZM", works.getWMeasured());
						serializer.attribute(null, "SLSKOLVO_PRCNT", String.valueOf(works.getWPercentDone()).replace(".", ","));
						serializer.attribute(null, "SLSKOLVO_DONE", String.valueOf(works.getWCountDone()).replace(".", ","));
						serializer.attribute(null, "SLSTOTAL", String.valueOf(works.getWTotal()).replace(".", ","));
						serializer.attribute(null, "SLSNPP", String.valueOf(works.getWNpp()).replace(".", ","));
						exec = getExecString(works.getWStartDate(), works.getWCountDone());
						serializer.attribute(null, "SLSEXEC", exec);
						serializer.attribute(null, "SLSITOGO", String.valueOf(works.getWItogo()).replace(".", ","));
						serializer.attribute(null, "SLSZP", String.valueOf(works.getWZP()).replace(".", ","));
						serializer.attribute(null, "SLSMACH", String.valueOf(works.getWMach()).replace(".", ","));
						serializer.attribute(null, "SLSZPMACH", String.valueOf(works.getWZPMach()).replace(".", ","));
						serializer.attribute(null, "SLSZPTOTAL", String.valueOf(works.getWZPTotal()).replace(".", ","));
						serializer.attribute(null, "SLSMACHTOTAL", String.valueOf(works.getWMachTotal()).replace(".", ","));
						serializer.attribute(null, "SLSZPMACHTOTAL", String.valueOf(works.getWZPMachTotal()).replace(".", ","));
						serializer.attribute(null, "SLSTZ", String.valueOf(works.getWTz()).replace(".", ","));
						serializer.attribute(null, "SLSTZMACH", String.valueOf(works.getWTZMach()).replace(".", ","));
						serializer.attribute(null, "SLSTZTOTAL",String.valueOf(String.valueOf(works.getWTZTotal())).replace(".", ","));
						serializer.attribute(null, "SLSTZMACHTOTAL", String.valueOf(works.getWTZMachTotal()).replace(".", ","));
						serializer.attribute(null, "SLSNAKLTOTAL", String.valueOf(works.getWNaklTotal()).replace(".", ","));																																
						
						worksresList = database.getWorksResource(works);
						for(int wri = 0; wri< worksresList.size(); wri++){
							worksres = worksresList.get(wri);
							//save WorksResources to XML 
							serializer.startTag(null, "—оставѕозиции");
							serializer.attribute(null,"RSNAME", worksres.getWrName());
							serializer.attribute(null, "RSSHIFR", worksres.getWrCipher());
							serializer.attribute(null, "RSIZM", worksres.getWrMeasured());
							serializer.attribute(null, "RSKOLVO1", String.valueOf(worksres.getWrCount()).replace(".", ","));
							serializer.attribute(null, "RSSTOIM1", String.valueOf(worksres.getWrCost()).replace(".", ","));
							serializer.attribute(null, "RSVSEGO1", String.valueOf(worksres.getWrTotalCost()).replace(".", ","));
							serializer.attribute(null, "RSONOFF", String.valueOf(worksres.getWrOnOff()));
							serializer.attribute(null, "RSRAZDEL", String.valueOf(worksres.getWrPart()).replace(".", ","));
							serializer.endTag(null, "—оставѕозиции");																												
						}
						serializer.endTag(null, "ѕозици€Ћокальной—меты");
						
						worksListChild = database.getWorksChild(projects, os, ls, works);
						if (worksListChild.size() > 0 ){
							for(int wci = 0; wci< worksListChild.size(); wci++){
								works = worksListChild.get(wci);
								//save WorksChild to XML 
								serializer.startTag(null, "ѕозици€Ћокальной—меты");
								
								serializer.attribute(null, "SLSNAME", works.getWName());
								serializer.attribute(null, "SLSSHIFR", works.getWCipher());
								serializer.attribute(null, "SLSOBOSN", works.getWCipherObosn());
								// с руком нужно погойдатьс€
								rec = works.getWRec();
								if((rec.contains("resources"))|(rec.contains("machines"))){
									serializer.attribute(null, "SLSREC", "record");
								}else{
									serializer.attribute(null, "SLSREC", rec);
								}						
								serializer.attribute(null, "SLSRAZDEL", String.valueOf(works.getWPart()));
								serializer.attribute(null, "SLSKOLVO", String.valueOf(works.getWCount()).replace(".", ","));
								serializer.attribute(null, "SLSIZM", works.getWMeasured());
								serializer.attribute(null, "SLSKOLVO_PRCNT", String.valueOf(works.getWPercentDone()).replace(".", ","));
								serializer.attribute(null, "SLSKOLVO_DONE", String.valueOf(works.getWCountDone()).replace(".", ","));
								serializer.attribute(null, "SLSTOTAL", String.valueOf(works.getWTotal()).replace(".", ","));
								serializer.attribute(null, "SLSNPP", String.valueOf(works.getWNpp()).replace(".", ","));
								exec = getExecString(works.getWStartDate(), works.getWCountDone());
								serializer.attribute(null, "SLSEXEC", exec);
								serializer.attribute(null, "SLSITOGO", String.valueOf(works.getWItogo()).replace(".", ","));
								serializer.attribute(null, "SLSZP", String.valueOf(works.getWZP()).replace(".", ","));
								serializer.attribute(null, "SLSMACH", String.valueOf(works.getWMach()).replace(".", ","));
								serializer.attribute(null, "SLSZPMACH", String.valueOf(works.getWZPMach()).replace(".", ","));
								serializer.attribute(null, "SLSZPTOTAL", String.valueOf(works.getWZPTotal()).replace(".", ","));
								serializer.attribute(null, "SLSMACHTOTAL", String.valueOf(works.getWMachTotal()).replace(".", ","));
								serializer.attribute(null, "SLSZPMACHTOTAL", String.valueOf(works.getWZPMachTotal()).replace(".", ","));
								serializer.attribute(null, "SLSTZ", String.valueOf(works.getWTz()).replace(".", ","));
								serializer.attribute(null, "SLSTZMACH", String.valueOf(works.getWTZMach()).replace(".", ","));
								serializer.attribute(null, "SLSTZTOTAL",String.valueOf(String.valueOf(works.getWTZTotal())).replace(".", ","));
								serializer.attribute(null, "SLSTZMACHTOTAL", String.valueOf(works.getWTZMachTotal()).replace(".", ","));
								serializer.attribute(null, "SLSNAKLTOTAL", String.valueOf(works.getWNaklTotal()).replace(".", ","));																																			
								
								worksresListChild = database.getWorksResource(works);
								for(int wri = 0; wri<worksresListChild.size(); wri++){
									worksres = worksresListChild.get(wri);
									//save WorksResources to XML 
									serializer.startTag(null, "—оставѕозиции");
									serializer.attribute(null,"RSNAME", worksres.getWrName());
									serializer.attribute(null, "RSSHIFR", worksres.getWrCipher());
									serializer.attribute(null, "RSIZM", worksres.getWrMeasured());
									serializer.attribute(null, "RSKOLVO1", String.valueOf(worksres.getWrCount()).replace(".", ","));
									serializer.attribute(null, "RSSTOIM1", String.valueOf(worksres.getWrCost()).replace(".", ","));
									serializer.attribute(null, "RSVSEGO1", String.valueOf(worksres.getWrTotalCost()).replace(".", ","));
									serializer.attribute(null, "RSONOFF", String.valueOf(worksres.getWrOnOff()));
									serializer.attribute(null, "RSRAZDEL", String.valueOf(worksres.getWrPart()).replace(".", ","));
									serializer.endTag(null, "—оставѕозиции");																												
								}
								serializer.endTag(null, "ѕозици€Ћокальной—меты");
							}
						}
						
					}
					serializer.endTag(null, "Ћокальна€—мета");
				}
				serializer.endTag(null, "ќбъектна€—мета");
			}
			serializer.endTag(null, "—тройка");									
			serializer.endDocument();
			serializer.flush();
			out.close();			
			//STOPED parse			
			Log.i(LOGTAG, "Loading stoped");
		}catch(Exception e){
			Log.i(LOGTAG,  e.getMessage().toString());
			return false;
		}
		return true;
	}

}
