package com.soon.slt.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.soon.slt.DataNotFound;
import com.soon.slt.entity.TbBoard;
import com.soon.slt.entity.TbUser;
import com.soon.slt.form.NoticeForm;
import com.soon.slt.repository.TbBoardRepository;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NoticeService {

	private final TbBoardRepository tbBoardRepository;

	public List<TbBoard> selectList(){
		return this.tbBoardRepository.findByNotice();
	}

	// 공지사항 생성
	public void noticeCreate(String bdTitle, String bdContent, TbUser tbUser){
		System.out.println("서비스 도착");
		TbBoard b = new TbBoard();
		b.setBdTitle(bdTitle);
		b.setBdCategory("Category 3");
		b.setBdContent(bdContent);
		b.setTbUser(tbUser);
		b.setCreatedAt(LocalDateTime.now());
		TbBoard saveBoard = this.tbBoardRepository.save(b);
		Long idx = saveBoard.getBdIdx();
		TbBoard board = tbBoardRepository.findById(idx).get();
		System.out.println("서비스 저장 완료");
		
	}

	// 공지사항 상세 조회
	public TbBoard noticeDetail(Long bdIdx) {
		Optional<TbBoard> b = this.tbBoardRepository.findById(bdIdx);
		if (b.isPresent()) {
			return b.get();
		} else {
			throw new DataNotFound("없는 공지사항입니다.");
		}
	}

	// 공지사항 삭제
	public void boardDelete(Long bdIdx) {
		this.tbBoardRepository.deleteById(bdIdx);
	}
	
	// 공지사항 수정
	public void noticeUpdate(TbBoard tbBoard, String bdTitle, String bdContent) {
		tbBoard.setBdTitle(bdTitle);
		tbBoard.setBdCategory("notice");
		tbBoard.setBdContent(bdContent);
		this.tbBoardRepository.save(tbBoard);
	}

}
