package com.soon.slt.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.soon.slt.entity.TbSignlang;

public interface TbSignlangRepository extends JpaRepository<TbSignlang, String>{
	
	Page<TbSignlang> findAll(Pageable pageable);

}
