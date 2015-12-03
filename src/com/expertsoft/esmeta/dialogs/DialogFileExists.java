package com.expertsoft.esmeta.dialogs;

import com.expertsoft.esmeta.DBORM;
import com.expertsoft.esmeta.R;
import com.expertsoft.esmeta.activities.DialogEnterName.OnGetNameListener;
import com.expertsoft.esmeta.data.Projects;
import com.expertsoft.esmeta.file_work.SaveBuild;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class DialogFileExists extends DialogFragment implements OnClickListener {

	boolean dialogresult = true;
	Context context;
	String name;
	String path;
	DBORM ormdatabase;
	Projects selectedProj;
	SaveBuild saveBuild;
	
	public DialogFileExists() {
		// TODO Auto-generated constructor stub
	}

	public DialogFileExists(String fileName, String filePath, Context ctx, DBORM ormdatabase, Projects proj) {		
		// TODO Auto-generated constructor stub
		name = fileName;
		path = filePath;
		context = ctx;
		this.ormdatabase = ormdatabase;
		selectedProj = proj;		
	}
	
	public Dialog onCreateDialog(Bundle savedInstanceState){
		super.onCreateDialog(savedInstanceState);
		AlertDialog.Builder adg = new AlertDialog.Builder(getActivity());		
		adg.setTitle("Сохранение...");
		adg.setMessage("Файл с таким именем существует. Заменить его?");
		adg.setPositiveButton("OK", this);
		adg.setNegativeButton("Отмена", this);
		adg.setCancelable(false);
		return adg.create();
	}
	
	public boolean getDialogResult(){
		return dialogresult;
	}
	
	@Override
	public void onClick(DialogInterface dialog, int which) {
		// TODO Auto-generated method stub
		switch(which){
		case Dialog.BUTTON_POSITIVE:			
			dialog.dismiss();					
			saveBuild = new SaveBuild(name, path, context, ormdatabase,  selectedProj);
			saveBuild.execute();			
			break;
		case Dialog.BUTTON_NEGATIVE:			
			dialog.dismiss();			
			break;
		}		
	}

}
