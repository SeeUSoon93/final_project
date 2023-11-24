package com.soon.slt.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.soon.slt.entity.TbBoard;
import com.soon.slt.entity.TbUser;

public interface TbBoardRepository extends JpaRepository<TbBoard, String> {

	Page<TbBoard> findAll(Pageable pageable);
	
	Page<TbBoard> findAll(Specification<TbBoard> specification, Pageable pageable);
	
	@Query("select "
            + "distinct b "
            + "from TbBoard b " 
            + "left outer join TbUser u on b.tbUser = u "
            + "where "
            + "   :category = '제목' and (b.bdTitle like %:kw%) "
            + "   or :category = '작성자' and (u.userNick like %:kw%) ")
    Page<TbBoard> findAllByKeyword(@Param("kw") String kw, @Param("category") String category, Pageable pageable);

	@Query("select "
			+ "distinct b "
			+ "from TbBoard b "
			+ "left outer join TbUser u on b.tbUser = u ")
	Page<TbBoard> findAllByUser(TbUser tbUser, Pageable pageable);

	@Query("select "
			+ "distinct b "
			+ "from TbBoard b left outer join TbUser u "
			+ "on b.tbLikesList = u.tbLikesList")
	Page<TbBoard> findAllByLike(TbUser TbUser, Pageable pageable);
	
	
}
