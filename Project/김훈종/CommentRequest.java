package com.soon.slt;

import com.soon.slt.entity.TbBoard;

import lombok.Data;

public class CommentRequest {

	private String content;
	private Long bdIdx;
	
	public Long getBdIdx() {
	    return bdIdx;
	}

	public String getContent() {
	    return content;
	}
		
}
