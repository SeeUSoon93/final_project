package com.soon.slt.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.soon.slt.entity.TbSignlang;
import com.soon.slt.repository.TbSignlangRepository;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TbSLangDicService {

	private final TbSignlangRepository tbSignlangRepository;
	
	// 수어사전 검색 조회
	public Page<TbSignlang> getSignlang(int page, String kw) {
		List<Sort.Order> sorts = new ArrayList<>();
		sorts.add(Sort.Order.desc("slangIdx"));
			
		Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
		return this.tbSignlangRepository.findAllByKeyword(kw, pageable);
	}
	
	// 수어사전 검색 기능
	private Specification<TbSignlang> searchSignlang(String kw){
		return new Specification<TbSignlang>() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public Predicate toPredicate(Root<TbSignlang> b, CriteriaQuery<?> query, CriteriaBuilder cb) {
				query.distinct(true);
				
				return cb.or(cb.like(b.get("slangText"), "%" + kw + "%"));
			}
		};
	}
	
	
	
}