package com.soon.slt.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.soon.slt.DataNotFound;
import com.soon.slt.entity.boardTB;
import com.soon.slt.entity.fileTB;
import com.soon.slt.entity.userTB;
import com.soon.slt.repository.BoardRepository;
import com.soon.slt.repository.FileRepository;
import com.soon.slt.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {
	
	private final BoardRepository boardRepository;
	private final FileRepository fileRepository;
	// 파일 업로드 시 파일이 저장될 디렉토리를 지정하는 변수
	private static final String UPLOAD_DIR = "uploads/";

	// 게시판 리스트 조회
	public Page<boardTB> selectList(int page){
		List<Sort.Order> sort = new ArrayList<>();
		sort.add(Sort.Order.desc("boardDate"));
		Pageable pageable = PageRequest.of(page, 10, Sort.by(sort));
		return this.boardRepository.findAll(pageable);
	}
	
	// 게시글 상세 조회
	public boardTB boardDetail(Long boardIdx) {
		Optional<boardTB> b = this.boardRepository.findById(boardIdx);
		if(b.isPresent()) {
			return b.get();
		}else {
			throw new DataNotFound("없는 게시글입니다.");
		}
	}
	
	// 게시글 작성
	@Transactional
	public void boardCreate(String boardTitle, String boardCon, String boardCate, userTB user, List<MultipartFile> files) throws IOException {
		boardTB b = new boardTB();
		b.setBoardTitle(boardTitle);
		b.setBoardCate(boardCate);
		b.setBoardCon(boardCon);
		b.setUserTB(user);
		b.setBoardDate(LocalDateTime.now());
		this.boardRepository.save(b);
		// 업로드 디렉토리의 Path를 생성
    	Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
        	// 업로드 디렉토리가 존재하지 않으면 디렉토리를 생성
            Files.createDirectories(uploadPath);
        }

        for (MultipartFile file : files) {
        	// 파일의 내용을 바이트 배열로 읽어옴
            byte[] bytes = file.getBytes();
            // 업로드 디렉토리와 업로드된 파일의 원래 이름을 이용하여 저장될 파일의 Path를 생성
            // MultipartFile 인터페이스에서 getOriginalFilename 메서드는 업로드된 파일의 원래 이름을 반환하는 메서드
            Path filePath = Paths.get(UPLOAD_DIR + file.getOriginalFilename());
            // 해당 Path에 바이트 배열을 쓰면서 실제 파일을 디스크에 저장
            Files.write(filePath, bytes);
            
            // 파일 정보를 데이터베이스에 저장
            fileTB f = new fileTB();
            f.setFileName(file.getOriginalFilename());
            f.setFilePath(filePath.toString());
            f.setFileSize(file.getSize());
            f.setFileDate(LocalDateTime.now());
            f.setBoardTB(b);
            this.fileRepository.save(f);
        }
		
		
		
		
	}
	
	
	@Transactional
	public void boardViewCount(boardTB board) {
		board.setBoardView(board.getBoardView()+1);
		this.boardRepository.save(board);
	}
	
		
		
		
		
		
		
		
}
