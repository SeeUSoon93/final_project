package com.soon.slt.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.soon.slt.entity.TbSignlang;

public interface TbSignlangRepository extends JpaRepository<TbSignlang, String>{

	Page<TbSignlang> findAll(Pageable pageable);
	
	Page<TbSignlang> findAll(Specification<TbSignlang> specification, Pageable pageable);
	
	@Query("select "
            + "distinct s "
            + "from TbSignlang s " 
            + "where "
            + "   s.slangText like %:kw% ")
    Page<TbSignlang> findAllByKeyword(@Param("kw") String kw, Pageable pageable);
}
