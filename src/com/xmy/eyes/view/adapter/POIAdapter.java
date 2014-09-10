package com.xmy.eyes.view.adapter;

import java.util.List;

import com.baidu.mapapi.search.core.PoiInfo;
import com.xmy.eyes.R;
import com.xmy.eyes.view.holder.POIViewHolder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class POIAdapter extends BaseAdapter {

	private Context mContext;
	private List<PoiInfo> mData;
	
	public POIAdapter(Context context,List<PoiInfo> data){
		this.mContext = context;
		this.mData = data;
	}
	
	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public Object getItem(int position) {
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			convertView = LayoutInflater.from(mContext).inflate(R.layout.poi_item_item, null);
			POIViewHolder hodler  = new POIViewHolder(convertView);
			convertView.setTag(hodler);
		}
		PoiInfo info = mData.get(position);
		POIViewHolder holder = (POIViewHolder)convertView.getTag();
		holder.getmNameTV().setText(info.name);
		holder.getmAddressTV().setText(info.address);
		return convertView;
		
	}

}
