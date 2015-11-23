package com.expertsoft.esmeta.activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.expertsoft.esmeta.OpenFileType;
import com.expertsoft.esmeta.R;
import com.expertsoft.esmeta.R.id;
import com.expertsoft.esmeta.R.layout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class ViewBuildersActivity extends Activity {

	final String BUILDNAME = "buildname";
	final String BUILDPATH = "buildpath";
	final String BUILDIMG = "buildimg";
	String filepath;
	
	protected void onCreate(Bundle savedInstanceParam){
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(savedInstanceParam);
		setContentView(R.layout.builder_founder_list);
		
		ListView lvbuilders = (ListView)findViewById(R.id.lViewbuild);
					
		ArrayList<Map<String,Object>> ExBuilders = OpenFileType.getBuilders();		
		String[] from = {BUILDNAME, BUILDIMG};
		int[] to = {R.id.builderName, R.id.imgBuild};
		
		SimpleAdapter BuildersAdapter = new SimpleAdapter(this,ExBuilders,R.layout.builders_founder, from, to );
		
		lvbuilders.setAdapter(BuildersAdapter);
		lvbuilders.setOnItemClickListener(BuildClickListener);
	}
	
	OnItemClickListener BuildClickListener = new OnItemClickListener(){

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			HashMap<String,Object> m;
			try{
			   m =  (HashMap<String,Object>)parent.getAdapter().getItem(position);
			   filepath = (String)m.get(BUILDPATH);
			   if (!filepath.isEmpty()){
				   //return path of selected item
				  Intent intent = new Intent();
				  intent.putExtra(BUILDPATH, filepath);
				  setResult(RESULT_OK, intent);
				  finish();
			   }
			}catch(Exception e){
				e.printStackTrace();
			}			
			
		}
		
	};

	public ViewBuildersActivity() {
		// TODO Auto-generated constructor stub
	}

}
