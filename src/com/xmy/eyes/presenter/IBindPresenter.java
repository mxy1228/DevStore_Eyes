package com.xmy.eyes.presenter;

import java.util.List;

import android.content.Context;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

import com.xmy.eyes.Contants;
import com.xmy.eyes.ELog;
import com.xmy.eyes.bean.MyUser;
import com.xmy.eyes.impl.IBindHandler;
import com.xmy.eyes.util.SPUtil;

public class IBindPresenter {

	private IBindHandler mHandler;
	
	public IBindPresenter(IBindHandler handler){
		this.mHandler = handler;
	}
	
	/**
	 * �����û�����Bmob��ѯע�����Ϣ
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
				}else{
					mHandler.onSearch(null);
				}
			}
			
			@Override
			public void onError(int arg0, String arg1) {
				ELog.e("search:onError:"+arg0+":"+arg1);
			}
		});
	}
	
	/**
	 * ��ָ�����û������а�
	 * @param my
	 * @param toBindUserName
	 */
	public void bind(final Context ctx,final MyUser my,final MyUser bindedUser){
		my.setBind(bindedUser.getUsername());
		my.setBindInstallationId(bindedUser.getInstallationId());
		my.update(ctx, new UpdateListener() {
			
			@Override
			public void onSuccess() {
				ELog.d("bind success");
				bindedUser.setBind(my.getUsername());
				bindedUser.setBindInstallationId(my.getInstallationId());
				bindedUser.update(ctx, new UpdateListener() {
					
					@Override
					public void onSuccess() {
						mHandler.onBind(true);
						//�ɹ��󣬽��û��ǳƴ洢��SP�У�����Ϊ�Ƿ��Ѿ���¼���ı�־
						SPUtil.setUserName(my.getUsername());
					}
					
					@Override
					public void onFailure(int arg0, String arg1) {
						mHandler.onBind(false);
					}
				});
			}
			
			@Override
			public void onFailure(int arg0, String arg1) {
				ELog.e("bind failure");
				mHandler.onBind(false);
			}
		});
		
		
	}
}
