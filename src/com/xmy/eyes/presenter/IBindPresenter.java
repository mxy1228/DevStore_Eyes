package com.xmy.eyes.presenter;

import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;

import android.content.Context;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

import com.xmy.eyes.Contants;
import com.xmy.eyes.ELog;
import com.xmy.eyes.EyesApplication;
import com.xmy.eyes.PushMessageContants;
import com.xmy.eyes.R;
import com.xmy.eyes.bean.MyUser;
import com.xmy.eyes.bean.ReqBindJsonBean;
import com.xmy.eyes.impl.IBindHandler;
import com.xmy.eyes.util.SPUtil;

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
	 * 请求绑定
	 * @param my
	 * @param toBindUserName
	 */
	public void requestBind(final Context ctx,final MyUser bindedUser){
		//发送推送消息询问对方绑定
		try {
			ReqBindJsonBean bean = new ReqBindJsonBean();
			bean.setType(PushMessageContants.MSG_TYPE_REQUEST_BIND);
			bean.setMsg(ctx.getString(R.string.req_bind_msg, EyesApplication.mMyUser.getUsername()));
			bean.setReqUid(EyesApplication.mMyUser.getUid());
			bean.setReqUserName(EyesApplication.mMyUser.getUsername());
			String jsonStr = new ObjectMapper().writeValueAsString(bean);
			JSONObject obj = new JSONObject(jsonStr);
			BmobPushMsgPresenter.getDefault().sendMessage(obj,bindedUser.getUid());
		} catch (Exception e) {
			ELog.e(e);
		}
		
	}
	
	/**
	 * 和指定的用户名进行绑定
	 */
	public void bind(String taUserName,String taUid,Context ctx){
		EyesApplication.mMyUser.setBind(taUserName);
		EyesApplication.mMyUser.setBindedUID(taUid);
		EyesApplication.mMyUser.update(ctx, new UpdateListener() {
			
			@Override
			public void onSuccess() {
				ELog.d("bind success");
				mHandler.onBind(true);
				//成功后，将用户昵称存储到SP中，以作为是否已经登录过的标志
				SPUtil.setUserName(EyesApplication.mMyUser.getUsername());
				SPUtil.setUID(EyesApplication.mMyUser.getUid());
			}
			
			@Override
			public void onFailure(int arg0, String arg1) {
				ELog.e("bind failure");
				mHandler.onBind(false);
			}
		});
	}
}
