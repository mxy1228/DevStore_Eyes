package com.xmy.eyes.presenter;

import java.util.HashMap;
import java.util.Map;

import com.xiaomi.infra.galaxy.common.model.AttributeValue;
import com.xiaomi.infra.galaxy.common.model.GetRequest;
import com.xiaomi.infra.galaxy.common.model.OAuthProvider;
import com.xiaomi.infra.galaxy.common.model.QQSatRequest;
import com.xmy.eyes.Contants;
import com.xmy.eyes.EyesApplication;
import com.xmy.eyes.impl.ILoginHandler;

public class ILoginPresenter {

	private ILoginHandler mHandler;
	
	public ILoginPresenter(ILoginHandler handler){
		this.mHandler = handler;
	}
	
	/**
	 * QQ登录
	 */
	public void login(){
		QQSatRequest satReq = new QQSatRequest();
		satReq.setAppId(Contants.MI_APP_ID);
		satReq.setOauthType(QQSatRequest.OAUTHTYPE_OPEN);
		satReq.setOauthAppId("QQ的APPID");//TODO
		satReq.setOauthProvider(OAuthProvider.QQ.name());
		satReq.setOpenid("QQ OPEN ID");//TODO
		satReq.setAccessToken("QQ AccessToken");//TODO
		EyesApplication.mMiClient.getstorageAccessToken(satReq);
	}
	
	
	/**
	 * 在小米结构化存储上进行用户注册
	 * （把用户信息保存在小米DB上）
	 */
	public void registInMiDB(final String qq,final String token){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				//首先判断该QQ是否已经注册
				GetRequest getReq = new GetRequest();
				getReq.setTableName(Contants.TABLE_NAME);
				Map<String,AttributeValue> key = new HashMap<String,AttributeValue>();
				AttributeValue value = new AttributeValue();
				value.setType("STRING");
				value.setValue(qq);
				key.put("uid", value);
				getReq.setKey(key);
			}
		}).start();
		
	}
}
