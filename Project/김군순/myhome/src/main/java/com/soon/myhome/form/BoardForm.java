package com.soon.myhome.form;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class BoardForm {

	@NotEmpty(message = "제목을 입력해주세요")
	@Size(max=200)
	private String boardTitle;
	
	@NotEmpty(message = "내용을 입력해주세요")
	private String boardCon;
}
