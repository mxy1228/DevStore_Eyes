package com.xmy.eyes.view;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.widget.Toast;

import com.xmy.eyes.Config;
import com.xmy.eyes.R;
import com.xmy.eyes.widget.WaitingDialog;

public abstract class BaseActivity extends FragmentActivity {

	private WaitingDialog mWaitingDialog;
	protected ActionBar mActionBar;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		this.mWaitingDialog = new WaitingDialog(this);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		this.mWaitingDialog.dismiss();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		mActionBar = getActionBar();
		mActionBar.setIcon(R.drawable.devstore_icon);
		mActionBar.setDisplayShowTitleEnabled(false);
		return true;
	}
	
	protected abstract void initView();
	
	protected abstract void initData();
	
	protected abstract void initEvent();	
	
	protected void showToast(String content){
		Toast.makeText(this, content, Toast.LENGTH_LONG).show();
	}
	
	protected void showToast(int res){
		Toast.makeText(this, res, Toast.LENGTH_SHORT).show();
	}
	
	protected void showDebugToast(String content){
		if(Config.DEBUG){
			Toast.makeText(this, content, Toast.LENGTH_LONG).show();
		}
	}
	
	protected void showDebugToast(int res){
		if(Config.DEBUG){
			Toast.makeText(this, res, Toast.LENGTH_SHORT).show();
		}
	}
	
	protected void showWaitingDialog(){
		this.mWaitingDialog.show();
	}
	
	protected void dissmisWaitingDialog(){
		this.mWaitingDialog.dismiss();
	}
	
}
