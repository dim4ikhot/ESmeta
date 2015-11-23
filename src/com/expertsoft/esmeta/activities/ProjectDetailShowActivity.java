package com.expertsoft.esmeta.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.expertsoft.esmeta.R;
import com.expertsoft.esmeta.data.Projects;

public class ProjectDetailShowActivity extends Activity implements OnClickListener {

	Projects currProj;
	TextView nameValue, cipherValue, customerValue, contractorValue, totalValue;
	Button closePD;
	
	protected void onCreate(Bundle savedInstanceState){
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.project_detail_show);
		currProj = (Projects)getIntent().getSerializableExtra("proj");
		
		initControls();
		getData();
		closePD.setOnClickListener(this);
	}
		
	private void initControls(){
		nameValue  = (TextView)findViewById(R.id.tvProjNameValue);
		cipherValue  = (TextView)findViewById(R.id.tvProjCipherValue); 
		customerValue  = (TextView)findViewById(R.id.tvProjCustomerValue); 
		contractorValue  = (TextView)findViewById(R.id.tvProjContractorValue);
		totalValue  = (TextView)findViewById(R.id.tvProjTotalValue);
		closePD = (Button)findViewById(R.id.btnClosePD);
	}
	
	//get projects data
	private void getData(){
		if(currProj != null){
			nameValue.setText(currProj.getProjectName());
			cipherValue.setText(currProj.getProjectCipher()); 
			customerValue.setText(currProj.getProjectCustomer()); 
			contractorValue.setText(currProj.getProjectContractor());
			totalValue.setText(String.valueOf(currProj.getProjectTotal()));			
		}
	}
	//if in future will need change data, this part of code will be useful
	private void setData(){
		if(currProj != null){
			try{
			currProj.setProjectName(nameValue.getText().toString());
			currProj.setProjectCipher(cipherValue.getText().toString()); 
			currProj.setProjectCustomer(customerValue.getText().toString()); 
			currProj.setProjectContractor(contractorValue.getText().toString());
			currProj.setProjectTotal(Float.parseFloat(totalValue.getText().toString()));
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	
	
	public ProjectDetailShowActivity() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		setData();
		// return result from this activity
		Intent intent = new Intent();
		intent.putExtra("proj", currProj);
		setResult(RESULT_OK, intent);
		finish();
	}

}
