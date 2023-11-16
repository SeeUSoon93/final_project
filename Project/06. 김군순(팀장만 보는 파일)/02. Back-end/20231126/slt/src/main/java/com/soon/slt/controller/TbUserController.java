package com.soon.slt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.soon.slt.form.TbUserForm;
import com.soon.slt.service.TbUserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/tbUser")
public class TbUserController {

	private final TbUserService tbUserService;

	@GetMapping("/signup")
	public String signup(TbUserForm tbUserForm) {
		return "회원가입 폼";
	}

	@PostMapping("/signup")
	public String signup(@Valid TbUserForm tbUserForm, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return "회원가입 폼";
		}
		if (!tbUserForm.getUserPw1().equals(tbUserForm.getUserPw2())) {
			bindingResult.rejectValue("userPw2", "passwordIncorrect", "비밀번호를 확인해주세요!");
			return "회원가입 폼";
		}
		try {
			this.tbUserService.create(tbUserForm.getUserEmail(), tbUserForm.getUserPw1(), tbUserForm.getUserNick());
		} catch (Exception e) {
			bindingResult.reject("signupfail", "이미 등록된 유저입니다.");
			return "회원가입 폼";
		}
		return "redirect:/main";
	}

	@GetMapping("/login")
	public String login() {
		return "로그인 폼";
	}
	
}
