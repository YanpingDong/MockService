package com.mock.controller;


import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpStatusCodeException;

import com.mock.exception.CustomHttpStatusCodeException;
import com.mock.model.Error;
import com.mock.util.AbstractHttpReqClient;
import com.mock.util.StringsUtils;

public class BasedErrorHandleController {
	private static Logger log = 
			LoggerFactory.getLogger(BasedErrorHandleController.class);
		
	@ExceptionHandler(MissingServletRequestParameterException.class)  
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ResponseBody
    public Error handleUnexpectedRequestError(MissingServletRequestParameterException ex,
    		                                 HttpServletRequest request) {  
		log.error("exception: missed parameter is {}, error message is {}.", 
				ex.getParameterName(), ex.getMessage());
    	Error error = new Error(request,HttpStatus.BAD_REQUEST.value(),ex.getMessage(),null);
        return error;  
    }
	
	/**
	 * This handle will be removed when platform stability. 
	 * All platform exception will be transform to CustomHttpStatusCodeException using AbstractHttpReqClient utility tool class
	 */
	@ExceptionHandler(HttpStatusCodeException.class)  
	@ResponseBody
    public Error handleUnexpectedServerError(HttpStatusCodeException ex,
    		                                 HttpServletRequest request,HttpServletResponse response) {  
		//TODO monitor the exceptions we catch. delete it after dev.
		log.error("exception: http status code is {}, message is {}.", ex.getStatusCode().value(), ex.getMessage());
		Error error = new Error(request,ex.getStatusCode().value(),ex.getMessage(),null);
    	response.setStatus(ex.getStatusCode().value());
        return error;  
    }
	
	/**
	 * deal of custom exceptions
	 */
	@ExceptionHandler(CustomHttpStatusCodeException.class)  
	@ResponseBody
    public Error handleHttpStatusCodeException(CustomHttpStatusCodeException ex,
    		                                 HttpServletRequest request,HttpServletResponse response) throws IOException {  
		log.error("exception: http status code is {}, error code is{}, message is{}", 
				ex.getStatusCode(), ex.getErrorCode(), ex.getErrorDesc());
		Error error = new Error(request,ex.getStatusCode(),ex.getErrorDesc(),null);
    	response.setStatus(ex.getStatusCode());
		return error;  
    }
	
    protected void checkInputIDs(String ids) {
		
		Iterable<String> idsIt = StringsUtils.spliterItFromStr(ids, ',');
		
		if(!StringsUtils.isAllDigit(idsIt))
		{
			AbstractHttpReqClient.throwNotFound( 
					"wrong ids input formart,please use as following: id1,id2 and "
					+ "the idx must be digit");
		}
	}
	
	public void checkInputParameters(BindingResult result) {
		if(result.hasErrors())
		{
			String errorInfo = null;
			errorInfo = extractErrorInfo(result);
			AbstractHttpReqClient.throwBadRequest(errorInfo);
		}
	}
	
	private String extractErrorInfo(BindingResult result) {
		String errorInfo = new String();
		List<FieldError> fieldErrors = result.getFieldErrors();
		for(FieldError fieldError : fieldErrors)
		{
			errorInfo += fieldError.getField() + " : " + fieldError.getDefaultMessage() + "; ";
		}
		return errorInfo;
	}
}
