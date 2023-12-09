package com.soon.slt.controller;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.soon.slt.entity.TbSignlang;
import com.soon.slt.repository.TbSignlangRepository;
import com.soon.slt.service.TbSLangDicService;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RequestMapping("/dic")
@Controller
@RequiredArgsConstructor
public class TbSLangDicController {

	private final TbSLangDicService tbSLangDicService;

	// Footer 와 Header 활성화
	@GetMapping("footer")
	public String footer() {
		return "footer";
	}
	@GetMapping("header")
	public String header() {
		return "header";
	}

	// 수어 게시글 리스트 조회
	@GetMapping("/main")
	public String langMain(Model model, @RequestParam(value = "page", defaultValue = "0")int page,
						   @RequestParam(value="category", defaultValue = "") String category) {
		Page<TbSignlang> langList = this.tbSLangDicService.getSignlang(page, category);
		model.addAttribute("langList", langList);
		return "sl-dictionary";
	}

//	@GetMapping("/list")
//	public Page<TbSignlang> lang(Model model, @RequestParam(value = "page", defaultValue = "0")int page,
//								 @RequestParam(value="category", defaultValue = "") String category) {
//		Page<TbSignlang> langList = this.tbSLangDicService.getSignlang(page, category);
//		return langList;
//	}

	@GetMapping("/list")
	@ResponseBody
	public Page<TbSignlang> lang(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
					   @RequestParam(value = "category", defaultValue = "") String category) {
		Page<TbSignlang> langList = this.tbSLangDicService.getSignlang(page, category);
		model.addAttribute("langList", langList.getContent()); // getContent()로 실제 데이터에 접근
		return langList;
	}

	// 수어 게시글 상세보기
	@GetMapping("/detail/{slangIdx}")
	public String langDetail(Model model, @PathVariable("slangIdx") Long slangIdx) {
		TbSignlang slang = this.tbSLangDicService.langDetail(slangIdx);
		// 나머지 데이터 뷰로 전달
		model.addAttribute("slang",slang);
		return "sl-dictview";
	}

	// 수어사전 검색 기능
	@GetMapping("/search")
	public String dicSearch(Model model, @RequestParam(value="page", defaultValue = "0")int page,
							@RequestParam(value="kw", defaultValue = "") String kw,
							@RequestParam(value="category", defaultValue = "") String category) {
		Page<TbSignlang> paging = this.tbSLangDicService.searching(page, kw, category);
		model.addAttribute("paging", paging);
		model.addAttribute("searchingWord", kw);
		return "index";
	}







}