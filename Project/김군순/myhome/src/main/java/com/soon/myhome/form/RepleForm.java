package com.soon.myhome.form;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class RepleForm {

	@NotEmpty(message = "내용을 입력하세요")
	private String repleCon;
}
