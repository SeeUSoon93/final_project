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
import com.soon.slt.entity.TbFile;
import com.soon.slt.entity.TbLikes;
import com.soon.slt.entity.TbUser;
import com.soon.slt.repository.TbBoardRepository;
import com.soon.slt.repository.TbFileRepository;
import com.soon.slt.repository.TbUserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TbBoardService {

   private final TbBoardRepository tbBoardRepository;
   private final TbFileRepository tbFileRepository;
   private final TbUserRepository tbUserRepository;

   // 검색 목록 리스트 조회
   public Page<TbBoard> searchList(int page, String kw){
      List<Sort.Order> sorts = new ArrayList<>();
      sorts.add(Sort.Order.desc("createdAt"));

      Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
      return this.tbBoardRepository.findAllByKeyword(kw, pageable);
   }

   // 게시글 생성
   @Transactional
   public void boardCreate(String bdTitle, String bdCategory, String bdContent, TbUser tbUser
         //,List<MultipartFile> files
		   ) throws IOException {
      TbBoard b = new TbBoard();
      b.setBdTitle(bdTitle);
      b.setBdCategory(bdCategory);
      b.setBdContent(bdContent);
      b.setTbUser(tbUser);
      b.setCreatedAt(LocalDateTime.now());
      TbBoard saveBoard = this.tbBoardRepository.save(b);
		/*
		 * String idx = saveBoard.getBdIdx(); TbBoard board =
		 * tbBoardRepository.findById(idx).get();
		 */
      // 파일이 있을 시 저장
		/*
		 * if (!files.isEmpty()) { UUID uuid = UUID.randomUUID(); TbFile f = new
		 * TbFile();
		 * 
		 * for (MultipartFile file : files) { // MultipartFile 인터페이스에서
		 * getOriginalFilename 메서드는 업로드된 파일의 원래 이름을 반환하는 메서드 // 파일 이름에서 확장자 추출 int
		 * extension = file.getOriginalFilename().lastIndexOf("."); String ext =
		 * file.getOriginalFilename().substring(extension);
		 * 
		 * // 파일 정보를 데이터베이스에 저장 f.setFileName(file.getOriginalFilename());
		 * f.setFileOriName(file.getOriginalFilename() + "_" + uuid.toString());
		 * f.setFileThumbName("thumb_" + file.getOriginalFilename() + "_" +
		 * uuid.toString()); f.setFileExt(ext); f.setFileSize((int) file.getSize());
		 * f.setUploadedAt(LocalDateTime.now()); f.setTbBoard(board);
		 * 
		 * // 파일을 저장할 경로 지정 String uploadDirectory = "/src/main/resources/boardFile";
		 * file.transferTo(new File(uploadDirectory));
		 * 
		 * // 업로드할 파일의 실제 경로 생성 String filePath = Paths.get(uploadDirectory,
		 * f.getFileOriName()).toString();
		 * 
		 * // 파일을 지정된 경로에 복사 try (FileOutputStream fos = new FileOutputStream(filePath))
		 * { fos.write(file.getBytes()); } catch (IOException e) { e.printStackTrace();
		 * // 예외 처리 로직 추가 } } this.tbFileRepository.save(f); }
		 */
   }

   // 게시글 상세 조회
   public TbBoard boardDetail(String bdIdx) {
	   
	  System.out.println("게시글 상세조회 시작");
	  System.out.println("---------------------------bdIdx : "+bdIdx);
	  Optional<TbBoard> b = this.tbBoardRepository.findById(bdIdx);
	  
	  System.out.println(b.toString());
	  
      if (b.isPresent()) {
         return b.get();
      } else {
         throw new DataNotFound("없는 게시글입니다.");
      }
   }

   // 게시글 삭제
   public void boardDelete(String bdIdx) {
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


