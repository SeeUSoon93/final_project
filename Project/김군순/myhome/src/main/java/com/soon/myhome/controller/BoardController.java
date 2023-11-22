package com.soon.myhome.controller;

import java.security.Principal;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import com.soon.myhome.entity.Board;
import com.soon.myhome.entity.Member;
import com.soon.myhome.form.BoardForm;
import com.soon.myhome.form.RepleForm;
import com.soon.myhome.service.BoardService;
import com.soon.myhome.service.MemberService;
import com.soon.myhome.service.RepleService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequestMapping("/board")
// RequiredArgsConstructor - final이 붙은 속성을 포함하는 생성자를 자동으로 생성해 줌
@RequiredArgsConstructor
@Controller
public class BoardController {

	// 의존성 주입
	// private final BoardRepository boardRepository;
	private final BoardService boardService;
	private final MemberService memberService;
	
	// 리스트 조회
	@GetMapping("/main")
	public String boardHome(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "serchingWord", defaultValue = "") String serchingWord) {
		System.out.println("검색단어 들어오냐?"+serchingWord);
//		// findAll - 테이블의 모든 데이터를 조회
//		List<Board> boardList = this.boardService.selectList();
//		model.addAttribute(boardList);
//		return "board_home";
       	Page<Board> paging = this.boardService.getList(page, serchingWord);
        model.addAttribute("paging", paging);
        model.addAttribute("serchingWord", serchingWord);
        return "board_main";
	}

	// 게시글 상세조회
	@GetMapping("/detail/{boardNum}")
	public String boardDetail(Model model, @PathVariable("boardNum") Integer boardNum, RepleForm repleForm) {
		Board board = this.boardService.getBoard(boardNum);
		model.addAttribute("board", board);
		return "board_detail";
	}
	
	@PreAuthorize("isAuthenticated()")
	// 게시글 등록하는 화면으로 이동
	@GetMapping("/create")
	public String questionCreate(BoardForm boardForm) {
		return "board_form";
	}
	
	@PreAuthorize("isAuthenticated()")
	// 게시글 등록하기
	// @Valid - 유효성 검사. BoardForm에서 설정한 유효성을 검사함
	// BindingResult 매개변수는 @Valid 애너테이션으로 인해 검증이 수행된 결과를 의미하는 객체
	// - BindingResult 매개변수는 항상 @Valid 매개변수 바로 뒤에 위치해야 한다.
	// - 만약 2개의 매개변수의 위치가 정확하지 않다면 @Valid만 적용이 되어 입력값 검증 실패 시 400 오류가 발생한다.
	@PostMapping("/create")
	public String questionCreate(@Valid BoardForm boardForm, BindingResult bindingResult, Principal principal) {
		if(bindingResult.hasErrors()) {
			return "board_form";
		}		
		Member member = this.memberService.getMem(principal.getName());
		this.boardService.createBoard(boardForm.getBoardTitle(), boardForm.getBoardCon(), member);
		return "redirect:/board/main";
	}
	
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/update/{boardNum}")
    public String boardUpdate(BoardForm boardForm, @PathVariable("boardNum") Integer boardNum, Principal principal) {
        Board board = this.boardService.getBoard(boardNum);
        if(!board.getMember().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        boardForm.setBoardTitle(board.getBoardTitle());
        boardForm.setBoardCon(board.getBoardCon());
        return "board_form";
    }
	
	// 게시글 수정
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/update/{boardNum}")
    public String boardUpdate(@Valid BoardForm boardForm, BindingResult bindingResult, Principal principal, @PathVariable("boardNum") Integer boardNum) {
    	if (bindingResult.hasErrors()) {
            return "board_form";
        }
        Board board = this.boardService.getBoard(boardNum);
        if (!board.getMember().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        this.boardService.updateBoard(board, boardForm.getBoardTitle(), boardForm.getBoardCon());
        return String.format("redirect:/board/detail/%s", boardNum);
    }
    
    // 게시글 삭제
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{boardNum}")
    public String boardDelete(Principal principal, @PathVariable("boardNum") Integer boardNum) {
    	Board board = this.boardService.getBoard(boardNum);
    	if(!board.getMember().getUsername().equals(principal.getName())) {
    		throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"삭제권한이 없습니다.");
    	}
    	this.boardService.boardDelete(board);
    	return "redirect:/";
    }
    
    // 게시글 추천
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/good/{boardNum}")
    public String boardGood(Principal principal, @PathVariable("boardNum") Integer boardNum) {
        Board board = this.boardService.getBoard(boardNum);
        Member member = this.memberService.getMem(principal.getName());
        this.boardService.boardGood(board, member);
        return String.format("redirect:/board/detail/%s", boardNum);
    }
    
    
}
