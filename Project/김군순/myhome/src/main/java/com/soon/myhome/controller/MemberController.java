package com.soon.myhome.controller;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.soon.myhome.form.MemberForm;
import com.soon.myhome.service.MemberService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/member")
public class MemberController {
	
	private final MemberService memberService;
	
	
	@GetMapping("/join")
	public String join(MemberForm memberForm) {
		return "join";
	}
	
	@PostMapping("/join")
	public String join(@Valid MemberForm memberForm, BindingResult bindingResult) {
		if(bindingResult.hasErrors()) {
			return "join";
		}
		
		// pw1과pw2가 일치하는지 검증
		// bindingResult.rejectValue(필드명, 오류코드, 에러메세지) - 일치하지 않을 경우에 오류 발생하도록
		if(!memberForm.getPassword1().equals(memberForm.getPassword2())) {
			bindingResult.rejectValue("password2", "PwNotSame","비밀번호가 일치하지 않습니다.");
			return "join";
		}
		try {
		memberService.join(memberForm.getUsername(), memberForm.getPassword1(),memberForm.getMemEmail());
		}catch (DataIntegrityViolationException e) {
			e.printStackTrace();
			bindingResult.reject("joinFail", "이미 가입되었습니다.");
			return "join";
		}catch(Exception e) {
			e.printStackTrace();
			bindingResult.reject("joinFail", e.getMessage());
			return "join";			
		}
		
		return "redirect:/";
	}
	
	// 로그인 페이지로 이동
	// 실제 로그인을 수행하는 메서드는 시큐리티가 대신하기 때문에 구현할 필요 없음!
    @GetMapping("/login")
    public String login() {
        return "login";
    }

}
