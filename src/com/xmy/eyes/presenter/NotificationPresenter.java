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
	 * ֪ͨ����ʾ
	 * @param bean
	 */
	public void showNotification(GeofenceStateChangeBean bean,Context mContext){
		NotificationManager manager = (NotificationManager)mContext.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notif = new Notification();
		notif.icon = R.drawable.bd_point;
		notif.tickerText = bean.isIn() ? "����Χ��":"�뿪Χ��";
		notif.when = System.currentTimeMillis();
		RemoteViews view = new RemoteViews(mContext.getPackageName(), R.layout.notifacation_view);
		view.setTextViewText(R.id.notification_view_tv, bean.isIn() ? bean.getUserName()+"����Χ��" : bean.getUserName()+"�뿪Χ��");
		SimpleDateFormat formater = new SimpleDateFormat("MM��dd�� HH:mm:ss");
		view.setTextViewText(R.id.notification_time_tv, formater.format(System.currentTimeMillis()));
		notif.contentView = view;
		notif.flags = Notification.FLAG_NO_CLEAR;
		notif.defaults = Notification.DEFAULT_VIBRATE;//��
		manager.notify(ID, notif);
	}
}
