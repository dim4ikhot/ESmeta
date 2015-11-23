package com.expertsoft.esmeta.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;

import com.expertsoft.esmeta.R;

public class ProgramInfo extends DialogFragment implements OnClickListener {

	
	public Dialog onCreateDialog(Bundle savedInstanceState){
		super.onCreateDialog(savedInstanceState);
		AlertDialog.Builder adg = new AlertDialog.Builder(getActivity());
		View dialogView = getActivity().getLayoutInflater().inflate(R.layout.about_program_dialog, null);
		adg.setTitle("О программе");
		adg.setView(dialogView);
		adg.setPositiveButton("OK", this);
		return adg.create();
	}
	
	
	
	public ProgramInfo() {
		// TODO Auto-generated constructor stub
	}



	@Override
	public void onClick(DialogInterface dialog, int which) {
		// TODO Auto-generated method stub
		dialog.dismiss();
	}

}
