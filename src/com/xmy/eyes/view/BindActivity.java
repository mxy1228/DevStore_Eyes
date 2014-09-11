package com.xmy.eyes.view;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.xmy.eyes.R;
import com.xmy.eyes.bean.MyUser;
import com.xmy.eyes.impl.IBindHandler;
import com.xmy.eyes.presenter.IBindPresenter;

public class BindActivity extends BaseActivity implements IBindHandler{

	private EditText mET;
	
	private IBindPresenter mPresenter;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.bind);
		initView();
		initData();
		initEvent();
	}
	
	
	@Override
	protected void initView() {
		this.mET = (EditText)findViewById(R.id.bind_username_et);
	}

	@Override
	protected void initData() {
		this.mPresenter = new IBindPresenter(this);
	}

	@Override
	protected void initEvent() {
		this.mET.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				String userName = mET.getText().toString();
				if(actionId == EditorInfo.IME_ACTION_SEARCH){
					mPresenter.search(BindActivity.this, userName);
				}
				return true;
			}
		});
	}


	/**
	 * ÔÚBmobËÑË÷
	 */
	@Override
	public void onSearch(MyUser user) {
		
	}

}
