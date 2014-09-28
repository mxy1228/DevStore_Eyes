package com.xmy.eyes.http;

import org.codehaus.jackson.type.TypeReference;

import com.loopj.android.http.RequestParams;
import com.xmy.eyes.ELog;
import com.xmy.eyes.bean.BaiduUserInfo;
import com.xmy.eyes.util.JSONUtil;

public class MyHttpRequest {

	
	public interface RequestHandler<T>{
		public void onStart();
		public void onFailed(String msg);
		public void onSuccess(T t);
	}
	
	/**
	 * ���ݰٶȵ�access_token������ϸ�û���Ϣ
	 * @param token
	 * @param handler
	 */
	public void requestBaiduUserInfo(String token,final RequestHandler<BaiduUserInfo> handler){
		RequestParams params = new RequestParams();
		params.put("access_token", token);
		new MyHttpClient().post(HttpContants.REQUEST_BAIDU_USER_INFO, params, new MyHttpHandler(){
			
			@Override
			public void onStart() {
				super.onStart();
				handler.onStart();
			}
			
			@Override
			public void onSuccess(int statusCode, String content) {
				super.onSuccess(statusCode, content);
				try {
					BaiduUserInfo info = JSONUtil.getMapper().readValue(content, new TypeReference<BaiduUserInfo>() {
					});
					if(info != null){
						handler.onSuccess(info);
					}
				} catch (Exception e) {
					ELog.e(e);
				}
			}
			
			@Override
			@Deprecated
			public void onFailure(int statusCode, Throwable error,
					String content) {
				super.onFailure(statusCode, error, content);
				handler.onFailed(content);
			}
			
		});
	}
	
	/**
	 * ����������Ϣ
	 */
	public void sendPushMessage(RequestParams params){
		new MyHttpClient().post("http://channel.api.duapp.com/rest/2.0/channel/channel", params, new MyHttpHandler(){
			
			@Override
			public void onStart() {
				super.onStart();
			}
			
			@Override
			public void onSuccess(int statusCode, String content) {
				super.onSuccess(statusCode, content);
			}
			
			@Override
			@Deprecated
			public void onFailure(int statusCode, Throwable error,
					String content) {
				super.onFailure(statusCode,error, content);
			}
		});
	}
}
