package com.expertsoft.esmeta.activities;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;

import com.expertsoft.esmeta.DBORM;
import com.expertsoft.esmeta.R;
import com.expertsoft.esmeta.adapters.WorksResourcesAdapter;
import com.expertsoft.esmeta.data.Works;
import com.expertsoft.esmeta.data.WorksResources;

public class WorksResourceShowActivity extends Fragment/*Activity*/ {

	Works currWork;
	List<WorksResources> worksResList;
	
	Context context;
	DBORM ormDB;	
	ListView wrListView;
	WorksResourcesAdapter wResAdp;
	
	public View onCreateView(LayoutInflater inflater,ViewGroup group, Bundle savedInstanceState){		
		super.onCreateView(inflater, group, savedInstanceState);
		View res = inflater.inflate(R.layout.activity_with_listview, group, false);		
		
		getParentTables();
		
		wrListView = (ListView)res.findViewById(R.id.oEstimateList);
		((TextView)res.findViewById(R.id.estimateCaption)).setText("Состав позиции");
		wResAdp = new WorksResourcesAdapter(getActivity().getApplicationContext(), worksResList);
		wrListView.setAdapter(wResAdp);
		return res;
	}
	
	private void getParentTables(){
		Intent intent = getActivity().getIntent();
		currWork = (Works)intent.getSerializableExtra("work");
		ormDB = new DBORM(getActivity().getApplicationContext());
		worksResList = ormDB.getWorksResource(currWork);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		super.onCreate(savedInstanceState);		
	}
	
	/*
	protected void onCreate(Bundle savedInstanceState){
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_with_listview);				
		getParentTables();
		
		wrListView = (ListView)findViewById(R.id.oEstimateList);
		wResAdp = new WorksResourcesAdapter(this, worksResList);
		wrListView.setAdapter(wResAdp);
	}
	
	private void getParentTables(){
		Intent intent = getIntent();
		currWork = (Works)intent.getSerializableExtra("Work");
		ormDB = new DBORM(this);
		worksResList = ormDB.getWorksResource(currWork);
	}
	*/
	public WorksResourceShowActivity() {
		// TODO Auto-generated constructor stub
	}

}
