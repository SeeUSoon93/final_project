package com.soon.slt.repository;

import com.soon.slt.entity.TbUser;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import com.soon.slt.entity.TbBoard;
import com.soon.slt.entity.TbLikes;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TbBoardRepository extends JpaRepository<TbBoard, String> {

	Page<TbBoard> findAll(Pageable pageable);

	Page<TbBoard> findAll(Specification<TbBoard> specification, Pageable pageable);
	
	// Category1 - 커뮤니티, Category2- 건의사항, Category3-공지사항
	@Query("select distinct b "
	        + "from TbBoard b left outer join TbUser u on b.tbUser = u "
	        + "where b.bdCategory IN('Category 1','Category 2') ")
	Page<TbBoard> findAllByKeyword(@Param("kw") String kw, Pageable pageable);

	@Query("select "
			+ "distinct b "
			+ "from TbBoard b "
			+ "where b.tbUser = :tbUser")
	List<TbBoard> findAllByUser(@Param("tbUser") TbUser tbUser);

	@Query("select "
			+ "distinct l "
			+ "from TbLikes l "
			+ "where l.tbUser = :tbUser")
	List<TbLikes> findAllByLike(@Param("tbUser") TbUser thUser);

	@Query("select b "
			+ "from TbBoard b "
			+ "where b.bdCategory = 'notice'")
	List<TbBoard>findNoticeByKeyword();

	
}