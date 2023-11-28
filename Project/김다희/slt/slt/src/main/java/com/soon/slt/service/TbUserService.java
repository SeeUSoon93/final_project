package com.soon.slt.service;

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
	
	public TbUser create(String userEmail, String userPw, String userNick) {
		TbUser TbUser = new TbUser();
		TbUser.setUserEmail(userEmail);		
		TbUser.setUserPw(passwordEncoder.encode(userPw));
		TbUser.setUserNick(userNick);		
		this.tbUserRepository.save(TbUser);
		return TbUser;
	}
	
	public TbUser getUser(String userEmail) {
		Optional<TbUser> user = this.tbUserRepository.findByUserEmail(userEmail);
		if(user.isPresent()) {
			return user.get();
		} else {
			throw new exception("user not found");
		}
	}

}
