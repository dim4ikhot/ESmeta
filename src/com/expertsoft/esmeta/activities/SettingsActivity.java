package com.expertsoft.esmeta.activities;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.expertsoft.esmeta.R;

public class SettingsActivity extends PreferenceFragment {

	@Override
	public void onCreate(Bundle savedParams){			
		super.onCreate(savedParams);	
        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.settings);
	}

}
