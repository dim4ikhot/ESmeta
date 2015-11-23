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
import com.expertsoft.esmeta.data.Projects;

public class ProjectLoaderAdapter extends BaseAdapter{

	List<Projects> projects = null;;
	Context context;
	LayoutInflater lInflater;
	int globalIterator = 0;
	
	public ProjectLoaderAdapter(Context context, List<Projects> projectsList){
		projects = projectsList;
		this.context = context;
		lInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
	}		
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return projects.size();
	}

	@Override
	public Projects getItem(int position) {
		// TODO Auto-generated method stub
		return projects.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View myview = lInflater.inflate(R.layout.main_screen_list_item, parent, false);
		LinearLayout ll = (LinearLayout)myview.findViewById(R.id.mainlayout);		
		TextView projname = (TextView)myview.findViewById(R.id.ProjectName);
		TextView projTotal = (TextView)myview.findViewById(R.id.ProjectTotalNum);
		
		final Projects projects = (Projects) this.projects.get(position);
		String projName = projects.getProjectName();
		if(projName.length()> 50){
			projname.setText(projName.substring(0, 49)+"...");
		}else{
			projname.setText(projName);
		}	
		
		int bcolor= 0;
		String total = String.valueOf(projects.getProjectTotal());
		DecimalFormat df = new DecimalFormat("0.###");
		total = df.format(projects.getProjectTotal());
		projTotal.setText(total);
		if(globalIterator % 2 == 0){
			bcolor = context.getResources().getColor(R.color.listItemPositionColor);
		}else{
			bcolor = context.getResources().getColor(R.color.listItemPositionColor2);
		}	
		ll.setBackgroundColor(bcolor);
		myview.setTag(projects);
		globalIterator++;
		return myview;
	}
	
}

