package com.soon.slt.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.soon.slt.entity.TbBoard;
import com.soon.slt.entity.TbUser;
import com.soon.slt.repository.TbBoardRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MyPageService {

	private final TbBoardRepository tbBoardRepository;

	// 마이페이지 게시글 리스트 출력
		public Page<TbBoard> myBoardList(int page, TbUser tbUser){
			List<Sort.Order> sorts = new ArrayList<>();
			sorts.add(Sort.Order.desc("createdAt"));
			
			Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
			return this.tbBoardRepository.findAllByUser(tbUser, pageable);
		}
		
		// 마이페이지 좋아요 게시글 리스트 출력
		public Page<TbBoard> myLikeList(int page, TbUser tbUser){
			List<Sort.Order> sorts = new ArrayList<>();
			sorts.add(Sort.Order.desc("createdAt"));
			
			Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
			return this.tbBoardRepository.findAllByLike(tbUser, pageable);
		}
	
	
	
	
	
	
}
