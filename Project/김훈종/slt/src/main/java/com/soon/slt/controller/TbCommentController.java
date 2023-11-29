package com.soon.slt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.soon.slt.service.TbBoardService;
import com.soon.slt.service.TbUserService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/comment")
@RequiredArgsConstructor
@Controller
public class TbCommentController {
	
	private final TbBoardService tbBoardService;
	private final TbUserService tbUserService;
	
	
	
	
	
	

}
