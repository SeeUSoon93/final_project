package com.soon.slt.form;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class TbCommentForm {

	@NotEmpty(message = "댓글 내용을 입력하세요")
	private String cmtContent;
	
}
