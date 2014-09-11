package com.xmy.eyes.presenter;

import java.util.List;

import android.content.Context;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

import com.xmy.eyes.Contants;
import com.xmy.eyes.ELog;
import com.xmy.eyes.bean.MyUser;
import com.xmy.eyes.impl.IBindHandler;

public class IBindPresenter {

	private IBindHandler mHandler;
	
	public IBindPresenter(IBindHandler handler){
		this.mHandler = handler;
	}
	
	/**
	 * 根据用户名在Bmob查询注册的信息
	 * @param username
	 */
	public void search(Context ctx,String username){
		BmobQuery<MyUser> query = new BmobQuery<MyUser>();
		query.addWhereEqualTo(Contants.BMOB_USER_NAME, username);
		query.findObjects(ctx, new FindListener<MyUser>() {
			
			@Override
			public void onSuccess(List<MyUser> arg0) {
				ELog.d("search:onSuccess");
				if(!arg0.isEmpty()){
					mHandler.onSearch(arg0.get(0));
					ELog.i("search:onSuccess:username="+arg0.get(0).getUsername());
				}
			}
			
			@Override
			public void onError(int arg0, String arg1) {
				ELog.e("search:onError:"+arg0+":"+arg1);
			}
		});
	}
}
