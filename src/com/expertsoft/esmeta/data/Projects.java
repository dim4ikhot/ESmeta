package com.expertsoft.esmeta.data;

import java.io.Serializable;
import java.util.Date;

import com.j256.ormlite.field.DatabaseField;

public class Projects implements Serializable {

	private static final long serialVersionUID = -222864131214757024L;
	public static final String TP_FIELD_ID = "_id";
	public static final String TP_FIELD_NAME = "proj_name";
	public static final String TP_FIELD_CIPHER = "proj_cipher";
	public static final String TP_FIELD_CREATEDATE="proj_create_date";
	public static final String TP_FIELD_CUSTOMER="proj_customer";
	public static final String TP_FIELD_CONTRACTOR = "proj_contractor";
	public static final String TP_FIELD_SORTID = "proj_sort_id";
	public static final String TP_FIELD_TOTAL = "proj_total";
	
	
	@DatabaseField(canBeNull = false, generatedId = true, columnName = TP_FIELD_ID)
	private int projId;
	
	@DatabaseField(columnName = TP_FIELD_NAME)
	private String projName;
	
	@DatabaseField(columnName = TP_FIELD_CIPHER)
	private String projCipher;
	
	@DatabaseField(columnName = TP_FIELD_CREATEDATE)
	private Date projCreateDate;
	
	@DatabaseField(columnName = TP_FIELD_CUSTOMER)
	private String projCustomer;
	
	@DatabaseField(columnName = TP_FIELD_CONTRACTOR)
	private String projContractor;
	
	@DatabaseField(columnName = TP_FIELD_SORTID)
	private int projSortId;
	
	@DatabaseField(columnName = TP_FIELD_TOTAL)
	private double projTotal;
	
	public int getProjectId(){
		return projId;
	}
	
	//Setter&Getter of project name
	public void setProjectName(String name){
		projName = name;
	}
	
	public String getProjectName(){
		return projName;
	}
	
	//Setter&Getter cipher
	public void setProjectCipher(String cipher){
		projCipher = cipher;
	}
	
	public String getProjectCipher(){
		return projCipher;
	}
	
	//...Created Date
	public void setProjectCreatedDate(Date date){
		projCreateDate = date;
	}
	
	public Date getProjectCreatedDate(){
		return projCreateDate;
	}
	
	//... Customer
	public void setProjectCustomer(String customer){
		projCustomer = customer;
	}
	
	public String getProjectCustomer(){
		return projCustomer;
	}
	
	//... Contractor
	public void setProjectContractor(String contractorName){
		projContractor = contractorName;
	}
	
	public String getProjectContractor(){
		return projContractor;
	}
	
	//... SortId
	public void setProjectSortId(int  sortId){
		projSortId = sortId;
	}
	
	public int getProjectSortId(){
		return projSortId;
	}
	
	//... Total
	public void setProjectTotal(double total){
		projTotal = total;
	}
	
	public double getProjectTotal(){
		return projTotal;
	}
	
	//default constructor
	public Projects() {
		// TODO Auto-generated constructor stub
	}

	
	public Projects(String name,String cipher,String customer,String contractor, Date createdate, double total, int sortid) {
		// TODO Auto-generated constructor stub
		projName = name;
		projCipher = cipher;
		projCustomer = customer;
		projContractor = contractor;
		projCreateDate = createdate;
		projTotal = total;
		projSortId = sortid;
		
	}
}
