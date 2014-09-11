package com.xmy.eyes.http;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.xmy.eyes.Contants;

public class MyHttpClient extends AsyncHttpClient {

	public MyHttpClient(){
		init();
	}
	
	private void init(){
		this.setTimeout(Contants.TIME_OUT);
		this.setMaxRetriesAndTimeout(2, Contants.TIME_OUT);
	}
	
	/**
	 * Get请求
	 * @param url
	 * @param params
	 * @param handler
	 */
	public void doGet(String url,RequestParams params,MyHttpHandler handler){
		if(handler != null){
			handler.setUrl(url);
		}
		this.get(url, params, handler);
	}
	
	/**
	 * Post请求
	 * @param url
	 * @param params
	 * @param handler
	 */
	public void doPost(String url,RequestParams params,MyHttpHandler handler){
		if(handler != null){
			handler.setUrl(url);
		}
		this.post(url, params, handler);
	}
}
