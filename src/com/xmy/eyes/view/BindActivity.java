package com.xmy.eyes.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
	private MyUser mMyUser;
	
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
		mMyUser = (MyUser)getIntent().getSerializableExtra("user");
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
	 * ��Bmob�����û�������ע����Ϣ
	 */
	@Override
	public void onSearch(final MyUser user) {
		if(user != null){
			if(user.getBindInstallationId() == null){
				//���������û���û���κ��˰�
				new AlertDialog.Builder(BindActivity.this).setMessage("Ҫ��"+user.getUsername()+"����")
				.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						mPresenter.bind(BindActivity.this, mMyUser, user);
						dialog.dismiss();
					}
				}).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).show();
			}else{
				//���������û��Ѿ��ͱ��˰󶨹���
				showToast(user.getUsername()+"�Ѿ��ͱ��˰�");
			}
			
		}else{
			//û�������û�
		}
	}


	/**
	 * ��Bmob���޸İ���Ϣ
	 */
	@Override
	public void onBind(boolean result) {
		if(result){
			//�󶨳ɹ�
			Intent intent = new Intent(BindActivity.this, MainActivity.class);
			intent.putExtra("user", mMyUser);
			startActivity(intent);
		}else{
			//��ʧ��
			
		}
	}

}
