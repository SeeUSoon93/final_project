package com.soon.slt.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.soon.slt.exception;
import com.soon.slt.entity.TbBoard;
import com.soon.slt.entity.TbComment;
import com.soon.slt.entity.TbUser;
import com.soon.slt.repository.TbCommentRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class TbCommentService {
	
	private final TbCommentRepository tbCommentRepository;
	
	// 댓글 아이디로 댓글 조회
	public TbComment getComment(String cmtIdx) {
		Optional<TbComment> tbcomment = this.tbCommentRepository.findById(cmtIdx);
		if (tbcomment.isPresent()) {
			return tbcomment.get();
		} else {
			throw new exception("댓글이 없습니다.");
		}
	}
	
	// 댓글 수정
	public void commentUpdate(TbComment tbComment, String cmtContent) {
		tbComment.setCmtContent(cmtContent);
		tbComment.setCreatedAt(LocalDateTime.now());
		this.tbCommentRepository.save(tbComment);
	}
	
	// 댓글 삭제
	public void commentDelete(TbComment tbComment) {
		this.tbCommentRepository.delete(tbComment);
	}

	// 댓글 좋아요
	public void commentGood(TbComment tbComment, TbUser tbUser) {
		tbComment.getCmtLikes().add(tbUser);
		this.tbCommentRepository.save(tbComment);
	}
	
	
	
}
