package com.soon.slt.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class fileTB {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long fileIdx;
	
	// 파일의 이름
	private String fileName;

	
	// 파일이 저장된 경로
	private String filePath;
	
	
	// 파일 크기
	// 기본적으로 바이트 단위
	// 사용자가 파일을 다운로드 하기전에 파일 크기를 미리 알 수 있음
	// 서버 측에서 특정 크기 이상의 파일을 업로드를 제한할 수 있음
	private Long fileSize;
	
	// 업로드 날짜
	private LocalDateTime fileDate;
	
	// 해달 파일이 속한 게시굴
	@ManyToOne
	private boardTB boardTB; 
	
	
}
