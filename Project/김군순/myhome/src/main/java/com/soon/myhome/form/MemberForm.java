package com.soon.myhome.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class MemberForm {
	
	@Size(min=2)
	@NotEmpty(message = "아이디를 입력해주세요")
	private String username;
	
	@NotEmpty(message = "비밀번호를 입력해주세요")
	private String password1;
	
	@NotEmpty(message = "비밀번호를 확인해주세요")
	private String password2;
	
	@NotEmpty(message = "이메일을 입력해주세요")
	@Email // @Email - 입력한 값이 이메일형식과 일치하는지 검증
	private String memEmail;

}
