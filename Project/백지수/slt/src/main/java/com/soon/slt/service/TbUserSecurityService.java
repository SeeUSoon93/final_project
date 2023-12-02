package com.soon.slt.service;

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

import com.soon.slt.entity.TbUser;
import com.soon.slt.repository.TbUserRepository;
import com.soon.slt.role.TbUserRole;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class TbUserSecurityService implements UserDetailsService{
	private final TbUserRepository tbUserRepository;
	
	public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException{
		Optional<TbUser> _tbUser = this.tbUserRepository.findByUserEmail(userEmail);
		if(_tbUser.isEmpty()) {
			throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
		}
		TbUser tbUser = _tbUser.get();
		List<GrantedAuthority> authorities = new ArrayList<>();
		if("admin".equals(userEmail)) {
			authorities.add(new SimpleGrantedAuthority(TbUserRole.ADMIN.getValue()));
		}else {
			authorities.add(new SimpleGrantedAuthority(TbUserRole.USER.getValue()));
		}
		return new User(tbUser.getUserEmail(),tbUser.getUserPw(), authorities); 
	}

}
