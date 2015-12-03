package com.expertsoft.esmeta.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.expertsoft.esmeta.R;
import com.expertsoft.esmeta.OpenFileType.GetPathDialogListener;

public class DialogEnterName extends DialogFragment implements OnClickListener {

	public interface OnGetNameListener{
		void getSaveName(String name);
	}
	
	View dialogView;
	String defName;
	
	
	public DialogEnterName(String name){
		defName = name;
	}
	
	public Dialog onCreateDialog(Bundle savedInstanceState){
		super.onCreateDialog(savedInstanceState);
		AlertDialog.Builder adg = new AlertDialog.Builder(getActivity());
		dialogView = getActivity().getLayoutInflater().inflate(R.layout.save_build, null);
		((EditText)dialogView.findViewById(R.id.etBuildName)).setText(defName);
		adg.setTitle("Имя стройки");
		adg.setView(dialogView);
		adg.setPositiveButton("OK", this);
		adg.setNegativeButton("Отмена", this);
		return adg.create();
	}
	
		
	public DialogEnterName() {
		// TODO Auto-generated constructor stub
	}



	@Override
	public void onClick(DialogInterface dialog, int which) {
		// TODO Auto-generated method stub
		switch(which){
		case Dialog.BUTTON_POSITIVE:
			String name = ((EditText)dialogView.findViewById(R.id.etBuildName)).getText().toString();
			dialog.dismiss();
			OnGetNameListener mainActivity = (OnGetNameListener)getActivity();
			mainActivity.getSaveName(name);			
			break;
		case Dialog.BUTTON_NEGATIVE:
			dialog.dismiss();
			Toast.makeText(getActivity(), "Сохранение отменено", Toast.LENGTH_LONG).show();
			break;
		}		
	}

}
