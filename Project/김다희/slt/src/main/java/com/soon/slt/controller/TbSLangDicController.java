package com.soon.slt.controller;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.soon.slt.entity.TbSignlang;
import com.soon.slt.service.TbSLangDicService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/dic")
@Controller
@RequiredArgsConstructor
public class TbSLangDicController {

	private final TbSLangDicService tbSLangDicService;
	
	// 수어사전 검색 기능
	@GetMapping("/search")
	public String dicSearch(Model model, @RequestParam(value="page", defaultValue = "0")int page,
			@RequestParam(value="searchingWord", defaultValue = "") String searchingWord) {
		Page<TbSignlang> paging = this.tbSLangDicService.getSignlang(page, searchingWord);
		model.addAttribute("paging", paging);
		model.addAttribute("searchingWord", searchingWord);
		return "index";
	}
	
	
	
	
}
