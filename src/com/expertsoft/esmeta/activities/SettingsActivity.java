package com.expertsoft.esmeta.activities;

import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceFragment;

import com.expertsoft.esmeta.R;

public class SettingsActivity extends PreferenceFragment {

	EditTextPreference zmlPath, arpPath;
	
	@Override
	public void onCreate(Bundle savedParams){			
		super.onCreate(savedParams);	
        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.settings);
        
        zmlPath = (EditTextPreference)findPreference("zmlpath");
        arpPath = (EditTextPreference)findPreference("arppath");
        zmlPath.setSummary(zmlPath.getText());
        arpPath.setSummary(arpPath.getText());                
        
        zmlPath.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newVal) {
                final String value = (String) newVal;
                zmlPath.setSummary(value);                
                return true;
            }

        });
        arpPath.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newVal) {
                final String value = (String) newVal;
                arpPath.setSummary(value);
                return true;
            }

        });
	}

}
