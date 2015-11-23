package com.expertsoft.esmeta.activities;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.expertsoft.esmeta.DateFragment;
import com.expertsoft.esmeta.DateFragment.GetDateListener;
import com.expertsoft.esmeta.R;
import com.expertsoft.esmeta.data.Works;

public class WorkDetailShowerActivity extends Fragment/*Activity*/implements GetDateListener {

	Works currWork;
	TextView cipherValue, nameValue, measuredValue, countValue;
	EditText costOfOneTotal, salaryOfOne, 
	         costOfOneMachine, salaryOfOneMachine,
	         totalCostCommon, totalSalary,
	         totalCostMachine,totalSalaryMachine,
	         laborCostOfOneWorker,laborCostOfOneMachine,
	         laborTotalCostWorker,laborTotalCostMachine,
	         startDate,percentDone,countDone;
	Button btnOk;
	ImageView btnSetDate, btnApplyFacts;
	LinearLayout layoutApply;
	Intent intent;
	SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy");
	boolean wasChangedPercent, wasChangedCount;
	
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){		
		super.onCreateView(inflater, container, savedInstanceState);		
		intent = getActivity().getIntent();
		currWork = (Works)intent.getSerializableExtra("work");
		View details;
		if((currWork.getWRec().equals("resource"))|(currWork.getWRec().equals("machine"))){
			details = inflater.inflate(R.layout.view_child_position, container, false);			
		}else{
			details = inflater.inflate(R.layout.view_position, container, false);			
		}		
		initControls(details);
		fillTheWorksDetail();
		btnOk.setOnClickListener(okListener);
		btnSetDate.setOnClickListener(okListener);
		btnApplyFacts.setOnClickListener(okListener);
		
		startDate.addTextChangedListener(editDateChangeListener);
		percentDone.addTextChangedListener(editPercentChangeListener);
		countDone.addTextChangedListener(editCountChangeListener);
		wasChangedPercent = false;
		wasChangedCount = false;
		
		return details;
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
		intent = getIntent();
		currWork = (Works)intent.getSerializableExtra("work");
		if((currWork.getWRec().equals("resource"))|(currWork.getWRec().equals("machine"))){
			setContentView(R.layout.view_child_position);
		}else{
			setContentView(R.layout.view_position);
		}
		initControls();
		fillTheWorksDetail();
		btnOk.setOnClickListener(okListener);
	}*/
	
	private void initControls(View v){
		cipherValue = (TextView)v.findViewById(R.id.posCipherValue);
		nameValue = (TextView)v.findViewById(R.id.posFullNameValue);
		measuredValue = (TextView)v.findViewById(R.id.posMeasuredValue);
		countValue = (TextView)v.findViewById(R.id.posCountValue);
		costOfOneTotal = (EditText)v.findViewById(R.id.editTotalOneValue);
		salaryOfOne = (EditText)v.findViewById(R.id.editZpOneValue);
		costOfOneMachine = (EditText)v.findViewById(R.id.editUseMachineOneValue);
		salaryOfOneMachine = (EditText)v.findViewById(R.id.editZpMachineOneValue);
		totalCostCommon = (EditText)v.findViewById(R.id.editPriceTotalValue);
		totalSalary = (EditText)v.findViewById(R.id.editZpTtotalValue);
		totalCostMachine = (EditText)v.findViewById(R.id.editUseMachineTotalValue);
		totalSalaryMachine = (EditText)v.findViewById(R.id.editZpMachineTotalValue);
		
		laborCostOfOneWorker = (EditText)v.findViewById(R.id.editHoursOneWValue);
		laborTotalCostWorker = (EditText)v.findViewById(R.id.editHoursTotalWValue);
		
		laborCostOfOneMachine = (EditText)v.findViewById(R.id.editHoursOneMValue);		
		laborTotalCostMachine = (EditText)v.findViewById(R.id.editHoursTotalMValue);
		
		startDate = (EditText)v.findViewById(R.id.editStartExecuting);
		percentDone = (EditText)v.findViewById(R.id.editExecutingPercent);
		countDone = (EditText)v.findViewById(R.id.editExecutingCount);
		
		btnOk = (Button)v.findViewById(R.id.applyChanges);	
		btnSetDate = (ImageView)v.findViewById(R.id.btnSetDate);
		btnApplyFacts = (ImageView)v.findViewById(R.id.btnApplyExecuting);
		layoutApply = (LinearLayout)v.findViewById(R.id.layoutApply);
		
	}
	
	private void showDateDialog(){
		DialogFragment newFragment = new DateFragment(this);
		newFragment.show(getFragmentManager(), "dialog");
	}
	
	TextWatcher editPercentChangeListener = new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			// TODO Auto-generated method stub
			try{
				if(s.length() > 0){
					wasChangedPercent = true;					
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			layoutApply.setVisibility(View.VISIBLE);			
		}
	};
	
	private void recalcCount(String s){
		float totalcount = currWork.getWCount();
		String percent = s.toString();
		float percentf = Float.parseFloat(percent);
		DecimalFormat df = new DecimalFormat("0.####");
		countDone.setText(df.format((totalcount * percentf) / 100));
	}
	
	private void recalcPercent(String s){
		float totalcount = currWork.getWCount();
		String eCount = s.toString();
		float countf = Float.parseFloat(eCount);
		DecimalFormat df = new DecimalFormat("0.##");
		percentDone.setText(df.format((100 * countf) / totalcount));
	}
	
	TextWatcher editCountChangeListener = new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			// TODO Auto-generated method stub
			try{
				if(s.length() > 0){
					wasChangedCount = true;					
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			layoutApply.setVisibility(View.VISIBLE);			
		}
	};
	
	TextWatcher editDateChangeListener = new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			layoutApply.setVisibility(View.VISIBLE);
		}
	};
	
	OnClickListener okListener = new OnClickListener(){

		@Override
		public void onClick(View v) {
			switch(v.getId()){
				case R.id.applyChanges:
					fillWorkForUpdate();
					intent = new Intent();
					intent.putExtra("work", currWork);
					getActivity().setResult(Activity.RESULT_OK, intent);
					getActivity().finish();
					break;
				case R.id.btnSetDate:
					showDateDialog();
					break;
				case R.id.btnApplyExecuting:
					try{
						if(wasChangedPercent){
							recalcCount(percentDone.getText().toString());
							wasChangedPercent = false;
						}else{
							recalcPercent(countDone.getText().toString());
							wasChangedCount = false;
						}
						currWork.setWStartDate(sdf.parse(startDate.getText().toString()));
						currWork.setWPercentDone(Float.parseFloat(percentDone.getText().toString()));
						currWork.setWCountDone(Float.parseFloat(countDone.getText().toString()));
						intent = new Intent();
						intent.putExtra("work", currWork);
						getActivity().setResult(Activity.RESULT_OK, intent);
						layoutApply.setVisibility(View.GONE);
					}catch(ParseException e){
						e.printStackTrace();
					}					
					break;
			}
		}
		
	};	
	
	private void fillTheWorksDetail(){
		if (currWork != null){
			cipherValue.setText(currWork.getWCipher());
			nameValue.setText(currWork.getWName());
			measuredValue.setText(currWork.getWMeasured());
			countValue.setText(String.valueOf(currWork.getWCount()));
			costOfOneTotal.setText(String.valueOf(currWork.getWItogo()));
			salaryOfOne.setText(String.valueOf(currWork.getWZP()));
			costOfOneMachine.setText(String.valueOf(currWork.getWMach()));
			salaryOfOneMachine.setText(String.valueOf(currWork.getWZPMach()));
			totalCostCommon.setText(String.valueOf(currWork.getWTotal()));
			totalSalary.setText(String.valueOf(currWork.getWZPTotal()));
			totalCostMachine.setText(String.valueOf(currWork.getWMachTotal()));
			totalSalaryMachine.setText(String.valueOf(currWork.getWZPMachTotal()));
			
			laborCostOfOneWorker.setText(String.valueOf(currWork.getWTz()));
			laborTotalCostWorker.setText(String.valueOf(currWork.getWTZTotal()));
			
			laborCostOfOneMachine.setText(String.valueOf(currWork.getWTZMach()));			
			laborTotalCostMachine.setText(String.valueOf(currWork.getWTZMachTotal()));			
			startDate.setText(sdf.format(currWork.getWStartDate()));
			percentDone.setText(String.valueOf(currWork.getWPercentDone()));
			countDone.setText(String.valueOf(currWork.getWCountDone()));
		}
	}
	
	private void fillWorkForUpdate(){
		if (currWork != null){
			currWork.setWCipher(cipherValue.getText().toString());
			currWork.setWName(nameValue.getText().toString());
			currWork.setWMeasured(measuredValue.getText().toString());
			currWork.setWCount(Float.parseFloat(countValue.getText().toString()));
			currWork.setWItogo(Float.parseFloat(costOfOneTotal.getText().toString()));
			currWork.setWZP(Float.parseFloat(salaryOfOne.getText().toString()));
			currWork.setWMach(Float.parseFloat(costOfOneMachine.getText().toString()));
			currWork.setWZPMach(Float.parseFloat(salaryOfOneMachine.getText().toString()));
			currWork.setWTotal(Float.parseFloat(totalCostCommon.getText().toString()));
			currWork.setWZPTotal(Float.parseFloat(totalSalary.getText().toString()));
			currWork.setWMachTotal(Float.parseFloat(totalCostMachine.getText().toString()));
			currWork.setWZPMachTotal(Float.parseFloat(totalSalaryMachine.getText().toString()));
			
			currWork.setWTz(Float.parseFloat(laborCostOfOneWorker.getText().toString()));
			currWork.setWTZTotal(Float.parseFloat(laborTotalCostWorker.getText().toString()));
			
			currWork.setWTZMach(Float.parseFloat(laborCostOfOneMachine.getText().toString()));			
			currWork.setWTZMachTotal(Float.parseFloat(laborTotalCostMachine.getText().toString()));
			try{
				currWork.setWStartDate(sdf.parse(startDate.getText().toString()));
			}catch(ParseException e){
				e.printStackTrace();
			}					
			currWork.setWPercentDone(Float.parseFloat(percentDone.getText().toString()));
			currWork.setWCountDone(Float.parseFloat(countDone.getText().toString()));
		}
	}
	
	public WorkDetailShowerActivity() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onGetDate(Date date) {
		// TODO Auto-generated method stub
		startDate.setText(sdf.format(date));
	}

}
