package com.expertsoft.esmeta.activities;

import java.io.Serializable;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.expertsoft.esmeta.DBORM;
import com.expertsoft.esmeta.R;
import com.expertsoft.esmeta.adapters.OSLoaderAdapter;
import com.expertsoft.esmeta.data.LS;
import com.expertsoft.esmeta.data.OS;
import com.expertsoft.esmeta.data.Projects;

public class ObjectsEstimateShowActivity extends Activity implements Serializable{
	
	private static final long serialVersionUID = -222864131214757024L;
	
	Projects currProject;
	Context context;
	DBORM ormDB;	
	OSLoaderAdapter OSLA;	
	
	ListView osLists;
	
	List<OS> osList; 
	List<LS> lsList;
	
	public ObjectsEstimateShowActivity(){
	}		
	
	public void onCreate(Bundle savedInstanceState){
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_with_listview);
		Intent intent = getIntent();
		currProject = (Projects)intent.getSerializableExtra("Project");		
		ormDB = new DBORM(this); 
		osList = ormDB.getObjectEstimate(currProject);
		OSLA = new OSLoaderAdapter(getApplicationContext(), osList);
		osLists = (ListView)findViewById(R.id.oEstimateList);
		((TextView)findViewById(R.id.estimateCaption)).setText("Список объектных смет");
		osLists.setAdapter(OSLA);
		osLists.setOnItemClickListener(osListener);
	}
	
	OnItemClickListener osListener = new OnItemClickListener(){

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			try{
				// TODO Auto-generated method stub
				OS tmpos = (OS)view.getTag();
				Intent intent;
				lsList = ormDB.getLocalEstimate(currProject, tmpos);
				if((lsList.size()>1)|((lsList.size() == 1)&(! lsList.get(0).getLsHidden()))){
					intent = new Intent(getApplicationContext(), LocalEstimateShowActivity.class);
					intent.putExtra("Project", currProject);
					intent.putExtra("Os", tmpos);
					try{				
						startActivity(intent);
					}catch(Exception e){
						e.printStackTrace();
					}
				}else{					
					LS currLs = lsList.get(0);
					intent = new Intent(getApplicationContext(), WorksShowActivity.class);
					intent.putExtra("Project", currProject);
					intent.putExtra("Os", tmpos);
					intent.putExtra("Ls", currLs);
					startActivity(intent);					
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			
		}
		
	};
	
	public void onSaveInstanceState(Bundle saveParams){		
		super.onSaveInstanceState(saveParams);
		saveParams.putBoolean("osListShown", true);
	}
}
