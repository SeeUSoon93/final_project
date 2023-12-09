package com.soon.slt.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.soon.slt.DataNotFound;
import com.soon.slt.entity.TbSignlang;
import com.soon.slt.repository.TbSignlangRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TbSLangDicService {

	private final TbSignlangRepository tbSignlangRepository;

	// 수어사전 카테고리별 리스트 출력
	public Page<TbSignlang> getSignlang(int page, String category) {
		List<Sort.Order> sorts = new ArrayList<>();
		sorts.add(Sort.Order.desc("slangIdx"));
		Pageable pageable = PageRequest.of(page, 9, Sort.by(sorts));
		if(category == null || category.isEmpty()) {
			return this.tbSignlangRepository.findAll(pageable);
		}else {
			return this.tbSignlangRepository.findAllByCategory(category, pageable);
		}
	}

	// 수어사전 검색
	public Page<TbSignlang> searching(int page, String kw, String category) {
		List<Sort.Order> sorts = new ArrayList<>();
		sorts.add(Sort.Order.desc("slangIdx"));
		Pageable pageable = PageRequest.of(page, 9, Sort.by(sorts));
		if(category == null || category.isEmpty()) {
			return this.tbSignlangRepository.findAllByOnlyKeyword(kw, pageable);
		}else {
			return this.tbSignlangRepository.findAllByCaKeyword(kw, category, pageable);
		}
	}

	// 수어 게시글 상세 조회
	public TbSignlang langDetail(Long slangIdx) {
		Optional<TbSignlang> b = this.tbSignlangRepository.findById(slangIdx);
		if (b.isPresent()) {
			return b.get();
		} else {
			throw new DataNotFound("없는 게시글입니다.");
		}
	}




}