package com.soon.slt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.soon.slt.entity.TbComment;

public interface TbCommentRepository extends JpaRepository<TbComment, Long>{
	
	@Query("select distinct c "
			+ "from TbComment c "
			+ "where c.tbBoard.bdIdx = :bdIdx")
	List<TbComment> findByboard(@Param("bdIdx") Long bdIdx);
}
