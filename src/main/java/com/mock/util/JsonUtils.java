package com.mock.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtils {
	
	private static Logger logger = LoggerFactory.getLogger(JsonUtils.class);

    private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final static ObjectMapper JSON = new ObjectMapper();

    static {
    	JSON.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    private JsonUtils() {

    }

    public static ObjectMapper getInstance() {
        return OBJECT_MAPPER;
    }

    
    @SuppressWarnings("unchecked")
	public static LinkedHashMap<String,Object> inputStreamToMap(InputStream stream)
    {
    	LinkedHashMap<String, Object> returnValue = null;
    	try {
    		returnValue = OBJECT_MAPPER.readValue(stream, LinkedHashMap.class);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			logger.error("JsonParseException!",e);
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			logger.error("JsonMappingException!",e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("IOException!",e);
		}
    	
    	return returnValue;
    }
    
	public static <T> T inputStreamToPojo(InputStream stream, Class<T> clazz)
    {
    	T returnValue = null;
    	try {
    		returnValue = OBJECT_MAPPER.readValue(stream, clazz);
		} catch (JsonParseException e) {
			logger.error("JsonParseException!",e);
		} catch (JsonMappingException e) {
			logger.error("JsonMappingException!",e);
		} catch (IOException e) {
			logger.error("IOException!",e);
		}
    	
    	return returnValue;
    }
    /**
     * javaBean,list,array convert to json string
     */
    public static String obj2json(Object obj) {
        String rString = null;
        try {
			rString = OBJECT_MAPPER.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			logger.error("json parse error!",e);
		}
    	return rString;
    }

    
    
    /**
     * json string convert to javaBean
     * @throws JsonMappingException 
     */
    public static <T> T json2pojo(String jsonStr, Class<T> clazz) throws JsonMappingException
            {
    	T t =null;
    	try {
			t = OBJECT_MAPPER.readValue(jsonStr,clazz);
		} catch (JsonParseException e) {
			logger.error("json parse error!",e);
		} catch (JsonMappingException e) {
			throw e;
		} catch (IOException e) {
			logger.error("io error!",e);
		}
        return t;
    }

    /**
     * json string convert to map
     */
    @SuppressWarnings("unchecked")
	public static <T> Map<String, Object> json2map(String jsonStr)
            throws Exception {
        return OBJECT_MAPPER.readValue(jsonStr, Map.class);
    }

    /**
     * json string convert to map with javaBean
     */
    public static <T> Map<String, T> json2map(String jsonStr, Class<T> clazz)
            throws Exception {
        Map<String, Map<String, Object>> map = OBJECT_MAPPER.readValue(jsonStr,
                new TypeReference<Map<String, T>>() {
                });
        Map<String, T> result = new HashMap<String, T>();
        for (Entry<String, Map<String, Object>> entry : map.entrySet()) {
            result.put(entry.getKey(), map2pojo(entry.getValue(), clazz));
        }
        return result;
    }

    /**
     * json array string convert to list with javaBean
     */
    public static <T> List<T> json2list(String jsonArrayStr, Class<T> clazz)
            throws Exception {
        List<Map<String, Object>> list = OBJECT_MAPPER.readValue(jsonArrayStr,
                new TypeReference<List<T>>() {
                });
        List<T> result = new ArrayList<T>();
        for (Map<String, Object> map : list) {
            result.add(map2pojo(map, clazz));
        }
        return result;
    }

    /**
     * map convert to javaBean
     */
    @SuppressWarnings("rawtypes")
	public static <T> T map2pojo(Map map, Class<T> clazz) {
        return OBJECT_MAPPER.convertValue(map, clazz);
    }

    public static byte[] toJsonAsBytes(Object object) {
        try {
            return JSON.writeValueAsBytes(object);
        } catch (IOException e) {
        	logger.error("to json as bytes error!",e);
            return null;
        }
    }
   
}