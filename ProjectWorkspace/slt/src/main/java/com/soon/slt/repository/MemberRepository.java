package com.soon.slt.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.soon.slt.entity.Member;


public interface MemberRepository extends JpaRepository<Member, Long>{
	Optional<Member> findByUsername(String username);
}
