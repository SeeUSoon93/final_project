package com.soon.myhome.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.soon.myhome.exception;
import com.soon.myhome.entity.Member;
import com.soon.myhome.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MemberService {
	
	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;
	
	public Member join(String username, String password, String memEmail) {
		Member member = new Member();
		member.setUsername(username);
		member.setMemEmail(memEmail);
		
		// BCryptPasswordEncoder - BCrypt 해싱 함수를 사용해서 비밀번호를 암호화
		// BCryptPasswordEncoder pwEncoder = new BCryptPasswordEncoder();
		
		// BCryptPasswordEncoder 객체를 직접 생성하여 사용하지 않고 PasswordEncoder 객체를 주입받아 사용
		member.setPassword(passwordEncoder.encode(password));
		
		this.memberRepository.save(member);
		return member;
		
	}
	
	// username를 통해 Member객체를 조회
    public Member getMem(String username) {
        Optional<Member> member = this.memberRepository.findByusername(username);
        if (member.isPresent()) {
            return member.get();
        } else {
            throw new exception("member not found");
        }
    }

}
