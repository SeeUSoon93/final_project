package com.soon.slt.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.soon.slt.entity.TbUser;

public interface TbUserRepository extends JpaRepository<TbUser, String>{
	Optional<TbUser> findByUserEmail(String userEmail);
	
	Optional<TbUser> findByUserNick(String userNick);
}
