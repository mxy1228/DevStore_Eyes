package com.xmy.eyes;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import cn.bmob.push.PushConstants;

/**
 * Ϊ��֤�ɹ��յ�������Ϣ 
 */
public class MyPushMessageReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		ELog.d("MyPushMessageReceiver:receive:"+intent.getStringExtra(PushConstants.EXTRA_PUSH_MESSAGE_STRING));
	}

}
