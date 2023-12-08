package com.soon.slt.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.soon.slt.exception;
import com.soon.slt.entity.TbBoard;
import com.soon.slt.entity.TbComment;
import com.soon.slt.entity.TbLikes;
import com.soon.slt.entity.TbUser;
import com.soon.slt.repository.TbBoardRepository;
import com.soon.slt.repository.TbCommentRepository;
import com.soon.slt.repository.TbUserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class TbUserService {
	
	private final TbUserRepository tbUserRepository;
	private final TbBoardRepository tbBoardRepository;
	private final TbCommentRepository tbCommentRepository;
	private final PasswordEncoder passwordEncoder;
	
	public TbUser create(String userEmail, String userPw, String userNick, LocalDateTime joinedAt) {
		TbUser TbUser = new TbUser();
		TbUser.setUserEmail(userEmail);		
		TbUser.setUserPw(passwordEncoder.encode(userPw));
		TbUser.setUserNick(userNick);		
		TbUser.setJoinedAt(joinedAt);
		this.tbUserRepository.save(TbUser);
		return TbUser;
	}
	
	// 마이페이지 회원 정보
	public TbUser getUser(String userEmail) {
		Optional<TbUser> tbuser = this.tbUserRepository.findByUserEmail(userEmail);
		if(tbuser.isPresent()) {
			return tbuser.get();
		} else {
			throw new exception("회원이 없습니다.");
		}
	}
	
	// 마이페이지 게시글 리스트 출력
	public List<TbBoard> myBoardList(TbUser tbUser){
		return this.tbBoardRepository.findAllByUser(tbUser);
	}
	
	// 마이페이지 좋아요 게시글 리스트 출력
	public List<TbLikes> myLikeList(TbUser tbUser){
		return this.tbBoardRepository.findAllByLike(tbUser);
	}

	// 이메일 중복 확인
	public boolean checkEmailDuplicate(String email) {
		return tbUserRepository.existsByUserEmail(email);
	}

	// 닉네임 중복 확인
	public boolean checkNickDuplicate(String nick) {
		return tbUserRepository.existsByUserNick(nick);
	}
	
	
	

}
