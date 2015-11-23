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
import com.expertsoft.esmeta.R.color;
import com.expertsoft.esmeta.R.id;
import com.expertsoft.esmeta.R.layout;
import com.expertsoft.esmeta.data.OS;

public class OSLoaderAdapter extends BaseAdapter {

	List<OS> oEstim = null;;
	Context context;
	LayoutInflater lInflater;	
	int globalIterator = 0;
	
	public OSLoaderAdapter(Context context, List<OS> OSList) {
		// TODO Auto-generated constructor stub
		oEstim = OSList;
		this.context = context;
		lInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return oEstim.size();
	}

	@Override
	public OS getItem(int position) {
		// TODO Auto-generated method stub
		return oEstim.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View myview = lInflater.inflate(R.layout.os_ls_fragment_item, parent, false);
		LinearLayout ll = (LinearLayout)myview.findViewById(R.id.oslayout);		
		TextView osname = (TextView)myview.findViewById(R.id.osName);
		TextView osTotal = (TextView)myview.findViewById(R.id.osTotalNum);
		int bcolor = 0;
		final OS oest = (OS) oEstim.get(position);
		osname.setText(oest.getOsName());
		
		String total = String.valueOf(oest.getOsTotal());
		DecimalFormat df = new DecimalFormat("0.###");
		total = df.format(oest.getOsTotal());
		osTotal.setText(total);
		if (!total.equals("0")){
			osTotal.setText(total);
			osTotal.setVisibility(View.VISIBLE);
			((TextView)myview.findViewById(R.id.totalostxt)).setVisibility(View.VISIBLE);
		}else{
			osTotal.setVisibility(View.GONE);
			((TextView)myview.findViewById(R.id.totalostxt)).setVisibility(View.GONE);
		}
		if(globalIterator % 2 == 0){
			bcolor = context.getResources().getColor(R.color.listItemPositionColor);
		}else{
			bcolor = context.getResources().getColor(R.color.listItemPositionColor2);
		}	
		ll.setBackgroundColor(bcolor);
		myview.setTag(oest);
		globalIterator++;
		return myview;
	}

}
