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
	 * QQ��¼
	 */
	public void login(){
		QQSatRequest satReq = new QQSatRequest();
		satReq.setAppId(Contants.MI_APP_ID);
		satReq.setOauthType(QQSatRequest.OAUTHTYPE_OPEN);
		satReq.setOauthAppId("QQ��APPID");//TODO
		satReq.setOauthProvider(OAuthProvider.QQ.name());
		satReq.setOpenid("QQ OPEN ID");//TODO
		satReq.setAccessToken("QQ AccessToken");//TODO
		EyesApplication.mMiClient.getstorageAccessToken(satReq);
	}
	
	
	/**
	 * ��С�׽ṹ���洢�Ͻ����û�ע��
	 * �����û���Ϣ������С��DB�ϣ�
	 */
	public void registInMiDB(final String qq,final String token){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				//�����жϸ�QQ�Ƿ��Ѿ�ע��
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
