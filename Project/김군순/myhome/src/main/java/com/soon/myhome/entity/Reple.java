package com.soon.myhome.entity;

import java.time.LocalDateTime;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class Reple {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer repleNum;
	
	private String repleCon;
	
	private LocalDateTime repleDate;
	
	@ManyToOne // N:1 관계를 가지는 것을 의미함
	private Board board;
	
	// 댓글쓴사람 추가
	@ManyToOne
	private Member member;
	
	// 수정 날짜 추가
	private LocalDateTime repleUpDate;
	
	@ManyToMany
	Set<Member> good;
}
