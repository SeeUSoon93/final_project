package com.soon.slt.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.soon.slt.entity.TbBoard;
import com.soon.slt.entity.TbFile;
import com.soon.slt.entity.TbUser;
import com.soon.slt.repository.TbBoardReposiory;
import com.soon.slt.repository.TbFileRepository;
import com.soon.slt.repository.TbUserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TbBoardService {
	
	private final TbBoardReposiory tbBoardReposiory;
	private final TbFileRepository tbFileRepository;
	// 파일 업로드 시 파일이 저장될 디렉토리를 지정하는 변수
	// private static final String UPLOAD_DIR = "uploads/";
	
	// 게시판 리스트 조회
	public Page<TbBoard> selectList(int page){
		List<Sort.Order> sort = new ArrayList<>();
		sort.add(Sort.Order.desc("boardDate"));
		Pageable pageable = PageRequest.of(page, 10, Sort.by(sort));
		return this.tbBoardReposiory.findAll(pageable);
	}
	
	// 게시글 작성
	@Transactional
	public void boardCreate(String bdTitle,String bdCategory, String bdContent, TbUser user, List<MultipartFile> files) throws IOException{
		TbBoard b = new TbBoard();
		b.setBdTitle(bdTitle);
		b.setBdCategory(bdCategory);
		b.setBdContent(bdContent);
		b.setTbUser(user);
		b.setCreatedAt(LocalDateTime.now());
		this.tbBoardReposiory.save(b);
		
		// 파일이 있을 시 저장
		if(!files.isEmpty()) {
			// 업로드 디렉토리의 Path를 생성
			TbFile f = new TbFile();
			/*
			 * Path uploadPath = Paths.get(UPLOAD_DIR); if (!Files.exists(uploadPath)) { //
			 * 업로드 디렉토리가 존재하지 않으면 디렉토리를 생성 Files.createDirectories(uploadPath); }
			 */
			
			for (MultipartFile file : files) {
				// 파일의 내용을 바이트 배열로 읽어옴
				byte[] bytes = file.getBytes();
				// 업로드 디렉토리와 업로드된 파일의 원래 이름을 이용하여 저장될 파일의 Path를 생성
				// MultipartFile 인터페이스에서 getOriginalFilename 메서드는 업로드된 파일의 원래 이름을 반환하는 메서드
				/*
				 * try { // 만들어준 경로를 이용하여 해당경로에 바이트파일을 저장 Files.write(filePath, bytes); }catch
				 * (Exception e) { e.printStackTrace(); }
				 */
				
				// 파일 이름에서 확장자 추출
	            int idx = file.getOriginalFilename().lastIndexOf(".");
	            String ext = file.getOriginalFilename().substring(idx);
				
				UUID uuid = UUID.randomUUID();
				
				// 파일 정보를 데이터베이스에 저장
				f.setFileName(file.getOriginalFilename());
				f.setFileOriName(file.getOriginalFilename()+"_"+uuid.toString());
				f.setFileThumbName("thumb_"+file.getOriginalFilename()+"_"+uuid.toString());
				f.setFileExt(ext);
				f.setFileSize((int) file.getSize());
				f.setUploadedAt(LocalDateTime.now());
				f.setTbBoard(b);
				
			}
			this.tbFileRepository.save(f);
		}
	}
}
