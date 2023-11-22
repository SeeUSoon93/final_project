package com.soon.myhome.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.soon.myhome.exception;
import com.soon.myhome.entity.Board;
import com.soon.myhome.entity.Member;
import com.soon.myhome.entity.Reple;
import com.soon.myhome.repository.BoardRepository;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service // 서비스에는 똑같이 @Service를 붙여줘야함 
public class BoardService {
	
	private final BoardRepository boardRepository;
	
	// 게시판 전체 조회
	//public List<Board> selectList(){
	//	return this.boardRepository.findAll(); 
	//}
	
	// 전체 목록 페이징
	public Page<Board> getList(int page, String kw){
		// 최근에 작성한 게시글이 가장먼저 보이게
		List<Sort.Order> sorts = new ArrayList<>();
		sorts.add(Sort.Order.desc("boardDate"));
		
		// page는 조회할 페이지의 번호, 10은 한 페이지에 보여줄 게시물의 갯수
		Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
		return this.boardRepository.findAllByKeyword(kw, pageable);
	}
	
	// 게시글 상세 조회
	public Board getBoard(Integer id) {
		Optional<Board> board = this.boardRepository.findById(id);
		if(board.isPresent()) {
			return board.get();
		}else {
			throw new exception("board not found");
		}
	}
	
	// 게시글 작성
	public void createBoard(String boardTitle, String boardCon, Member member) {
		Board board = new Board();
		board.setBoardTitle(boardTitle);
		board.setBoardCon(boardCon);
		board.setBoardDate(LocalDateTime.now());
		board.setMember(member);
		this.boardRepository.save(board);
	}
	
	// 게시글 수정
	public void updateBoard(Board board, String boardTitle, String boardCon) {
		board.setBoardTitle(boardTitle);
		board.setBoardCon(boardCon);
		board.setBoardUpDate(LocalDateTime.now());
		this.boardRepository.save(board);
	}
	
	// 게시글 삭제
    public void boardDelete(Board board) {
        this.boardRepository.delete(board);
    }
	
    // 게시글 추천
    public void boardGood(Board board, Member member) {
        board.getGood().add(member);
        this.boardRepository.save(board);
    }
	
    // 검색 기능
    // Specification    
    private Specification<Board> searchBoard(String kw) {
        return new Specification<>() {
            private static final long serialVersionUID = 1L;
            
            @Override
            public Predicate toPredicate(Root<Board> b, CriteriaQuery<?> query, CriteriaBuilder cb) {
                query.distinct(true);  // 중복을 제거 
                Join<Board, Member> boardWriterJoin = b.join("member", JoinType.LEFT);
                Join<Board, Reple> repleJoin = b.join("repleList", JoinType.LEFT);
                Join<Reple, Member> repleWriterJoin = repleJoin.join("member", JoinType.LEFT);
                
                return cb.or(cb.like(b.get("boardTitle"), "%" + kw + "%"), // 제목 
                        cb.like(b.get("boardCon"), "%" + kw + "%"),      // 내용 
                        cb.like(boardWriterJoin.get("memId"), "%" + kw + "%"),    // 질문 작성자 
                        cb.like(repleJoin.get("repleCon"), "%" + kw + "%"),      // 답변 내용 
                        cb.like(repleWriterJoin.get("memId"), "%" + kw + "%"));   // 답변 작성자 
            }
        };
    }
    

}
