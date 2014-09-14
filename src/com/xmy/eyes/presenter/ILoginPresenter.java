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
	 * 将QQ登录信息提交到小米结构化存储
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
	 * 在小米结构化存储上进行用户注册
	 * （把用户信息保存在小米DB上）
	 */
//	public void registInMiDB(final QQLoginOnMiResultBean bean){
//		new Thread(new Runnable() {
//			
//			@Override
//			public void run() {
//				//首先判断该QQ是否已经注册
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
	 * QQ登录
	 */
//	public void tencentLogin(final Activity act){
//		EyesApplication.mTencent.login(act, "get_simple_userinfo", new IUiListener() {
//			
//			@Override
//			public void onError(UiError arg0) {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			@Override
//			public void onComplete(Object arg0) {
//				ELog.d("QQ login result = "+arg0.toString());
//				try {
//					QQLoginResultBean bean = JSONUtil.getMapper().readValue(arg0.toString(), new TypeReference<QQLoginResultBean>() {
//					});
//					if(bean != null){
////						registOnBmob(act, bean);
//					}
//				} catch (Exception e) {
//					ELog.e(e);
//				}
//			}
//			
//			@Override
//			public void onCancel() {
//				// TODO Auto-generated method stub
//				
//			}
//		});
//	}
	
	/**
	 * 百度登陆
	 * @param atv
	 */
	public void baiduLogin(final Activity atv){
		if(SPUtil.isLogin()){
			//用户已经登录，在Bmob上查询用户注册的信息
			BaiduUserInfo info = new BaiduUserInfo();
			info.setUsername(SPUtil.getUserName());
			registOnBmob(atv, info);
		}else{
			//用户还未登录，在Bmob上注册信息
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
							//然后将查询用户是否已经在Bmob上注册
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
	 * 根据百度用户名在Bmob上查询是否已经注册，若没注册则进行注册
	 * @param ctx
	 * @param bean
	 */
	public void registOnBmob(final Context ctx,final BaiduUserInfo info){
		BmobQuery<MyUser> query = new BmobQuery<MyUser>();
		query.addWhereEqualTo(Contants.BMOB_USER_NAME, info.getUsername());
		query.findObjects(ctx, new FindListener<MyUser>() {

			@Override
			public void onError(int arg0, String arg1) {
				ELog.e("registOnBmob:onError:"+arg0+":"+arg1);
			}

			@Override
			public void onSuccess(List<MyUser> arg0) {
				ELog.d("registOnBmob:onSuccess");
				if(arg0.size() == 0){
					//还没注册
					ELog.d("uid = "+info.getUsername()+" 还未注册");
					final MyUser user = new MyUser();
					user.setUid(info.getUserid());
					user.setUsername(info.getUsername());
					user.save(ctx, new SaveListener() {
						
						@Override
						public void onSuccess() {
							//注册用户信息成功后再保存推送信息
							registPushInfoOnBmob(ctx, user);
						}
						
						@Override
						public void onFailure(int arg0, String arg1) {
							ELog.e("first regist uid = "+user.getUid()+" failed:"+arg0+":"+arg1);
						}
					});
				}else{
					//已经注册
					ELog.d("uid = "+info.getUsername()+" 已经注册");
					mHandler.onSuccessRegistOnBmob(arg0.get(0));
				}
			}
			
		});
		
	}
	
	/**
	 * 在Bmob上注册推送信息，该信息会保存在Bmob后台的Installation表中
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
