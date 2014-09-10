package com.xmy.eyes.widget;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.xmy.eyes.R;

public class WaitingDialog extends ProgressDialog{
	
	private Context mContext;
	private View mView;

	public WaitingDialog(Context context, int theme) {
		super(context, theme);
		this.mContext = context;
		init();
	}

	public WaitingDialog(Context context) {
		super(context);
		this.mContext = context;
		init();
	}
	
	protected WaitingDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context);
		this.mContext = context;
		init();
	}

	public void init(){
		LayoutInflater inflater = LayoutInflater.from(mContext);
		mView = inflater.inflate(R.layout.waiting_dialog, null);
		getWindow().setBackgroundDrawableResource(android.R.color.transparent);
	}
	
	@Override
	public void show() {
		super.show();
		setContentView(mView);
	}
}
