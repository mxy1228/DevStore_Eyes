package com.xmy.eyes.presenter;

import org.json.JSONObject;

import cn.bmob.push.BmobPush;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobQuery;

import com.xmy.eyes.EyesApplication;
import com.xmy.eyes.util.JSONUtil;

public class BmobPushMsgPresenter {

	private static BmobPushMsgPresenter mInstance;
	
	private BmobPushMsgPresenter(){
		
	}
	
	public static BmobPushMsgPresenter getDefault(){
		if(mInstance == null){
			synchronized (BmobPushMsgPresenter.class) {
				if(mInstance == null){
					mInstance = new BmobPushMsgPresenter();
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
		BmobQuery<BmobInstallation> query = BmobInstallation.getQuery();
		query.addWhereEqualTo("uid", uid);
		EyesApplication.mBmobPushManager.setQuery(query);
		EyesApplication.mBmobPushManager.pushMessage(obj);
	}
	
	public void sendMessage(Object obj,String uid){
		BmobQuery<BmobInstallation> query = BmobInstallation.getQuery();
		query.addWhereEqualTo("uid", uid);
		EyesApplication.mBmobPushManager.setQuery(query);
		EyesApplication.mBmobPushManager.pushMessage(JSONUtil.convertObjToJsonObject(obj));
	}
}
