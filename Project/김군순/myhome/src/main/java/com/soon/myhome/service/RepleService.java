package com.soon.myhome.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.soon.myhome.exception;
import com.soon.myhome.entity.Board;
import com.soon.myhome.entity.Member;
import com.soon.myhome.entity.Reple;
import com.soon.myhome.repository.RepleRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class RepleService {

	private final RepleRepository repleRepository;
	
	public Reple createReple(Board board, String repleCon, Member member) {
		Reple reple = new Reple();
		reple.setRepleCon(repleCon);
		reple.setRepleDate(LocalDateTime.now());
		reple.setBoard(board);
		reple.setMember(member);
		this.repleRepository.save(reple);
		return reple;
	}
	
	// 댓글 아이디로 댓글 조회
    public Reple getReple(Integer repleNum) {
        Optional<Reple> reple = this.repleRepository.findById(repleNum);
        if (reple.isPresent()) {
            return reple.get();
        } else {
            throw new exception("reple not found");
        }
    }
    // 댓글 수정 	
    public void repleUpdate(Reple reple, String repleCon) {
        reple.setRepleCon(repleCon);
        reple.setRepleUpDate(LocalDateTime.now());
        this.repleRepository.save(reple);
    }
    
    // 댓글 삭제
    public void repleDelete(Reple reple) {
    	this.repleRepository.delete(reple);
    }
    
    // 댓글 추천
    public void repleGood(Reple reple, Member member) {
        reple.getGood().add(member);
        this.repleRepository.save(reple);
    }
	
}
