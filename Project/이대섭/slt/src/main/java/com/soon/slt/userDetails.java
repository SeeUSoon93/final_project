package com.soon.slt;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class userDetails implements UserDetails {
	private String username; // 사용자 아이디
	private String password; // 비밀번호
	private String nickname; // 닉네임
	private Collection<? extends GrantedAuthority> authorities; // 권한 목록

// 기본 생성자, getter, setter 생략
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

// 닉네임을 반환하는 메서드
	public String getNickname() {
		return nickname;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true; // 계정 만료 여부
	}

	@Override
	public boolean isAccountNonLocked() {
		return true; // 계정 잠금 여부
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true; // 비밀번호 만료 여부
	}

	@Override
	public boolean isEnabled() {
		return true; // 계정 활성화 여부
	}

}
