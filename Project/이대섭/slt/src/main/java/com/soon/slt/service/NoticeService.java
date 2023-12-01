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
import com.soon.slt.entity.TbFile;
import com.soon.slt.entity.TbUser;
import com.soon.slt.form.NoticeForm;
import com.soon.slt.repository.TbBoardRepository;
import com.soon.slt.repository.TbFileRepository;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NoticeService {

	private final TbBoardRepository tbBoardRepository;
	private final TbFileRepository tbFileRepository;

	public List<TbBoard> selectList(){
		return this.tbBoardRepository.findNoticeByKeyword();
	}

	public void noticeCreate(String bdTitle, String bdContent, TbUser tbUser, List<MultipartFile> files)
				throws IOException {
		TbBoard b = new TbBoard();
		b.setBdTitle(bdTitle);
		b.setBdCategory("notice");
		b.setBdContent(bdContent);
		b.setTbUser(tbUser);
		b.setCreatedAt(LocalDateTime.now());
		TbBoard saveBoard = this.tbBoardRepository.save(b);
		String idx = saveBoard.getBdIdx();
		TbBoard board = tbBoardRepository.findById(idx).get();
		
		// 파일이 있을 시 저장
				if (!files.isEmpty()) {
					UUID uuid = UUID.randomUUID();
					TbFile f = new TbFile();

					for (MultipartFile file : files) {
						// MultipartFile 인터페이스에서 getOriginalFilename 메서드는 업로드된 파일의 원래 이름을 반환하는 메서드
						// 파일 이름에서 확장자 추출
						int extension = file.getOriginalFilename().lastIndexOf(".");
						String ext = file.getOriginalFilename().substring(extension);

						// 파일 정보를 데이터베이스에 저장
						f.setFileName(file.getOriginalFilename());
						f.setFileOriName(file.getOriginalFilename() + "_" + uuid.toString());
						f.setFileThumbName("thumb_" + file.getOriginalFilename() + "_" + uuid.toString());
						f.setFileExt(ext);
						f.setFileSize((int) file.getSize());
						f.setUploadedAt(LocalDateTime.now());
						f.setTbBoard(board);

						// 파일을 저장할 경로 지정
						String uploadDirectory = "/src/main/resources/boardFile";
						file.transferTo(new File(uploadDirectory));

						// 업로드할 파일의 실제 경로 생성
						String filePath = Paths.get(uploadDirectory, f.getFileOriName()).toString();

						// 파일을 지정된 경로에 복사
						try (FileOutputStream fos = new FileOutputStream(filePath)) {
							fos.write(file.getBytes());
						} catch (IOException e) {
							e.printStackTrace();
							// 예외 처리 로직 추가
						}
					}
					this.tbFileRepository.save(f);
				}
	}

	// 공지사항 상세 조회
	public TbBoard noticeDetail(String bdIdx) {
		Optional<TbBoard> b = this.tbBoardRepository.findById(bdIdx);
		if (b.isPresent()) {
			return b.get();
		} else {
			throw new DataNotFound("없는 공지사항입니다.");
		}
	}

	// 공지사항 삭제
	public void boardDelete(String bdIdx) {
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
