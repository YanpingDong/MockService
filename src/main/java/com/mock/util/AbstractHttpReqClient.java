package com.mock.util;

import com.mock.exception.CustomHttpStatusCodeException;
import com.mock.exception.ExtHttpStatus;

public class AbstractHttpReqClient {

	public static void throwWrappedCustomHttpStatusCodeExp(int statusCode, String errorMessage, int errorCode,
			String errorDesc) {
		throw new CustomHttpStatusCodeException(statusCode, errorMessage, errorCode, errorDesc);
	}

	public static void throwWrappedCustomHttpStatusCodeExp(int statusCode, String errorMessage, int errorCode,
			String errorDesc, byte[] responseBody) {
		throw new CustomHttpStatusCodeException(statusCode, errorMessage, errorCode, errorDesc, responseBody);
	}
	
	/**
	 * note: http status equals error code
	 * @param statusCode
	 * @param errorMessage
	 */
	public static void throwWrappedCustomHttpStatusCodeExp(int statusCode,String statusText, String errorDesc) {
		throw new CustomHttpStatusCodeException(statusCode, statusText, statusCode, errorDesc);
	}
	
	/**
	 * note: http status equals error code
	 * @param statusCode
	 * @param errorMessage
	 */
	public static void throwWrappedCustomHttpStatusCodeExp(int statusCode,String statusText, String errorDesc,byte[] responseBody) {
		throw new CustomHttpStatusCodeException(statusCode, statusText, statusCode, errorDesc,responseBody);
	}

	/**
	 * throw 500 error
	 * 
	 * @param errorDesc
	 */
	public static void throwServerError(String errorDesc) {
		throw new CustomHttpStatusCodeException(ExtHttpStatus.SERVER_ERROR.value(), ExtHttpStatus.SERVER_ERROR.getReasonPhrase(),
				ExtHttpStatus.SERVER_ERROR.value(), errorDesc);
	}

	/**
	 * throw 220 error,it means 3rd service give use a 200,but the structure of
	 * response is error e.g. when we search lots, we get 200 with a message :
	 * the lots are full.
	 * 
	 * @param errorDesc
	 */
	public static void throwResultError(String errorDesc, byte[] responseBody) {
		throw new CustomHttpStatusCodeException(ExtHttpStatus.RESULT_ERROR.value(), ExtHttpStatus.RESULT_ERROR.getReasonPhrase(),
				ExtHttpStatus.RESULT_ERROR.value(), errorDesc, responseBody);
	}

	/**
	 * throw 504 error
	 * 
	 * @param errorDesc
	 */
	public static void throwTimeOut(String errorDesc) {
		throw new CustomHttpStatusCodeException(ExtHttpStatus.PROCCESS_TIMEOUT.value(), ExtHttpStatus.PROCCESS_TIMEOUT.getReasonPhrase(),
				ExtHttpStatus.PROCCESS_TIMEOUT.value(), errorDesc);
	}

	/**
	 * throw 400 error
	 * 
	 * @param errorDesc
	 */
	public static void throwBadRequest(String errorDesc) {
		throw new CustomHttpStatusCodeException(ExtHttpStatus.BAD_REQUEST.value(), ExtHttpStatus.BAD_REQUEST.getReasonPhrase(),
				ExtHttpStatus.BAD_REQUEST.value(), errorDesc);
	}
	
	/**
	 * throw 404 error
	 * 
	 * @param errorDesc
	 */
	public static void throwNotFound(String errorDesc) {
		throw new CustomHttpStatusCodeException(ExtHttpStatus.NOT_FOUND.value(), ExtHttpStatus.NOT_FOUND.getReasonPhrase(),
				ExtHttpStatus.NOT_FOUND.value(), errorDesc);
	}

	/**
	 * return 200 with custom message
	 * 
	 * @param errorDesc
	 */
	public static void throwSucceed(String errorDesc) {
		throw new CustomHttpStatusCodeException(ExtHttpStatus.SUCCEED.value(), ExtHttpStatus.SUCCEED.getReasonPhrase(),
				ExtHttpStatus.SUCCEED.value(), errorDesc);
	}

	/**
	 * return 520 error
	 * 
	 * @param errorDesc
	 */
	public static void throwRelatedServerError(String errorDesc) {
		throw new CustomHttpStatusCodeException(ExtHttpStatus.RELATED_SERVER_ERROR.value(),
				ExtHttpStatus.RELATED_SERVER_ERROR.getReasonPhrase(), ExtHttpStatus.RELATED_SERVER_ERROR.value(), errorDesc);
	}

	/**
	 * return 599 error
	 * 
	 * @param errorDesc
	 */
	public static void throwUnknow3rdServerError(String errorDesc) {
		throw new CustomHttpStatusCodeException(ExtHttpStatus.UNKNOW_3RD_ERROR.value(), ExtHttpStatus.UNKNOW_3RD_ERROR.getReasonPhrase(),
				ExtHttpStatus.UNKNOW_3RD_ERROR.value(), errorDesc);
	}

}