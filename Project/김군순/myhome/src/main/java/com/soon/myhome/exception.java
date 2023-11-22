package com.soon.myhome;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "entity not found")
public class exception extends RuntimeException{
	// RuntimeException을 상속하여 만들어짐. 
	
	private static final long serialVersionUID = 1L;
	
	public exception(String message) {
		super(message);
	}
}
