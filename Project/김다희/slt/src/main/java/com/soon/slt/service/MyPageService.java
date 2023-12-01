package com.soon.slt.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.soon.slt.entity.TbBoard;
import com.soon.slt.entity.TbLikes;
import com.soon.slt.entity.TbUser;
import com.soon.slt.repository.TbBoardRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MyPageService {

	private final TbBoardRepository tbBoardRepository;

	
	// 마이페이지 게시글 리스트 출력
	public List<TbBoard> myBoardList(TbUser tbUser){
		return this.tbBoardRepository.findAllByUser(tbUser);
	
	}
	
	// 마이페이지 좋아요 게시글 리스트 출력
	public List<TbLikes> myLikeList(TbUser tbUser){
		return this.tbBoardRepository.findAllByLike(tbUser);
	}
	
	
	
	
	
	
}
