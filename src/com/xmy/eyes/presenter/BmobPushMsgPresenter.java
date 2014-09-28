package com.xmy.eyes.presenter;

import java.security.MessageDigest;

import org.json.JSONObject;

import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobQuery;

import com.loopj.android.http.RequestParams;
import com.xmy.eyes.ELog;
import com.xmy.eyes.EyesApplication;
import com.xmy.eyes.http.MyHttpRequest;
import com.xmy.eyes.util.JSONUtil;

public class BmobPushMsgPresenter {

	private static BmobPushMsgPresenter mInstance;
	private static MyHttpRequest mHttpRequest;
	
	private BmobPushMsgPresenter(){
		
	}
	
	public static BmobPushMsgPresenter getDefault(){
		if(mInstance == null){
			synchronized (BmobPushMsgPresenter.class) {
				if(mInstance == null){
					mInstance = new BmobPushMsgPresenter();
					mHttpRequest = new MyHttpRequest();
				}
			}
		}
		return mInstance;
	}
	
	/**
	 * ÍÆËÍÏûÏ¢
	 * @param obj
	 */
	public void sendMessage(JSONObject obj,String uid){
//		send(obj, uid);
		BmobQuery<BmobInstallation> query = BmobInstallation.getQuery();
		query.addWhereEqualTo("uid", uid);
		EyesApplication.mBmobPushManager.setQuery(query);
		EyesApplication.mBmobPushManager.pushMessage(obj);
	}
	
	public void sendMessage(Object obj,String uid){
//		JSONObject jsonObj = JSONUtil.convertObjToJsonObject(obj); 
//		send(jsonObj, uid);
		BmobQuery<BmobInstallation> query = BmobInstallation.getQuery();
		query.addWhereEqualTo("uid", uid);
		EyesApplication.mBmobPushManager.setQuery(query);
		EyesApplication.mBmobPushManager.pushMessage(JSONUtil.convertObjToJsonObject(obj));
	}
	
	private void send(JSONObject jsonObj,String uid){
		try {
			JSONObject sendObj = new JSONObject();
			sendObj.put("title", "title");
			sendObj.put("description", "description");
			sendObj.put("custom_content", "test");
			StringBuilder sb = new StringBuilder();
			RequestParams params = new RequestParams();
			params.put("apikey", "Al8y2B0qikgt6viHQXAYbgZp");
			sb.append("apikey=Al8y2B0qikgt6viHQXAYbgZp");
			params.put("method", "push_msg");
			sb.append("method=push_msg");
			params.put("messages", sendObj.toString());
			sb.append("messages="+sendObj.toString());
			params.put("msg_keys", "android");
			sb.append("msg_keys=android");
			params.put("push_type", "1");
			sb.append("push_type=1");
			String timestamp = System.currentTimeMillis()+"";
			params.put("timestamp", timestamp);
			sb.append("timestamp="+timestamp);
			params.put("user_id", uid);
			sb.append("user_id="+uid);
			String paramsStr = sb.toString();
			String sign = "POST"+"http://channel.api.duapp.com/rest/2.0/channel/channel"+paramsStr+"pC3sWwdweV7pz4jiXGeDbg0rZpQuLFx4";
			params.put("sign", md5(sign));
			mHttpRequest.sendPushMessage(params);
		} catch (Exception e) {
			ELog.e(e);
		}
		
	}
	
	
	private String md5(String input){
		try {
			byte[] hash = MessageDigest.getInstance("MD5").digest(input.getBytes("UTF-8"));    
		    StringBuilder hex = new StringBuilder();  
		    for (byte b : hash) {  
		       int num = b & 0xff;
		       String h = Integer.toHexString(num);
		       if(h.length() == 1){
		    	   hex.append(0);
		       }
		       hex.append(h);
		    }  
		    return hex.toString();  
		} catch (Exception e) {
			ELog.e(e);
		}
		return null;
	}
	
	
}
