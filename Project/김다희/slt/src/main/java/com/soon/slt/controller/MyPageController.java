package com.soon.slt.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.soon.slt.entity.TbBoard;
import com.soon.slt.entity.TbUser;
import com.soon.slt.service.MyPageService;
import com.soon.slt.service.TbBoardService;
import com.soon.slt.service.TbUserService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/mypage")
@RequiredArgsConstructor
public class MyPageController {
	
	private final TbUserService tbUserService;
	private final MyPageService myPageService;
	private final TbBoardService tbBoardService;

	// 마이페이지 조회
	@PostMapping("/main")
	public String goMyPage(Model model, BindingResult bindingResult, Principal principal, HttpServletRequest request,
			@RequestParam(value="page", defaultValue="0") int page) {
		if(bindingResult.hasErrors()) {
			String uri = request.getHeader("Referer");
			return "redirect:"+uri;
		}
		TbUser tbUser = this.tbUserService.getUser(principal.getName());
		model.addAttribute("tbUser", tbUser);
		
		Page<TbBoard> tbBoard = this.tbBoardService.myBoardList(page, tbUser);
		model.addAttribute("tbBoard", tbBoard);
		Page<TbBoard> tbLike = this.tbBoardService.myLikeList(page, tbUser);
		model.addAttribute("tbLike", tbLike);
		return "index";
	}
}
