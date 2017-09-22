package com.mock.mappings;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mock.model.MappingInfo;
import com.mock.util.JsonUtils;

public class MappingsManager {
	/*
	 * The following info will store in the matchMap
	 * GET | List<MappingInfo>
	 * PUT | List<MappingInfo>
	 * DEL | List<MappingInfo>
	 * POST| List<MappingInfo>
	 */
	private Map<String, List<MappingInfo>> matchMap = new HashMap<String, List<MappingInfo>>();

	public Map<String, List<MappingInfo>> getMatchMap() {
		return matchMap;
	}

	public void setMatchMap(Map<String, List<MappingInfo>> matchMap) {
		this.matchMap = matchMap;
	} 
	
	public List<MappingInfo> getMappingInfoByMothed(String method)
	{
		return matchMap.get(method);
	}
	
	public void setMappingsInfoFromFile(String fileNameWithAbsolutePath)
	{
		File file = new File(fileNameWithAbsolutePath);
		InputStream input = null;
		try {
			input = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
        MappingInfo mappingInfo = JsonUtils.inputStreamToPojo(input,MappingInfo.class);
        String method = mappingInfo.getRequest().getMethod();
        List<MappingInfo> mappingInfos = matchMap.get(method);
        if(null != mappingInfos)
        {
        	mappingInfos.add(mappingInfo);
        }
        else
        {
        	mappingInfos = new ArrayList<MappingInfo>();
        	mappingInfos.add(mappingInfo);
        	matchMap.put(method, mappingInfos);
        }
	}
}
