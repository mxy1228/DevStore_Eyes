package com.xmy.eyes.util;

import java.io.IOException;

import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.json.JSONObject;

import com.xmy.eyes.ELog;

public class JSONUtil {

	public static ObjectMapper getMapper(){
		return new ObjectMapper().configure(
				DeserializationConfig.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY,
				true).configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}
	
	public static JsonGenerator getJsonGenerator(){
		try {
			return new ObjectMapper().getJsonFactory().createJsonGenerator(System.out, JsonEncoding.UTF8);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * ½«Object×ª»»³ÉJSONObject
	 * @param obj
	 * @return
	 */
	public static JSONObject convertObjToJsonObject(Object obj){
		try {
			String jsonStr = new ObjectMapper().writeValueAsString(obj);
			JSONObject jsonObj = new JSONObject(jsonStr);
			return jsonObj;
		} catch (Exception e) {
			ELog.e(e);
		}
		return null;
	}
}
