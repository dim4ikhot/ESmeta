package com.expertsoft.esmeta.file_work;

import java.io.File;
import java.nio.charset.Charset;
import java.util.List;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.widget.ListView;
import android.widget.Toast;

import com.expertsoft.esmeta.DBORM;
import com.expertsoft.esmeta.MainActivity;
import com.expertsoft.esmeta.adapters.ProjectLoaderAdapter;
import com.expertsoft.esmeta.data.ORMDatabaseHelper;
import com.expertsoft.esmeta.data.Projects;
import com.expertsoft.esmeta.parsers.ArpParser;
import com.expertsoft.esmeta.parsers.ZmlParser;

public class ParalelOpenBuild extends AsyncTask<String,Integer,Integer>{
		
	FragmentActivity context;
	Projects loadedProject = new Projects();
	ORMDatabaseHelper databaseHelper;
	DBORM ormdb;
	ProgressDialog pd;
	Handler h;
	ListView projectsList;
	List<Projects> allProjectsList;
	String globalProjectName;
	MainActivity mainActivity;
	
	public ParalelOpenBuild(ORMDatabaseHelper data, FragmentActivity cnxt, ListView projList, DBORM ormDB){
		 
		databaseHelper = data;		
		projectsList = projList;
		ormdb = ormDB;
		context = cnxt;
		mainActivity = (MainActivity)cnxt;
	}
	
	public void setNewContext(FragmentActivity context){
		this.context = context;
	}
	
	public void refreshProjectsTable(){
		allProjectsList = ormdb.getAllProjectsData();		
	    ProjectLoaderAdapter PLA = new ProjectLoaderAdapter(context, allProjectsList);
	    PLA.notifyDataSetChanged();
	    projectsList.setAdapter(PLA);
	}
	
	public void createDialog(){
		if(pd == null)
		{
			pd = new ProgressDialog(context);
			pd.setTitle("Загрузка...");
			pd.setMessage("Подождите, пожалуйста, выполняется загрузка стройки");
			// меняем стиль на индикатор
			pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			// включаем анимацию ожидания
		    pd.setIndeterminate(true);
		    pd.setCancelable(false);
		    pd.setCanceledOnTouchOutside(false);
		    pd.show();
		}
	}
	
	
	
	public void freeDialog(){
		 try{
	    	  if ((pd!= null)&(pd.isShowing())){
	    		  pd.dismiss();
	    	  }
	      }catch(IllegalArgumentException e){
	    	  e.printStackTrace();
	    	  pd = null;
	      }
	}
	
	protected void onPreExecute(){
		super.onPreExecute();		
		createDialog();
	}
	
	@Override
	protected Integer doInBackground(String... params) {
		// TODO Auto-generated method stub
		String path = params[0];
		globalProjectName = path;
		String FileType = params[1];
		int mustReturn = 1;									
		FileUtils FUrename = new FileUtils(path, "");				
		File tempfile = null;
		try{
			if(FileType.equals(".ZML")){
				try{
					File f = new File(path);
					UnZipBuild unzip = new UnZipBuild(path,  f.getParent());						
					String WIN = "windows-1251";					
					tempfile = FUrename.encoddeAnyToUTF8(unzip.ExUnzip().getPath(), WIN,".xml");
					/*
					if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1){					
						tempfile = FUrename.encoddeAnyToUTF8(unzip.ExUnzip().getPath(), WIN,".xml");
					}else{
						tempfile = FUrename.encoddeAnyToUTF8(unzip.unzip().getPath(), WIN,".xml");
					}
					*/					
				}catch(Exception e){
					e.printStackTrace();
				}
			}else{										
				String DOS = "IBM866";
				if (Charset.isSupported(DOS)){						
					tempfile = FUrename.encoddeAnyToUTF8(path, DOS, ".txt");
				}else{						
					tempfile = FUrename.encodeCP866ToUTF8(path, DOS);
				}
			}
		}
		finally{				
			try{
				if(tempfile != null){
					if (!StartExtractData(tempfile, FileType)){
						mustReturn = 0;
					}
				}
			}
			finally{
				if (tempfile != null)
				{
					tempfile.delete();
				}
			}
		}			
		return mustReturn;			
	}
	
	 @Override
	   public void onProgressUpdate(Integer... values) {
	      super.onProgressUpdate(values);	     
	    }
	
	protected void onPostExecute(Integer result) {
	      super.onPostExecute(result);
	      switch(result){
	      case 0:
	    	  doWhenDoneLoading("Ошибка загрузки стройки");	    	  
	    	  break;	      
	      case 1:
	    	  doWhenDoneLoading("Стройка успешно загружена");	    	  
	    	  break;
	      }
	}
	
	private void doWhenDoneLoading(String toastText){
		freeDialog();  	  
		Toast.makeText(context, toastText, Toast.LENGTH_LONG).show();
		refreshProjectsTable();
		pd = null;
	}
	
	public boolean StartExtractData(File inputFile, String filetype){
		if(filetype.equals(".ZML"))
			return StartExtractingZml(inputFile);
		else
			return StartExtractingArp(inputFile);
	}
	
	public boolean StartExtractingZml(File zmlFile){
		ZmlParser zmlparser = new ZmlParser(zmlFile, databaseHelper);
//		zmlparser.startXmlParse();
		return zmlparser.startParce();			
	}
	public boolean StartExtractingArp(File arpFile){
		ArpParser arpparser = new ArpParser(arpFile, databaseHelper,globalProjectName);
		return arpparser.startParce();
	}
}

