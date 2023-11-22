package com.soon.myhome.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.soon.myhome.entity.Board;

//리포지터리 - 엔티티에 의해 생성된 데이터베이스 테이블에 접근하는 메서드들을 사용하기 위한 인터페이스
//데이터 처리에는 CRUD가 필요함. 이 CRUD를 어떻게 처리할지 정의하는 계층

//JpaRepository를 상속할 때는 대상이 되는 엔티티의 타입(Question)과 해당 엔티티의 PK속성타입(Integer)를 지정해야함
public interface BoardRepository extends JpaRepository<Board, Integer> {

	// page
	Page<Board> findAll(Pageable pageable);
	
	Page<Board> findAll(Specification<Board> specification, Pageable pageable);
	
    @Query("select "
            + "distinct b "
            + "from Board b " 
            + "left outer join Member m1 on b.member=m1 "
            + "left outer join Reple r on r.board=b "
            + "left outer join Member m2 on r.member=m2 "
            + "where "
            + "   b.boardTitle like %:kw% "
            + "   or b.boardCon like %:kw% "
            + "   or m1.username like %:kw% "
            + "   or r.repleCon like %:kw% "
            + "   or m2.username like %:kw% ")
    Page<Board> findAllByKeyword(@Param("kw") String kw, Pageable pageable);
}
