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

public interface TbBoardRepository extends JpaRepository<TbBoard, Long> {

	Page<TbBoard> findAll(Pageable pageable);

	Page<TbBoard> findAll(Specification<TbBoard> specification, Pageable pageable);

	// Category 1 - 커뮤니티, Category 2- 건의사항, Category 3-공지사항
	// 제목 검색
	@Query("select distinct b "
			+ "from TbBoard b "
			+ "where b.bdCategory IN('Category 1','Category 2') "
			+ "and b.bdTitle like %:kw%")
	Page<TbBoard> findByTitleKeyword( @Param("kw") String kw, Pageable pageable);

	// 작성자 검색
	@Query("select distinct b "
			+ "from TbBoard b "
			+ "where b.bdCategory IN('Category 1','Category 2') "
			+ "and b.tbUser.userNick like %:kw%")
	Page<TbBoard> findByUserKeyword( @Param("kw") String kw, Pageable pageable);

	// 마이페이지 게시글 리스트
	@Query("select "
			+ "distinct b "
			+ "from TbBoard b "
			+ "where b.tbUser = :tbUser")
	List<TbBoard> findAllByUser(@Param("tbUser") TbUser tbUser);

	// 게시글 좋아요 리스트
	@Query("select "
			+ "distinct l "
			+ "from TbLikes l "
			+ "where l.tbUser = :tbUser")
	List<TbLikes> findAllByLike(@Param("tbUser") TbUser thUser);

	// 공지사항 리스트
	@Query("select distinct b "
			+ "from TbBoard b "
			+ "where b.bdCategory = 'Category 3'")
	List<TbBoard> findByNotice();

	// 카테고리별
	@Query("select distinct b "
			+ "from TbBoard b "
			+ "where b.bdCategory = :filter ")
	Page<TbBoard> findByFilter(@Param("filter") String filter, Pageable pageable);

	// 게시글 리스트 조회
	@Query("select distinct b "
			+ "from TbBoard b "
			+ "where b.bdCategory IN('Category 1','Category 2') ")
	Page<TbBoard> findAllCategory(Pageable pageable);



}