package com.soon.slt.repository;

import com.soon.slt.entity.TbUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import com.soon.slt.entity.TbBoard;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TbBoardRepository extends JpaRepository<TbBoard, String> {

	Page<TbBoard> findAll(Pageable pageable);

	Page<TbBoard> findAll(Specification<TbBoard> specification, Pageable pageable);

	@Query("select distinct b "
			+ "from TbBoard b left outer join b.tbUser = u "
			+ "where b.bdCategory IN('b0001','b0002') AND" // b0001 - 커뮤니티, b0002- 건의사항, b0003-공지사항
			+ "   ((:option = 'writer' and b.bdTitle like %:kw%)) "
			+ "   or (:option = 'title' and u.userNick like %:kw%)))")
	Page<TbBoard> findAllByKeyword(@Param("kw") String kw, @Param("option") String option, Pageable pageable);

	@Query("select "
			+ "distinct b "
			+ "from TbBoard b left outer join TbUser u "
			+ "where b.tbUser = TbUser")
	Page<TbBoard> findAllByUser(TbUser tbUser, Pageable pageable);

	@Query("select              distinct b "
			+ "from TbBoard b left outer join TbLikes l "
			+ "where l.tbUser = TbUser")
	Page<TbBoard> findAllByLike(TbUser tbUser, Pageable pageable);
	
	@Query("select b "
	         + "from TbBoard b "
	         + "where b.bdCategory = 'notice' and b.bdTitle like %:kw%")
	   Page<TbBoard> findNoticeByKeyword(String kw, Pageable pageable);


}
