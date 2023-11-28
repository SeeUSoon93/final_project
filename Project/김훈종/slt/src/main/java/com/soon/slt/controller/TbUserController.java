package com.soon.slt.controller;

import java.time.LocalDateTime;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
	
	
	// Footer 와 Header 활성화
	@GetMapping("footer")
	public String footer() {
		return "footer";
	}
	@GetMapping("header")
	public String header() {
		return "header";
	}

	@GetMapping("/signup")
	public String signup(Model model, TbUserForm tbUserForm) {
		model.addAttribute("tbUserForm", tbUserForm);
		return "signUp";
	}

	@PostMapping("/signup")
	public String signup(@Valid TbUserForm tbUserForm, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return "signUp";
		}
		if (!tbUserForm.getUserPw1().equals(tbUserForm.getUserPw2())) {
			bindingResult.rejectValue("userPw2", "passwordIncorrect", "비밀번호를 확인해주세요!");
			return "signUp";
		}
		try {
			tbUserForm.setJoinedAt(LocalDateTime.now());
			this.tbUserService.create(tbUserForm.getUserEmail(), tbUserForm.getUserPw1(), tbUserForm.getUserNick(), tbUserForm.getJoinedAt());
		} catch (Exception e) {
			bindingResult.reject("signupfail", "이미 등록된 유저입니다.");
			return "signUp";
		}
		return "redirect:/main";
	}

	@GetMapping("/login")
	public String login() {
		return "login";
	}

}
