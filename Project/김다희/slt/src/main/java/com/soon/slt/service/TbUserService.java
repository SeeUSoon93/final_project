package com.soon.slt.service;

import java.time.LocalDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
	
	
	
	
	
	

}
