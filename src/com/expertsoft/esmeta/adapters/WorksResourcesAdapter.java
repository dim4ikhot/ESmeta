package com.expertsoft.esmeta.adapters;

import java.text.DecimalFormat;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.expertsoft.esmeta.R;
import com.expertsoft.esmeta.data.WorksResources;

public class WorksResourcesAdapter extends BaseAdapter {

	Context context;
	List<WorksResources> worksResList;
	LayoutInflater lInflater;
	int globalIterator = 0;
	
	public WorksResourcesAdapter(Context cnt,List<WorksResources> worksResList) {
		// TODO Auto-generated constructor stub
		context = cnt;
		this.worksResList = worksResList;
		lInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return worksResList.size();
	}

	@Override
	public WorksResources getItem(int position) {
		// TODO Auto-generated method stub
		return worksResList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View myview = convertView;
		if (myview == null){
			myview = lInflater.inflate(R.layout.work_resource_item, parent, false);
		}
		LinearLayout ll = (LinearLayout)myview.findViewById(R.id.wrlayout);		
		TextView wrname = (TextView)myview.findViewById(R.id.wrName);
		TextView wrTotal = (TextView)myview.findViewById(R.id.wrTotalNum);
		TextView wrCount = (TextView)myview.findViewById(R.id.wrCountsNum);
		TextView wrCost = (TextView)myview.findViewById(R.id.wrCostsNum);		
						
		final WorksResources workRes = (WorksResources)  worksResList.get(position);
		if(workRes.getWrName().length() <= 50){
			wrname.setText(workRes.getWrName());
		}else{
			wrname.setText(workRes.getWrName().substring(0, 49) + "...");
		}
		int bcolor = 0;
		DecimalFormat df2 = new DecimalFormat("#.##");
		wrTotal.setText(df2.format(workRes.getWrTotalCost()));//(String.valueOf(workRes.getWrTotalCost()));
		wrCount.setText(String.valueOf(workRes.getWrCount()));		
		wrCost.setText(String.valueOf(workRes.getWrCost()));
		if(globalIterator % 2 == 0){
			bcolor = context.getResources().getColor(R.color.listItemPositionColor);
		}else{
			bcolor = context.getResources().getColor(R.color.listItemPositionColor2);
		}	
		ll.setBackgroundColor(bcolor);
		myview.setTag(workRes);
		globalIterator++;
		return myview;		
	}

}
