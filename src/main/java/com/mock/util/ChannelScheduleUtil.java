package com.mock.util;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ChannelScheduleUtil {
	
	@SuppressWarnings("unchecked")
	public static  void changeFakeDataId(LinkedHashMap<String, Object> serviceChannelsMap, String serviceId) {
		Map<String,Object> refMap = (Map<String, Object>) serviceChannelsMap.get("ref");
		refMap.put("id", serviceId);
	}

	@SuppressWarnings("unchecked")
	public static void changeChannelId(String serviceId, List<Object> airingsList) {
		if(null != airingsList && airingsList.size() > 0)
		{
			for(Object airing : airingsList)
			{
				LinkedHashMap<String,Object> sourceMap = (LinkedHashMap<String, Object>)((LinkedHashMap<String, Object>) airing).get("source");
				LinkedHashMap<String,Object> refMap2 = (LinkedHashMap<String, Object>) sourceMap.get("ref");
				Long oldChannelId = Long.parseLong(((Number) refMap2.get("id")).toString());
				Long newChannleId = oldChannelId + Long.valueOf(serviceId);
				refMap2.put("id", newChannleId);
			}
		}
	}

}
