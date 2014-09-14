package com.xmy.eyes;

import org.codehaus.jackson.type.TypeReference;
import org.json.JSONObject;

import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.xmy.eyes.bean.ReqBindJsonBean;
import com.xmy.eyes.bean.ReqBindResultJsonBean;
import com.xmy.eyes.bean.SetGeofenceResultBean;
import com.xmy.eyes.impl.IMainHandler;
import com.xmy.eyes.presenter.IMainPresenter;
import com.xmy.eyes.util.JSONUtil;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.Toast;
import cn.bmob.push.PushConstants;
import de.greenrobot.event.EventBus;

/**
 * 为保证成功收到推送消息 
 */
public class MyPushMessageReceiver extends BroadcastReceiver {

	
	@Override
	public void onReceive(Context context, Intent intent) {
		NotificationManager manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
		String content = intent.getStringExtra(PushConstants.EXTRA_PUSH_MESSAGE_STRING);
		ELog.d("MyPushMessageReceiver:receive:"+EyesApplication.mMyUser.getUsername()+":"+intent.getStringExtra(PushConstants.EXTRA_PUSH_MESSAGE_STRING));
		Toast.makeText(context, content, Toast.LENGTH_LONG).show();
		try {
			JSONObject obj = new JSONObject(content);
			int type = obj.getInt(PushMessageContants.TYPE);
			switch (type) {
			case PushMessageContants.MSG_TYPE_GEOFENCE:
				
				break;
			//普通消息
			case PushMessageContants.MSG_TYPE_NORMAL:
				Notification notif = new Notification(R.drawable.search_btn, content, 0);
				RemoteViews view = new RemoteViews(context.getPackageName(), R.layout.notifacation_view);
				view.setTextViewText(R.id.notification_view_tv, content);
				notif.contentView = view;
				notif.flags = Notification.FLAG_NO_CLEAR;
				manager.notify(1, notif);
				break;
			//设置电子围栏
			case PushMessageContants.MSG_TYPE_SET_GEOFENCE:
				double x = obj.getDouble(PushMessageContants.X);
				double y = obj.getDouble(PushMessageContants.Y);
				int radius = obj.getInt(PushMessageContants.RADIUS);
				new IMainPresenter(new IMain()).setAndStartBDGeofence(x, y, radius);
				break;
			//设置电子围栏结果
			case PushMessageContants.MSG_TYPE_GEOFENCE_RESULT:
				SetGeofenceResultBean bean = new SetGeofenceResultBean();
				bean.setResult(obj.getBoolean(PushMessageContants.RESULT));
				EventBus.getDefault().post(bean);
				break;
			//别人请求绑定
			case PushMessageContants.MSG_TYPE_REQUEST_BIND:
				ReqBindJsonBean reqBindBean = JSONUtil.getMapper().readValue(content, new TypeReference<ReqBindJsonBean>() {
				});
				EventBus.getDefault().post(reqBindBean);
				break;
			//请求绑定结果
			case PushMessageContants.MSG_TYPE_REQUEST_BIND_RESULT:
				ReqBindResultJsonBean reqBindResultBean = JSONUtil.getMapper().readValue(content, new TypeReference<ReqBindResultJsonBean>() {
				});
				EventBus.getDefault().post(reqBindResultBean);
				break;
			default:
				break;
			}
		} catch (Exception e) {
			ELog.e(e);
		}
		
	}
	
	private class IMain implements IMainHandler{

		@Override
		public void onSuccessAddBDGeofences() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onLocated(double longitude, double latitude, String city) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onSuggestionSearch(SuggestionResult result) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onPOIDetailSearch(PoiDetailResult result) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onPOISeach(PoiResult result) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onGeofenceExit() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onGeofenceIn() {
			// TODO Auto-generated method stub
			
		}
		
	}

}
