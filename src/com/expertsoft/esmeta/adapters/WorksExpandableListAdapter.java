package com.expertsoft.esmeta.adapters;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.expertsoft.esmeta.R;
import com.expertsoft.esmeta.data.Works;
import com.google.android.gms.appdatasearch.GetRecentContextCall;

public class WorksExpandableListAdapter extends BaseExpandableListAdapter {

	Context context;
	List<Works> groupWorks;
	ArrayList<List<Works>> childWorks;
	LayoutInflater lInflater;
	int globalIterator = 0;
	
	public WorksExpandableListAdapter(){}
	
	public WorksExpandableListAdapter(Context cnt, List<Works> GroupList, ArrayList<List<Works>> ChildList ){
		context = cnt;
		groupWorks = GroupList;
		childWorks = ChildList;
		lInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
	}
	
	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return groupWorks.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return childWorks.get(groupPosition).size();
	}

	@Override
	public Works getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return groupWorks.get(groupPosition);
	}

	@Override
	public Works getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childWorks.get(groupPosition).get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View myview = convertView;
		int bcolor = 0;
		if (myview == null){
			myview = lInflater.inflate(R.layout.works_shower_item, parent, false);
		}
		LinearLayout ll = (LinearLayout)myview.findViewById(R.id.worklayout);		
		TextView osname = (TextView)myview.findViewById(R.id.workName);
		TextView osTotal = (TextView)myview.findViewById(R.id.workTotalNum);
		TextView osMeasure = (TextView)myview.findViewById(R.id.worksMeasuredValue);
		TextView osCount = (TextView)myview.findViewById(R.id.worksCountValue);
		ImageView imngView = (ImageView)myview.findViewById(R.id.imgExpColl);	
		LinearLayout llMeas = (LinearLayout)myview.findViewById(R.id.layoutMeas); 
		LinearLayout llCount = (LinearLayout)myview.findViewById(R.id.layoutCount);
		LinearLayout llTotal = (LinearLayout)myview.findViewById(R.id.layoutTotal);
		((ExpandableListView)parent).expandGroup(groupPosition, false);
		imngView.setVisibility(View.GONE);
		/*
		if(childWorks.get(groupPosition).size() == 0){
			imngView.setVisibility(View.GONE);
		}
		else{
			imngView.setVisibility(View.VISIBLE);
			if(isExpanded){
				Drawable img = context.getResources().getDrawable(R.drawable.collapse);
				imngView.setImageDrawable(img);
			}else
			{
				Drawable img = context.getResources().getDrawable(R.drawable.expand);
				imngView.setImageDrawable(img);
			}
		}
		*/
		final Works work = (Works) groupWorks.get(groupPosition);
		if(work.getWName().length() <= 50){
			osname.setText(work.getWName());
		}else{
			osname.setText(work.getWName().substring(0, 49) + "...");
		}
		
		if((work.getWRec().contains("razdel"))|(work.getWRec().contains("chast"))){
			osname.setTextAppearance(context, R.style.boldText);
			bcolor = context.getResources().getColor(R.color.listItemPartPositionColor);
			ll.setBackgroundColor(bcolor);
			llMeas.setVisibility(View.GONE);
			llCount.setVisibility(View.GONE);
			
		}else 
		if(work.getWRec().contains("koef")){
			llMeas.setVisibility(View.GONE);
			llTotal.setVisibility(View.GONE);
			bcolor = context.getResources().getColor(R.color.listItemPartPositionColor);
			ll.setBackgroundColor(bcolor);
			((TextView)myview.findViewById(R.id.worksCount)).setText(context.getResources().getString(R.string.koefIs));
		}
		else{
			llMeas.setVisibility(View.VISIBLE);
			llTotal.setVisibility(View.VISIBLE);
			llCount.setVisibility(View.VISIBLE);
			osname.setTextAppearance(context, R.style.normalText);
			if(groupPosition % 2 == 0){
				bcolor = context.getResources().getColor(R.color.listItemPositionColor);
			}else{
				bcolor = context.getResources().getColor(R.color.listItemPositionColor2);
			}				
			
			ll.setBackgroundColor(bcolor);
		}
		float percentDone = work.getWPercentDone();
		if((percentDone>0)&(percentDone < 100)){
			bcolor = context.getResources().getColor(R.color.executingExists);
			osname.setTextColor(bcolor);
		}else if(percentDone == 100){
			bcolor = context.getResources().getColor(R.color.executingDone);
			osname.setTextColor(bcolor);
		}else{			
			osname.setTextColor(Color.BLACK);
		}
		
		try{
			String total = String.valueOf(work.getWTotal());
			DecimalFormat df = new DecimalFormat("0.###");
			total = df.format(work.getWTotal());
			osTotal.setText(total);
		
			String mes = work.getWMeasured();
			if (mes != null){
				osMeasure.setText(work.getWMeasured());
			}			
			total = df.format(work.getWCount());
			osCount.setText(total);			
		}catch(Exception e){
			e.printStackTrace();
		}		
		globalIterator++;				
		myview.setTag(work);
		return myview;		
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View childView = convertView;
		int bcolor = 0;
		if(childView == null){
			childView = lInflater.inflate(R.layout.works_shower_item, parent, false);
		}
		LinearLayout ll = (LinearLayout)childView.findViewById(R.id.worklayout);		
		TextView osname = (TextView)childView.findViewById(R.id.workName);
		TextView osTotal = (TextView)childView.findViewById(R.id.workTotalNum);
		TextView osMeasure = (TextView)childView.findViewById(R.id.worksMeasuredValue);
		TextView osCount = (TextView)childView.findViewById(R.id.worksCountValue);
		ImageView imngView = (ImageView)childView.findViewById(R.id.imgExpColl);
		
		imngView.setVisibility(View.VISIBLE);
		imngView.setAlpha(0f);
		final Works work = childWorks.get(groupPosition).get(childPosition);//getChild(groupPosition, childPosition);
		if(work.getWName().length() <= 50){
			osname.setText(work.getWName());
		}else{
			osname.setText(work.getWName().substring(0, 49) + "...");
		}
		float percentDone = work.getWPercentDone();
		if((percentDone>0)&(percentDone < 100)){
			bcolor = context.getResources().getColor(R.color.executingExists);
			osname.setTextColor(bcolor);
		}else if(percentDone == 100){
			bcolor = context.getResources().getColor(R.color.executingDone);
			osname.setTextColor(bcolor);
		}else{			
			osname.setTextColor(Color.BLACK);
		}
		try{
			String total = String.valueOf(work.getWTotal());
			DecimalFormat df = new DecimalFormat("0.###");
			total = df.format(work.getWTotal());
			osTotal.setText(total);
		
			String mes = work.getWMeasured();
			if (mes != null){
				osMeasure.setText(work.getWMeasured());
			}			
			total = df.format(work.getWCount());
			osCount.setText(total);			
		}catch(Exception e){
			e.printStackTrace();
		}	
		bcolor = context.getResources().getColor(R.color.posChildColor);
		ll.setBackgroundColor(bcolor);
		childView.setTag(work);
		
		return childView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}

}
