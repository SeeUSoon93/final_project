package com.soon.slt.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.soon.slt.entity.boardTB;
import com.soon.slt.entity.userTB;
import com.soon.slt.form.BoardForm;
import com.soon.slt.form.RepleForm;
import com.soon.slt.service.BoardService;
import com.soon.slt.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequestMapping("/board")
@RequiredArgsConstructor
@Controller
public class BoardController {
	
	private final BoardService boardService;

	// 게시글 리스트 조회
	@GetMapping("/main")
	public String goBoardMain(Model model, @RequestParam(value = "page", defaultValue = "0")int page) {
		Page<boardTB> boardList = this.boardService.selectList(page);
		model.addAttribute("boardList",boardList);
		//return "board_main";
		return "index";
	}
	
	// 게시글 상세 조회
	@GetMapping("/detail/{boardIdx}")
	public String boardDetail(Model model,@PathVariable("boardIdx") Long boardIdx,RepleForm repleForm) {
		boardTB board = this.boardService.boardDetail(boardIdx);
		this.boardService.boardViewCount(board);
		model.addAttribute(board);		
		//return "board_detail";
		return "index";
	}
		
	// 게시글 작성 폼으로 이동
	@GetMapping("/create")
	public String boardCreate(BoardForm boardForm) {
		//return "board_form";
		return "index";
	}
		
	// 게시글 생성
	@PostMapping("/create")
	public String boardCreate(@Valid BoardForm boardForm, BindingResult bindingResult, @RequestParam userTB userName,
								@RequestParam("files") List<MultipartFile> files, RedirectAttributes redirectAttributes) {
		if(bindingResult.hasErrors()) {
			//return "board_form";
			return "index";
		}
        try {
        	this.boardService.boardCreate(boardForm.getBoardTitle(),boardForm.getBoardCate(), boardForm.getBoardCon(), userName, files);
        } catch (IOException e) {
        	redirectAttributes.addFlashAttribute("message",
        			"파일업로드에 실패하셨습니다.");
        	//return "board_form";
        	return "index";
        }
		
		//return "redirect:/board/main";
		return "index";
	}
	
}
