package com.soon.slt.repository;

import com.soon.slt.entity.TbBoard;
import com.soon.slt.entity.TbLikes;
import com.soon.slt.entity.TbUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TbLikeRepository extends JpaRepository<TbLikes, Long> {

    @Query("select "
            + "distinct b "
            + "from TbLikes b "
            + "where b.tbUser = :tbUser "
            + "and b.tbBoard = :tbBoard ")
    List<TbLikes> findByUserBoard(@Param("tbUser")TbUser tbUser, @Param("tbBoard")TbBoard tbBoard);
}