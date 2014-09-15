package com.xmy.eyes.presenter;

import java.util.List;

import org.codehaus.jackson.type.TypeReference;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import cn.bmob.push.BmobPush;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

import com.baidu.api.BaiduDialog.BaiduDialogListener;
import com.baidu.api.BaiduDialogError;
import com.baidu.api.BaiduException;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;
import com.xmy.eyes.Contants;
import com.xmy.eyes.ELog;
import com.xmy.eyes.EyesApplication;
import com.xmy.eyes.bean.BaiduUserInfo;
import com.xmy.eyes.bean.MyBmobInstallation;
import com.xmy.eyes.bean.MyUser;
import com.xmy.eyes.bean.QQLoginResultBean;
import com.xmy.eyes.http.MyHttpRequest;
import com.xmy.eyes.http.MyHttpRequest.RequestHandler;
import com.xmy.eyes.impl.ILoginHandler;
import com.xmy.eyes.util.JSONUtil;
import com.xmy.eyes.util.SPUtil;
import com.xmy.eyes.view.LoginActivity;

public class ILoginPresenter {

	private ILoginHandler mHandler;
	
	public ILoginPresenter(ILoginHandler handler){
		this.mHandler = handler;
	}
	
	/**
	 * ��QQ��¼��Ϣ�ύ��С�׽ṹ���洢
	 */
//	public void loginOnMi(final QQLoginResultBean bean){
//		new Thread(new Runnable() {
//			
//			@Override
//			public void run() {
//				QQSatRequest satReq = new QQSatRequest();
//				satReq.setAppId(Contants.MI_APP_ID);
//				satReq.setOauthType(QQSatRequest.OAUTHTYPE_OPEN);
//				satReq.setOauthAppId(Contants.QQ_APPID);
//				satReq.setOauthProvider(OAuthProvider.QQ.name());
//				satReq.setOpenid(bean.getOpenid());
//				satReq.setAccessToken(bean.getAccess_token());
//				if(EyesApplication.mMiClient.getstorageAccessToken(satReq)){
//					ELog.i("success login on mi");
//					QQLoginOnMiResultBean b = new QQLoginOnMiResultBean();
//					b.setmQQOpenID(bean.getOpenid());
//					EventBus.getDefault().post(b);
//				}else{
//					ELog.e("failed login on mi");
//				}
//			}
//		}).start();
//		
//	}
	
	
	/**
	 * ��С�׽ṹ���洢�Ͻ����û�ע��
	 * �����û���Ϣ������С��DB�ϣ�
	 */
//	public void registInMiDB(final QQLoginOnMiResultBean bean){
//		new Thread(new Runnable() {
//			
//			@Override
//			public void run() {
//				//�����жϸ�QQ�Ƿ��Ѿ�ע��
//				GetRequest getReq = new GetRequest();
//				getReq.setTableName(Contants.TABLE_NAME);
//				Map<String,AttributeValue> key = new HashMap<String,AttributeValue>();
//				AttributeValue value = new AttributeValue();
//				value.setType("STRING");
//				value.setValue(bean.getmQQOpenID());
//				key.put("uid", value);
//				getReq.setKey(key);
//				GetResult result = EyesApplication.mMiClient.get(getReq);
//				QQRegistOnMiResultBean registResult = new QQRegistOnMiResultBean();
//				registResult.setmRegisted(result.getAttributes().get("uid") == null);
//				EventBus.getDefault().post(registResult);
//			}
//		}).start();
//		
//	}
	
	/*
	 * QQ��¼
	 */
	public void tencentLogin(final Activity act){
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
//						registOnBmob(act, bean);
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
	
	/**
	 * �ٶȵ�½
	 * @param atv
	 */
	public void baiduLogin(final Activity atv){
		if(SPUtil.isLogin()){
			//�û��Ѿ���¼����Bmob�ϲ�ѯ�û�ע�����Ϣ
			BaiduUserInfo info = new BaiduUserInfo();
			info.setUserid(SPUtil.getUID());
			info.setUsername(SPUtil.getUserName());
			registOnBmob(atv, info);
		}else{
			//�û���δ��¼����Bmob��ע����Ϣ
			EyesApplication.mBaidu.authorize(atv, true, true, new BaiduDialogListener() {
				
				@Override
				public void onError(BaiduDialogError arg0) {
					ELog.e("baiduLogin error"+arg0.getMessage());
				}
				
				@Override
				public void onComplete(Bundle arg0) {
					ELog.d("baiduLogin success");
					new MyHttpRequest().requestBaiduUserInfo(arg0.getString("access_token"), new RequestHandler<BaiduUserInfo>() {
						
						@Override
						public void onSuccess(BaiduUserInfo t) {
							//Ȼ�󽫲�ѯ�û��Ƿ��Ѿ���Bmob��ע��
							registOnBmob(atv, t);
						}
						
						@Override
						public void onStart() {
							// TODO Auto-generated method stub
							
						}
						
						@Override
						public void onFailed(String msg) {
							// TODO Auto-generated method stub
							
						}
					});
				}
				
				@Override
				public void onCancel() {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onBaiduException(BaiduException arg0) {
					ELog.e("baiduLogin exception"+arg0.getMessage());
				}
			});
		}
	}
	
	
	/**
	 * ���ݰٶ��û�����Bmob�ϲ�ѯ�Ƿ��Ѿ�ע�ᣬ��ûע�������ע��
	 * @param ctx
	 * @param bean
	 */
	public void registOnBmob(final Context ctx,final BaiduUserInfo info){
		BmobQuery<MyUser> query = new BmobQuery<MyUser>();
		query.addWhereEqualTo(Contants.BMOB_UID, info.getUserid());
		query.findObjects(ctx, new FindListener<MyUser>() {

			@Override
			public void onError(int arg0, String arg1) {
				ELog.e("registOnBmob:onError:"+arg0+":"+arg1);
			}

			@Override
			public void onSuccess(List<MyUser> arg0) {
				ELog.d("registOnBmob:onSuccess");
				if(arg0.size() == 0){
					//��ûע��
					ELog.d("uid = "+info.getUsername()+" ��δע��");
					final MyUser user = new MyUser();
					user.setUid(info.getUserid());
					user.setUsername(info.getUsername());
					user.save(ctx, new SaveListener() {
						
						@Override
						public void onSuccess() {
							//ע���û���Ϣ�ɹ����ٱ���������Ϣ
							registPushInfoOnBmob(ctx, user);
						}
						
						@Override
						public void onFailure(int arg0, String arg1) {
							ELog.e("first regist uid = "+user.getUid()+" failed:"+arg0+":"+arg1);
						}
					});
				}else{
					//�Ѿ�ע��
					ELog.d("uid = "+info.getUserid()+" �Ѿ�ע��");
					SPUtil.setUID(arg0.get(0).getUid());
					SPUtil.setUserName(arg0.get(0).getUsername());
					mHandler.onSuccessRegistOnBmob(arg0.get(0));
				}
			}
			
		});
		
	}
	
	/**
	 * ��Bmob��ע��������Ϣ������Ϣ�ᱣ����Bmob��̨��Installation����
	 */
	private void registPushInfoOnBmob(final Context ctx,final MyUser myUser){
		ELog.d("first regist uid = "+myUser.getUsername()+" success");
		MyBmobInstallation installation = new MyBmobInstallation(ctx);
		installation.setUid(myUser.getUid());
		installation.setUsername(myUser.getUsername());
		installation.save(ctx, new SaveListener() {
			
			@Override
			public void onSuccess() {
				ELog.d("registPushInfoOnBmob:onSuccess");
				mHandler.onSuccessRegistOnBmob(myUser);
			}
			
			@Override
			public void onFailure(int arg0, String arg1) {
				ELog.e("registPushInfoOnBmob:onFailure:"+arg0+":"+arg1);
			}
		});
	}
}
