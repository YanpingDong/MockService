package com.mock.expression.handler;

import java.util.Map;

public interface ExpressionHandler {
	public boolean isSuitableForProcess(String express);
	public String doProcess(String needToProcessStr, String[] needToProcessPart, Map<String, Object> referenceValue);
	public String getFirsExpressionStr(String needToProcessPart);
}
