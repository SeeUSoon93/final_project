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
	
	private final TbSLangDicService tbSignlangService;
	
	// 수어 게시글 리스트 조회
	@GetMapping("/main")
	public String langMain(Model model, @RequestParam(value = "page", defaultValue = "0")int page) {
		Page<TbSignlang> langList = this.tbSignlangService.langList(page);
		model.addAttribute("langList", langList);
		return "lang_main";
	}
	
	
	// 수어 게시글 상세보기
	@GetMapping("/detail/{slangIdx}")
	public String langDetail(Model model, @PathVariable("slangIdx") String slangIdx) {
		TbSignlang tbSignlang = this.tbSignlangService.langDetail(slangIdx);
		
		// 영상 경로를 뷰로 전달
		model.addAttribute("videoPath", tbSignlang.getSlangVideo());
		
		// 나머지 데이터 뷰로 전달
		model.addAttribute("tbSignlang",tbSignlang);
		//return "lang_detail";
		return "lang_main";
	}
	
	
	
	
	
	
	

}
