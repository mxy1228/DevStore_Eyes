package com.xmy.eyes.util;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.DeserializationConfig.Feature;

public class JSONUtil {

	public static ObjectMapper getMapper(){
		return new ObjectMapper().configure(
				DeserializationConfig.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY,
				true).configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}
	
}
