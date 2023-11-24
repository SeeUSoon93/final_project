package com.soon.slt.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.soon.slt.DataNotFound;
import com.soon.slt.entity.TbSignlang;
import com.soon.slt.repository.TbSignlangRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TbSLangDicService {
	
	private final TbSignlangRepository tbSignlangRepository;
	
	// 수어 게시글 리스트 조회
	public Page<TbSignlang> langList(int page){
		List<Sort.Order> sort = new ArrayList<>();
		sort.add(Sort.Order.desc("slangText"));
		Pageable pageable = PageRequest.of(page, 10, Sort.by(sort));
		return this.tbSignlangRepository.findAll(pageable);
	}
	
	// 수어 게시글 상세 조회
	public TbSignlang langDetail(String slangIdx) {
		Optional<TbSignlang> b = this.tbSignlangRepository.findById(slangIdx);
		if (b.isPresent()) {
			return b.get();
		} else {
			throw new DataNotFound("없는 게시글입니다.");
		}
	}
	
	
	

}
