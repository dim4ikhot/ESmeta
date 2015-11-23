package com.expertsoft.esmeta;

import java.util.Calendar;
import java.util.Date;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import com.expertsoft.esmeta.activities.WorkDetailShowerActivity;

public class DateFragment extends DialogFragment {

	public interface GetDateListener {
        void onGetDate(Date date);        
    }
	
	final Calendar c = Calendar.getInstance();  // Get calendar instance to use in DateSlider
	WorkDetailShowerActivity WDSA;
	
	public DateFragment() {
		// TODO Auto-generated constructor stub
	}
	
	public DateFragment(WorkDetailShowerActivity WDSA) {
		// TODO Auto-generated constructor stub
		this.WDSA = WDSA;
	}
	
	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {                
        DatePickerDialog dpd = new DatePickerDialog(getActivity(),setNewDate, 
    															c.get(Calendar.YEAR),
										        		        c.get(Calendar.MONTH), 
										        		        c.get(Calendar.DAY_OF_MONTH));
        return dpd;
    }
	
	OnDateSetListener setNewDate = new OnDateSetListener() {
		
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			// TODO Auto-generated method stub
			c.set(year, monthOfYear, dayOfMonth);
			GetDateListener mainActivity = (GetDateListener)WDSA;
			mainActivity.onGetDate(c.getTime());
		}
	};
	
}
