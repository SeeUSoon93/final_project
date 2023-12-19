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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.soon.slt.entity.TbBoard;
import com.soon.slt.entity.TbUser;
import com.soon.slt.form.NoticeForm;
import com.soon.slt.service.NoticeService;
import com.soon.slt.service.TbUserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/notice")
@RequiredArgsConstructor
public class NoticeController {

   private final NoticeService noticeService;
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
   
   // 공지사항 리스트 출력
   @GetMapping("/main")
   public String noticeList(Model model) {
      List<TbBoard> boardList = this.noticeService.selectList();
      model.addAttribute("boardList", boardList);
      return "notice-list";
   }
   
   // 공지사항 생성 페이지 이동
   @GetMapping("/create")
   public String noticeForm() {
      return "notice-write";
   }
   
   
// 공지사항 생성
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/create")
	public String noticeCreate(@Valid NoticeForm noticeForm, BindingResult bindingResult, Principal principal, RedirectAttributes redirectAttributes) {
		if (bindingResult.hasErrors()) {
			System.out.println("공지사항 생성 중 에러 발생");
			return "notice-list";
		}
		System.out.println("공지사항 생성");
		TbUser user = this.tbUserService.getUser(principal.getName());
		this.noticeService.noticeCreate(noticeForm.getBdTitle(), noticeForm.getBdContent(), user);
		return "redirect:/notice/main";
	}
	
	// 공지사항 삭제
	@GetMapping("/delete/{bdIdx}")
	public String noticeDelete(@PathVariable("bdIdx") Long bdIdx, Principal principal) {
		TbBoard tbBoard = this.noticeService.noticeDetail(bdIdx);
		if(!tbBoard.getTbUser().getUserNick().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제 권한이 없습니다.");
		}
		this.noticeService.boardDelete(bdIdx);
		return "redirect:/board/main";
	}

   // 공지사항 수정 페이지로 이동
   @GetMapping("/update/{bdIdx}")
   public String noticeUpdate(NoticeForm noticeForm, @PathVariable("bdIdx") Long bdIdx, Principal principal) {
      TbBoard tbBoard = noticeService.noticeDetail(bdIdx);
      if (!tbBoard.getTbUser().getUserNick().equals(principal.getName())) {
         throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
      }
      noticeForm.setBdTitle(tbBoard.getBdTitle());
      noticeForm.setBdContent(tbBoard.getBdContent());
      return "notice-update";
   }
   
   // 공지사항 수정
      @PostMapping("/update/{bdIdx}")
      public String noticeUpdate(@Valid NoticeForm noticeForm, BindingResult bindingResult, Principal principal, @PathVariable("bdIdx") Long bdIdx ){
         if(bindingResult.hasErrors()) {
            return "notice_form";
         }
         TbBoard tbBoard = this.noticeService.noticeDetail(bdIdx);
         if(!tbBoard.getTbUser().getUserNick().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
         }
         this.noticeService.noticeUpdate(tbBoard, noticeForm.getBdTitle(), noticeForm.getBdContent());
         return String.format("redirect:/board/detail/%s", bdIdx);
      }
   
}