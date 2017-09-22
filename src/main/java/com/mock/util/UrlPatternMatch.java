package com.mock.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UrlPatternMatch {

	public static boolean isMatched(String targetUrl, String matchedPatternUrl)
	{
		String patternStr = matchedPatternUrl;
		Pattern pattern = Pattern.compile(patternStr);
		Matcher matcher = pattern.matcher(targetUrl);
		return matcher.find();
	}

}