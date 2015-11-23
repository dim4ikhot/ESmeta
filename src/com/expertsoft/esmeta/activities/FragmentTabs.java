package com.expertsoft.esmeta.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.Window;
import android.view.WindowManager;

import com.expertsoft.esmeta.R;
import com.expertsoft.esmeta.data.Works;

public class FragmentTabs extends FragmentActivity {

	 private FragmentTabHost mTabHost;
	
	protected void onCreate(Bundle savedInstanceState){
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.work_info_tab_fragment);
		
		mTabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
        
        mTabHost.addTab(mTabHost.newTabSpec("detail").setIndicator("Детали"),
                WorkDetailShowerActivity.class, null);
        Works work = (Works)getIntent().getSerializableExtra("work");
        boolean isMachine = false;
        boolean isRes = false;
        if(work != null){
	        isRes = work.getWRec().equals("resource");
	        isMachine = work.getWRec().equals("machine");
        }        
        if(!isRes & !isMachine){
	        mTabHost.addTab(mTabHost.newTabSpec("consist").setIndicator("Состав"),
	                WorksResourceShowActivity.class, null);
        }
	}
	
	public FragmentTabs() {
		// TODO Auto-generated constructor stub
	}

}
