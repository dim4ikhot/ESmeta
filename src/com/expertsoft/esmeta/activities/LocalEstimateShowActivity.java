package com.expertsoft.esmeta.activities;

import java.io.Serializable;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.expertsoft.esmeta.DBORM;
import com.expertsoft.esmeta.R;
import com.expertsoft.esmeta.adapters.LSLoaderAdapter;
import com.expertsoft.esmeta.data.LS;
import com.expertsoft.esmeta.data.OS;
import com.expertsoft.esmeta.data.Projects;

public class LocalEstimateShowActivity extends Activity implements Serializable {

	
	private static final long serialVersionUID = -222864131214757024L;
	Projects currProj;
	OS currOS;
	Context context;
	DBORM ormDB;	
	LSLoaderAdapter LSLA;		
	ListView lsLists;
	
	List<LS> lsList; 
	
	public LocalEstimateShowActivity(){
	
	}
	
	

	public void onCreate(Bundle savedInstanceState){
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_with_listview);
		
		Intent intent = getIntent();
		currProj = (Projects)intent.getSerializableExtra("Project");
		currOS = (OS)intent.getSerializableExtra("Os");		
		ormDB = new DBORM(this);
		lsList = ormDB.getLocalEstimate(currProj, currOS);
		LSLA = new LSLoaderAdapter(getApplicationContext(), lsList);
		lsLists = (ListView)findViewById(R.id.oEstimateList);
		((TextView)findViewById(R.id.estimateCaption)).setText("Список локальных смет");
		lsLists.setAdapter(LSLA);
		lsLists.setOnItemClickListener(osListener);				 
	}
	
	OnItemClickListener osListener = new OnItemClickListener(){

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {				
			// TODO Auto-generated method stub			
			LS tmpls = (LS)view.getTag();
			try{				
				Intent intent = new Intent(getApplicationContext(), WorksShowActivity.class);
				intent.putExtra("Project", currProj);
				intent.putExtra("Os", currOS);
				intent.putExtra("Ls", tmpls);
				startActivity(intent);				
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
	};
	
	public void onSaveInstanceState(Bundle saveParams){		
		super.onSaveInstanceState(saveParams);
		saveParams.putBoolean("lsListShown", true);
	}
}
