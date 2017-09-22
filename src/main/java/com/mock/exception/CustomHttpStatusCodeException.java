package com.mock.exception;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestClientException;

/**
 * note: all kinds of exception's getMessage value is set to ErrorDesc. 
 * so be sure to use ex.getErrorDesc instead of ex.getMessage
 * @author hzy
 *
 */
public class CustomHttpStatusCodeException extends RestClientException {

	private static final long serialVersionUID = 1L;

	private static final String DEFAULT_CHARSET = "UTF-8";

	private final int statusCode;

	private final int errorCode;
	
	private final String statusText;

	private final String errorDesc;
	
	private final byte[] responseBody;

	private final HttpHeaders responseHeaders;

	private final String responseCharset;

	public CustomHttpStatusCodeException(int statusCode, String statusText, 
			int errorCode, String errorDesc) {
		this(statusCode, statusText, errorCode, errorDesc, null, null, null);
	}
	
	public CustomHttpStatusCodeException(int statusCode, String statusText, 
			int errorCode, String errorDesc,byte[] responseBody) {
		this(statusCode, statusText, errorCode, errorDesc, null, responseBody, null);
	}
	
	/**
	 * default is 500 error
	 * @param errorDesc
	 */
	public CustomHttpStatusCodeException(String errorDesc) {
		this(ExtHttpStatus.SERVER_ERROR.value(), ExtHttpStatus.SERVER_ERROR.getReasonPhrase(), 
				ExtHttpStatus.SERVER_ERROR.value(), errorDesc, null, null, null);
	}
	
	/**
	 * Construct a new instance of {@code CustomeHttpStatusCodeException} based on an
	 * HttpStatus, status text, and response body content.
	 * @param statusCode the status code
	 * @param statusText the status text
	 * @param responseHeaders the response headers, may be {@code null}
	 * @param responseBody the response body content, may be {@code null}
	 * @param responseCharset the response body charset, may be {@code null}
	 * @since 3.1.2
	 */
	protected CustomHttpStatusCodeException(int statusCode, String statusText, 
			int errorCode, String errorDesc, HttpHeaders responseHeaders, 
			byte[] responseBody, Charset responseCharset) {

		super(statusCode + " " + statusText);
		this.statusCode = statusCode;
		this.statusText = statusText;
		this.errorCode = errorCode;
		this.errorDesc = errorDesc;
		this.responseHeaders = responseHeaders;
		this.responseBody = responseBody != null ? responseBody : new byte[0];
		this.responseCharset = responseCharset != null ? responseCharset.name() : DEFAULT_CHARSET;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public String getStatusText() {
		return statusText;
	}

	public byte[] getResponseBody() {
		return responseBody;
	}

	public HttpHeaders getResponseHeaders() {
		return responseHeaders;
	}

	public String getResponseCharset() {
		return responseCharset;
	}
	
	/**
	 * Return the response body as a string.
	 */
	public String getResponseBodyAsString() {
		try {
			return new String(this.responseBody, this.responseCharset);
		}
		catch (UnsupportedEncodingException ex) {
			// should not occur
			throw new IllegalStateException(ex);
		}
	}

	public int getErrorCode() {
		return errorCode;
	}

	public String getErrorDesc() {
		return errorDesc;
	}
}
