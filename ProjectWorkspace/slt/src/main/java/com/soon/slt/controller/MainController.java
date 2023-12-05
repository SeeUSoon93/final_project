package com.soon.slt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

@Controller
public class MainController {

	// 메인 화면으로 이동
	@GetMapping("/main")
	public String goMain() {
		return "main";
	}

	@GetMapping("/board")
	public String goBoard() {
		return "board-list2";
	}


	// Footer 와 Header 활성화
	@GetMapping("footer")
	public String footer() {
		return "footer";
	}
	@GetMapping("header")
	public String header() {
		return "header";
	}

}
