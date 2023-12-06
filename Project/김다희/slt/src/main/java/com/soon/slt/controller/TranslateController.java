package com.soon.slt.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@RequestMapping("/translate")
@RestController
public class TranslateController {

	// Footer 와 Header 활성화
		@GetMapping("footer")
		public String footer() {
			return "footer";
		}
		@GetMapping("header")
		public String header() {
			return "header";
		}
	
    private final RestTemplate restTemplate;

    
    

}