package com.expertsoft.esmeta.data;

import com.j256.ormlite.field.DatabaseField;

public class WorksResources {

	public static final String TWS_FIELD_ID = "_id";
	public static final String TWS_FIELD_WORK_ID = "workid";
	public static final String TWS_FIELD_NAME = "workres_name";
	public static final String TWS_FIELD_CIPHER = "workres_cipher";
	public static final String TWS_FIELD_MEASURED = "workres_measured";
	public static final String TWS_FIELD_COUNT = "workres_count";
	public static final String TWS_FIELD_COST = "workres_cost";
	public static final String TWS_FIELD_TOTALCOST = "workres_totalcost";
	public static final String TWS_FIELD_PART = "workres_part";
	
	@DatabaseField(canBeNull = false, generatedId = true, columnName = TWS_FIELD_ID)
	private int workResId;
	
	@DatabaseField(columnName = TWS_FIELD_WORK_ID)
	private int wrWorkId;
	
	@DatabaseField(columnName = TWS_FIELD_NAME)
	private String wrName;
	
	@DatabaseField(columnName = TWS_FIELD_CIPHER)
	private String wrCipher;
	
	@DatabaseField(columnName = TWS_FIELD_MEASURED)
	private String wrMeasured;
	
	@DatabaseField(columnName = TWS_FIELD_COUNT)
	private float wrCount;
	
	@DatabaseField(columnName = TWS_FIELD_COST)
	private float wrCost;
	
	@DatabaseField(columnName = TWS_FIELD_TOTALCOST)
	private float wrTotalCost;
	
	@DatabaseField(columnName = TWS_FIELD_PART)
	private int wrPart;
	
	@DatabaseField(canBeNull = false, foreign = true, foreignAutoRefresh = true)
	private Works wrWFK;
	
	public int getWrId(){
		return workResId;
	}
	
	// Setter & Getter of works resource Work id
	public void setWrWorkId(int workId){
		wrWorkId = workId;
	}
	
	public int getWrWorkId(){
		return wrWorkId;
	}
	
	//... Name
	public void setWrName(String name){
		wrName = name;
	}
	
	public String getWrName(){
		return wrName;
	}
	//... Cipher
	public void setWrCipher(String cipher){
		wrCipher = cipher;
	}
	
	public String getWrCipher(){
		return wrCipher;
	}
	//... Measured
	public void setWrMeasured(String measured){
		wrMeasured = measured;
	}
	
	public String getWrMeasured(){
		return wrMeasured;
	}
	//... Count
	public void setWrCount(float count){
		wrCount = count;
	}
	
	public float getWrCount(){
		return wrCount;
	}
	//... Cost
	public void setWrCost(float cost){
		wrCost = cost;
	}
	
	public float getWrCost(){
		return wrCost;
	}
	//... Total Cost
	public void setWrTotalCost(float totalCost){
		wrTotalCost = totalCost;
	}
	
	public float getWrTotalCost(){
		return wrTotalCost;
	}
	//... Part
	public void setWrPart(int part){
		wrPart = part;
	}
	
	public int getWrPart(){
		return wrPart;
	}
	//... Foreign Works
	public void setWrWork(Works work){
		wrWFK = work;
	}
	
	public int getWrWork(){
		return wrWorkId;
	}
	public WorksResources() {
		// TODO Auto-generated constructor stub
	}

	public WorksResources(String wrname, String wrcipher, String wrmeas, float wrcount, 
			              float wrcost, float wrtotalcost,int wrpart, Works worksfk) {
		// TODO Auto-generated constructor stub
		wrName = wrname;
		wrCipher = wrcipher;
		wrMeasured = wrmeas;
		wrCount = wrcount;
		wrCost = wrcost;
		wrTotalCost = wrtotalcost;
		wrPart = wrpart;
		wrWFK = worksfk;
	}
}
