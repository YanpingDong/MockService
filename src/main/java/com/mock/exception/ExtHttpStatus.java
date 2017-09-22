package com.mock.exception;
/**
 * custom http code
 * this class is for customException error. it's more simple than HttpCode.
 * @author zhiyuan
 *
 */
public enum ExtHttpStatus {

	/**
	 * OK
	 */
	SUCCEED(200,"ok"),
	/**
	 * request processed, but the result is not want we want
	 */
	RESULT_ERROR(220,"result error"),
	/**
	 * bad request
	 */
	BAD_REQUEST(400,"bad request"),
    /**
     * not found
     */
    NOT_FOUND(404,"not found"),
	
	/**
     * not found
     */
	DATA_NOT_FOUND(440,"data not found"),
	/**
	 * unknow error
	 */
	UNKNOW_3RD_ERROR(499,"unknow 3rd error"),
	/**
	 * server error
	 */
	SERVER_ERROR(500,"server error"),
	/**
	 * related server error (like userDataInfo Server)
	 */
	RELATED_SERVER_ERROR(520,"related server error"),
	/**
	 * process timeout
	 */
	PROCCESS_TIMEOUT(504,"timeout");

	private int httpCode;
	private String reasonPhrase;
	
	ExtHttpStatus(int httpCode,String reasonPhrase){
		this.httpCode = httpCode;
		this.reasonPhrase = reasonPhrase;
	}
	
	public int value(){
		return this.httpCode;
	}
	public String getReasonPhrase(){
		return this.reasonPhrase;
	}
	
}
