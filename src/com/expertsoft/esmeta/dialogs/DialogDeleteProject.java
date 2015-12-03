package com.expertsoft.esmeta.dialogs;

import java.sql.SQLException;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.ListView;

import com.expertsoft.esmeta.DBORM;
import com.expertsoft.esmeta.adapters.ProjectLoaderAdapter;
import com.expertsoft.esmeta.data.Projects;
import com.j256.ormlite.dao.Dao;

public class DialogDeleteProject  extends DialogFragment implements OnClickListener {

	List<Projects> allProjectsList;
	ProjectLoaderAdapter PLA;
	ListView projectsList;
	Context context;
	DBORM ormdatabase;
	Projects currProj;
	
	public DialogDeleteProject() {
		// TODO Auto-generated constructor stub
	}

	public DialogDeleteProject(List<Projects> prList, ProjectLoaderAdapter pla, 
			ListView projLW, Context ctx, DBORM base, Projects proj) {
		allProjectsList = prList;
		PLA = pla;
		projectsList = projLW;
		context = ctx;
		ormdatabase = base;
		currProj = proj;
		// TODO Auto-generated constructor stub
	}
	
	public Dialog onCreateDialog(Bundle savedInstanceState){
		super.onCreateDialog(savedInstanceState);
		AlertDialog.Builder adg = new AlertDialog.Builder(getActivity());		
		adg.setTitle("Удаление...");
		adg.setMessage("Вы действительно желаете удалить стройку?");
		adg.setPositiveButton("OK", this);
		adg.setNegativeButton("Отмена", this);
		adg.setCancelable(false);
		return adg.create();
	}
	
	public void RefreshProjectsList(){
		allProjectsList = ormdatabase.getAllProjectsData();		
	    PLA = new ProjectLoaderAdapter(context, allProjectsList);
	    PLA.notifyDataSetChanged();
	    projectsList.setAdapter(PLA);
	}
	
	@Override
	public void onClick(DialogInterface dialog, int which) {
		// TODO Auto-generated method stub
		switch(which){
		case Dialog.BUTTON_POSITIVE:			
			dialog.dismiss();					
			try{
		    	//Getting Dao object
		    	Dao<Projects,Integer> projectDao = ormdatabase.getHelper().getProjectsDao();
		    	//Delete project from DataBase
		    	projectDao.delete(currProj);
		    	//Remove position frpm List
		    	allProjectsList.remove(currProj);
		    	RefreshProjectsList();
		    }catch(SQLException e){
		    	e.printStackTrace();
		    }
			break;
		case Dialog.BUTTON_NEGATIVE:			
			dialog.dismiss();			
			break;
		}	
	}

}
