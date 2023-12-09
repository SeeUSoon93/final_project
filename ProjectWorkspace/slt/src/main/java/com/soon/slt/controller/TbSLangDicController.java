package com.soon.slt.controller;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.soon.slt.entity.TbSignlang;
import com.soon.slt.repository.TbSignlangRepository;
import com.soon.slt.service.TbSLangDicService;

import lombok.RequiredArgsConstructor;

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
			@RequestParam(value="searchingWord", defaultValue="") String kw) {
		Page<TbSignlang> langList = this.tbSLangDicService.getSignlang(page, kw);
		model.addAttribute("langList", langList);
		return "sl-dictionary";
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
							@RequestParam(value="kw", defaultValue = "") String kw) {
		Page<TbSignlang> paging = this.tbSLangDicService.getSignlang(page, kw);
		model.addAttribute("paging", paging);
		model.addAttribute("searchingWord", kw);
		return "index";
	}
	
	
	
	
	
	

}
