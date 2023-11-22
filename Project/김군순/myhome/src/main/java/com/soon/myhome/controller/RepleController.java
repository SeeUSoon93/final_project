package com.soon.myhome.controller;

import java.security.Principal;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import com.soon.myhome.entity.Board;
import com.soon.myhome.entity.Member;
import com.soon.myhome.entity.Reple;
import com.soon.myhome.form.RepleForm;
import com.soon.myhome.service.BoardService;
import com.soon.myhome.service.MemberService;
import com.soon.myhome.service.RepleService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequestMapping("/reple")
@RequiredArgsConstructor
@Controller
public class RepleController {

	private final BoardService boardService;
	private final RepleService repleService;
	private final MemberService memberService;
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/create/{boardNum}")
	public String createReple(Model model, @PathVariable("boardNum") Integer boardNum,

			@Valid RepleForm repleForm, BindingResult bindingResult, Principal principal) {
		// Principal - 시큐리티에서 제공하는 객체 : 현재 로그인한 사용자의 정보를 가져옴
		Board board = this.boardService.getBoard(boardNum);
		Member member = this.memberService.getMem(principal.getName());
		if (bindingResult.hasErrors()) {
			model.addAttribute("board", board);
			return "board_detail";
		}
		Reple reple = this.repleService.createReple(board, repleForm.getRepleCon(), member);
		return String.format("redirect:/board/detail/%s#reple_%s", reple.getBoard().getBoardNum(),reple.getRepleNum());
	}
	
	// 댓글 수정
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/update/{repleNum}")
    public String updateReple(@Valid RepleForm repleForm,BindingResult bindingResult, @PathVariable("repleNum") Integer repleNum, Principal principal) {
    	if(bindingResult.hasErrors()) {
    		return "reple_form";
    	}
    	Reple reple = this.repleService.getReple(repleNum);
        if (!reple.getMember().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        this.repleService.repleUpdate(reple, repleForm.getRepleCon());
        repleForm.setRepleCon(reple.getRepleCon());
        return String.format("redirect:/board/detail/%s#reple_%s", reple.getBoard().getBoardNum(),reple.getRepleNum());
    }
	
    //댓글 삭제
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{repleNum}")
    public String repleDelete(Principal principal, @PathVariable("repleNum") Integer repleNum) {
    	Reple reple = this.repleService.getReple(repleNum);
    	if(!reple.getMember().getUsername().equals(principal.getName())) {
    		throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"삭제권한이 없습니다.");
    	}
    	this.repleService.repleDelete(reple);
    	return String.format("redirect:/board/detail/%s", reple.getBoard().getBoardNum());
    }

    // 댓글 추천
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/good/{repleNum}")
    public String repleGood(Principal principal, @PathVariable("repleNum") Integer repleNum) {
        Reple reple = this.repleService.getReple(repleNum);
        Member member = this.memberService.getMem(principal.getName());
        this.repleService.repleGood(reple, member);
        return String.format("redirect:/board/detail/%s#reple_%s", reple.getBoard().getBoardNum(),reple.getRepleNum());
    }
}
