package com.soon.slt.entity;

import java.time.LocalDateTime;
import java.util.List;



import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Data
@Entity
public class boardTB {
	
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long boardIdx;
	
	private String boardCate;
	
	private String boardTitle;
	
	private String boardCon;
	
	private String boardFile;
	
	@Column(columnDefinition = "Integer default 0", nullable = false)
	private Integer boardView;
	
	@Column(columnDefinition = "Integer default 0", nullable = false)
	private Integer boardLike;
	
	private LocalDateTime boardDate;
	
	
	@OneToMany(mappedBy = "boardTB", cascade = CascadeType.REMOVE)
	private List<repleTB> repleList;
	
	@ManyToOne
	private userTB userTB;
	

}
