package com.soon.myhome.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.soon.myhome.entity.Member;
import com.soon.myhome.repository.MemberRepository;
import com.soon.myhome.role.MemberRole;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MemberSecurityService implements UserDetailsService {
	// UserDetailsService - 스프링 시큐리티가 제공하는 인터페이스.
	// loadUserByUsername 메서드를 강제함 : 사용자명으로 비밀번호를 조회하여 리턴하는 메서드

	private final MemberRepository memberRepository;

	// loadUserByUsername 메서드 오버라이드
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// 사용자 이름(username)을 이용하여 데이터베이스에서 사용자 정보 조회
		Optional<Member> mem = this.memberRepository.findByusername(username);

		// 사용자 정보가 없을 경우 예외 처리
		if (mem.isEmpty()) {
			throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
		}

		// 사용자 정보를 가져오고 권한 목록을 초기화
		Member member = mem.get();
		List<GrantedAuthority> authorities = new ArrayList<>();

		// 사용자 이름이 "admin"일 경우 ADMIN 권한을 추가, 그렇지 않으면 USER 권한을 추가
		if ("admin".equals(username)) {
			authorities.add(new SimpleGrantedAuthority(MemberRole.ADMIN.getValue()));
		} else {
			authorities.add(new SimpleGrantedAuthority(MemberRole.USER.getValue()));
		}

		// UserDetails 객체를 생성하고 반환
		return new User(member.getUsername(), member.getPassword(), authorities);
	}

}
