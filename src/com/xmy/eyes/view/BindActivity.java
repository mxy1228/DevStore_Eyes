package com.xmy.eyes.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.xmy.eyes.EyesApplication;
import com.xmy.eyes.PushMessageContants;
import com.xmy.eyes.R;
import com.xmy.eyes.bean.MyUser;
import com.xmy.eyes.bean.ReqBindJsonBean;
import com.xmy.eyes.bean.ReqBindResultJsonBean;
import com.xmy.eyes.impl.IBindHandler;
import com.xmy.eyes.presenter.BmobPushMsgPresenter;
import com.xmy.eyes.presenter.IBindPresenter;
import com.xmy.eyes.util.JSONUtil;

import de.greenrobot.event.EventBus;

public class BindActivity extends BaseActivity implements IBindHandler,OnClickListener{

	private EditText mET;
	private Button mWaitBnt;
	private Button mSetBtn;
	private TextView mWaitTV;
	
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
		this.mSetBtn = (Button)findViewById(R.id.bind_set_geofence_btn);
		this.mWaitBnt = (Button)findViewById(R.id.bind_wait_btn);
		this.mWaitTV = (TextView)findViewById(R.id.bind_wait_tv);
	}

	@Override
	protected void initData() {
		this.mPresenter = new IBindPresenter(this);
		mMyUser = (MyUser)getIntent().getSerializableExtra("user");
		EventBus.getDefault().register(this);
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
		this.mWaitBnt.setOnClickListener(this);
		this.mSetBtn.setOnClickListener(this);
	}


	/**
	 * 在Bmob根据用户名搜索注册信息
	 */
	@Override
	public void onSearch(final MyUser user) {
		if(user != null){
			if(user.getBindedUID() == null){
				//搜索到的用户还没和任何人绑定
				new AlertDialog.Builder(BindActivity.this).setMessage("要和"+user.getUsername()+"绑定吗？")
				.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						mPresenter.requestBind(BindActivity.this, user);
						dialog.dismiss();
					}
				}).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).show();
			}else{
				//搜索到的用户已经和别人绑定过了
				showToast(user.getUsername()+"已经和别人绑定");
			}
			
		}else{
			//没搜索到用户
		}
	}


	/**
	 * 在Bmob上修改绑定信息
	 */
	@Override
	public void onBind(boolean result) {
		if(result){
			//绑定成功
//			mSetBtn.setVisibility(View.VISIBLE);
//			mWaitBnt.setVisibility(View.VISIBLE);
//			mET.setVisibility(View.GONE);
			Intent intent = new Intent(BindActivity.this,MainActivity.class);
			startActivity(intent);
		}else{
			//绑定失败
			
		}
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bind_set_geofence_btn:
			mET.setVisibility(View.VISIBLE);
			mWaitBnt.setVisibility(View.GONE);
			mSetBtn.setVisibility(View.GONE);
			break;
		case R.id.bind_wait_btn:
			mSetBtn.setVisibility(View.GONE);
			mWaitBnt.setVisibility(View.GONE);
			mWaitTV.setVisibility(View.VISIBLE);
			break;
		default:
			break;
		}
	}

	/**
	 * 收到别人绑定的请求
	 * @param bean
	 */
	public void onEventMainThread(final ReqBindJsonBean bean){
		final ReqBindResultJsonBean result = new ReqBindResultJsonBean();
		result.setType(PushMessageContants.MSG_TYPE_REQUEST_BIND_RESULT);
		result.setTaUid(EyesApplication.mMyUser.getUid());
		result.setTaUserName(EyesApplication.mMyUser.getUsername());
		result.setMyUid(bean.getReqUid());
		result.setMyUserName(bean.getReqUserName());
		new AlertDialog.Builder(this).setMessage(bean.getMsg()).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				result.setAgree(true);
				mPresenter.bind(bean.getReqUserName(),bean.getReqUid(), BindActivity.this);
				BmobPushMsgPresenter.getDefault().sendMessage(JSONUtil.convertObjToJsonObject(result),bean.getReqUid());
			}
		}).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				result.setAgree(false);
				BmobPushMsgPresenter.getDefault().sendMessage(JSONUtil.convertObjToJsonObject(result),bean.getReqUid());
			}
		}).show();
	}
	
	/**
	 * 别人对请求绑定的回执结果
	 * @param bean
	 */
	public void onEventMainThread(ReqBindResultJsonBean bean){
		if(bean.isAgree()){
			mPresenter.bind(bean.getTaUserName(),bean.getTaUid(), BindActivity.this);
		}else{
			//TODO
		}
	}
}
