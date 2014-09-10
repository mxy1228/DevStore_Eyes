package com.xmy.eyes.presenter;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.type.TypeReference;

import android.app.Activity;
import android.provider.ContactsContract.CommonDataKinds.Event;

import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;
import com.xiaomi.infra.galaxy.common.model.AttributeValue;
import com.xiaomi.infra.galaxy.common.model.GetRequest;
import com.xiaomi.infra.galaxy.common.model.GetResult;
import com.xiaomi.infra.galaxy.common.model.OAuthProvider;
import com.xiaomi.infra.galaxy.common.model.QQSatRequest;
import com.xmy.eyes.Contants;
import com.xmy.eyes.ELog;
import com.xmy.eyes.EyesApplication;
import com.xmy.eyes.bean.QQLoginOnMiResultBean;
import com.xmy.eyes.bean.QQLoginResultBean;
import com.xmy.eyes.bean.QQRegistOnMiResultBean;
import com.xmy.eyes.impl.ILoginHandler;
import com.xmy.eyes.util.JSONUtil;

import de.greenrobot.event.EventBus;

public class ILoginPresenter {

	private ILoginHandler mHandler;
	
	public ILoginPresenter(ILoginHandler handler){
		this.mHandler = handler;
	}
	
	/**
	 * 将QQ登录信息提交到小米结构化存储
	 */
	public void loginOnMi(final QQLoginResultBean bean){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				QQSatRequest satReq = new QQSatRequest();
				satReq.setAppId(Contants.MI_APP_ID);
				satReq.setOauthType(QQSatRequest.OAUTHTYPE_OPEN);
				satReq.setOauthAppId(Contants.QQ_APPID);
				satReq.setOauthProvider(OAuthProvider.QQ.name());
				satReq.setOpenid(bean.getOpenid());
				satReq.setAccessToken(bean.getAccess_token());
				if(EyesApplication.mMiClient.getstorageAccessToken(satReq)){
					ELog.i("success login on mi");
					QQLoginOnMiResultBean b = new QQLoginOnMiResultBean();
					b.setmQQOpenID(bean.getOpenid());
					EventBus.getDefault().post(b);
				}else{
					ELog.e("failed login on mi");
					QQLoginOnMiResultBean b = new QQLoginOnMiResultBean();
					b.setmQQOpenID(bean.getOpenid());
					EventBus.getDefault().post(b);
				}
			}
		}).start();
		
	}
	
	
	/**
	 * 在小米结构化存储上进行用户注册
	 * （把用户信息保存在小米DB上）
	 */
	public void registInMiDB(final QQLoginOnMiResultBean bean){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				//首先判断该QQ是否已经注册
				GetRequest getReq = new GetRequest();
				getReq.setTableName(Contants.TABLE_NAME);
				Map<String,AttributeValue> key = new HashMap<String,AttributeValue>();
				AttributeValue value = new AttributeValue();
				value.setType("STRING");
				value.setValue(bean.getmQQOpenID());
				key.put("uid", value);
				getReq.setKey(key);
				GetResult result = EyesApplication.mMiClient.get(getReq);
				QQRegistOnMiResultBean registResult = new QQRegistOnMiResultBean();
				registResult.setmRegisted(result.getAttributes().get("uid") == null);
				EventBus.getDefault().post(registResult);
			}
		}).start();
		
	}
	
	/*
	 * QQ登录
	 */
	public void tencentLogin(Activity act){
		EyesApplication.mTencent.login(act, "get_simple_userinfo", new IUiListener() {
			
			@Override
			public void onError(UiError arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onComplete(Object arg0) {
				ELog.d("QQ login result = "+arg0.toString());
				try {
					QQLoginResultBean bean = JSONUtil.getMapper().readValue(arg0.toString(), new TypeReference<QQLoginResultBean>() {
					});
					if(bean != null){
						mHandler.onQQLoginSuccess(bean);
					}
				} catch (Exception e) {
					ELog.e(e);
				}
			}
			
			@Override
			public void onCancel() {
				// TODO Auto-generated method stub
				
			}
		});
	}
}
