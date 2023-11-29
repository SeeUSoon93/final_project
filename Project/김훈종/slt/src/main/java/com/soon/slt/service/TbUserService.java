package com.soon.slt.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.soon.slt.exception;
import com.soon.slt.entity.TbUser;
import com.soon.slt.repository.TbUserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class TbUserService {
	
	private final TbUserRepository tbUserRepository;
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
	public TbUser getUser(String userNick) {
		Optional<TbUser> tbuser = this.tbUserRepository.findByUserNick(userNick);
		if(tbuser.isPresent()) {
			return tbuser.get();
		} else {
			throw new exception("회원이 없습니다.");
		}
	}
	
	
	
	
	

}
