package com.xmy.eyes.http;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.xmy.eyes.ELog;

public class MyHttpHandler extends AsyncHttpResponseHandler {

	private String url;
	
	public void setUrl(String u){
		this.url = u;
	}
	
	@Override
	public void onStart() {
		super.onStart();
	}
	
	@Override
	public void onSuccess(int statusCode, String content) {
		super.onSuccess(statusCode, content);
		ELog.d(url+":onSuccess="+content);
	}
	
	@Override
	public void onFailure(Throwable error, String content) {
		super.onFailure(error, content);
		ELog.d(url+":onFailure="+content);
	}
	
	@Override
	@Deprecated
	public void onFailure(int statusCode, Throwable error, String content) {
		super.onFailure(statusCode, error, content);
		ELog.d(url+":statusCode="+statusCode+":onFailure="+content);
	}
}
