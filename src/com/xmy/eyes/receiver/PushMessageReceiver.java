package com.xmy.eyes.receiver;

import com.baidu.android.pushservice.PushConstants;
import com.xmy.eyes.ELog;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class PushMessageReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		if(intent.getAction().equals(PushConstants.ACTION_RECEIVE)){
			Bundle b = intent.getExtras();
			if(b != null){
				String extra_content = b.getString(PushConstants.EXTRA_CONTENT);
				if(extra_content != null){
					ELog.d("Receive message = "+extra_content);
				}
			}
		}else if(intent.getAction().equals(PushConstants.ACTION_MESSAGE)){
			String message = intent.getExtras().getString(PushConstants.EXTRA_PUSH_MESSAGE_STRING);
			if(message != null){
				ELog.d("Receive message = "+message);
			}
		}
	}

}
