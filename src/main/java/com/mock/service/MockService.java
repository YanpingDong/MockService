package com.mock.service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.AsyncContext;
import javax.servlet.DispatcherType;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpUpgradeHandler;
import javax.servlet.http.Part;

import org.apache.catalina.servlet4preview.http.Mapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.mock.expression.handler.ExpressionHandler;
import com.mock.expression.handler.NumericIncExpressionHandler;
import com.mock.mappings.MappingFileFinder;
import com.mock.mappings.MappingsManager;
import com.mock.model.MappingInfo;
import com.mock.util.AbstractHttpReqClient;
import com.mock.util.JsonUtils;
import com.mock.util.StringsUtils;
import com.mock.util.UrlPatternMatch;

@Service
public class MockService {
	
	private static Logger logger = LoggerFactory.getLogger(MockService.class);
	private MappingFileFinder mappingFileFinder; 
	private MappingsManager mappingsManager;
	private ExpressionHandler numericIncExpressionHandler;
	
	public MappingFileFinder getMappingFileFinder() {
		return mappingFileFinder;
	}

	public void setMappingFileFinder(MappingFileFinder mappingFileFinder) {
		this.mappingFileFinder = mappingFileFinder;
	}
	
    public MappingsManager getMappingsManager() {
		return mappingsManager;
	}

	public void setMappingsManager(MappingsManager mappingsManager) {
		this.mappingsManager = mappingsManager;
	}

	public void init()
	{
		List<String> mappingsAbsDir = mappingFileFinder.getFileFromMappingsDir();
		for(String mappingJsonFile : mappingsAbsDir)
		{
			mappingsManager.setMappingsInfoFromFile(mappingJsonFile);
		}
		numericIncExpressionHandler = new NumericIncExpressionHandler();
	}
	
	public MockService() {
    	logger.debug("start setting mapping configuration");
	}
    
    public LinkedHashMap<String, Object> getFakeRequestInfo(String url, HttpServletRequest request, HttpServletResponse response)
    {
    	String method = request.getMethod();
    	MappingInfo matchedMappingInfo = getMappingInfo(url, method);
    	if(null == matchedMappingInfo)
    	{
    		AbstractHttpReqClient.throwServerError("cann't get response info for {"+url + "," + method +"}");
    	}
    
    	return customizeResponseInfo(matchedMappingInfo);

    }
    
    private LinkedHashMap<String, Object> customizeResponseInfo(MappingInfo mappingInfo)
    {
    	String startAndEndCharacter = mappingInfo.getMatchStartAndEndCharacter();
    	String[] startAndEndCharacters = startAndEndCharacter.split(",");
    	StringBuffer patternBuffer = new StringBuffer(startAndEndCharacters[0] + "[0-9A-Za-z_$%#@!()\\{\\}\\.]*" +startAndEndCharacters[1]);
		Pattern pattern = Pattern.compile(patternBuffer.toString());
		
		String responseJsonStr = createResponseString(mappingInfo);
		Matcher matcher = pattern.matcher(responseJsonStr);
		if (matcher.find()) {
			String selfDefineExpression = matcher.group();
			System.out.println(selfDefineExpression);
			if(numericIncExpressionHandler.isSuitableForProcess(selfDefineExpression))
			{
				Map<String, Object> referenceValue = new HashMap<String, Object>();
				referenceValue.put("ab", new Long(123456l));
				responseJsonStr = numericIncExpressionHandler.doProcess(responseJsonStr, startAndEndCharacters, referenceValue);
			}
		}
		
		try {
			System.out.println(responseJsonStr);
			return (LinkedHashMap<String, Object>) JsonUtils.json2map(responseJsonStr);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
    }

	private MappingInfo getMappingInfo(String url, String method) {
		List<MappingInfo> mappingInfos = mappingsManager.getMappingInfoByMothed(method);
    	MappingInfo matchedMappingInfo = null;
    	for(MappingInfo mappingInfo : mappingInfos)
    	{
    		String urlPattern = mappingInfo.getRequest().getUrlPattern();
    		if(UrlPatternMatch.isMatched(url, urlPattern))
    		{
    			matchedMappingInfo = mappingInfo;
    			break;
    		}
    	}
		return matchedMappingInfo;
	}

	private String createResponseString(MappingInfo matchedMappingInfo){
		if(null == matchedMappingInfo)
    	{
    		return null;
    	}
    	else
    	{
    		String responseFileName = matchedMappingInfo.getResponse().getBodyFileName();
    		InputStream inputStream = mappingFileFinder.getInputStreamFromFileName(responseFileName);
    		if(null == inputStream)
    		{
    			AbstractHttpReqClient.throwServerError("cann't find any response file that match the name " + responseFileName);
    		}
    		
    		return StringsUtils.inputStreamToString(inputStream);
    	}
	}
	
	private LinkedHashMap<String, Object> createResponse(MappingInfo matchedMappingInfo) {
		if(null == matchedMappingInfo)
    	{
    		return null;
    	}
    	else
    	{
    		String responseFileName = matchedMappingInfo.getResponse().getBodyFileName();
    		InputStream inputStream = mappingFileFinder.getInputStreamFromFileName(responseFileName);
    		if(null == inputStream)
    		{
    			AbstractHttpReqClient.throwServerError("cann't find any response file that match the name " + responseFileName);
    		}
    		
    		return JsonUtils.inputStreamToMap(inputStream);
    	}
	}
    
    public static void main(String[] args) throws FileNotFoundException, IOException
	{
    	MockService mockService = new MockService();
    	mockService.setMappingFileFinder(new MappingFileFinder());
    	mockService.setMappingsManager(new MappingsManager());
    	HttpServletRequest httpServletRequest = getMockRequest();
		mockService.init();
		LinkedHashMap<String, Object> ret = mockService.getFakeRequestInfo("/data/2/2.4/lookup/airing/13423", httpServletRequest, null);
	    System.out.println(JsonUtils.obj2json(ret));
	}

	private static HttpServletRequest getMockRequest() {
		HttpServletRequest httpServletRequest = new HttpServletRequest() {
			
			@Override
			public AsyncContext startAsync(ServletRequest servletRequest, ServletResponse servletResponse)
					throws IllegalStateException {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public AsyncContext startAsync() throws IllegalStateException {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public void setCharacterEncoding(String env) throws UnsupportedEncodingException {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void setAttribute(String name, Object o) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void removeAttribute(String name) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public boolean isSecure() {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean isAsyncSupported() {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean isAsyncStarted() {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public ServletContext getServletContext() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public int getServerPort() {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public String getServerName() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public String getScheme() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public RequestDispatcher getRequestDispatcher(String path) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public int getRemotePort() {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public String getRemoteHost() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public String getRemoteAddr() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public String getRealPath(String path) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public BufferedReader getReader() throws IOException {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public String getProtocol() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public String[] getParameterValues(String name) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Enumeration<String> getParameterNames() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Map<String, String[]> getParameterMap() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public String getParameter(String name) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Enumeration<Locale> getLocales() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Locale getLocale() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public int getLocalPort() {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public String getLocalName() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public String getLocalAddr() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public ServletInputStream getInputStream() throws IOException {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public DispatcherType getDispatcherType() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public String getContentType() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public long getContentLengthLong() {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public int getContentLength() {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public String getCharacterEncoding() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Enumeration<String> getAttributeNames() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Object getAttribute(String name) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public AsyncContext getAsyncContext() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public <T extends HttpUpgradeHandler> T upgrade(Class<T> httpUpgradeHandlerClass)
					throws IOException, ServletException {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public void logout() throws ServletException {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void login(String username, String password) throws ServletException {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public boolean isUserInRole(String role) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean isRequestedSessionIdValid() {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean isRequestedSessionIdFromUrl() {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean isRequestedSessionIdFromURL() {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean isRequestedSessionIdFromCookie() {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public Principal getUserPrincipal() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public HttpSession getSession(boolean create) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public HttpSession getSession() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public String getServletPath() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public String getRequestedSessionId() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public StringBuffer getRequestURL() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public String getRequestURI() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public String getRemoteUser() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public String getQueryString() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public String getPathTranslated() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public String getPathInfo() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Collection<Part> getParts() throws IOException, ServletException {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Part getPart(String name) throws IOException, ServletException {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public String getMethod() {
				// TODO Auto-generated method stub
				return "GET";
			}
			
			@Override
			public int getIntHeader(String name) {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public Enumeration<String> getHeaders(String name) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Enumeration<String> getHeaderNames() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public String getHeader(String name) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public long getDateHeader(String name) {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public Cookie[] getCookies() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public String getContextPath() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public String getAuthType() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public String changeSessionId() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public boolean authenticate(HttpServletResponse response) throws IOException, ServletException {
				// TODO Auto-generated method stub
				return false;
			}
		};
		return httpServletRequest;
	}
    
}
