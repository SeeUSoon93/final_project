package com.soon.slt.entity;

import java.time.LocalDateTime;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class repleTB {
	
	// 댓글 번호
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long repleIdx;
	
	// 댓글 내용
	private String repleCon;
	
	// 댓글 작성 날짜
	private LocalDateTime repleDate;
	
	// 댓글 좋아요
	@Column(columnDefinition = "Integer default 0", nullable = false)
	private Integer repleLike;
	
	
	@ManyToOne
	private boardTB boardTB;
	
	@ManyToOne
	private userTB userTB;
	
	
	

}
