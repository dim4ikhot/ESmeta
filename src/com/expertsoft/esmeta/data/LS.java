package com.expertsoft.esmeta.data;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;

public class LS implements Serializable {

	private static final long serialVersionUID = -222864131214757024L;
	
	public static final String TLS_FIELD_ID = "_id";
	public static final String TLS_FIELD_PROJECT_ID = "projectid";
	public static final String TLS_FIELD_OS_ID = "osid";
	public static final String TLS_FIELD_HIDDEN = "ls_hidden";
	public static final String TLS_FIELD_NAME = "ls_name";
	public static final String TLS_FIELD_CIPHER = "ls_cipher";
	public static final String TLS_FIELD_SORT_ID = "ls_sort_id";
	public static final String TLS_FIELD_TOTAL = "ls_total";	

	@DatabaseField(canBeNull = false, generatedId = true, columnName = TLS_FIELD_ID)
	private int lsId;
	
	@DatabaseField(columnName = TLS_FIELD_PROJECT_ID)
	private int lsProjectId;
	
	@DatabaseField(columnName = TLS_FIELD_OS_ID)
	private int lsOsId;
	
	@DatabaseField(columnName = TLS_FIELD_HIDDEN)
	private boolean lsHidden;
	
	@DatabaseField(columnName = TLS_FIELD_NAME)
	private String lsName;
	
	@DatabaseField(columnName = TLS_FIELD_CIPHER)
	private String lsCipher;
	
	@DatabaseField(columnName = TLS_FIELD_SORT_ID)
	private int lsSortId;
	
	@DatabaseField(columnName = TLS_FIELD_TOTAL)
	private float lsTotal;
	
	@DatabaseField(canBeNull = false, foreign = true)
	private Projects lsProjects;
	
	@DatabaseField(canBeNull = false, foreign = true, foreignAutoRefresh = true)
	private OS lsOsTable;
	
	public int getLsId(){
		return lsId;
	}
	
	//Setter&Getter of project id
	public void setLsProjectId(int projId){
		lsProjectId = projId;
	}
	
	public int getLsProjectId(){
		return lsProjectId;
	}
	
	//... Os id
	public void setLsOsId(int osId){
		lsOsId = osId;
	}
	
	public int getLsOsId(){
		return lsOsId;
	}
	
	//... Hidden
	public void setLsHidden(boolean isHidden){
		lsHidden = isHidden;
	}
	
	public boolean getLsHidden(){
		return lsHidden;
	}
	
	//... Name
	public void setLsName(String Name){
		lsName = Name;
	}
	
	public String getLsName(){
		return lsName;
	}
	//... Shifr
	public void setLsCipher(String cipher){
		lsCipher = cipher;
	}
	
	public String getLsCipher(){
		return lsCipher;
	}
	//... SortId
	public void setLsSortId(int sortId){
		lsSortId = sortId;
	}
	
	public int getLsSortId(){
		return lsSortId;
	}
	//... Total
	public void setLsTotal(float total){
		lsTotal = total;
	}
	
	public float getLsTotal(){
		return lsTotal;
	}
	//... Foreign key Projects
	public void setLsProjects(Projects proj){
		lsProjects = proj;
	}
	
	public Projects getLsProjects(){
		return lsProjects;
	}
	
	//... Foreign key OS
	public void setLsOs(OS os){
		lsOsTable = os;
	}
	
	public OS getLsOs(){
		return lsOsTable;
	}
	
	public LS() {
		// TODO Auto-generated constructor stub
	}

	public LS(String name, String cipher,boolean hidden, int sortid, float total, Projects projects, OS ostable) {
		
		lsName = name;
		lsCipher = cipher;
		lsHidden = hidden;
		lsSortId = sortid;
		lsTotal = total;
		lsProjects = projects;
		lsOsTable = ostable;
	}
	
}

