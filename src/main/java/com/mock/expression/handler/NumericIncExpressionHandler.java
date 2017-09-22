package com.mock.expression.handler;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mock.util.AbstractHttpReqClient;

public class NumericIncExpressionHandler implements ExpressionHandler{

	@Override
	public boolean isSuitableForProcess(String express) {
		return express.contains("inc");
	}

	@Override
	public String doProcess(String needToProcessStr, String[] startAndEndCharacters, Map<String, Object> referenceValue) {
		//StringBuffer patternBuffer = new StringBuffer(startAndEndCharacters[0] + "[0-9A-Za-z_$%#@!()\\{\\}\\.]*inc[0-9A-Za-z_$%#@!()\\{\\}\\.]*" +startAndEndCharacters[1]);
		StringBuffer patternBuffer = new StringBuffer(startAndEndCharacters[0] + "[0-9A-Za-z_$%#@!\\(\\)\\{\\}\\.]*\\)" +startAndEndCharacters[1]);
		Pattern pattern = Pattern.compile(patternBuffer.toString());
		Matcher matcher = pattern.matcher(needToProcessStr);
		 
		int i = 0;
		while(matcher.find()) 
		{
			String matcheredExpressStr = matcher.group();
			System.out.println(matcheredExpressStr);
			Long copyValue = caculateExpressionValue(referenceValue, i, matcheredExpressStr);
			i++;
			needToProcessStr = matcher.replaceFirst(copyValue.toString());
			matcher = pattern.matcher(needToProcessStr);
			
		}
		
		return needToProcessStr;
	}

	private Long caculateExpressionValue(Map<String, Object> referenceValue, int i, String matcheredExpressStr) {

		matcheredExpressStr = removeIrrelevantStr(matcheredExpressStr);
	
		String expressionStr =  getFirsExpressionStr(matcheredExpressStr);
		
		Number leftValue = createValue(referenceValue, expressionStr);

		matcheredExpressStr = matcheredExpressStr.substring(expressionStr.length() );
		
		leftValue = doCaculate(referenceValue, matcheredExpressStr, leftValue);
		
		return (Long) leftValue;
	}

	private Number doCaculate(Map<String, Object> referenceValue, String matcheredExpressStr, Number leftValue) {
		//get action
		String action = matcheredExpressStr.substring(1, matcheredExpressStr.indexOf('('));
		matcheredExpressStr =  matcheredExpressStr.substring(action.length() + 1);
		//Number rightValue = getFirsExpressionStr()
		String actionInputParameter = matcheredExpressStr.substring(1, matcheredExpressStr.indexOf(')'));
		 
		Number rightValue = createValue(referenceValue, actionInputParameter);
		
		leftValue = caculateOnAction(leftValue, rightValue, action);
		matcheredExpressStr = matcheredExpressStr.substring(matcheredExpressStr.indexOf(')')+1);
		
		
		return matcheredExpressStr.length()>1 ? doCaculate(referenceValue, matcheredExpressStr, leftValue):leftValue;
	}

	private Number caculateOnAction(Number leftValue, Number rightValue, String action)
	{
		Number retNum =null;
		switch (action) {
		case "inc":
			retNum = (leftValue.longValue() - rightValue.longValue());
			break;
		case "add":
			retNum = (leftValue.longValue() + rightValue.longValue());
			break;
		default:
			break;
		}
		
		return retNum;
	}
	
	private Number createValue(Map<String, Object> referenceValue, String expressionStr) {
		Number leftValue = null;
		String key = null; 
		if(expressionStr.contains("{") && expressionStr.contains("}"))
		{
			key = expressionStr.substring(1, expressionStr.length()-1);
			leftValue = (Number) referenceValue.get(key);
		}
		else if( !expressionStr.contains("{") && !expressionStr.contains("}") )
		{
			leftValue = Long.valueOf(expressionStr);
		}
		else {
			AbstractHttpReqClient.throwServerError("Please check you response file in __file DIR, Expression syntax error");
		}
		return leftValue;
	}

	private String removeIrrelevantStr(String matchedStr)
	{
		int startIndex = matchedStr.indexOf('{');
		int endIndex = matchedStr.lastIndexOf(')');
		return matchedStr.substring(startIndex, endIndex+1);
	}
	
	@Override
	public String getFirsExpressionStr(String needToProcessPart) {
		int startIndex = needToProcessPart.indexOf("{");
		int endIndex = needToProcessPart.indexOf('}');
		
		return needToProcessPart.substring(startIndex, endIndex + 1);
	}



}
