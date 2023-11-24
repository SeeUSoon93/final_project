package com.soon.slt.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.soon.slt.entity.TbBoard;
import com.soon.slt.entity.TbUser;
import com.soon.slt.form.TbBoardForm;
import com.soon.slt.service.TbBoardService;
import com.soon.slt.service.TbUserSecurityService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequestMapping("/board")
@Controller
@RequiredArgsConstructor
public class TbBoardController {

	private final TbBoardService tbBoardService;
	private final TbUserSecurityService tbUserServiceSecurityService;

	// 게시글 리스트 조회
	@GetMapping("/main")
	public String goBoardMain(Model model, @RequestParam(value = "page", defaultValue = "0") int page) {
		Page<TbBoard> boardList = this.tbBoardService.selectList(page);
		model.addAttribute("boardList", boardList);
		// return "board_main";
		return "index";
	}

	// 게시글 생성
	@PostMapping("/create")
	public String boardCreate(@Valid TbBoardForm tbBoardForm, BindingResult bindingResult, Principal principal,
			@RequestPart("files") List<MultipartFile> files, RedirectAttributes redirectAttributes) {
		if (bindingResult.hasErrors()) {
			// return "board_form";
			return "index";
		}
		TbUser user = (TbUser) this.tbUserServiceSecurityService.loadUserByUsername(principal.getName());
		try {
			this.tbBoardService.boardCreate(tbBoardForm.getBdTitle(), tbBoardForm.getBdCategory(),
					tbBoardForm.getBdContent(), user, files);
		} catch (IOException e) {
			redirectAttributes.addFlashAttribute("message", "파일업로드에 실패하셨습니다.");
			// return "board_form";
			return "index";
		}

		// return "redirect:/board/main";
		return "index";
	}

	// 게시글 삭제
	@PostMapping("/delete/{bdIdx}")
	public String boardDelete(@PathVariable("bdIdx") String bdIdx, Principal principal) {
		TbBoard tbBoard = this.tbBoardService.boardDetail(bdIdx);
		if(!tbBoard.getTbUser().getUserNick().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제 권한이 없습니다.");
		}
		this.tbBoardService.boardDelete(bdIdx);
		return "redirect:/board/main";
	}

	// 게시글 상세보기
	@GetMapping("/detail/{bdIdx}")
	public String boardDetail(Model model, @PathVariable("bdIdx") String bdIdx) {
		TbBoard tbBoard = this.tbBoardService.boardDetail(bdIdx);
		model.addAttribute("tbBoard", tbBoard);
		return "board_datail";
	}

	// 게시글 수정 페이지로 이동
	@GetMapping("/update/{bdIdx}")
	public String boardUpdate(TbBoardForm tbBoardForm, @PathVariable("bdIdx") String bdIdx, Principal principal) {
		TbBoard tbBoard = tbBoardService.boardDetail(bdIdx);
		if(!tbBoard.getTbUser().getUserNick().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
		}
		tbBoardForm.setBdCategory(tbBoard.getBdCategory());
		tbBoardForm.setBdTitle(tbBoard.getBdTitle());
		tbBoardForm.setBdContent(tbBoard.getBdContent());
		return "board_form";
	}

	// 게시글 수정
	@PostMapping("/update/{bdIdx}")
	public String boardUpdate(@Valid TbBoardForm tbBoardForm, BindingResult bindingResult, Principal principal, @PathVariable("bdIdx") String bdIdx ){
		if(bindingResult.hasErrors()) {
			return "board_form";
		}
		TbBoard tbBoard = this.tbBoardService.boardDetail(bdIdx);
		if(!tbBoard.getTbUser().getUserNick().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
		}
		this.tbBoardService.boardUpdate(tbBoard, tbBoardForm.getBdTitle(), tbBoardForm.getBdCategory(), tbBoardForm.getBdContent());
		return String.format("redirect:/board/detail/%s", bdIdx);
	}

}
