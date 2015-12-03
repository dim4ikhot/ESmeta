package com.expertsoft.esmeta;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.expertsoft.esmeta.activities.SettingsActivity;

public class ShowSettings extends Activity {

	public ShowSettings() {
		// TODO Auto-generated constructor stub
	}
    protected void onCreate(Bundle savedParameters){
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    	super.onCreate(savedParameters);    	    	
    	
    	getFragmentManager().beginTransaction()
		//.addToBackStack("MyStack")
        .replace(android.R.id.content, new SettingsActivity()).commit();
    }
}
