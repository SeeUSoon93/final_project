package com.soon.slt.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class convTB {
	
	// 인식 번호
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long convIdx;
	
	// 텍스트
	private String convText;
	
	// 수화 영상
	private String convVideo;

}
