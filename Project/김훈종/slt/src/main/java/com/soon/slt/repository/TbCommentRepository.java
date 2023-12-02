package com.soon.slt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.soon.slt.entity.TbComment;
import com.soon.slt.entity.TbUser;

public interface TbCommentRepository extends JpaRepository<TbComment, String>{

	@Query("select "
			+ "distinct c "
			+ "from TbComment c "
			+ "where c.tbUser = :tbUser")
	List<TbComment> findAllByUser(@Param("tbUser") TbUser tbUser);
}
