package com.expertsoft.esmeta.file_work;

import java.io.File;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.expertsoft.esmeta.DBORM;
import com.expertsoft.esmeta.data.Projects;
import com.expertsoft.esmeta.parsers.SaveInXML;

public class SaveBuild extends AsyncTask<Void, Void, Integer>{

	ProgressDialog pd;
	Context context;
	String name;
	String path;
	DBORM ormdatabase;
	Projects selectedProj;
	SaveInXML save;
	
	public SaveBuild() {
		// TODO Auto-generated constructor stub
	}
	
	public SaveBuild(String fileName, String filePath, Context ctx, DBORM ormdatabase, Projects proj) {
		// TODO Auto-generated constructor stub
		name = fileName;
		path = filePath;
		context = ctx;
		this.ormdatabase = ormdatabase;
		selectedProj = proj;
	}

	public void createDialog(){
		if(pd == null)
		{
			pd = new ProgressDialog(context);
			pd.setTitle("Сохранение...");
			pd.setMessage("Подождите, пожалуйста, выполняется сохранение стройки");
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
	
	private void doWhenDoneLoading(String toastText){
		freeDialog();  	  
		Toast.makeText(context, toastText, Toast.LENGTH_LONG).show();		
		pd = null;
	}
	
	protected void onPreExecute(){
		super.onPreExecute();		
		createDialog();
		save = new SaveInXML(ormdatabase, selectedProj, path);
	}
	
	
	@Override
	protected Integer doInBackground(Void... params) {
		// TODO Auto-generated method stub
		try{
			if (!save.startSave()){
				return 0;
			}else{
				File f = new File(path);
				FileUtils FUrename = new FileUtils(f.getPath(), "");
				f = FUrename.encoddeUTF8ToWin1251(path, name, "UTF-8", ".zml~");
				UnZipBuild zipping = new UnZipBuild(f.getParent()+"/"+ name + ".zml", path);
				zipping.exZip(f);
			}
				
		}catch(Exception e){
			e.printStackTrace();
			return 0;
		}		
		return 1;
	}

	protected void onPostExecute(Integer result) {
	      super.onPostExecute(result);
	      switch(result){
	      case 0:
	    	  doWhenDoneLoading("Ошибка сохранения стройки");	    	  
	    	  break;	      
	      case 1:
	    	  doWhenDoneLoading("Стройка успешно сохранена");	    	  
	    	  break;
	      }
	}
}
