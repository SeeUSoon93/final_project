package com.soon.slt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.soon.slt.entity.boardTB;
import com.soon.slt.entity.userTB;
import com.soon.slt.form.RepleForm;
import com.soon.slt.service.BoardService;
import com.soon.slt.service.UserService;
import com.soon.slt.service.RepleService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/reple")
public class RepleController {

	private final RepleService repleService;
	private final BoardService boardService;
	private final UserService userService;
	
	// 댓글 
	@PostMapping("/create/{boardIdx}")
	public String repleCreate(Model model,@PathVariable("boardIdx")Long boardIdx,
			@Valid RepleForm repleForm, BindingResult bindingResult) {
		boardTB boardTB = this.boardService.boardDetail(boardIdx);
		
		// 로그인 memberService.?(username); 가져오기
		userTB userTB = this.userService.login(userName);
		if(bindingResult.hasErrors()) {
			model.addAttribute(boardTB);
			//return "board_detail";
			return "index";
		}
		this.repleService.repleCreate(boardTB, repleForm.getRepleCon(), userTB);
		//return "redirect:/board/detail/"+boardIdx;
		return "index";
	}
}
