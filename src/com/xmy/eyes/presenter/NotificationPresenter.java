package com.xmy.eyes.presenter;

import java.text.SimpleDateFormat;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.widget.RemoteViews;

import com.xmy.eyes.R;
import com.xmy.eyes.bean.GeofenceStateChangeBean;

public class NotificationPresenter {

	private static final int ID = 1;
	
	/**
	 * 通知栏提示
	 * @param bean
	 */
	public void showNotification(GeofenceStateChangeBean bean,Context mContext){
		NotificationManager manager = (NotificationManager)mContext.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notif = new Notification();
		notif.icon = R.drawable.bd_point;
		notif.tickerText = bean.isIn() ? "进入围栏":"离开围栏";
		notif.when = System.currentTimeMillis();
		RemoteViews view = new RemoteViews(mContext.getPackageName(), R.layout.notifacation_view);
		view.setTextViewText(R.id.notification_view_tv, bean.isIn() ? bean.getUserName()+"进入围栏" : bean.getUserName()+"离开围栏");
		SimpleDateFormat formater = new SimpleDateFormat("MM月dd日 HH:mm:ss");
		view.setTextViewText(R.id.notification_time_tv, formater.format(System.currentTimeMillis()));
		notif.contentView = view;
		notif.flags = Notification.FLAG_NO_CLEAR;
		notif.defaults = Notification.DEFAULT_VIBRATE;//震动
		manager.notify(ID, notif);
	}
}
