package com.soon.slt.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
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
	// @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@PostMapping("/create")
	public String boardCreate(@Valid TbBoardForm tbBoardForm, BindingResult bindingResult, Principal principal,
			@RequestPart("files") List<MultipartFile> files, RedirectAttributes redirectAttributes) {
		if (bindingResult.hasErrors()) {
			// return "board_form";
			return "index";
		}
		TbUser user = (TbUser)this.tbUserServiceSecurityService.loadUserByUsername(principal.getName());
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
	@PostMapping("/delete")
	public String boardDelete(String bdIdx) {
		this.tbBoardService.boardDelete(bdIdx);
		return "redirect:/board/main";
	}

	// 게시글 상세보기
	@GetMapping("/post/{bdIdx}")
	public String boardDetail(Model model, @PathVariable("bdIdx") String bdIdx) {
		TbBoard tbBoard = this.tbBoardService.boardDetail(bdIdx);
		model.addAttribute(tbBoard);
		return "board_datail";
	}

	// 게시글 수정 페이지로 이동
	@GetMapping("/post/update/{bdIdx}")
	public String boardUpdate(@PathVariable("bdIdx") String bdIdx, Model model) {
		TbBoard tbBoard = tbBoardService.boardDetail(bdIdx);
		model.addAttribute(tbBoard);
		return "board_update";
	}

	// 게시글 수정
	@PostMapping("/post/update/{bdIdx}")
	public String boardUpdate(String bdTitle, String bdCategory, String bdContent, String user,
			List<MultipartFile> files) throws IOException {
		this.tbBoardService.boardCreate(bdTitle, bdCategory, bdContent, null, files);
		return "redirect:/main";
	}

}
