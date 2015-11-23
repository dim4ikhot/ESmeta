package com.expertsoft.esmeta.data;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;

public class OS implements Serializable {

	private static final long serialVersionUID = -222864131214757024L;
	
	public static final String TOS_FIELD_ID = "_id";
	public static final String TOS_FIELD_PROJECT_ID = "projectid";
	public static final String TOS_FIELD_NAME = "os_name";
	public static final String TOS_FIELD_CIPHER = "os_cipher";
	public static final String TOS_FIELD_SORT_ID = "os_sort_id";
	public static final String TOS_FIELD_TOTAL = "os_total";	

	@DatabaseField(canBeNull = false, generatedId = true, columnName = TOS_FIELD_ID)
	private int osId;
	
	@DatabaseField(columnName = TOS_FIELD_PROJECT_ID)
	private int osProjectId;
	
	@DatabaseField(columnName = TOS_FIELD_NAME)
	private String osName;
	
	@DatabaseField(columnName = TOS_FIELD_CIPHER)
	private String osCipher;
	
	@DatabaseField(columnName = TOS_FIELD_SORT_ID)
	private int osSortId;
	
	@DatabaseField(columnName = TOS_FIELD_TOTAL)
	private float osTotal;
	
	@DatabaseField(canBeNull = false, foreign = true, foreignAutoRefresh = true)
	private Projects osProjects;
	
	public int getOsId(){
		return osId;
	}
	
	//Setter&Getter of project id
	public void setOsProjectId(int projId){
		osProjectId = projId;
	}
	
	public int getOsProjectId(){
		return osProjectId;
	}
	
	//... Name
	public void setOsName(String Name){
		osName = Name;
	}
	
	public String getOsName(){
		return osName;
	}
	//... Shifr
	public void setOsCipher(String cipher){
		osCipher = cipher;
	}
	
	public String getOsCipher(){
		return osCipher;
	}
	//... SortId
	public void setOsSortId(int sortId){
		osSortId = sortId;
	}
	
	public int getOsSortId(){
		return osSortId;
	}
	//... Total
	public void setOsTotal(float total){
		osTotal = total;
	}
	
	public float getOsTotal(){
		return osTotal;
	}
	//... Foreign key Projects
	public void setOsProjects(Projects proj){
		osProjects = proj;
	}
	
	public Projects getOsProjects(){
		return osProjects;
	}
	public OS() {
		// TODO Auto-generated constructor stub
	}

	public OS(String name, String cipher, int sortid, float total, Projects projects) {
		
		osName = name;
		osCipher = cipher;
		osSortId = sortid;
		osTotal = total;
		osProjects = projects;
	}
	
}
