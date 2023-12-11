package com.soon.slt.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import com.soon.slt.CommentRequest;
import com.soon.slt.DataNotFound;
import com.soon.slt.entity.TbBoard;
import com.soon.slt.entity.TbComment;
import com.soon.slt.entity.TbUser;
import com.soon.slt.form.TbCommentForm;
import com.soon.slt.service.TbBoardService;
import com.soon.slt.service.TbCommentService;
import com.soon.slt.service.TbUserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequestMapping("/comment")
@RequiredArgsConstructor
@Controller
public class TbCommentController {
	
	private final TbBoardService tbBoardService;
	private final TbUserService tbUserService;
	private final TbCommentService tbCommentService;
	

	
	// 댓글 생성 //Mono<Comment>
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/create/{bdIdx}")
	public String createComment(Model model, @PathVariable("bdIdx")Long bdIdx, Principal principal,
			@Valid TbCommentForm commentForm, BindingResult bindingResult) {
		System.out.println("댓글 생성 컨트롤러");
		TbBoard tbBoard = this.tbBoardService.boardDetail(bdIdx);
		TbUser tbUser = this.tbUserService.getUser(principal.getName());
		if(bindingResult.hasErrors()) {
			model.addAttribute(tbBoard);
			return "board-view";
		}
		this.tbCommentService.addComment(tbBoard, commentForm.getCmtContent(),  tbUser);
		return "redirect:/board/detail/"+bdIdx;
	}
	
	// 댓글 불러오기 //Flux<Comment>
	@GetMapping("/get")
	public List<TbComment> getAllComments(@RequestBody Long bdIdx) {
		return tbCommentService.getAllComments(bdIdx);
	}
	
	// 댓글 수정
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/update/{cmtIdx}")
    public String updateComment(@Valid TbCommentForm tbCommentForm, BindingResult bindingResult, @PathVariable("cmtIdx") Long cmtIdx, Principal principal) {
    	if(bindingResult.hasErrors()) {
    		return "board-view";
    	}
    	TbComment tbComment = this.tbCommentService.getComment(cmtIdx);
    	if (!tbComment.getTbUser().getUserNick().equals(principal.getName())) {
    		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
    	}
    	this.tbCommentService.commentUpdate(tbComment, tbComment.getCmtContent());
    	tbCommentForm.setCmtContent(tbComment.getCmtContent());
    	return String.format("redirect:/board/detail/%s#reple_%s", tbComment.getTbBoard().getBdIdx(), tbComment.getCmtIdx());
    }
    
    // 댓글 삭제
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{cmtIdx}")
    public String commentDelete(Principal principal, @PathVariable("cmtIdx") Long cmtIdx) {
    	TbComment tbComment = this.tbCommentService.getComment(cmtIdx);
    	if(!tbComment.getTbUser().getUserNick().equals(principal.getName())) {
    		throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"삭제 권한이 없습니다.");
    	}
    	this.tbCommentService.commentDelete(tbComment);
    	return String.format("redirect:/board/detail/%s", tbComment.getTbBoard().getBdIdx());
    }
	
    // 댓글 좋아요
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/good/{cmtIdx}")
    public String commentGood(Principal principal, @PathVariable("cmtIdx") Long cmtIdx) {
    	TbComment tbComment = this.tbCommentService.getComment(cmtIdx);
    	TbUser tbUser = this.tbUserService.getUser(principal.getName());
    	this.tbCommentService.commentGood(tbComment, tbUser);
    	return String.format("redirect:/board/detail/%s#reple_%s", tbComment.getTbBoard().getBdIdx(), tbComment.getCmtIdx());
    	
    }
	

}
