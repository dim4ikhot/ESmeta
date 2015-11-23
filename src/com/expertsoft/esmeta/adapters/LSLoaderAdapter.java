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
import com.expertsoft.esmeta.data.LS;
import com.expertsoft.esmeta.data.OS;

public class LSLoaderAdapter  extends BaseAdapter{

	List<LS> lEstim = null;;
	Context context;
	LayoutInflater lInflater;
	int globalIterator = 0;
	
	public LSLoaderAdapter(Context context, List<LS> LSList) {
		// TODO Auto-generated constructor stub
		lEstim = LSList;
		this.context = context;
		lInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return lEstim.size();
	}

	@Override
	public LS getItem(int position) {
		// TODO Auto-generated method stub
		return lEstim.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View myview = lInflater.inflate(R.layout.os_ls_fragment_item, parent, false);
		LinearLayout ll = (LinearLayout)myview.findViewById(R.id.oslayout);		
		TextView osname = (TextView)myview.findViewById(R.id.osName);
		TextView osTotal = (TextView)myview.findViewById(R.id.osTotalNum);
		int bcolor = 0;
		final LS lest = (LS) lEstim.get(position);
		osname.setText(lest.getLsName());
		String total = String.valueOf(lest.getLsTotal());
		DecimalFormat df = new DecimalFormat("0.###");
		total = df.format(lest.getLsTotal());
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
		myview.setTag(lest);
		globalIterator++;
		return myview;		
	}

}
