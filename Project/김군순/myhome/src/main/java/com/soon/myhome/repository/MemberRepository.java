package com.soon.myhome.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.soon.myhome.entity.Member;


public interface MemberRepository extends JpaRepository<Member, Integer>{
	
	// 사용자의 아이디를 조회해야하기 때문에 만들어주기
	// Jpa가 기본적으로 가진 메서드가 아니기 떄문에 findAll, findById(Id는 프라이머리키를 의미한다!)와는 달리 만들어야함
	
	// Optional은 기존에 이야기했듯 NULL값을 유연하게 처리하기 위함이다.
	// 우리가 생성한 객체를 받아올땐 기본적으로 Optional을 사용한다고 생각하면 됨
	
	// 사용자의 아이디로 조회하기
	Optional<Member> findByusername(String username);

}
