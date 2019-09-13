package com.tmobile.ct.tep.qapi.exceptions;

public class ApiFailureException extends RuntimeException {

	public ApiFailureException(){
		super();
	}

	public ApiFailureException(String message){
		super(message);
	}
}
