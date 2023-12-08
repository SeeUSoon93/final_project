package com.soon.slt;

import com.soon.slt.entity.TbBoard;

import lombok.Data;

public class CommentRequest {

	private String content;
	private String bdIdx;
	
	public String getBdIdx() {
	    return bdIdx;
	}

	public String getContent() {
	    return content;
	}
		
}
