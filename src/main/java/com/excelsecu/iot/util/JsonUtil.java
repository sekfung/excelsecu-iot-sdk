package com.excelsecu.iot.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.io.IOException;


public class JsonUtil {

	private static final ObjectMapper JSON = new ObjectMapper();

	static {
		JSON.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		JSON.configure(SerializationFeature.INDENT_OUTPUT, false);
		JSON.configure(SerializationFeature.WRITE_ENUMS_USING_INDEX, true);
		SimpleModule simpleModule = new SimpleModule();
		JSON.registerModule(simpleModule);
		JSON.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		JSON.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}

	public static String toJsonString(Object obj) {
		try {
			return JSON.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static <T> T toObject(Class<T> clz, String json) {
		try {
			return JSON.readValue(json, clz);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static <T> T toObject(Class<T> clz, Object obj) {
	    try {
            return JSON.convertValue(obj, clz);
        }catch (IllegalArgumentException e) {
	        e.printStackTrace();
        }
	    return null;
    }
}
