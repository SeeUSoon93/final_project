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
import com.soon.slt.entity.TbLikes;
import com.soon.slt.entity.TbUser;
import com.soon.slt.repository.TbBoardRepository;
import com.soon.slt.repository.TbUserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class TbUserService {
	
	private final TbUserRepository tbUserRepository;
	private final TbBoardRepository tbBoardRepository;
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
	
	// userNick을 통해 TbUser객체를 조회
	public TbUser getUser(String userEmail) {
		Optional<TbUser> tbuser = this.tbUserRepository.findByUserEmail(userEmail);
		if(tbuser.isPresent()) {
			return tbuser.get();
		} else {
			throw new exception("회원이 없습니다.");
		}
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
