package com.soon.slt.form;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class MemberForm {
	
	@NotEmpty(message = "아이디를 입력해주세요")
	private String username;
	
	@NotEmpty(message = "비밀번호를 입력해주세요")
	private String password1;
	
	@NotEmpty(message = "비밀번호를 확인해주세요")
	private String password2;
	
	@NotEmpty(message = "이메일을 입력해주세요")
	private String email;
	
	
	

}
