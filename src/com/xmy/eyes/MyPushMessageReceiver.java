package com.xmy.eyes;

import org.codehaus.jackson.type.TypeReference;
import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.Toast;
import cn.bmob.push.PushConstants;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.xmy.eyes.bean.GeofenceBean;
import com.xmy.eyes.bean.ReqBindJsonBean;
import com.xmy.eyes.bean.ReqBindResultJsonBean;
import com.xmy.eyes.bean.RequestLocateResultBean;
import com.xmy.eyes.bean.SetGeofenceResultBean;
import com.xmy.eyes.impl.IMainHandler;
import com.xmy.eyes.presenter.IMainPresenter;
import com.xmy.eyes.util.JSONUtil;
import com.xmy.eyes.view.MainActivity;

import de.greenrobot.event.EventBus;

/**
 * Ϊ��֤�ɹ��յ�������Ϣ 
 */
public class MyPushMessageReceiver extends BroadcastReceiver {

	private Context mContext;
	private IMainPresenter mPresenter;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		if(mPresenter == null){
			mPresenter = new IMainPresenter(new IMain());
		}
		this.mContext = context;
		NotificationManager manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
		String content = intent.getStringExtra(PushConstants.EXTRA_PUSH_MESSAGE_STRING);
		ELog.d("MyPushMessageReceiver:receive:"+":"+intent.getStringExtra(PushConstants.EXTRA_PUSH_MESSAGE_STRING));
		if(Config.DEBUG){
			Toast.makeText(context, content, Toast.LENGTH_LONG).show();
		}
		try {
			JSONObject obj = new JSONObject(content);
			int type = obj.getInt(PushMessageContants.TYPE);
			switch (type) {
			case PushMessageContants.MSG_TYPE_GEOFENCE:
				GeofenceBean geofenceBean = JSONUtil.getMapper().readValue(content, new TypeReference<GeofenceBean>() {
				});
				EventBus.getDefault().post(geofenceBean);
				showNotification(geofenceBean);
				break;
			//��ͨ��Ϣ
			case PushMessageContants.MSG_TYPE_NORMAL:
				Notification notif = new Notification(R.drawable.search_btn, content, 0);
				RemoteViews view = new RemoteViews(context.getPackageName(), R.layout.notifacation_view);
				view.setTextViewText(R.id.notification_view_tv, content);
				notif.contentView = view;
				notif.flags = Notification.FLAG_NO_CLEAR;
				manager.notify(1, notif);
				break;
			//���ò���������Χ��
			case PushMessageContants.MSG_TYPE_SET_GEOFENCE:
				double lon = obj.getDouble(PushMessageContants.LON);
				double lat = obj.getDouble(PushMessageContants.LAT);
				int radius = obj.getInt(PushMessageContants.RADIUS);
				mPresenter.setAndStartBDGeofence(context,lon, lat, radius);
				break;
			//���õ���Χ�����
			case PushMessageContants.MSG_TYPE_GEOFENCE_RESULT:
				SetGeofenceResultBean bean = JSONUtil.getMapper().readValue(content, new TypeReference<SetGeofenceResultBean>() {
				});
				EventBus.getDefault().post(bean);
				break;
			//���������
			case PushMessageContants.MSG_TYPE_REQUEST_BIND:
				ReqBindJsonBean reqBindBean = JSONUtil.getMapper().readValue(content, new TypeReference<ReqBindJsonBean>() {
				});
				EventBus.getDefault().post(reqBindBean);
				break;
			//����󶨽��
			case PushMessageContants.MSG_TYPE_REQUEST_BIND_RESULT:
				ReqBindResultJsonBean reqBindResultBean = JSONUtil.getMapper().readValue(content, new TypeReference<ReqBindResultJsonBean>() {
				});
				EventBus.getDefault().post(reqBindResultBean);
				break;
			//�յ��Է�����λ
			case PushMessageContants.MSG_TYPE_REQUEST_LOCATE:
				mPresenter.requestMyLocate();
				break;
			//�յ��Է�����λ���
			case PushMessageContants.MSG_TYPE_REQUEST_LOCATE_RESULT:
				RequestLocateResultBean locateResultBean = JSONUtil.getMapper().readValue(content, new TypeReference<RequestLocateResultBean>() {
				});
				EventBus.getDefault().post(locateResultBean);
				break;
			default:
				break;
			}
		} catch (Exception e) {
			ELog.e(e);
		}
		
	}
	
	private void showNotification(GeofenceBean bean){
		NotificationManager manager = (NotificationManager)mContext.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notif = new Notification(R.drawable.search_btn, bean.isIn() ? "����Χ��" : "�뿪Χ��", 0);
		RemoteViews view = new RemoteViews(mContext.getPackageName(), R.layout.notifacation_view);
		view.setTextViewText(R.id.notification_view_tv, bean.isIn() ? "����Χ��" : "�뿪Χ��");
		notif.contentView = view;
		notif.flags = Notification.FLAG_NO_CLEAR;
		manager.notify(1, notif);
	}
	
	private class IMain implements IMainHandler{

		@Override
		public void onSuccessAddBDGeofences() {
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
		public void onLocated(BDLocation location, double distance) {
			// TODO Auto-generated method stub
			
		}

		
	}

}
