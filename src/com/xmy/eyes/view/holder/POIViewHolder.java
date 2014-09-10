package com.xmy.eyes.view.holder;

import com.xmy.eyes.R;

import android.view.View;
import android.widget.TextView;

public class POIViewHolder {

	private View mView;
	private TextView mNameTV;
	private TextView mAddressTV;
	
	
	public POIViewHolder(View view){
		this.mView = view;
	}


	public TextView getmNameTV() {
		if(mNameTV == null){
			mNameTV = (TextView)mView.findViewById(R.id.poi_item_name_tv);
		}
		return mNameTV;
	}


	public TextView getmAddressTV() {
		if(mAddressTV == null){
			mAddressTV = (TextView)mView.findViewById(R.id.poi_item_address_tv);
		}
		return mAddressTV;
	}
	
	
}
