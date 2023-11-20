package com.soon.slt;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// Data가 Null인 경우 에러를 처리하기 위한 클래스
// RuntimeException을 상속받고
// @ResponseStatus을 통해서 404에러를 처리
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "entity not found")
public class DataNotFound extends RuntimeException{
	private static final long serialVersionUID = 1L;
	
	public DataNotFound(String message) {
		super(message);
	}
	
}
