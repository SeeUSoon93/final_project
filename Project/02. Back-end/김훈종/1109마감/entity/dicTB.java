package com.soon.slt.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class dicTB {
	
	// 수어사전 번호
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long dicIdx;
	
	// 분류
	private String dicCate;
	
	// 표제어
	private String dicKey;
	
	// 수어사전 영상
	private String dicVideo;

}
