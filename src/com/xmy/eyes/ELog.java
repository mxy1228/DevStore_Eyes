package com.xmy.eyes;

import android.util.Log;

public class ELog{
	private static String TAG = "com.xmy.eye";

	private static void debug(String tag,Object obj) {
		String funName = getFunName();
		String info = (funName == null ? obj.toString() : (funName + ":" + obj
				.toString()));
		Log.d(tag, info);
	}
	
	private static String getCurrentInfo() {

		StackTraceElement[] eles = Thread.currentThread().getStackTrace();
		StackTraceElement targetEle = eles[5];
		String info = "(" + targetEle.getClassName() + "."
				+ targetEle.getMethodName() + ":" + targetEle.getLineNumber()
				+ ")";
		return info;
	}

	private static void error(String tag,Exception exception) {
		try {
			Log.e(tag, "", exception);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void error(String tag,Object obj) {
		try {
			if (obj != null) {
				String funName = getFunName();
				String info = (funName == null ? obj.toString() : (funName
						+ ":" + obj.toString()));
				Log.e(tag, info);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//	private static String getFileName(String s) {
//		SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyyMMdd");
//		StringBuffer stringbuffer = new StringBuffer();
//		stringbuffer.append(simpledateformat.format(new Date())).append(s);
//		return stringbuffer.toString();
//	}

	private static String getFunName() {
		StackTraceElement[] sts = Thread.currentThread().getStackTrace();
		if (sts == null) {
			return null;
		}
		for (StackTraceElement st : sts) {
			if (st.isNativeMethod()) {
				continue;
			}
			if (st.getClassName().equals(Thread.class.getName())) {
				continue;
			}
//			if (st.getClassName().equals(this.getClass().getName())) {
//				continue;
//			}
			return "[" + Thread.currentThread().getId() + ":"
					+ st.getFileName() + ":" + st.getLineNumber() + "]";
		}
		return null;
	}

//	private static String getTimerFileName() {
//		SimpleDateFormat simpledateformat = new SimpleDateFormat(FORMAT);
//		StringBuffer stringbuffer = new StringBuffer();
//		stringbuffer.append(simpledateformat.format(new Date())).append(
//				"Timer.log");
//		return stringbuffer.toString();
//	}

	private static synchronized void verbose(String tag,Object obj) {
		String funName = getFunName();
		String info = (funName == null ? obj.toString() : (funName + ":" + obj
				.toString()));
		Log.v(tag, info);
	}

	private static synchronized void warn(String tag,Object obj) {
		String funName = getFunName();
		String info = (funName == null ? obj.toString() : (funName + ":" + obj
				.toString()));
		Log.w(tag, info);
	}

	public static synchronized void d(Object obj) {
		if (Config.DEBUG){
			debug(TAG,obj);
		}
	}

	public static synchronized void e(Exception exception) {
		if (Config.DEBUG){
			error(TAG,exception);
		}
	}

	public static synchronized void e(Object obj) {
		if (Config.DEBUG){
			error(TAG,obj);
		}
	}

	public static synchronized void i(Object obj) {
		if(Config.DEBUG){
			String funName = getFunName();
			String info = (funName == null ? obj.toString() : (funName + ":" + obj
					.toString()));
			Log.i(TAG, info);
		}
	}

	public static synchronized void v(Object obj) {
		if (Config.DEBUG){
			verbose(TAG,obj);
		}
	}

	public static synchronized void w(Object obj) {
		if (Config.DEBUG){
			warn(TAG,obj);
		}
	}
}
