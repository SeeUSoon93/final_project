package com.soon.slt.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class infTB {

	// 안내번호
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long infIdx;
	
	// 분류
	private String infCate;
	
	// 안내 문장
	private String infSen;
	
	// 안내 수화 동영상 
	private String infVideo;
	
	
}
