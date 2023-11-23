package com.soon.slt.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.soon.slt.entity.TbBoard;

public interface TbBoardRepository extends JpaRepository<TbBoard, String> {

	Page<TbBoard> findAll(Pageable pageable);
	
	

}
