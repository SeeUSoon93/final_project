package com.soon.slt.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.soon.slt.entity.TbBoard;
import com.soon.slt.entity.TbComment;
import com.soon.slt.entity.TbLikes;
import com.soon.slt.entity.TbUser;
import com.soon.slt.form.TbUserForm;
import com.soon.slt.service.TbUserService;

import jakarta.servlet.http.HttpServletRequest;
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
	public String signup(TbUserForm tbUserForm) {
		return "signUp";
	}

	@PostMapping("/signup")
	public String signup(@Valid TbUserForm tbUserForm, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return "signUp";
		}
		if (!tbUserForm.getUserPw1().equals(tbUserForm.getUserPw2())) {
			bindingResult.rejectValue("userPw2", "PwNotSame", "비밀번호를 확인해주세요!");
			return "signUp";
		}
		try {
			tbUserForm.setJoinedAt(LocalDateTime.now());
			this.tbUserService.create(tbUserForm.getUserEmail(), tbUserForm.getUserPw1(), tbUserForm.getUserNick(), tbUserForm.getJoinedAt());
		} catch (Exception e) {
			e.printStackTrace();
			bindingResult.reject("signupfail", "이미 등록된 유저입니다.");
			return "signUp";
		}
		return "redirect:/main";
	}

	@GetMapping("/login")
	public String login() {
		return "login";
	}

	// 마이페이지 조회
	@GetMapping("/mypage")
	public String goMyPage(Model model, Principal principal) {
		
		TbUser tbUser = this.tbUserService.getUser(principal.getName());
		model.addAttribute("tbUser", tbUser);
		
		List<TbBoard> boardList = this.tbUserService.myBoardList(tbUser);
		model.addAttribute("boardList", boardList);
		
		
		List<TbLikes> likeList = this.tbUserService.myLikeList(tbUser);
		model.addAttribute("likeList", likeList);
		
		return "mypage";
	}
	
	
	@GetMapping("/user_email/{email}")
	public ResponseEntity<Boolean> checkEmailDuplicate(@PathVariable String email){
		return ResponseEntity.ok(tbUserService.checkEmailDuplicate(email));
	}
	
	@GetMapping("/user_nick/{nick}")
	public ResponseEntity<Boolean> checkNickDuplicate(@PathVariable String nick){
		return ResponseEntity.ok(tbUserService.checkNickDuplicate(nick));
	}
	
}
