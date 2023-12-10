package com.soon.slt.service;

import java.io.Console;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.soon.slt.DataNotFound;
import com.soon.slt.entity.TbBoard;
import com.soon.slt.entity.TbLikes;
import com.soon.slt.entity.TbUser;
import com.soon.slt.repository.TbBoardRepository;
import com.soon.slt.repository.TbUserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TbBoardService {

	private final TbBoardRepository tbBoardRepository;
	private final TbUserRepository tbUserRepository;

	// 검색 목록 리스트 조회
	public Page<TbBoard> searchList(int page, String category, String kw) {
		List<Sort.Order> sorts = new ArrayList<>();
		sorts.add(Sort.Order.desc("createdAt"));
		Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
		if (category.equals("bdTitle")) {
			return this.tbBoardRepository.findByTitleKeyword(kw, pageable);
		} else {
			return this.tbBoardRepository.findByUserKeyword(kw, pageable);
		}
	}

	// 게시글 카테고리별
	public Page<TbBoard> boardFilter(int page, String filter){
		List<Sort.Order> sorts = new ArrayList<>();
		sorts.add(Sort.Order.desc("createdAt"));
		Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
		if (filter == null || filter.isEmpty()) {
			return this.tbBoardRepository.findAllCategory(pageable);
		} else {
			return this.tbBoardRepository.findByFilter(filter, pageable);
		}
	}

	// 게시글 생성
	@Transactional
	public void boardCreate(String bdTitle, String bdCategory, String bdContent, TbUser tbUser
	// ,List<MultipartFile> files
	) throws IOException {
		TbBoard b = new TbBoard();
		b.setBdTitle(bdTitle);
		b.setBdCategory(bdCategory);
		b.setBdContent(bdContent);
		b.setTbUser(tbUser);
		b.setCreatedAt(LocalDateTime.now());
		this.tbBoardRepository.save(b);
	}

	// 게시글 상세 조회
	public TbBoard boardDetail(Long dbIdx) {
		Optional<TbBoard> b = this.tbBoardRepository.findById(dbIdx);
		if (b.isPresent()) {
			TbBoard tbBoard = b.get();
			//조회수 증가
			tbBoard.setBdViews(tbBoard.getBdViews()+1);
			tbBoardRepository.save(tbBoard);
			return tbBoard;

		} else {
			throw new DataNotFound("없는 게시글입니다.");
		}
	}

	// 게시글 삭제
	public void boardDelete(Long bdIdx) {
		this.tbBoardRepository.deleteById(bdIdx);

	}

	// 게시글 수정
	public void boardUpdate(TbBoard tbBoard, String bdTitle, String bdCategory, String bdContent) {
		tbBoard.setBdTitle(bdTitle);
		tbBoard.setBdCategory(bdCategory);
		tbBoard.setBdContent(bdContent);
		this.tbBoardRepository.save(tbBoard);
	}

// 게시글 추천
	@Transactional
	public boolean boardLike(TbBoard tbBoard, TbUser tbUser) {
		// 좋아요가 눌렸을 때 중복 체크를 하고, 중복되지 않으면 좋아요를 추가하고 게시글을 저장
		if (tbBoard.getBdLikes().contains(tbUser)) {
			tbBoard.getBdLikes().remove(tbUser);
		} else {
			tbBoard.getBdLikes().add(tbUser);
		}
		this.tbBoardRepository.save(tbBoard);

		// 여기서 return 값 설정
		// 만약 사용자가 이미 좋아요를 눌렀다면 contains는 true를 반환하게 되는데, 여기에 ! 연산자를 사용하여 false로 바꾸어주고,
		// 그 반대의 경우에는 true로 바꾸어줍니다.
		return !tbBoard.getBdLikes().contains(tbUser);
	}
}
