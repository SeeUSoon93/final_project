package com.soon.slt.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.soon.slt.entity.boardTB;


public interface BoardRepository extends JpaRepository<boardTB, Long>{

	Page<boardTB> findAll(Pageable pageable);
}
