package com.expertsoft.esmeta;

import java.io.File;
import java.nio.charset.Charset;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.SortedMap;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.expertsoft.esmeta.OpenFileType.GetPathDialogListener;
import com.expertsoft.esmeta.activities.DialogEnterName;
import com.expertsoft.esmeta.activities.DialogEnterName.OnGetNameListener;
import com.expertsoft.esmeta.activities.ObjectsEstimateShowActivity;
import com.expertsoft.esmeta.activities.ProgramInfo;
import com.expertsoft.esmeta.activities.ProjectDetailShowActivity;
import com.expertsoft.esmeta.adapters.ProjectLoaderAdapter;
import com.expertsoft.esmeta.data.Projects;
import com.expertsoft.esmeta.dialogs.DialogDeleteProject;
import com.expertsoft.esmeta.dialogs.DialogFileExists;
import com.expertsoft.esmeta.file_work.ParalelOpenBuild;
import com.expertsoft.esmeta.file_work.SaveBuild;
import com.j256.ormlite.dao.Dao;
//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.AdView;
//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.AdView;

public class MainActivity extends FragmentActivity implements GetPathDialogListener,OnGetNameListener{

	//Items menu ID
	static final int DELETE_ID = 0;
	static final int VIEW_DETAIL_ID = 1;
	static final int SAVE_PROJECT = 2;
	
	//request codes for Activity
	static final int SETTINGS_RESULT = 1;
	static final int PROJECT_DETAIL_RESULT = 2;
	
	//Key constants
	static final String BUILDPATH = "buildpath";	 	
	static final String ZML_PATH_SP = "zmlpath";
	static final String ARP_PATH_SP = "arppath";
	
	//Paths to builders
	public String ZML_PATH;
	public String ARP_PATH;
	private String SAVE_PATH;
	public static String ZMLPath = "";
	public static String ARPPath = "";		
	
	//Dialog what kind of Project need open
	OpenFileType OpenFormatDialog;
	//project settings
	SharedPreferences Settings;	
	//List which shows loaded projects
	public ListView ProjectsList;	
	
	String OpenableFilePath = "";
	String FilePath = "";
	
	//Current data base
	private DBORM ormdatabase;
	//List of projects for Adapter
	public List<Projects> allProjectsList;
	//Adapter for List
	ProjectLoaderAdapter PLA;
	//AsyncTask open build
	ParalelOpenBuild StartLoadBuild;
	SaveBuild saveBuild;
	Projects globalSaveProj;
	
	ProgramInfo aboutProg;
	DialogEnterName enterName;
	DialogFileExists fileExtsts;
	DialogDeleteProject delProj;
		
	Button btnAddBuild;
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		//set full screen without title
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);		
		
		btnAddBuild = (Button)findViewById(R.id.btnAddNewBuild);
		btnAddBuild.setOnClickListener(openBuild);
		//Load ad
		/*AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);*/
		//default paths
		ZML_PATH = Environment.getExternalStorageDirectory().toString()+ getString(R.string.ZML);
		ARP_PATH = Environment.getExternalStorageDirectory().toString()+ getString(R.string.ARP);
		SAVE_PATH = Environment.getExternalStorageDirectory().toString()+getString(R.string.SAVES);
		//Create new dialog
		OpenFormatDialog = new OpenFileType();				
        //Create new data base
		ormdatabase = new DBORM(this);		
		
		CreateFilePathes();
						
	    ProjectsList = (ListView)findViewById(R.id.ProjectsList);
	    //get all projects
	    allProjectsList = ormdatabase.getAllProjectsData();
	    //new adapter
	    PLA = new ProjectLoaderAdapter(this, allProjectsList);
	    ProjectsList.setAdapter(PLA);
	   	    	    
	    registerForContextMenu(ProjectsList);
	    //set on click method
	    ProjectsList.setOnItemClickListener(ItemListener);	 
	    //it will be
	    StartLoadBuild = (ParalelOpenBuild)getLastCustomNonConfigurationInstance();
	    
	  //Read data when file open on touching
  		try{
  			Uri data = getIntent().getData();
  			if (data != null){
  				OpenableFilePath = data.getEncodedPath();
  				OpenableFilePath = OpenableFilePath.replaceAll("%20", " ");
  				startLoadBuild(OpenableFilePath, getFileExtension(OpenableFilePath).toUpperCase());
  			//	Toast.makeText(this, OpenableFilePath, Toast.LENGTH_LONG).show();
  			}
  		}catch(Exception e){
  			e.printStackTrace();
  		}  		  	
	}
	
	private String getFileExtension(String f) {
	    String ext = "";
	    int i = f.lastIndexOf('.');
	    if (i > 0 &&  i < f.length() - 1) {
	      ext = f.substring(i);
	    }
	    return ext;
	}
	
	OnClickListener openBuild = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			OpenFormatDialog.show(getFragmentManager(), "OpenFgt");
		}
	};
	
	protected void onSaveInstanceState(Bundle outState){
		super.onSaveInstanceState(outState);
	}
	
	public void RefreshProjectsList(){
		allProjectsList = ormdatabase.getAllProjectsData();		
	    PLA = new ProjectLoaderAdapter(this, allProjectsList);
	    PLA.notifyDataSetChanged();
	    ProjectsList.setAdapter(PLA);
	}
	
	OnItemClickListener ItemListener = new OnItemClickListener(){

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			Projects tmpproj = (Projects)view.getTag();			
			Intent intent = new Intent(getApplicationContext(), ObjectsEstimateShowActivity.class);
			intent.putExtra("Project", tmpproj);			
			try{
				startActivity(intent);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
	};
	//NOT USE:
	void getAllCharsets()
	{
		try {
            SortedMap<String,Charset> registeredCharsets = Charset.availableCharsets();
            String currentName;
            for ( Iterator<Charset> charsets = registeredCharsets.values().iterator(); charsets.hasNext(); ) {
                Charset charset = charsets.next(); 
                // Display name
                currentName = charset.name() + " Aliases: [";                
                // Display aliases
                for ( Iterator<String> aliases = charset.aliases().iterator(); aliases.hasNext(); ) {
                	currentName = currentName  + aliases.next();                	
                    if ( aliases.hasNext() ) currentName = currentName  + ", ";
                }
                currentName = currentName  + "]";
                Log.d("myLogs",currentName);
            }                            
            Log.d("myLogs","\nIs cp866 supported? - " + Charset.isSupported("cp866")); // testing aliase
            Log.d("myLogs","Is IBM866 supported? - " + Charset.isSupported("IBM866"));
        } catch ( Exception ex ) {
            ex.printStackTrace();
            Log.d("myLogs", "ERROR: " + ex.getMessage().toString());
        }
	}
	
	void CreateFilePathes(){
		if(! new File(ZML_PATH).isDirectory())
		  new File(ZML_PATH).mkdirs();
		if(! new File(ARP_PATH).isDirectory())
     		new File(ARP_PATH).mkdirs();		
		if(! new File(SAVE_PATH).isDirectory())
     		new File(SAVE_PATH).mkdirs();
	}
	
	protected void onResume(){
	//	getAllCharsets();
		getParams();	
		super.onResume();
	}
	
	public static String getZmlPath(){
		return ZMLPath;
	}
	
	public static String getArpPath(){
		return ARPPath;
	}
	
	//Get file paths from settings or by default
	public void getParams(){
		Settings = PreferenceManager.getDefaultSharedPreferences(this);
		if(! Settings.getString(ZML_PATH_SP, "").equals("")){	
			ZMLPath = Environment.getExternalStorageDirectory().toString()+Settings.getString(ZML_PATH_SP, "");
		}else{
			ZMLPath = Environment.getExternalStorageDirectory().toString()+getResources().getString(R.string.ZML);
		}
		if(!Settings.getString(ARP_PATH_SP, "").equals("")){
			ARPPath = Environment.getExternalStorageDirectory().toString()+Settings.getString(ARP_PATH_SP, "");
		}else{
			ARPPath = Environment.getExternalStorageDirectory().toString()+getResources().getString(R.string.ARP);
		}		
	}
	protected void onDestroy(){
		super.onDestroy();
		Log.d("myLogs", "onDestroy Activity");
		ormdatabase.destroyHelper();		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);		
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();	
		switch(id){
		case R.id.OpenFormat:
			OpenFormatDialog.show(getFragmentManager(), "OpenFgt");
			break;
		case R.id.AppSettings:
			startActivityForResult(new Intent(this, ShowSettings.class).putExtra(ZML_PATH_SP, ZML_PATH).putExtra(ARP_PATH_SP, ARP_PATH), SETTINGS_RESULT);
			break;
		case R.id.about:
			aboutProg = new ProgramInfo();
			aboutProg.show(getSupportFragmentManager(), "aboutProg");
			break;
		case R.id.ExitApp:
			finish();
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		// Inflate the menu; this adds items to the action bar if it is present.
		menu.add(0, DELETE_ID, 2, "Удалить стройку");	
		menu.add(0, VIEW_DETAIL_ID, 0, "Детали стройки");
		menu.add(0, SAVE_PROJECT, 1, "Сохранить стройку");		
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) { 
		int id = item.getItemId();
		AdapterContextMenuInfo acmi = (AdapterContextMenuInfo) item.getMenuInfo();;
		allProjectsList = ormdatabase.getAllProjectsData();
		Projects proj;
		switch(id){
		case DELETE_ID:					
			// получаем инфу о пункте списка		    
		    proj = allProjectsList.get(acmi.position);			   
		    /*
		    try{
		    	//Getting Dao object
		    	Dao<Projects,Integer> projectDao = ormdatabase.getHelper().getProjectsDao();
		    	//Delete project from DataBase
		    	projectDao.delete(proj);
		    	//Remove position frpm List
		    	allProjectsList.remove(acmi.position);
		    	RefreshProjectsList();
		    }catch(SQLException e){
		    	e.printStackTrace();
		    }*/
		    delProj = new DialogDeleteProject(allProjectsList, PLA, ProjectsList,this, ormdatabase, proj);
		    delProj.show(getSupportFragmentManager(), "fileExists");		
			break;
		case VIEW_DETAIL_ID:					    
		    try{
		    	//Get project
		    	proj = allProjectsList.get(acmi.position);
		    	//Create new intent
		    	Intent intent = new Intent(this, ProjectDetailShowActivity.class);
		    	//write object into intent
		    	intent.putExtra("proj", proj);
		    	//call activity
		    	startActivityForResult(intent, PROJECT_DETAIL_RESULT);
		    }catch(Exception e){
		    	e.printStackTrace();
		    }
			break;
		case SAVE_PROJECT:
			globalSaveProj = allProjectsList.get(acmi.position);
			enterName = new DialogEnterName(globalSaveProj.getProjectName());
			enterName.show(getSupportFragmentManager(), "saveProg");
			break;
		}
		return super.onContextItemSelected(item);
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		super.onActivityResult(requestCode, requestCode, data);		
		switch(requestCode){
		case SETTINGS_RESULT:
			if (resultCode == RESULT_CANCELED){
				getParams();
			}
			break;			
		case PROJECT_DETAIL_RESULT:
			if(resultCode == RESULT_OK){
				try{
					Dao<Projects,Integer> projectDao = ormdatabase.getHelper().getProjectsDao();
			    	projectDao.update((Projects)data.getSerializableExtra("proj"));
					RefreshProjectsList();
				}catch(SQLException e){
					e.printStackTrace();
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			break;
		}		
	}

	private void startLoadBuild(String CurrFilePath, String FileType){
		StartLoadBuild = new ParalelOpenBuild(ormdatabase.getHelper(), this, ProjectsList, ormdatabase);
		StartLoadBuild.execute(CurrFilePath,FileType);				
	}
	
	@Override
	public void onGetFileParams(String CurrFilePath, String FileType) {
		// TODO Auto-generated method stub		
		startLoadBuild(CurrFilePath,FileType);	
	}
			              
	public Object onRetainCustomNonConfigurationInstance() {		
	    return StartLoadBuild;
	}

	@Override
	public void getSaveName(String name) {
		// TODO Auto-generated method stub
		if(! new File(SAVE_PATH + "/" + name + ".zml").exists()){
			saveBuild = new SaveBuild(name,SAVE_PATH +"/tempFile.xml", this, ormdatabase,  globalSaveProj);
			saveBuild.execute();
		}else{
			fileExtsts = new DialogFileExists(name, SAVE_PATH +"/tempFile.xml", this, ormdatabase,  globalSaveProj);
			fileExtsts.show(getSupportFragmentManager(), "fileExists");			
		}
	}
}
