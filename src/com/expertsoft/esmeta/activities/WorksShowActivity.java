package com.expertsoft.esmeta.activities;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.ExpandableListContextMenuInfo;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.expertsoft.esmeta.DBORM;
import com.expertsoft.esmeta.R;
import com.expertsoft.esmeta.adapters.WorksExpandableListAdapter;
import com.expertsoft.esmeta.data.LS;
import com.expertsoft.esmeta.data.OS;
import com.expertsoft.esmeta.data.Projects;
import com.expertsoft.esmeta.data.Works;
import com.j256.ormlite.dao.Dao;

public class WorksShowActivity extends Activity implements OnGroupClickListener, OnChildClickListener {

	
	public static final int CONSIST_ID = 1; 
	public static final int VIEW_POSITION = 2;
	
	Projects currProj;
	OS currOS;
	LS currLS;
	Context context;
	DBORM ormDB;		
	WorksExpandableListAdapter WexpLA;
	Works globalGroupWork;
	int groupPos;
	int childPos;	
	ExpandableListView worksLists;
	ProgressDialog pd;
		
	List<Works> groupList;
	ArrayList<List<Works>> childList;
	List<Works> childListItem;
	
	LoadData loadData;
	
	protected void onCreate(Bundle savedInstanceState){
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.works_shower);				
		childList = new ArrayList<List<Works>>();
		
		Intent intent = getIntent();
		currProj = (Projects)intent.getSerializableExtra("Project");
		currOS = (OS)intent.getSerializableExtra("Os");
		currLS = (LS)intent.getSerializableExtra("Ls");
		ormDB = new DBORM(this);
			
		worksLists = (ExpandableListView)findViewById(R.id.exWorksShow);
		worksLists.setOnGroupClickListener(this);
		worksLists.setOnChildClickListener(this);
		((TextView)findViewById(R.id.estimateCaption)).setText("Состав сметы (" + 
		                                                 currLS.getLsName().substring(0,
		                                                		 currLS.getLsName().length()) +")");
		
		registerForContextMenu(worksLists);
		
		startLoadData("Загрузка...","Подождите, пожалуйста, выполняется загрузка позиций");			
	}		
	
	private void startLoadData(String dialogTitle, String dialogCaption){
		loadData = new LoadData(this,dialogTitle, dialogCaption);
		loadData.execute();
	}
		
	
	class LoadData extends AsyncTask<Void,Void,Void>{

		
		Context cntxt;
		String title,caption;
		
		public LoadData(Context c, String dialogTitle, String dialogCaption){
			cntxt = c;
			title = dialogTitle;
			caption = dialogCaption;
		}
		
		void createDialog(Context context){
			if(pd == null)
			{
				pd = new ProgressDialog(context);
				pd.setTitle(title);
				pd.setMessage(caption);
				// меняем стиль на индикатор
				pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				// включаем анимацию ожидания
			    pd.setIndeterminate(true);
			    pd.setCancelable(false);
			    pd.setCanceledOnTouchOutside(false);
			    pd.show();
			}
		}
		
		
		
		void freeDialog(){
			 try{
		    	  if ((pd!= null)&(pd.isShowing())){
		    		  pd.dismiss();
		    		  pd = null;
		    	  }
		      }catch(IllegalArgumentException e){
		    	  e.printStackTrace();
		    	  pd = null;
		      }
		}
		
		protected void onPreExecute(){
			super.onPreExecute();	
			createDialog(cntxt);
		}
		
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			groupList = ormDB.getWorks(currProj, currOS, currLS);		
			fillChildList();
			return null;
		}
		
		protected void onPostExecute(Void result){
			super.onPostExecute(result);
			WexpLA = new WorksExpandableListAdapter(getApplicationContext(), groupList, childList);
			WexpLA.notifyDataSetChanged();
			worksLists.setAdapter(WexpLA);
			freeDialog();
		}
		
	}
		
	private void fillChildList(){
		childList.clear();
		for(Works w : groupList){
			childListItem = ormDB.getWorksChild(currProj,currOS, currLS, w);
			childList.add(childListItem);
		}
	}
	
	private void refreshWorksList(){
		groupList = ormDB.getWorks(currProj, currOS, currLS);		
		fillChildList();
		WexpLA = new WorksExpandableListAdapter(getApplicationContext(), groupList, childList);
		WexpLA.notifyDataSetChanged();
		worksLists.setAdapter(WexpLA);
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View view, ContextMenuInfo menuInfo){
		super.onCreateContextMenu(menu, view, menuInfo);
		//menu.add(0, CONSIST_ID, 0, "Состав позиции");
		//menu.add(0, VIEW_POSITION, 1, "Детали позиции");		
		menu.add(0, VIEW_POSITION, 1, "Данные о позиции");
	}
	
	public boolean onContextItemSelected(MenuItem item){
		Works groupWork = null;
		ExpandableListContextMenuInfo info = (ExpandableListContextMenuInfo) item.getMenuInfo();		
		switch(item.getItemId()){
		case CONSIST_ID:						
			groupPos = ExpandableListView.getPackedPositionGroup(info.packedPosition);
			childPos = ExpandableListView.getPackedPositionChild(info.packedPosition);
			if(groupPos != -1){
				groupWork = WexpLA.getGroup(groupPos);
			}
			if(((groupWork != null)&(! groupWork.getWRec().equals("razdel"))
					    &(! groupWork.getWRec().equals("chast"))
					    &(! groupWork.getWRec().equals("koef")))&(childPos == -1)){
				try{
					Intent intent = new Intent(getApplicationContext(), WorksResourceShowActivity.class);				
					intent.putExtra("Work", groupWork);
					startActivity(intent);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			break;
		case VIEW_POSITION:
			groupPos = ExpandableListView.getPackedPositionGroup(info.packedPosition);
			childPos = ExpandableListView.getPackedPositionChild(info.packedPosition);
			if(groupPos != -1){
				groupWork = WexpLA.getGroup(groupPos);
			}
			if((groupPos != -1)&(childPos != -1)){
				groupWork = WexpLA.getChild(groupPos, childPos);
			}
			if((groupWork != null)&(! groupWork.getWRec().equals("razdel"))
					                       &(! groupWork.getWRec().equals("chast"))
					                       &(! groupWork.getWRec().equals("koef"))){
				try{
					//Intent intent = new Intent(getApplicationContext(), WorkDetailShowerActivity.class);
					Intent intent = new Intent(getApplicationContext(), FragmentTabs.class);					
					intent.putExtra("work", groupWork);
					startActivityForResult(intent,VIEW_POSITION);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			break;
		}
		return super.onContextItemSelected(item);
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		super.onActivityResult(requestCode, resultCode, data);
		if ((resultCode == RESULT_OK)&(requestCode == VIEW_POSITION)){
			try{
				Dao<Works, Integer> workDao = ormDB.getHelper().getWorksDao();
				Works work = (Works)data.getSerializableExtra("work");
				workDao.update(work);	
				startLoadData("Обновление...", "Применение изменений");
			}catch(SQLException e){
				e.printStackTrace();
			}
		}
	}
	
	public WorksShowActivity() {
		// TODO Auto-generated constructor stub
	}
	

	@Override
	public boolean onChildClick(ExpandableListView parent, View v,
			int groupPosition, int childPosition, long id) {
		// TODO Auto-generated method stub
		Works groupWork = (Works)v.getTag();
		if((groupWork != null)&(! groupWork.getWRec().equals("razdel"))
                &(! groupWork.getWRec().equals("chast"))
                &(! groupWork.getWRec().equals("koef"))){
			try{
				//Intent intent = new Intent(getApplicationContext(), WorkDetailShowerActivity.class);
				Intent intent = new Intent(getApplicationContext(), FragmentTabs.class);
				
				intent.putExtra("work", groupWork);
				startActivityForResult(intent,VIEW_POSITION);
			}catch(Exception e){
			e.printStackTrace();
			}
		}
		return true;
	}

	@Override
	public boolean onGroupClick(ExpandableListView parent, View v,
			int groupPosition, long id) {
		// TODO Auto-generated method stub
		globalGroupWork = (Works)v.getTag();
		if((globalGroupWork != null)&(! globalGroupWork.getWRec().equals("razdel"))
                &(! globalGroupWork.getWRec().equals("chast"))
                &(! globalGroupWork.getWRec().equals("koef"))){
			try{
				//Intent intent = new Intent(getApplicationContext(), WorkDetailShowerActivity.class);
				Intent intent = new Intent(getApplicationContext(), FragmentTabs.class);
				
				intent.putExtra("work", globalGroupWork);
				startActivityForResult(intent,VIEW_POSITION);
			}catch(Exception e){
			e.printStackTrace();
			}
		}
		return true;
	}

}
