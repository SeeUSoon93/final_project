package com.soon.myhome.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class MainController {

	// 메인 화면으로 이동
	@GetMapping("/")
	public String goMain() {
		return "main";
	}
	
}
