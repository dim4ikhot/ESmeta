package com.expertsoft.esmeta.data;

import java.io.Serializable;
import java.util.Date;

import com.j256.ormlite.field.DatabaseField;

public class Works implements Serializable {

	public static final String TABLE_WORKS_NAME = "WORKS";
	public static final String TW_FIELD_ID = "_id";
	public static final String TW_FIELD_PROJECT_ID = "projectid";
	public static final String TW_FIELD_OS_ID = "osid";
	public static final String TW_FIELD_LS_ID = "lsid";
	
	public static final String TW_FIELD_PARENT_ID = "work_parent_id";
	public static final String TW_FIELD_PARENT_NORM_ID = "work_parent_norm_id";
	public static final String TW_FIELD_NAME = "work_name";
	public static final String TW_FIELD_CIPHER = "work_cipher";
	public static final String TW_FIELD_CIPHER_OBOSN = "work_shifr_obosn";
	public static final String TW_FIELD_REC = "work_rec";
	public static final String TW_FIELD_PART = "work_part";
	public static final String TW_FIELD_COUNT = "work_count";
	public static final String TW_FIELD_MEASURED = "work_measured";	
	public static final String TW_FIELD_DATE_START = "work_date_start";
	public static final String TW_FIELD_DATE_END = "work_date_end";
	public static final String TW_FIELD_DATE_FOR_CURR_STATE = "work_date_for_curr_state";
	public static final String TW_FIELD_PERCENT_DONE = "work_percent_done";
	public static final String TW_FIELD_COUNT_DONE = "work_count_done";
	public static final String TW_FIELD_TOTAL = "work_total";
	public static final String TW_FIELD_NPP = "work_npp";
	public static final String TW_FIELD_SORT_ORDER = "work_sort_order";
	public static final String TW_FIELD_ITOGO = "work_itogo";
	public static final String TW_FIELD_ZP ="work_zp";
	public static final String TW_FIELD_MACH ="work_mach";
	public static final String TW_FIELD_ZPMACH ="work_zpmach";
	public static final String TW_FIELD_ZPTOTAL ="work_zptotal";
	public static final String TW_FIELD_MACHTOTAL ="work_machtotal";
	public static final String TW_FIELD_ZPMACHTOTAL ="work_zpmachtotal";
	public static final String TW_FIELD_TZ ="work_tz";
	public static final String TW_FIELD_TZMACH ="work_tzmach";
	public static final String TW_FIELD_TZTOTAL ="work_tztotal";
	public static final String TW_FIELD_TZMACHTOTAL ="work_tzmachtotal";
	public static final String TW_FIELD_NALTOTAL ="work_nakltotal";
	public static final String TW_FIELD_ADMIN ="work_admin";
	public static final String TW_FIELD_PROFIT ="work_profit";
	
	private static final long serialVersionUID = -222864131214757024L;
	
	@DatabaseField(canBeNull = false, generatedId = true, columnName = TW_FIELD_ID)
	private int workId;
	
	@DatabaseField(columnName = TW_FIELD_PROJECT_ID)
	private int wProjectId;
	
	@DatabaseField(columnName = TW_FIELD_OS_ID)
	private int wOsId;
	
	@DatabaseField(columnName = TW_FIELD_LS_ID)
	private int wLsId;
	
	@DatabaseField(canBeNull = false, columnName = TW_FIELD_PARENT_ID)
	private int wParentId;
	
	@DatabaseField(canBeNull = false, columnName = TW_FIELD_PARENT_NORM_ID)
	private int wParentNormId;
	
	@DatabaseField(columnName = TW_FIELD_NAME )
	private String wName;
	
	@DatabaseField(columnName = TW_FIELD_CIPHER)
	private String wCipher;
	
	@DatabaseField(columnName = TW_FIELD_CIPHER_OBOSN)
	private String wCipherObosn;
	
	@DatabaseField(columnName = TW_FIELD_REC)
	private String wRec;
	
	@DatabaseField(columnName = TW_FIELD_PART)
	private int wPart;
	
	@DatabaseField(columnName = TW_FIELD_COUNT)
	private float wCount;
	
	@DatabaseField(columnName = TW_FIELD_MEASURED)
	private String wMeasured;
	
	@DatabaseField(format = "dd.mm.yyyy HH:nn", columnName = TW_FIELD_DATE_START)
	private Date wStartDate;
	
	@DatabaseField(format = "dd.mm.yyyy HH:nn", columnName = TW_FIELD_DATE_END)
	private Date wEndDate;
	
	@DatabaseField(format = "dd.mm.yyyy HH:nn", columnName =TW_FIELD_DATE_FOR_CURR_STATE)
	private Date wCurrStateDate;
	
	@DatabaseField(columnName = TW_FIELD_PERCENT_DONE)
	private float wPercentDone;
	
	@DatabaseField(columnName = TW_FIELD_COUNT_DONE)
	private float wCountDone;
	
	@DatabaseField(columnName = TW_FIELD_TOTAL)
	private float wTotal;
	
	@DatabaseField(columnName = TW_FIELD_NPP)
	private int wNPP;
	
	@DatabaseField(columnName = TW_FIELD_SORT_ORDER)
	private int wSortOrder;
	
	@DatabaseField(columnName = TW_FIELD_ITOGO)
	private float wItogo;
	
	@DatabaseField(columnName = TW_FIELD_ZP)
	private float wZP;
		
	@DatabaseField(columnName = TW_FIELD_MACH)
	private float wMach;
	
	@DatabaseField(columnName = TW_FIELD_ZPMACH)
	private float wZPMach;
	
	@DatabaseField(columnName = TW_FIELD_ZPTOTAL)
	private float wZPTotal;
	
	@DatabaseField(columnName = TW_FIELD_MACHTOTAL)
	private float wMachTotal;
	
	@DatabaseField(columnName = TW_FIELD_ZPMACHTOTAL)
	private float wZPMachTotal;
	
	@DatabaseField(columnName = TW_FIELD_TZ)
	private float wTZ;
	
	@DatabaseField(columnName = TW_FIELD_TZMACH)
	private float wTZMach;
	
	@DatabaseField(columnName = TW_FIELD_TZTOTAL)
	private float wTZTotal;
	
	@DatabaseField(columnName = TW_FIELD_TZMACHTOTAL)
	private float wTZMachTotal;
	
	@DatabaseField(columnName = TW_FIELD_NALTOTAL)
	private float wNaklTotal;
	
	@DatabaseField(columnName = TW_FIELD_ADMIN)
	private float wAdmin;
	
	@DatabaseField(columnName = TW_FIELD_PROFIT)
	private float wProfit;
	
	@DatabaseField(canBeNull = false, foreign = true)
	private Projects wProjectFK;
	
	@DatabaseField(canBeNull = false, foreign = true)
	private OS wOSFK;
	
	@DatabaseField(canBeNull = false, foreign = true, foreignAutoRefresh = true)
	private LS wLSFK;
	
	public int getWorkId(){
		return workId;
	}
	
	public void setWProjectId(int id){
		wProjectId = id;
	}	
	public int getWProjectId(){
		return wProjectId;
	}
	
	public void setWOsId(int id){
		wOsId = id;
	}
	public int getWOsId(){
		return wOsId;
	}
	
	public void setWLsId(int id){
		wLsId = id;
	}
	public int getWLsId(){
		return wLsId;
	}
	
	public void setWParentId(int parentId){
		wParentId = parentId;
	}	
	public int getWParentId(){
		return wParentId;
	}
	
	public void setWParentNormId(int wparentnormid){
		wParentNormId = wparentnormid;
	}	
	public int getWParentNormId(){
		return wParentNormId;
	}
	
	public void setWName(String wname){
		wName = wname;
	}
	public String getWName(){
		return wName;
	}
	
	public void setWCipher(String wshifr){
		wCipher = wshifr;
	}
	public String getWCipher(){
		return wCipher;
	}
	
	public void setWCipherObosn(String wshifrobosn){
		wCipherObosn = wshifrobosn;
	}
	public String getWCipherObosn(){
		return wCipherObosn;
	}
	
	public void setWRec(String wrec){
		wRec = wrec;
	}
	public String getWRec(){
		return wRec;
	}
	
	public void setWPart(int wpart){
		wPart = wpart;
	}
	public int getWPart(){
		return wPart;
	}
	
	public void setWCount(float wcount){
		wCount = wcount;
	}
	public float getWCount(){
		return wCount;
	}
	
	public void setWMeasured(String wmeasured){
		wMeasured = wmeasured;
	}
	public String getWMeasured(){
		return wMeasured;
	}
	
	public void setWStartDate(Date wdatestart){
		wStartDate = wdatestart;
	}
	public Date getWStartDate(){
		return wStartDate;
	}
	
	public void setWEndDate(Date wdateend){
		wEndDate = wdateend;
	}
	public Date getWEndDate(){
		return wEndDate;
	}
	
	public void setWCurrStateDate(Date wdateforcurrstate){
		wCurrStateDate = wdateforcurrstate;
	}
	public Date getWCurrStateDate(){
		return wCurrStateDate;
	}
	
	public void setWPercentDone(float wpercentdone){
		wPercentDone =  wpercentdone;
	}
	public float getWPercentDone(){
		return wPercentDone;
	}
	
	public void setWCountDone(float wcountdone){
		wCountDone = wcountdone;
	}
	public float getWCountDone(){
		return wCountDone;
	}
	
	public void setWTotal(float wtotal){
		wTotal = wtotal;
	}
	public float getWTotal(){
		return wTotal;
	}
	
	public void setWNpp(int wnpp){
		wNPP = wnpp;
	}
	public int getWNpp(){
		return wNPP;
	}
	
	public void setWSortOrder(int wsortorder){
		wSortOrder = wsortorder;
	}
	public int getWSortOrder(){
		return wSortOrder;
	}
	
	public void setWItogo(float witogo){
		wItogo = witogo;
	}
	public float getWItogo(){
		return wItogo;
	}
	
	public void setWZP(float wzp){
		wZP = wzp;
	}
	public float getWZP(){
		return wZP;
	}
	
	public void setWMach(float wmach){
		wMach = wmach;
	}
	public float getWMach(){
		return wMach;
	}
	
	public void setWZPMach(float wzpmach){
		wZPMach = wzpmach;
	}
	public float getWZPMach(){
		return wZPMach;
	}
	
	public void setWZPTotal(float wzptotal){
		wZPTotal = wzptotal;
	}
	public float getWZPTotal(){
		return wZPTotal;
	}
	
	public void setWMachTotal(float wmachtotal){
		wMachTotal = wmachtotal;
	}
	public float getWMachTotal(){
		return wMachTotal;
	}
	
	public void setWZPMachTotal(float wzpmachtotal){
		wZPMachTotal = wzpmachtotal;
	}
	public float getWZPMachTotal(){
		return wZPMachTotal;
	}
	
	public void setWTz(float wtz){
		wTZ = wtz;
	}
	public float getWTz(){
		return wTZ;
	}
	
	public void setWTZMach(float wtzmach){
		wTZMach = wtzmach;
	}
	public float getWTZMach(){
		return wTZMach;
	}
	
	public void setWTZTotal(float wtztotal){
		wTZTotal = wtztotal;
	}
	public float getWTZTotal(){
		return wTZTotal;
	}
	
	public void setWTZMachTotal(float wtzmachtotal){
		wTZMachTotal = wtzmachtotal;
	}
	public float getWTZMachTotal(){
		return wTZMachTotal;
	}
	
	public void setWNaklTotal(float wnakltotal){
		wNaklTotal = wnakltotal;
	}
	public float getWNaklTotal(){
		return wNaklTotal;
	}
	
	public void setWAdmin(float wAdmin){
		this.wAdmin = wAdmin;
	}
	public float getWAdmin(){
		return wAdmin;
	}
	
	public void setWProfit(float wProfit){
		this.wProfit = wProfit;
	}
	public float getWProfit(){
		return wProfit;
	}
	
	public void setWProjectFK(Projects wProjFK){
		wProjectFK = wProjFK;
	}
	public Projects getWProjectFK(){
		return wProjectFK;
	}
	
	public void setWOSFK(OS wOSFK){
		this.wOSFK = wOSFK;
	}
	public OS getWOSFK(){
		return wOSFK;
	}
	
	public void setWLSFK(LS wLSFK){
		this.wLSFK = wLSFK;
	}
	public LS getWLSFK(){
		return wLSFK;
	}
	
	public Works() {
		// TODO Auto-generated constructor stub
	}
	
	public Works(
			int wparentid, 
			int wparentnormid,
			String wname, 
			String wshifr,
			String wshifrobosn,
			String wrec,
			int wpart,
			float wcount,
			String wmeasured,	
			Date wdatestart,
			Date wdateend,
			Date wdateforcurrstate,
			float wpercentdone,
			float wcountdone,
			float wtotal,
			int wnpp,
			int wsortorder,
			float witogo,
			float wzp,
			float wmach,
			float wzpmach,
			float wzptotal,
			float wmachtotal,
			float wzpmachtotal,
			float wtz,
			float wtzmach,
			float wtztotal,
			float wtzmachtotal,
			float wnakltotal,
			float wadmin,
			float wprofit,
			Projects wProjFK,
			OS wOSFK,
			LS wLSFK
			) {
		// TODO Auto-generated constructor stub
		wParentId = wparentid;
		wParentNormId = wparentnormid;
		wName = wname;
		wCipher = wshifr;
		wCipherObosn = wshifrobosn;
		wRec = wrec;
		wPart = wpart;
		wCount = wcount;
		wMeasured = wmeasured;
		wStartDate = wdatestart;
		wEndDate = wdateend;
		wCurrStateDate = wdateforcurrstate;
		wPercentDone =  wpercentdone;
		wCountDone = wcountdone;
		wTotal = wtotal;
		wNPP = wnpp;
		wSortOrder = wsortorder;
		wItogo = witogo;
		wZP = wzp;
		wMach = wmach;
		wZPMach = wzpmach;
		wZPMachTotal = wzptotal;
		wMachTotal = wmachtotal;
		wZPMachTotal = wzpmachtotal;
		wTZ = wtz;
		wTZMach = wtzmach;
		wTZTotal = wtztotal;
		wTZMachTotal = wtzmachtotal;
		wNaklTotal = wnakltotal;
		wAdmin = wadmin;
		wProfit = wprofit;
		wProjectFK = wProjFK;
		this.wOSFK = wOSFK;
		this.wLSFK = wLSFK;
	}
	
}
