package com.soon.slt.form;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TbUserForm {
	
	@NotEmpty(message = "이메일을 입력해주세요")
	private String userEmail;
	
	@NotEmpty(message = "비밀번호를 입력해주세요")
	private String userPw1;
	
	@NotEmpty(message = "비밀번호를 확인해주세요")
	private String userPw2;
	
	@NotEmpty(message = "닉네임을 입력해주세요")
	private String userNick;
	
	
	

}
