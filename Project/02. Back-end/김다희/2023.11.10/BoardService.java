package com.soon.slt.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.soon.slt.DataNotFound;
import com.soon.slt.entity.boardTB;
import com.soon.slt.repository.BoardRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {
	
	private final BoardRepository boardRepository;

	// 게시판 리스트 조회
	public Page<boardTB> selectList(int page){
		List<Sort.Order> sort = new ArrayList<>();
		sort.add(Sort.Order.desc("boardDate"));
		Pageable pageable = PageRequest.of(page, 10, Sort.by(sort));
		return this.boardRepository.findAll(pageable);
	}
	
	// 게시글 상세 조회
	public boardTB boardDetail(Long boardIdx) {
		Optional<boardTB> b = this.boardRepository.findById(boardIdx);
		if(b.isPresent()) {
			return b.get();
		}else {
			throw new DataNotFound("없는 게시글입니다.");
		}
	}
	
	// 게시글 작성
	@Transactional
	public void boardCreate(String boardTitle, String boardCon, String boardCate) {
		boardTB b = new boardTB();
		b.setBoardTitle(boardTitle);
		b.setBoardCate(boardCate);
		b.setBoardCon(boardCon);
		b.setBoardDate(LocalDateTime.now());
		this.boardRepository.save(b);
	}
	
	@Transactional
	public void boardViewCount(boardTB board) {
		board.setBoardView(board.getBoardView()+1);
		this.boardRepository.save(board);
	}
		
		
		
		
		
		
		
}
