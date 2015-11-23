package com.expertsoft.esmeta;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.FileObserver;
import android.util.Log;
import android.widget.Toast;

public class OpenFileType extends DialogFragment {
	
	public interface GetPathDialogListener {
        void onGetFileParams(String CurrFilePath, String FileType);        
    }
	
	final static String BUILDNAME = "buildname";
	final static String BUILDPATH = "buildpath";
	final static String BUILDIMG = "buildimg";
	final int PATH_RESULT = 2;
	
	final String TAG = "myLogs";
	private String OpenFormats[] = {".ZML", ".ARP",};// "O-CAD(Unknow)"};
	public static String FormatType;
	public String FilePath;
	FileObserver FOsmetafiles;
	
	//Constructor by default
	public OpenFileType(){
		
	}
	public Dialog onCreateDialog(Bundle savedInstanceState) {    
		AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
        adb.setTitle(R.string.FormatTitle);
        adb.setSingleChoiceItems(OpenFormats, -1, SelectFormatListener);
		return adb.create();
	}
	
	OnClickListener SelectFormatListener = new OnClickListener(){

		@Override
		public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
			FormatType = OpenFormats[which];										
			Intent intent = new Intent("com.expertsoft.VIEWBUILDERS");					    
		    startActivityForResult(intent, PATH_RESULT);					
			//onStartWatching();
		}		
	};
	
	private static String getPathFiles(){				
		if(FormatType.equals(".ZML")){        	       
			return MainActivity.getZmlPath();//MainActivity.ZMLPath;
        }else
    	if(FormatType.equals(".ARP")){
    		return MainActivity.getArpPath();//ARPPath;	
    	}
    	else 
    	  return "";
	}
	
	//Get all builders in default path
	public static ArrayList<Map<String,Object>> getBuilders() {		   
		File dir =new File(getPathFiles());		
		ArrayList<Map<String,Object>> ListForAdapter = new ArrayList<Map<String,Object>>();
		Map<String,Object> listitem;
		if (dir.exists() && dir.isDirectory()){
			File files[] = dir.listFiles(new FilenameFilter(){
				@Override
				public boolean accept(File dir,String name){
					return name.contains(FormatType.toLowerCase());
				}
			});	
			if (files.length > 0)
			{
				for(int i =0; i< files.length; i++){
					listitem = new HashMap<String,Object>();
					listitem.put(BUILDNAME, files[i].getName());
					listitem.put(BUILDPATH, files[i].getPath());
					listitem.put(BUILDIMG, R.drawable.ic_launcher);
					ListForAdapter.add(listitem);				
				}
			}else
			{
				listitem = new HashMap<String,Object>();
				listitem.put(BUILDNAME, "Не найдено ни одной стройки");
				listitem.put(BUILDPATH, "");
				listitem.put(BUILDIMG, R.drawable.bulds_no_found);
				ListForAdapter.add(listitem);
			}
		}		
		return ListForAdapter;	
	}
	
	void onStartWatching() {       
        Log.d(TAG, "onStartWatching FileObserver");        
        final String pathToWatch = getPathFiles();        
        Toast.makeText(getActivity(), "App started and trying to watch " + pathToWatch, Toast.LENGTH_LONG).show();
        FOsmetafiles = new FileObserver(pathToWatch) { // set up a file observer to watch this directory on sd card
            @Override
            public void onEvent(int event, String file) {
                //if(event == FileObserver.CREATE && !file.equals(".probe")){ // check if its a "create" and not equal to .probe because thats created every time camera is launched
                    Log.d(TAG, "File created [" + pathToWatch + file + "]");

                    Toast.makeText(getActivity(), file + " was saved!", Toast.LENGTH_LONG).show();                  
                //}
            }
        };
        FOsmetafiles.startWatching();
    }
	
	public String getSelectedFormat()
	{
		return FormatType;
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		super.onActivityResult(requestCode, requestCode, data);
		if (resultCode == Activity.RESULT_OK){
			switch(requestCode){			
			case PATH_RESULT:
				FilePath = data.getStringExtra(BUILDPATH);
				if (!FilePath.isEmpty())
				{
					GetPathDialogListener mainActivity = (GetPathDialogListener)getActivity();
					mainActivity.onGetFileParams(FilePath, FormatType);
					this.dismiss();
				}
				break;
			}
		}
		else{
			this.dismiss();
		}
	}
	
	public void onSaveInstanceState(Bundle outParams){
		
	}
}
