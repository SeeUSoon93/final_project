package com.soon.slt.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class likeTB {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 고유 식별자 필드 추가
	
	@ManyToOne
	private boardTB boardTB;
	
	@ManyToOne
	private userTB userTB;
	


}
