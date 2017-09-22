package com.mock.controller;

import java.io.IOException;
import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.mock.service.MockService;
import com.mock.util.AbstractHttpReqClient;

import io.swagger.annotations.Api;

@Api(value = "get epg related info ", produces = "application/json", tags={"closerTV EPG API"}, description="get epg related info" )
@RestController
public class MockController extends BasedErrorHandleController{
	
	private static Logger log = LoggerFactory.getLogger(MockController.class);

	@Autowired
	private MockService fakeDataService;
	
	@RequestMapping(value = "/**", method = { RequestMethod.GET }, produces = "application/json;charset=UTF-8")
	public LinkedHashMap<String, Object> determinePackageByStation(HttpServletRequest request, HttpServletResponse response) 
			throws NumberFormatException, IOException 
	{
		String uri = request.getRequestURI();
		log.debug(request.getRequestURI());
		LinkedHashMap<String, Object> mockedInfo = fakeDataService.getFakeRequestInfo(uri, request, response);
		if(null == mockedInfo)
		{
			AbstractHttpReqClient.throwBadRequest("path not support!");
		}
		else
		{
			return mockedInfo;
		}

		return null;
	}
}
